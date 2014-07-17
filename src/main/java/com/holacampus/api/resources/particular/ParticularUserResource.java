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

import com.holacampus.api.domain.CommentContainer;
import com.holacampus.api.domain.Permission;
import com.holacampus.api.domain.PhotoContainer;
import com.holacampus.api.domain.ProfilePhotoContainer;
import com.holacampus.api.domain.User;
import com.holacampus.api.exceptions.HTTPErrorException;
import com.holacampus.api.mappers.UserMapper;
import com.holacampus.api.security.AuthenticationRequired;
import com.holacampus.api.security.AuthenticationScheme;
import com.holacampus.api.security.PermissionScheme;
import com.holacampus.api.security.PermissionScheme.Action;
import com.holacampus.api.security.UserPrincipal;
import com.holacampus.api.subresources.CommentsResource;
import com.holacampus.api.subresources.FriendsResource;
import com.holacampus.api.subresources.PhotosResource;
import com.holacampus.api.subresources.ProfilePhotoResource;
import com.holacampus.api.subresources.StagesResource;
import com.holacampus.api.utils.MyBatisConnectionFactory;
import com.theoryinpractise.halbuilder.api.RepresentationFactory;
import java.util.Objects;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 *
 *  @author Mikel Corcuera <mik.corcuera@gmail.com>  
 */

@Path( "/users")
public class ParticularUserResource {
    
    private static final Logger logger = LogManager.getLogger( ParticularUserResource.class.getName());
    
    private static final PermissionScheme commentsScheme        = new PermissionScheme();
    private static final PermissionScheme photosScheme          = new PermissionScheme();
    private static final PermissionScheme photosCommentScheme   = new PermissionScheme();
    
    static {
        commentsScheme  .addPermissionScheme( Action.GET_MULTIPLE, Permission.LEVEL_MEMBER)
                        .addPermissionScheme(Action.POST_MULTIPLE, Permission.LEVEL_MEMBER)
                        .addPermissionScheme(Action.GET_UNIQUE, Permission.LEVEL_MEMBER)
                        .addPermissionScheme(Action.DELETE_UNIQUE, Permission.LEVEL_PARENT_OWNER);
        
        photosScheme    .addPermissionScheme( Action.GET_MULTIPLE, Permission.LEVEL_MEMBER)
                        .addPermissionScheme(Action.POST_MULTIPLE, Permission.LEVEL_PARENT_OWNER)
                        .addPermissionScheme(Action.GET_UNIQUE, Permission.LEVEL_MEMBER)
                        .addPermissionScheme(Action.PUT_UNIQUE, Permission.LEVEL_OWNER)
                        .addPermissionScheme(Action.DELETE_UNIQUE, Permission.LEVEL_PARENT_OWNER);
        
        photosCommentScheme     .addPermissionScheme( Action.GET_MULTIPLE, Permission.LEVEL_MEMBER)
                                .addPermissionScheme(Action.POST_MULTIPLE, Permission.LEVEL_MEMBER)
                                .addPermissionScheme(Action.GET_UNIQUE, Permission.LEVEL_MEMBER)
                                .addPermissionScheme(Action.DELETE_UNIQUE, Permission.LEVEL_PARENT_OWNER);
    }
    
    
    @Context
    private UriInfo uriInfo;
    @Context
    private SecurityContext sc;
    
    @Path("/{id}/comments")
    public CommentsResource getCommentResource( @PathParam("id") long id) 
    {
        SqlSession session = MyBatisConnectionFactory.getSession().openSession();
        CommentContainer c;
        try {            
            UserMapper userMapper   = session.getMapper( UserMapper.class);
            c = userMapper.getCommentContainer(id);
            session.commit();            
            
            if( c == null) {
                throw new HTTPErrorException( Status.NOT_FOUND, "User " + id + " not found");
            }
        } catch( Exception e) {
            logger.error( e.toString());
            throw new HTTPErrorException( Status.NOT_FOUND, "User " + id + " not found");
        } finally {
            session.close();
        }
        
        return new CommentsResource( id, c, uriInfo.getPath(), ParticularUserResource.commentsScheme);
    }
    
    @Path( "/{id}/photos")
    public PhotosResource getPhotosResource( @PathParam("id") long id)
    {
        SqlSession session = MyBatisConnectionFactory.getSession().openSession();
        PhotoContainer c;
        try {            
            UserMapper userMapper   = session.getMapper( UserMapper.class);
            c = userMapper.getPhotoContainer(id);
            session.commit();            
            
            if( c == null) {
                throw new HTTPErrorException( Status.NOT_FOUND, "User " + id + " not found");
            }
        } catch( Exception e) {
            logger.error( e.toString());
            throw new HTTPErrorException( Status.NOT_FOUND, "User " + id + " not found");
        } finally {
            session.close();
        }  
        
        return new PhotosResource( id, c, uriInfo.getPath(), photosScheme, photosCommentScheme);
    }
    
     @Path( "/{id}/profile-photo")
    public ProfilePhotoResource getProfilePhotoResource( @PathParam("id") long id)
    {
        SqlSession session = MyBatisConnectionFactory.getSession().openSession();
        PhotoContainer pc;
        ProfilePhotoContainer ppc;
        try {            
            UserMapper userMapper   = session.getMapper( UserMapper.class);
            pc                      = userMapper.getPhotoContainer(id);
            ppc                     = userMapper.getProfilePhotoContainer(id);
            
            if( pc == null) {
                throw new HTTPErrorException( Status.NOT_FOUND, "User " + id + " not found");
            }
            
            session.commit();            

        } catch( Exception e) {
            logger.error( e.toString());
            throw new HTTPErrorException( Status.NOT_FOUND, "User " + id + " not found");
        } finally {
            session.close();
        }  
        
        return new ProfilePhotoResource( pc, ppc, id, uriInfo.getPath());
    }
    
    @Path( "/{id}/friends")
    public FriendsResource getFriendsResource( @PathParam("id") Long id)
    {
        SqlSession session = MyBatisConnectionFactory.getSession().openSession();

        try {
            UserMapper mapper    = session.getMapper( UserMapper.class);
            User user = mapper.getUser(id);
            if( user == null)
                throw new HTTPErrorException( Status.NOT_FOUND, "User not found");
            session.commit();
        }catch( HTTPErrorException e) {
            throw e;
        }catch( Exception e) {
            logger.info( e);
            throw new InternalServerErrorException( e.getMessage());
        }finally {
            session.close();
        }

        return new FriendsResource( id);
    }
    
    @Path( "/{id}/stages")
    public StagesResource getStagesResource( @PathParam("id") Long id)
    {
        SqlSession session = MyBatisConnectionFactory.getSession().openSession();
        User user = null;
        try {
            UserMapper mapper    = session.getMapper( UserMapper.class);
            user = mapper.getUser(id);
            if( user == null)
                throw new HTTPErrorException( Status.NOT_FOUND, "User not found");
            session.commit();
        }catch( HTTPErrorException e) {
            throw e;
        }catch( Exception e) {
            logger.info( e);
            throw new InternalServerErrorException( e.getMessage());
        }finally {
            session.close();
        }

        return new StagesResource( user);
    }
    
    
    @GET
    @Path("/{id}")
    @AuthenticationRequired( AuthenticationScheme.AUTHENTICATION_SCHEME_TOKEN)
    @Produces( { RepresentationFactory.HAL_JSON})
    public User getUser( @PathParam("id") long id)
    {
        logger.info( "[GET] " + uriInfo.getPath());
        
        User user;
        UserPrincipal up = (UserPrincipal) sc.getUserPrincipal();
        
        SqlSession session = MyBatisConnectionFactory.getSession().openSession();
        
        try {            
            UserMapper userMapper   = session.getMapper( UserMapper.class);
            user = userMapper.getUser(id);      
            
            if( user == null) {
                throw new HTTPErrorException( Status.NOT_FOUND, "User not found");
            }
            
            if( user.getProfilePhoto() != null) {
                user.getProfilePhoto().setProfilePhoto( true);
                user.getProfilePhoto().setSelfLink( uriInfo.getPath() + "/profile-photo");
            }
            
            Permission permission = new Permission();
            
            userMapper.getPermissions( up.getId(), id, permission);
            user.setPermission(permission);
            session.commit();      
        } catch( Exception e) {
            logger.error( e.toString());
            throw new HTTPErrorException( Status.NOT_FOUND, "User not found");
        } finally {
            session.close();
        }   
        return user;
    }
     
    @DELETE
    @Path("/{id}")
    @AuthenticationRequired( AuthenticationScheme.AUTHENTICATION_SCHEME_BASIC)
    public void deleteUser( @PathParam( "id") Long id, @Context SecurityContext sc)
    {
        /*
        * Only the user authenticated can delete himself, so we check it here
        */
        if( Objects.equals(id, ((UserPrincipal) sc.getUserPrincipal()).getId())) {
            SqlSession session = MyBatisConnectionFactory.getSession().openSession();
            try {
                UserMapper mapper    = session.getMapper( UserMapper.class);
                int result           = mapper.deleteUser(id);
                
                if( result == 0) {
                    throw new HTTPErrorException( Status.INTERNAL_SERVER_ERROR);
                }
                session.commit();
            }catch( Exception e) {
                logger.info( e);
                throw new InternalServerErrorException( e.getMessage());
            }finally {
                session.close();
            }
        }else {
            throw new HTTPErrorException( Status.UNAUTHORIZED, "You are not authorized");
        }
    }
}
