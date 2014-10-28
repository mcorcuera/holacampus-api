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

package com.holacampus.api.subresources;

import com.holacampus.api.domain.City;
import com.holacampus.api.domain.Permission;
import com.holacampus.api.domain.Study;
import com.holacampus.api.domain.University;
import com.holacampus.api.domain.User;
import com.holacampus.api.exceptions.HTTPErrorException;
import com.holacampus.api.hal.HalList;
import com.holacampus.api.mappers.CityMapper;
import com.holacampus.api.mappers.StudyMapper;
import com.holacampus.api.mappers.UserMapper;
import com.holacampus.api.security.AuthenticationRequired;
import com.holacampus.api.security.AuthenticationScheme;
import com.holacampus.api.security.UserPrincipal;
import com.holacampus.api.utils.MyBatisConnectionFactory;
import com.holacampus.api.utils.Utils;
import com.holacampus.api.validators.CreationValid;
import com.theoryinpractise.halbuilder.api.RepresentationFactory;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.Encoded;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * Esta clase se encarga de gestionar las peticiones de la API al subrecurso
 * Studies. Es decir, gestiona las peticiones a la URL 
 * <code>universities/{uid}/studies</code>.
 * <br/><br/>
 * Este subrecurso se encarga de gestionar los diferentes estudios ofertados 
 * por una universidad
 * @author Mikel Corcuera <mik.corcuera@gmail.com>
 */
public class StudiesResource {
    
    private final University university;
    
    private static final Logger logger = LogManager.getLogger( StudiesResource.class.getName());
    
    /**
     * Constructor del subrecurso de estudios
     * @param university universidad a la que pertenecen los estudios
     */
    public StudiesResource( University university)
    {
        this.university = university;
    }
    
    /**
      * Esta función gestiona las peticiones GET al recurso 
     * <code>universities/{uid}/studies</code>. Esta operación devuelve un lista 
     * con los estudios ofertados por la universidad
     * @param page página de los resultados
     * @param size tamaño de los resultados
     * @param q cadena de caracteres que sirve para filtrar por nombre los
     * resultados
     * @param uriInfo  información de la URL de la petición
     * @return lista con los estudios ofertados del tamaño especificado y filtrada
     * @throws UnsupportedEncodingException
     */
    @GET
    @AuthenticationRequired( AuthenticationScheme.AUTHENTICATION_SCHEME_TOKEN)
    @Produces( { RepresentationFactory.HAL_JSON})
    @Encoded
    public HalList<Study> getStudies( @QueryParam("page") Integer page, 
            @QueryParam( "size") Integer size, @QueryParam( "q") String q, 
            @Context UriInfo uriInfo)  throws UnsupportedEncodingException
    {
        logger.info( "[GET] " + uriInfo.getPath());
        
        HalList<Study> studies; 
        
        page = Utils.getValidPage(page);
        size = Utils.getValidSize(size);
        if( q != null) {
            q   = URLDecoder.decode(q, "UTF-8");
        }
        RowBounds rb = Utils.createRowBounds(page, size);
        
        SqlSession session = MyBatisConnectionFactory.getSession().openSession();

        try {
            StudyMapper mapper      = session.getMapper( StudyMapper.class);
            List<Study> studyList   = mapper.getStudies( university, q, rb);
            int total               = mapper.getTotalStudies( university, q);
            session.commit();
            
            studies = new HalList<Study> ( studyList, total);
            studies.setResourceRelativePath( uriInfo.getPath());
            studies.setPage(page);
            studies.setSize(size);
            studies.setQuery(q);
            
         }catch( HTTPErrorException e) {
            throw e;
        }catch( Exception e) {
            logger.info( e);
            throw new HTTPErrorException( Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }finally {
            session.close();
        }

        return studies;
    }
    
    /**
     * Esta función gestiona las peticiones POST al recurso 
     * <code>universities/{uid}/studies</code>. Esta operación añade un nuevo
     * estudio a los ofertados por la universidad
     * @param study datos del estudio a añadir
     * @param sc información de seguridad y autenticación de la petición
     * @param uriInfo  información de la URL de la petición
     * @return representación del estudio recien añadido
     */
    @POST
    @AuthenticationRequired( AuthenticationScheme.AUTHENTICATION_SCHEME_TOKEN)
    @Consumes( { RepresentationFactory.HAL_JSON, MediaType.APPLICATION_JSON})
    @Produces( { RepresentationFactory.HAL_JSON})
    public Study createStudy( @CreationValid @Valid Study study, @Context SecurityContext sc, @Context UriInfo uriInfo) {
        
        logger.info( "[POST] " + uriInfo.getPath());
        
        SqlSession session = MyBatisConnectionFactory.getSession().openSession();
        UserPrincipal up = (UserPrincipal) sc.getUserPrincipal();
        
        if( !up.getId().equals( university.getId()))
            throw new HTTPErrorException( Status.FORBIDDEN, "You are not allowed here");
        
        try {
            StudyMapper mapper       = session.getMapper( StudyMapper.class);
            int result              = mapper.createStudy( university, study);
            
            if( result == 0) {
                throw new HTTPErrorException( Response.Status.CONFLICT, "could not create study");
            }
            
            study = mapper.getStudy( university, study.getId());
            
            session.commit();
         }catch( HTTPErrorException e) {
            throw e;
        }catch( Exception e) {
            logger.info( e);
            throw new HTTPErrorException( Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }finally {
            session.close();
        }
        return study;
    }
    
      /* Particular Study */

    /**
      * Esta función gestiona las peticiones GET al recurso 
     * <code>universities/{uid}/studies/{id}</code>. Esta operación devuelve el 
     * estudio identificado por <b>id</b> dentro de la oferta académica de la 
     * universidad
     * @param id identificador del estudio
     * @param uriInfo  información de la URL de la petición
     * @return representación del estudio
     */
    
    
    @Path( "{id}")
    @GET
    @AuthenticationRequired( AuthenticationScheme.AUTHENTICATION_SCHEME_TOKEN)
    @Produces( { RepresentationFactory.HAL_JSON})
    public Study getStudy( @PathParam( "id") Long id, @Context UriInfo uriInfo) {
        
        logger.info("[GET] " + uriInfo.getPath());
        Study study = null;
        SqlSession session = MyBatisConnectionFactory.getSession().openSession();

        try {
            StudyMapper mapper  = session.getMapper( StudyMapper.class);
            study               = mapper.getStudy( university, id);
            
            if( study == null) {
                throw new HTTPErrorException( Status.NOT_FOUND, "study " + id + " not found");
            }
            
            session.commit();
         }catch( HTTPErrorException e) {
            throw e;
        }catch( Exception e) {
            logger.info( e);
            throw new HTTPErrorException( Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }finally {
            session.close();
        }
        return study;
    }
    
    /**
    * Esta función gestiona las peticiones PUT al recurso 
     * <code>universities/{uid}/studies/{id}</code>. Esta operación modifica los
     * datos del estudio identificado por <b>id</b> dentro de la oferta 
     * académica de la universidad
     * @param study datos del estudio a modificar
     * @param id identificador del estudio
     * @param sc información de seguridad y autenticación de la petición
     * @param uriInfo  información de la URL de la petición
     * @return representafción del estudio ya modificado
     */
    @Path( "{id}")
    @PUT
    @AuthenticationRequired( AuthenticationScheme.AUTHENTICATION_SCHEME_TOKEN)
    @Produces( { RepresentationFactory.HAL_JSON})
    @Consumes( { RepresentationFactory.HAL_JSON, MediaType.APPLICATION_JSON})
    public Study updateStudy( @Valid Study study, @PathParam( "id") Long id,  @Context SecurityContext sc, @Context UriInfo uriInfo) {
        
        logger.info( "[PUT] " + uriInfo.getPath());
        
        study.setId(id);
        
        UserPrincipal up = (UserPrincipal) sc.getUserPrincipal();
        
        if( !up.getId().equals( university.getId()))
            throw new HTTPErrorException( Status.FORBIDDEN, "You are not allowed here");
        
        
        SqlSession session = MyBatisConnectionFactory.getSession().openSession();

        try {
            StudyMapper mapper   = session.getMapper( StudyMapper.class);
            int result          = mapper.updateStudy( university, study);
            
            if( result == 0) {
                throw new HTTPErrorException( Status.INTERNAL_SERVER_ERROR, "Error updating study");
            }
            study = mapper.getStudy( university, study.getId());
            session.commit();
         }catch( HTTPErrorException e) {
            throw e;
        }catch( Exception e) {
            logger.info( e);
            throw new HTTPErrorException( Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }finally {
            session.close();
        }
        return study;
    }
    
    /**
     * Esta función gestiona las peticiones DELETE al recurso 
     * <code>universities/{uid}/studies/{id}</code>. Esta operación elimina 
     * el estudio identificado por <b>id</b> dentro de la oferta 
     * académica de la universidad
     * @param id identificador del estudio
     * @param sc información de seguridad y autenticación de la petición
     * @param uriInfo  información de la URL de la petición
     */
    @Path( "{id}")
    @DELETE
    @AuthenticationRequired( AuthenticationScheme.AUTHENTICATION_SCHEME_TOKEN)
    public void deleteStudy( @PathParam( "id") Long id,  @Context SecurityContext sc, @Context UriInfo uriInfo) {
        
        logger.info( "[DELETE] " + uriInfo.getPath());
        
        UserPrincipal up = (UserPrincipal) sc.getUserPrincipal();
        
        if( !up.getId().equals( university.getId()))
            throw new HTTPErrorException( Status.FORBIDDEN, "You are not allowed here");
        
        SqlSession session = MyBatisConnectionFactory.getSession().openSession();

        try {
            StudyMapper mapper  = session.getMapper( StudyMapper.class);
            Study study         = mapper.getStudy( university, id);
            
            if( study == null) {
                throw new HTTPErrorException( Status.NOT_FOUND, "Study " + id + " not found");
            }
            
            int result = mapper.deleteStudy( university, study.getId());
            
            if( result == 0) {
                throw new HTTPErrorException( Status.INTERNAL_SERVER_ERROR, "Error deleting study");
            }
            
            session.commit();
         }catch( HTTPErrorException e) {
            throw e;
        }catch( Exception e) {
            logger.info( e);
            throw new HTTPErrorException( Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }finally {
            session.close();
        }
    }
    
    /**
     * Esta función gestiona las peticiones GET al recurso 
     * <code>universities/{uid}/studies/{id}/students</code>. Esta operación 
     * devuelve un lista con los usuarios estudiantes del estudio identificado por
     * <b>id</b>
     * @param page página de los resultados
     * @param size tamaño de los resultados
     * @param q cadena de caracteres que sirve para filtrar por nombre los
     * resultados
     * @param id identificador del estudio
     * @param sc información de seguridad y autenticación de la petición
     * @param uriInfo  información de la URL de la petición
     * @return lista con los usuario que estudian en el estudio especificado
     * del tamaño especificado y filtrada
     * @throws UnsupportedEncodingException
     */
    @Path( "{id}/students")
    @GET
    @AuthenticationRequired( AuthenticationScheme.AUTHENTICATION_SCHEME_TOKEN)
    @Produces( { RepresentationFactory.HAL_JSON})
    @Encoded
    public HalList<User> getStudents( @PathParam( "id") Long id,  @QueryParam("page") Integer page, 
            @QueryParam( "size") Integer size, @QueryParam( "q") String q, 
            @Context UriInfo uriInfo, @Context SecurityContext sc)  throws UnsupportedEncodingException
    {
        
        logger.info( "[GET] " + uriInfo.getPath());
        
        HalList<User> users = null; 
        
        page = Utils.getValidPage(page);
        size = Utils.getValidSize(size);
        if( q != null) {
            q   = URLDecoder.decode(q, "UTF-8");
        }
        RowBounds rb = Utils.createRowBounds(page, size);
        UserPrincipal up = (UserPrincipal) sc.getUserPrincipal();
        SqlSession session = MyBatisConnectionFactory.getSession().openSession();

        try {
            StudyMapper mapper      = session.getMapper( StudyMapper.class);
            UserMapper userMapper   = session.getMapper( UserMapper.class);
            List<User> usersList    = mapper.getStudents( university, id, q, rb);
            int total               = mapper.getTotalStudents( university, id, q);
            
            for( User user : usersList) {
                Permission permissions = new Permission();
                userMapper.getPermissions( up.getId(), user.getId(), permissions);
                user.setPermission(permissions);
            }
            
            users = new HalList( usersList, total);
            
            users.setResourceRelativePath( uriInfo.getPath());
            users.setPage(page);
            users.setSize(size);
            users.setQuery(q);
            
            session.commit();
         }catch( HTTPErrorException e) {
            throw e;
        }catch( Exception e) {
            logger.info( e);
            throw new HTTPErrorException( Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }finally {
            session.close();
        }
        return users;
    }
    
}
