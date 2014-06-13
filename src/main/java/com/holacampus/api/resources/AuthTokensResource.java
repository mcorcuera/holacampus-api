/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.holacampus.api.resources;

import com.holacampus.api.beans.AuthToken;
import com.holacampus.api.beans.User;
import com.holacampus.api.exceptions.HTTPErrorException;
import com.holacampus.api.mappers.AuthTokenMapper;
import com.holacampus.api.security.AuthTokenGenerator;
import com.holacampus.api.security.AuthenticationRequired;
import com.holacampus.api.security.AuthenticationScheme;
import com.holacampus.api.security.UserPrincipal;
import com.holacampus.api.utils.HALBuilderUtils;
import com.holacampus.api.utils.MyBatisConnectionFactory;
import com.theoryinpractise.halbuilder.api.Representation;
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
 *
 * @author Mikel Corcuera <mik.corcuera@gmail.com>
 */

@Path( "/")
public class AuthTokensResource {
    
    private static final Logger logger = LogManager.getLogger( AuthTokensResource.class.getName());
    
    @Path("/auth-tokens")
    @POST
    @AuthenticationRequired( scheme=AuthenticationScheme.AUTHENTICATION_SCHEME_BASIC)
    @Produces( { RepresentationFactory.HAL_JSON})
    public Representation newAuthenticationToken ( @Context SecurityContext sc)
    {
        logger.info( "[POST] /auth-tokens");
        
        AuthToken authToken = new AuthToken();
        authToken.setAuthToken( AuthTokenGenerator.getAuthToken());
        
        SqlSession session = MyBatisConnectionFactory.getSession().openSession();
        
        Long id = ((UserPrincipal) sc.getUserPrincipal()).getId();
        
        try {
            AuthTokenMapper authMapper = session.getMapper( AuthTokenMapper.class);
            authMapper.storeAuthTokenForUserId(authToken, id);
            
            authToken = authMapper.getAuthToken( authToken.getAuthToken());
            User user = new User();
            user.setId(id);
            authToken.setUser( user);
            
            session.commit(); 
        } catch( Exception ex) {
            logger.error(ex);
            throw new HTTPErrorException( Response.Status.CONFLICT, "Error while generating token");

        } finally {
            session.close();
        }
        return HALBuilderUtils.fromRepresentable(authToken);
    }
    
    @Path("/users/{id}/auth-tokens")
    @DELETE
    @AuthenticationRequired( scheme=AuthenticationScheme.AUTHENTICATION_SCHEME_TOKEN)
    public void deleteAllUserTokens( @Context SecurityContext sc, @PathParam( "id") Long id)
    {
        logger.info( "[DELETE] /users/" + id + "/auth-tokens");
        SqlSession session = MyBatisConnectionFactory.getSession().openSession();
        
           /*
        * Check if the user has permissions over this resource
        */
        
        if( !Objects.equals(id, ((UserPrincipal) sc.getUserPrincipal()).getId())) {
            throw new HTTPErrorException( Response.Status.FORBIDDEN, "Not allowed to delete auth-tokens");
        }        
        try {
            AuthTokenMapper authMapper = session.getMapper( AuthTokenMapper.class);
            authMapper.deleteAllUserTokens( id);
            session.commit(); 
        } catch( Exception ex) {
            logger.error(ex);
        } finally {
            session.close();
        }
    }
    
    @Path("/users/{id}/auth-tokens/{auth-token}")
    @GET
    @AuthenticationRequired( scheme=AuthenticationScheme.AUTHENTICATION_SCHEME_TOKEN)
    @Produces( { RepresentationFactory.HAL_JSON})
    public Representation getAuthenticationToken( @PathParam("auth-token") String token, @PathParam("id") Long id, @Context SecurityContext sc)
    {
        logger.info( "[GET] /users/" + id + "/auth-tokens");        
        
        AuthToken authToken;
        
        SqlSession session = MyBatisConnectionFactory.getSession().openSession();
        
           /*
        * Check if the user has permissions over this resource
        */
        
        if( !Objects.equals(id, ((UserPrincipal) sc.getUserPrincipal()).getId())) {
            throw new HTTPErrorException( Response.Status.FORBIDDEN, "Not allowed to get auth-tokens");
        }
        
        try {
            AuthTokenMapper authMapper = session.getMapper( AuthTokenMapper.class);
            
            authToken = authMapper.getAuthToken( token);
            
             if( authToken == null) {
                throw new HTTPErrorException( Response.Status.NOT_FOUND, "Auth-token not found on database");
            }
            session.commit(); 
            
            User user = new User();
            user.setId(id);
            authToken.setUser( user);
            
        } catch( NotFoundException | ForbiddenException ex) {
            throw ex;
        } catch( Exception ex) {
            logger.error(ex);
            throw new InternalServerErrorException();
        } finally {
            session.close();
        }
        
        return HALBuilderUtils.fromRepresentable(authToken);
    }
    
    @Path("/users/{id}/auth-tokens/{auth-token}")
    @DELETE
    @AuthenticationRequired( scheme=AuthenticationScheme.AUTHENTICATION_SCHEME_TOKEN)
    public void deleteAtuthenticationToken( @PathParam("auth-token") String token, @PathParam( "id") Long id, @Context SecurityContext sc)
    {
        logger.info( "[DELETE] /auth-tokens/" + token);
        AuthToken authToken;
        
        SqlSession session = MyBatisConnectionFactory.getSession().openSession();
        
           /*
        * Check if the user has permissions over this resource
        */
        
        if( !Objects.equals(id, ((UserPrincipal) sc.getUserPrincipal()).getId())) {
            throw new HTTPErrorException( Response.Status.FORBIDDEN, "Not allowed to delete auth-tokens");
        }
        
        try {
            AuthTokenMapper authMapper = session.getMapper( AuthTokenMapper.class);
            
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
