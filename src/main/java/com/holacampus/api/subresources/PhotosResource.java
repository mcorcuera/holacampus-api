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

import com.holacampus.api.domain.CommentContainer;
import com.holacampus.api.domain.Permission;
import com.holacampus.api.domain.Photo;
import com.holacampus.api.domain.PhotoContainer;
import com.holacampus.api.domain.User;
import com.holacampus.api.exceptions.HTTPErrorException;
import com.holacampus.api.hal.HalList;
import com.holacampus.api.mappers.PhotoContainerMapper;
import com.holacampus.api.mappers.PhotoMapper;
import com.holacampus.api.security.AuthenticationRequired;
import com.holacampus.api.security.AuthenticationScheme;
import com.holacampus.api.security.PermissionScheme;
import com.holacampus.api.security.PermissionScheme.Action;
import com.holacampus.api.security.UserPrincipal;
import com.holacampus.api.utils.MyBatisConnectionFactory;
import com.holacampus.api.utils.PhotoUploader;
import com.holacampus.api.utils.PhotoUploader.PhotoUrls;
import com.holacampus.api.utils.Utils;
import com.holacampus.api.validators.CreationValid;
import com.theoryinpractise.halbuilder.api.RepresentationFactory;
import java.util.List;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.InternalServerErrorException;
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
 *
 * @author Mikel Corcuera <mik.corcuera@gmail.com>
 */

public class PhotosResource {
    
    private static final Logger logger = LogManager.getLogger( PhotosResource.class.getName());

    private PhotoContainer          container;
    private long                    elId;
    private String                  path;
    private final PermissionScheme  permissionScheme;
    private final PermissionScheme  commentsScheme;
    
    public PhotosResource( long id, PhotoContainer pc, String path, PermissionScheme scheme, PermissionScheme commentScheme)
    {
        this.container          = pc;
        this.elId               = id;
        this.path               = path;
        this.permissionScheme   = scheme;
        this.commentsScheme     = commentScheme;
    }  
    
    @GET
    @AuthenticationRequired( AuthenticationScheme.AUTHENTICATION_SCHEME_TOKEN)
    @Produces( { RepresentationFactory.HAL_JSON})
    public HalList<Photo> getPhotos(  @QueryParam("page") Integer page, 
            @QueryParam( "size") Integer size, @Context SecurityContext sc) 
    {
        logger.info("[GET] " + path);
        
        HalList<Photo> photos           = null;
        Permission containerPermission  = new Permission();
        UserPrincipal up                = (UserPrincipal)sc.getUserPrincipal();
        
        page            = Utils.getValidPage(page);
        size            = Utils.getValidSize(size);
        RowBounds rb    = Utils.createRowBounds(page, size);
        
        SqlSession session = MyBatisConnectionFactory.getSession().openSession();

        try {
            PhotoMapper mapper                      = session.getMapper(PhotoMapper.class);
            PhotoContainerMapper containerMapper    = session.getMapper( PhotoContainerMapper.class);
            List<Photo> photoList                   = mapper.getPhotos( container.getId(), rb);
            int total                               = mapper.getTotalPhotos( container.getId());
            
            // Get container permission level
            containerMapper.getPermissions( up.getId(), container.getId(), containerPermission);
             
            if( !permissionScheme.isAllowed(PermissionScheme.Action.GET_MULTIPLE, containerPermission.getLevel())) {
                throw new HTTPErrorException( Status.FORBIDDEN, "You are not allowed here");
            }
            session.commit();
            
            /* Add permission level */
            for( Photo photo : photoList) {
                
                logger.info("Current user: " + up.getId() + " creator: " + photo.getCreator().getId());
                Permission permissions = new Permission( containerPermission.getLevel());
                if( up.getId() ==  photo.getCreator().getId()) {
                     permissions.setLevel( Permission.LEVEL_OWNER);

                }
                photo.setPermission(permissions);
                
                photo.setSelfLink( path + "/" + photo.getId());
            }
            
            photos = new HalList<Photo>( photoList, total);
            photos.setResourceRelativePath(path);
            photos.setPage(page);
            photos.setSize(size);
            
        }catch( HTTPErrorException e){
            throw e;
        }catch (Exception e) {
            throw new HTTPErrorException( Status.INTERNAL_SERVER_ERROR, "Internal server error: " + e);
        }finally {
            session.close();
        }
        
        return photos;
    }
    
    @POST
    @AuthenticationRequired( AuthenticationScheme.AUTHENTICATION_SCHEME_TOKEN)
    @Consumes( { RepresentationFactory.HAL_JSON, MediaType.APPLICATION_JSON})
    @Produces( { RepresentationFactory.HAL_JSON})
    public Photo postPhoto( @CreationValid @Valid Photo photo, @Context SecurityContext sc) {
        
        logger.info( "[POST] " + path);
        UserPrincipal up                = (UserPrincipal) sc.getUserPrincipal();
        Permission containerPermission  = new Permission();
        SqlSession session              = MyBatisConnectionFactory.getSession().openSession();

        try {
            PhotoMapper mapper                      = session.getMapper( PhotoMapper.class);
            PhotoContainerMapper containerMapper    = session.getMapper( PhotoContainerMapper.class);
            
             // Get container permission level
            containerMapper.getPermissions( up.getId(), container.getId(), containerPermission);
             
            if( !permissionScheme.isAllowed(PermissionScheme.Action.POST_MULTIPLE, containerPermission.getLevel())) {
                throw new HTTPErrorException( Status.FORBIDDEN, "You are not allowed here");
            }
            
            // Store the image in the corresponding folder
            PhotoUrls urls = PhotoUploader.uploadPhoto( photo.getData());
            
            if( urls == null) {
                throw new HTTPErrorException( Status.BAD_REQUEST, "unknown photo format");
            }           
            photo.setUrl(urls.url);
            // TODO Generate thumbnail Image
            photo.setThumbnailUrl(urls.thumbnailUrl);
            photo.setCreator( new User(up));
            photo.setPhotoContainer( container);
            //Start DB transaction
            int result          = mapper.createPhoto(photo);
            
            if( result == 0)
                throw new HTTPErrorException( Status.BAD_REQUEST, "problems creating photo");
            
            photo.setPermission( new Permission( Permission.LEVEL_OWNER));
            photo.setSelfLink( path + "/" + photo.getId());
            
            session.commit();
        }catch( HTTPErrorException e) {
            throw e;
        }catch (Exception e) {
            throw new HTTPErrorException( Status.INTERNAL_SERVER_ERROR, "Internal server error: " + e);
        }finally {
            session.close();
        }
        return photo;
    }
    
    // Photo detail
    @Path("{id}/comments")
    public CommentsResource getCommentResource( @PathParam("id") long id, @Context UriInfo uriInfo) 
    {
        SqlSession session = MyBatisConnectionFactory.getSession().openSession();
        CommentContainer c;
        
        try {            
            PhotoMapper mapper   = session.getMapper( PhotoMapper.class);
            c = mapper.getCommentContainer(id);
            session.commit();            
            
            logger.info( "Comment container get: " + c);
            
            if( c == null) {
                throw new HTTPErrorException( Response.Status.NOT_FOUND, "Comment " + id + " not found");
            }else if( c.getId() == null) {
                throw new HTTPErrorException( Response.Status.METHOD_NOT_ALLOWED, "Comment " + id + " is a recomment");
            }
            
        }catch( HTTPErrorException e) {
            throw e;
        } 
        catch( Exception e) {
            logger.error( e.toString());
            throw new InternalServerErrorException(e);
        } finally {
            session.close();
        }   
        
        return new CommentsResource( id, c, uriInfo.getPath(), commentsScheme);
    }
    
    @Path("/{id}")
    @GET
    @AuthenticationRequired( AuthenticationScheme.AUTHENTICATION_SCHEME_TOKEN)
    @Produces( { RepresentationFactory.HAL_JSON})
    public Photo getPhoto( @PathParam("id") Long id, @Context SecurityContext sc, @Context UriInfo uriInfo) 
    {
        logger.info( "[GET] " + uriInfo.getPath());
        Photo photo;
        UserPrincipal up = (UserPrincipal) sc.getUserPrincipal();
        Permission permission = new Permission();
        
        SqlSession session = MyBatisConnectionFactory.getSession().openSession();

        try {
            PhotoMapper mapper    = session.getMapper( PhotoMapper.class);
            photo                 = mapper.getPhoto(id);
            
            if( photo == null)
                throw new HTTPErrorException( Response.Status.NOT_FOUND, "comment " + id + " not found");
           
            mapper.getPermissions(up.getId(), photo.getId(), permission);
            
            if( !permissionScheme.isAllowed(PermissionScheme.Action.GET_UNIQUE, permission.getLevel())) {
                throw new HTTPErrorException( Status.FORBIDDEN, "You are not allowed here");
            }
            
            photo.setPermission(permission);
            
            photo.setSelfLink( uriInfo.getPath());
            
            photo.getCreator().getProfilePhoto().setProfilePhoto(true);
            session.commit();
            
        }catch( HTTPErrorException e) {
            throw e;
        }catch( Exception e) {
            logger.info( e);
            throw new InternalServerErrorException( e.getMessage());
        }finally {
            session.close();
        }
        
        return photo;
    }
    
    @Path("/{id}")
    @PUT
    @AuthenticationRequired( AuthenticationScheme.AUTHENTICATION_SCHEME_TOKEN)
    @Produces( { RepresentationFactory.HAL_JSON})
    @Consumes( { RepresentationFactory.HAL_JSON, MediaType.APPLICATION_JSON})
    public Photo updatePhoto( @Valid Photo photo, @PathParam("id") Long id, @Context SecurityContext sc, @Context UriInfo uriInfo) 
    {
        UserPrincipal up = (UserPrincipal) sc.getUserPrincipal();
        Permission permission = new Permission();
        
        SqlSession session = MyBatisConnectionFactory.getSession().openSession();

        try {
            PhotoMapper mapper  = session.getMapper( PhotoMapper.class);
            photo.setId(id);
                        
            mapper.getPermissions(up.getId(), photo.getId(), permission);
            
            if( !permissionScheme.isAllowed(PermissionScheme.Action.PUT_UNIQUE, permission.getLevel())) {
                throw new HTTPErrorException( Status.FORBIDDEN, "You are not allowed here");
            }
            
            int result = mapper.updatePhoto(photo);
            
            if( result == 0) {
                throw new HTTPErrorException( Response.Status.NOT_FOUND, "photo " + id + " not found");
            }
            
            photo = mapper.getPhoto( photo.getId());
            photo.setPermission(permission);
            
            photo.setSelfLink( uriInfo.getPath());
            
            session.commit();
            
        }catch( HTTPErrorException e) {
            throw e;
        }catch( Exception e) {
            logger.info( e);
            throw new InternalServerErrorException( e.getMessage());
        }finally {
            session.close();
        }
         
        return photo;
    }
    
    @Path("/{id}")
    @DELETE
    @AuthenticationRequired( AuthenticationScheme.AUTHENTICATION_SCHEME_TOKEN)
    public void deleteComment( @PathParam("id") Long id, @Context SecurityContext sc)
    {
        Photo photo;
        UserPrincipal up = (UserPrincipal) sc.getUserPrincipal();
        Permission permission = new Permission();
        
        SqlSession session = MyBatisConnectionFactory.getSession().openSession();
        
        try {
            PhotoMapper mapper  = session.getMapper( PhotoMapper.class);
            photo               = mapper.getPhoto(id);
            
            if( photo == null)
                throw new HTTPErrorException( Response.Status.NOT_FOUND, "comment " + id + " not found");
            
            
            mapper.getPermissions(up.getId(), photo.getId(), permission);
            
            // If the user doesn't  own the photo or the parent, throw 403 HTTP error
            if( !permissionScheme.isAllowed(PermissionScheme.Action.DELETE_UNIQUE, permission.getLevel())) {
                throw new HTTPErrorException( Status.FORBIDDEN, "You are not allowed here");
            }
            //If we own the comment, delete it
            int result = mapper.deletePhoto(photo.getId());
            
            if( result <= 0) {
                 throw new HTTPErrorException( Response.Status.CONFLICT, "comment " + id + " could not be deleted");
            }
            session.commit();
            
        }catch( HTTPErrorException e) {
            throw e;
        }catch( Exception e) {
            logger.info( e);
            throw new InternalServerErrorException( e.getMessage());
        }finally {
            session.close();
        }
    }
}
