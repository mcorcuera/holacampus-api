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

package com.holacampus.api.resources.particular;

import com.holacampus.api.domain.City;
import com.holacampus.api.domain.CommentContainer;
import com.holacampus.api.domain.Permission;
import com.holacampus.api.domain.PhotoContainer;
import com.holacampus.api.domain.ProfilePhotoContainer;
import com.holacampus.api.domain.University;
import com.holacampus.api.domain.User;
import com.holacampus.api.exceptions.HTTPErrorException;
import com.holacampus.api.hal.HalList;
import com.holacampus.api.mappers.CityMapper;
import com.holacampus.api.mappers.UniversityMapper;
import com.holacampus.api.mappers.UserMapper;
import com.holacampus.api.security.AuthenticationRequired;
import com.holacampus.api.security.AuthenticationScheme;
import com.holacampus.api.security.PermissionScheme;
import com.holacampus.api.security.PermissionScheme.Action;
import com.holacampus.api.security.UserPrincipal;
import com.holacampus.api.subresources.CommentsResource;
import com.holacampus.api.subresources.PhotosResource;
import com.holacampus.api.subresources.ProfilePhotoResource;
import com.holacampus.api.subresources.StudiesResource;
import com.holacampus.api.utils.MyBatisConnectionFactory;
import com.holacampus.api.utils.Utils;
import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;
import com.theoryinpractise.halbuilder.api.RepresentationFactory;
import java.net.URLDecoder;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
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
 *
 * @author Mikel Corcuera <mik.corcuera@gmail.com>
 */

@Path( "/universities/{id}")
public class ParticularUniversityResource {
    
    private static final Logger logger = LogManager.getLogger( ParticularUniversityResource.class.getName());

    @Context
    private SecurityContext sc;
    
    @Context
    private UriInfo uriInfo;
    
    @PathParam( "id")
    private Long id;
    
    private static final PermissionScheme commentsScheme  = new PermissionScheme();
    private static final PermissionScheme photosScheme    = new PermissionScheme();
    private static final PermissionScheme photosCommentScheme   = new PermissionScheme();
    
    static {
        commentsScheme  .addPermissionScheme( Action.GET_MULTIPLE, Permission.LEVEL_USER)
                        .addPermissionScheme(Action.POST_MULTIPLE, Permission.LEVEL_USER)
                        .addPermissionScheme(Action.GET_UNIQUE, Permission.LEVEL_USER)
                        .addPermissionScheme(Action.DELETE_UNIQUE, Permission.LEVEL_PARENT_OWNER);
        
        photosScheme    .addPermissionScheme( Action.GET_MULTIPLE, Permission.LEVEL_USER)
                        .addPermissionScheme(Action.POST_MULTIPLE, Permission.LEVEL_PARENT_OWNER)
                        .addPermissionScheme(Action.GET_UNIQUE, Permission.LEVEL_USER)
                        .addPermissionScheme(Action.PUT_UNIQUE, Permission.LEVEL_OWNER)
                        .addPermissionScheme(Action.DELETE_UNIQUE, Permission.LEVEL_PARENT_OWNER);
        
        photosCommentScheme     .addPermissionScheme( Action.GET_MULTIPLE, Permission.LEVEL_USER)
                                .addPermissionScheme(Action.POST_MULTIPLE, Permission.LEVEL_USER)
                                .addPermissionScheme(Action.GET_UNIQUE, Permission.LEVEL_USER)
                                .addPermissionScheme(Action.DELETE_UNIQUE, Permission.LEVEL_PARENT_OWNER);
    }
    
    @Path("/comments")
    public CommentsResource getCommentResource() 
    {
        SqlSession session = MyBatisConnectionFactory.getSession().openSession();
        CommentContainer c;
        try {            
            UniversityMapper uniMapper   = session.getMapper( UniversityMapper.class);
            c = uniMapper.getCommentContainer(id);
            session.commit();            
            
            if( c == null) {
                throw new HTTPErrorException( Response.Status.NOT_FOUND, "University " + id + " not found");
            }
        } catch( Exception e) {
            logger.error( e.toString());
            throw new HTTPErrorException( Response.Status.NOT_FOUND, "University " + id + " not found");
        } finally {
            session.close();
        }
        
        return new CommentsResource( id, c, uriInfo.getPath(), commentsScheme);
    }
    
    @Path( "/photos")
    public PhotosResource getPhotosResource( )
    {
        SqlSession session = MyBatisConnectionFactory.getSession().openSession();
        PhotoContainer c;
        try {            
             UniversityMapper uniMapper   = session.getMapper( UniversityMapper.class);
            c = uniMapper.getPhotoContainer(id);
            session.commit();            
            
            if( c == null) {
                throw new HTTPErrorException( Response.Status.NOT_FOUND, "University " + id + " not found");
            }
        } catch( Exception e) {
            logger.error( e.toString());
            throw new HTTPErrorException( Response.Status.NOT_FOUND, "University " + id + " not found");
        } finally {
            session.close();
        }  
        
        return new PhotosResource( id, c, uriInfo.getPath(), photosScheme, photosCommentScheme);
    }
    
    @Path( "/profile-photo")
    public ProfilePhotoResource getProfilePhotoResource()
    {
        SqlSession session = MyBatisConnectionFactory.getSession().openSession();
        PhotoContainer pc;
        ProfilePhotoContainer ppc;
        try {            
            UniversityMapper uniMapper  = session.getMapper( UniversityMapper.class);
            pc                          = uniMapper.getPhotoContainer(id);
            ppc                         = uniMapper.getProfilePhotoContainer(id);
            
            if( pc == null) {
                throw new HTTPErrorException( Response.Status.NOT_FOUND, "University " + id + " not found");
            }
            
            session.commit();            

        } catch( Exception e) {
            logger.error( e.toString());
            throw new HTTPErrorException( Response.Status.NOT_FOUND, "University " + id + " not found");
        } finally {
            session.close();
        }  
        
        return new ProfilePhotoResource( pc, ppc, id, uriInfo.getPath());
    }
    
    @Path("/studies")
    public StudiesResource getStudiesResource()    
    {
        SqlSession session = MyBatisConnectionFactory.getSession().openSession();
        University university;
        try {            
            UniversityMapper uniMapper  = session.getMapper( UniversityMapper.class);
            university                  = uniMapper.getUniversity(id);
            
            if( university == null) {
                throw new HTTPErrorException( Response.Status.NOT_FOUND, "University " + id + " not found");
            }
            
            session.commit();            

        } catch( Exception e) {
            logger.error( e.toString());
            throw new HTTPErrorException( Response.Status.NOT_FOUND, "University " + id + " not found");
        } finally {
            session.close();
        }  
        
        return new StudiesResource( university);
    }
    
    @GET
    @AuthenticationRequired( AuthenticationScheme.AUTHENTICATION_SCHEME_TOKEN)
    @Produces( { RepresentationFactory.HAL_JSON})
    public University getUniversity( )
    {
        logger.info( "[GET] " + uriInfo.getPath());
        
        University university;
        UserPrincipal up = (UserPrincipal) sc.getUserPrincipal();
        
        SqlSession session = MyBatisConnectionFactory.getSession().openSession();
        
        try {            
            UniversityMapper uniMapper   = session.getMapper( UniversityMapper.class);
            university = uniMapper.getUniversity(id);      
            
            if( university == null) {
                throw new HTTPErrorException( Response.Status.NOT_FOUND, "University not found");
            }
            
            if( university.getProfilePhoto() != null) {
                university.getProfilePhoto().setProfilePhoto( true);
                university.getProfilePhoto().setSelfLink( uriInfo.getPath() + "/profile-photo");
            }
            
            Permission permission = new Permission();
            
            uniMapper.getPermissions( up.getId(), id, permission);
            university.setPermission(permission);
            
            session.commit();      
        }catch( HTTPErrorException e) {
            throw e;
        }catch( Exception e) {
            logger.error( e.toString());
            throw new HTTPErrorException( Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
        } finally {
            session.close();
        }   
        return university;
    }
    
    @Path( "/cities")
    @GET
    @AuthenticationRequired( AuthenticationScheme.AUTHENTICATION_SCHEME_TOKEN)
    @Produces( { RepresentationFactory.HAL_JSON})
    public HalList<City> getUniversityCities(@QueryParam("page") Integer page, 
            @QueryParam( "size") Integer size)
    {
        HalList<City> cities;
         
        page = Utils.getValidPage(page);
        size = Utils.getValidSize(size);
        RowBounds rb = Utils.createRowBounds(page, size);
        
        SqlSession session = MyBatisConnectionFactory.getSession().openSession();

        try {
            UniversityMapper uniMapper      = session.getMapper( UniversityMapper.class);
            University university           = uniMapper.getUniversity(id);
            
            if( university == null)
                throw new HTTPErrorException( Status.NOT_FOUND, "University " + id + " not found");
            
            List<City> cityList = uniMapper.getCities(id, rb);
            int total           = uniMapper.getTotalCities( id);
            session.commit();
            
            cities = new HalList<City> ( cityList, total);
            cities.setResourceRelativePath( uriInfo.getPath());
            cities.setPage(page);
            cities.setSize(size);
        }catch( HTTPErrorException e) {
            throw e;
        }catch( Exception e) {
            logger.info( e);
             throw new HTTPErrorException( Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }finally {
            session.close();
        }

        return cities;
    }
    
    @Path( "/cities")
    @POST
    @AuthenticationRequired( AuthenticationScheme.AUTHENTICATION_SCHEME_TOKEN)
    @Consumes( { RepresentationFactory.HAL_JSON, MediaType.APPLICATION_JSON})
    @Produces( { RepresentationFactory.HAL_JSON})
    public City addCity( City city)
    {
        if( city.getCountry() == null || city.getCountry().getId() == null)
            throw new HTTPErrorException( Status.BAD_REQUEST, "You must provide the city belonging country");
        
        UserPrincipal up = (UserPrincipal) sc.getUserPrincipal();
        
        if( !up.getId().equals( id))
            throw new HTTPErrorException( Status.FORBIDDEN, "You are not allowed here");
        
        SqlSession session = MyBatisConnectionFactory.getSession().openSession();

        try {
            UniversityMapper    uniMapper   = session.getMapper( UniversityMapper.class);
            CityMapper          cityMapper  = session.getMapper( CityMapper.class);    
            int result                      = uniMapper.insertCity(id, city);
            
            if( result == 0)
                throw new HTTPErrorException( Status.INTERNAL_SERVER_ERROR, "Problem inserting city");
            city = cityMapper.getCity( city.getCountry(), city.getId());
            session.commit();
         }catch( MySQLIntegrityConstraintViolationException e) {
            throw new HTTPErrorException( Status.CONFLICT, "City already exists in database");
        }catch( HTTPErrorException e) {
            throw e;
        }catch( Exception e) {
            logger.info( e);
            throw new HTTPErrorException( Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }finally {
            session.close();
        }
        
        return city;
    }
    
    @Path( "/cities")
    @DELETE
    @AuthenticationRequired( AuthenticationScheme.AUTHENTICATION_SCHEME_TOKEN)
    @Consumes( { RepresentationFactory.HAL_JSON, MediaType.APPLICATION_JSON})
    public void deleteCity( @QueryParam("city") long cityId, @QueryParam("country") long countryId)
    {
        UserPrincipal up = (UserPrincipal) sc.getUserPrincipal();
        
        if( !up.getId().equals( id))
            throw new HTTPErrorException( Status.FORBIDDEN, "You are not allowed here");
        
        SqlSession session = MyBatisConnectionFactory.getSession().openSession();

        try {
            UniversityMapper    uniMapper   = session.getMapper( UniversityMapper.class);
            int result                      = uniMapper.deleteCity(id, cityId, countryId);
            
            if( result == 0)
                throw new HTTPErrorException( Status.INTERNAL_SERVER_ERROR, "Problem deleting city");
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
}
