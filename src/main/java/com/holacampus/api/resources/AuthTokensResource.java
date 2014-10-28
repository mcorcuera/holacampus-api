/*
 * Copyright (C) 2014 Mikel Corcuera <mik.corcuera@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.holacampus.api.resources;

import com.holacampus.api.domain.AuthToken;
import com.holacampus.api.domain.User;
import com.holacampus.api.exceptions.HTTPErrorException;
import com.holacampus.api.mappers.AuthTokenMapper;
import com.holacampus.api.security.AuthTokenGenerator;
import com.holacampus.api.security.AuthenticationRequired;
import com.holacampus.api.security.AuthenticationScheme;
import com.holacampus.api.security.UserPrincipal;
import com.holacampus.api.utils.MyBatisConnectionFactory;
import com.theoryinpractise.halbuilder.api.RepresentationFactory;
import java.util.Objects;
import javax.ws.rs.DELETE;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.GET;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * Esta clase se encarga de gestionar las peticiones de la API al recurso
 * Authentication Token. Es decir, gestiona las peticiones a la URL 
 * <code>/auth-tokens</code>.
 * <br/><br/>
 * Este recurso es utilizado para la creación y manipulación de los tokens
 * de autenticación que sirven para utilizar la aplicación de forma segura.
 * @author Mikel Corcuera <mik.corcuera@gmail.com>
 */
@Path( "/auth-tokens")
public class AuthTokensResource {
    
    private static final Logger logger = LogManager.getLogger( AuthTokensResource.class.getName());
    
    @Context 
    private SecurityContext sc;
    
    /**
     * Esta función gestiona las peticiones POST al recurso 
     * <code>/auth-tokens</code>. Esta operación 
     * tiene como resultado la creación y obtención de un nuevo Token de 
     * autenticación para el usuario que lo solicita. Con este token el usuario
     * podrá hacer peticiones al resto de recursos de forma autenticada.
     * @return El token de autenticación
     */
    @POST
    @AuthenticationRequired( AuthenticationScheme.AUTHENTICATION_SCHEME_BASIC)
    @Produces( { RepresentationFactory.HAL_JSON})
    public AuthToken getNewAuthenticationToken ( )
    {
        logger.info( "[POST] /auth-tokens");
        
        AuthToken authToken = new AuthToken();
        authToken.setAuthToken( AuthTokenGenerator.getAuthToken());
        
        SqlSession session = MyBatisConnectionFactory.getSession().openSession();
        
        Long id = ((UserPrincipal) sc.getUserPrincipal()).getId();
        
        try {
            AuthTokenMapper authMapper = session.getMapper( AuthTokenMapper.class);
            authMapper.storeAuthToken(authToken, id);
            
            authToken = authMapper.getAuthToken( authToken.getAuthToken());
            
            session.commit(); 
        } catch( Exception ex) {
            logger.error(ex);
            throw new HTTPErrorException( Response.Status.CONFLICT, "Error while generating token");

        } finally {
            session.close();
        }
        return authToken;
    }
    
    /**
     * Esta función gestiona las peticiones DELETE al recurso
     * <code>/auth-tokens</code>. Esta operación elimina todos los tokens
     * de autenticación activos para el usuario.
     */
    @DELETE
    @AuthenticationRequired(AuthenticationScheme.AUTHENTICATION_SCHEME_TOKEN)
    public void deleteAllUserTokens()
    {
        logger.info( "[DELETE] /auth-tokens");
        SqlSession session = MyBatisConnectionFactory.getSession().openSession();
        
        Long id = ((UserPrincipal) sc.getUserPrincipal()).getId();
        
           /*
        * Check if the user has permissions over this resource
        */
        
        if( !Objects.equals(id, ((UserPrincipal) sc.getUserPrincipal()).getId())) {
            throw new HTTPErrorException( Response.Status.FORBIDDEN, "Not allowed to delete auth-tokens");
        }        
        try {
            AuthTokenMapper authMapper = session.getMapper( AuthTokenMapper.class);
            authMapper.deleteAllElementTokens(id);
            session.commit(); 
        } catch( Exception ex) {
            logger.error(ex);
        } finally {
            session.close();
        }
    }
    
    /**
     *  Esta función gestiona las peticiones GET al recurso
     * <code>/auth-tokens/{auth-token}</code>. Esta operación obtiene la
     * representación de un token de autenticación en concreto
     * @param token identificador del token de autentificación
     * @return la representación del token de autenticación
     */
    @Path("/{auth-token}")
    @GET
    @AuthenticationRequired( AuthenticationScheme.AUTHENTICATION_SCHEME_TOKEN)
    @Produces( { RepresentationFactory.HAL_JSON})
    public AuthToken getAuthenticationToken( @PathParam("auth-token") String token)
    {
        logger.info( "[GET] /auth-tokens");        
        
        AuthToken authToken;
        
        SqlSession session = MyBatisConnectionFactory.getSession().openSession();
        
           /*
        * Check if the user has permissions over this resource
        */
        
        try {
            AuthTokenMapper authMapper = session.getMapper( AuthTokenMapper.class);
            
            authToken = authMapper.getAuthToken( token);
            
            if( !Objects.equals( authToken.getElement().getId(), ((UserPrincipal) sc.getUserPrincipal()).getId())) {
                throw new HTTPErrorException( Response.Status.FORBIDDEN, "Not allowed to get auth-tokens");
            }
            
             if( authToken == null) {
                throw new HTTPErrorException( Response.Status.NOT_FOUND, "Auth-token not found on database");
            }
            session.commit(); 
                        
        } catch( NotFoundException | ForbiddenException ex) {
            throw ex;
        } catch( Exception ex) {
            logger.error(ex);
            throw new InternalServerErrorException();
        } finally {
            session.close();
        }
        
        return authToken;
    }
    
    /**
     * Esta función gestiona las peticiones DELETE al recurso
     * <code>/auth-tokens/{auth-token}</code>. Esta operación se encarga
     * de eliminar un token de autenticación en concreto del usuario.
     * @param token identificador del token de autenticación
     */
    @Path("/{auth-token}")
    @DELETE
    @AuthenticationRequired( AuthenticationScheme.AUTHENTICATION_SCHEME_TOKEN)
    public void deleteAtuthenticationToken( @PathParam("auth-token") String token)
    {
        logger.info( "[DELETE] /auth-tokens/" + token);
        AuthToken authToken;
        
        SqlSession session = MyBatisConnectionFactory.getSession().openSession();
        
        try {
            AuthTokenMapper authMapper = session.getMapper( AuthTokenMapper.class);
            authToken = authMapper.getAuthToken(token);
            
            /*
             * Check if the user has permissions over this resource
             */
            if( !Objects.equals( authToken.getElement().getId(), ((UserPrincipal) sc.getUserPrincipal()).getId())) {
                throw new HTTPErrorException( Response.Status.FORBIDDEN, "Not allowed to get auth-tokens");
            }
            
            int removed = authMapper.deleteAuthToken(token);
            
            /*
             * If nothing removed, the Token didn't exist
            */
            if( removed == 0) {
                throw new HTTPErrorException( Response.Status.NOT_FOUND, "Auth-token not found on database");
            }
            
            session.commit(); 
            
        } catch( NotFoundException | ForbiddenException ex) {
            throw ex;
        } catch( Exception ex) {
            logger.error(ex);
            throw new InternalServerErrorException();
        } finally {
            session.close();
        }
    }

}
