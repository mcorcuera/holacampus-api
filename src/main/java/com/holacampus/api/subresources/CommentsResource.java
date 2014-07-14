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

import com.holacampus.api.domain.Comment;
import com.holacampus.api.domain.CommentContainer;
import com.holacampus.api.domain.Container;
import com.holacampus.api.domain.Permission;
import com.holacampus.api.domain.User;
import com.holacampus.api.exceptions.HTTPErrorException;
import com.holacampus.api.hal.HalList;
import com.holacampus.api.mappers.CommentContainerMapper;
import com.holacampus.api.mappers.CommentMapper;
import com.holacampus.api.security.AuthenticationRequired;
import com.holacampus.api.security.AuthenticationScheme;
import com.holacampus.api.security.PermissionScheme;
import com.holacampus.api.security.PermissionScheme.Action;
import com.holacampus.api.security.UserPrincipal;
import com.holacampus.api.utils.MyBatisConnectionFactory;
import com.holacampus.api.utils.Utils;
import com.holacampus.api.validators.CreationValid;
import com.theoryinpractise.halbuilder.api.RepresentationFactory;
import java.util.List;
import java.util.Objects;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.InternalServerErrorException;
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
public class CommentsResource {
    
    private static final Logger logger = LogManager.getLogger( CommentsResource.class.getName());
 
    private final CommentContainer  container;
    private final String            path;
    private Long                    elId;
    private final PermissionScheme  permissionScheme;
    
    public CommentsResource( long id, CommentContainer c, String path, PermissionScheme scheme)
    {
        this.elId               = id;
        this.container          = c;
        this.path               = path;
        this.permissionScheme   = scheme;
    }
    
    @GET
    @AuthenticationRequired( AuthenticationScheme.AUTHENTICATION_SCHEME_TOKEN)
    @Produces( { RepresentationFactory.HAL_JSON})
    public HalList<Comment> getComments( @QueryParam("page") Integer page, @QueryParam( "size") Integer size, 
            @Context SecurityContext sc, @Context UriInfo uriInfo)
    {
        logger.info("[GET] " + path);

        HalList<Comment> comments   = null;
        Permission containerPermissions = new Permission();
        
        page            = Utils.getValidPage(page);
        size            = Utils.getValidSize(size);
        RowBounds rb    = Utils.createRowBounds(page, size);
        
        SqlSession session = MyBatisConnectionFactory.getSession().openSession();
        
        try {
            CommentMapper mapper                        = session.getMapper( CommentMapper.class);
            CommentContainerMapper  containerMapper     = session.getMapper( CommentContainerMapper.class);
            List<Comment> commentList                   = mapper.getComments( container.getId(), rb);
            int total                                   = mapper.getTotalComments( container.getId());
            
            // Get container permission level
           containerMapper.getPermissions( ((UserPrincipal)sc.getUserPrincipal()).getId(), container.getId(), containerPermissions);
           
           if( !permissionScheme.isAllowed(Action.GET_MULTIPLE, containerPermissions.getLevel())) {
               throw new HTTPErrorException( Status.FORBIDDEN, "You are not allowed here");
           }
           
           session.commit();
            
            /* Add permission level */
            for( Comment comment : commentList) {
               
                Permission permissions = new Permission( containerPermissions.getLevel());;
                
                //If current user is the creator of the resource, override permission
                if( Objects.equals(comment.getCreator().getId(), ((UserPrincipal)sc.getUserPrincipal()).getId())) {
                    permissions.setLevel( Permission.LEVEL_OWNER);
                }
                //Set resource permission
                comment.setPermission( permissions);
                
                comment.setSelfLink( uriInfo.getPath() + "/" + comment.getId());
            }
            
            comments = new HalList<Comment>( commentList, total);
            comments.setResourceRelativePath(path);
            comments.setPage(page);
            comments.setSize(size);
        }catch( HTTPErrorException e) {
            throw e;
        }catch( Exception e) {
            logger.info( e);
            throw new InternalServerErrorException( e.getMessage());
        }finally {
            session.close();
        } 
        return comments;
    }
    
    @POST
    @AuthenticationRequired( AuthenticationScheme.AUTHENTICATION_SCHEME_TOKEN)
    @Consumes( { RepresentationFactory.HAL_JSON, MediaType.APPLICATION_JSON})
    @Produces( { RepresentationFactory.HAL_JSON})
    public Comment postNewComment( @CreationValid @Valid Comment comment, @Context SecurityContext sc, @Context UriInfo uriInfo)
    {
        logger.info("[POST] " + path);
        
        comment.setCreator( new User( (UserPrincipal) sc.getUserPrincipal()));
        comment.setBelongingCommentContainer(container);
        
        /*
        * Check if the comment is being made over another comment
        */
        if( container.getType() == Container.ElementType.COMMENT)
            comment.setIsRecomment(true);
        else
            comment.setIsRecomment(false);
        
        Permission containerPermissions = new Permission();
        
        SqlSession session = MyBatisConnectionFactory.getSession().openSession();

        try {
            CommentMapper mapper                        = session.getMapper( CommentMapper.class);
            CommentContainerMapper  containerMapper     = session.getMapper( CommentContainerMapper.class);
            
            // Get container permission level
            containerMapper.getPermissions( ((UserPrincipal)sc.getUserPrincipal()).getId(), container.getId(), containerPermissions);
           
            if( !permissionScheme.isAllowed(Action.POST_MULTIPLE, containerPermissions.getLevel())) {
                throw new HTTPErrorException( Status.FORBIDDEN, "You are not allowed here");
            }
            
            int result              = mapper.createComment(comment);
            comment                 = mapper.getComment( comment.getId());
            session.commit();
            
            if( result == 0 || comment.getId() == null) {
                logger.info( "Problem creating comment");
                throw new Exception( "Error while creating the comment");
            }
            // The current user is the creator of the comment
            comment.setPermission( new Permission( Permission.LEVEL_OWNER));
            comment.setSelfLink( uriInfo.getPath() + "/" + comment.getId());
        }catch( HTTPErrorException e) {
            throw e;
        }catch( Exception e) {
            logger.info( e);
            throw new InternalServerErrorException( e.getMessage());
        }finally {
            session.close();
        }
        
        return comment;
    }
    
    // Comment Detail resource
    
    @Path("/{id}/recomments")
    public CommentsResource getCommentResource( @PathParam("id") long id, @Context UriInfo uriInfo) {
        
        SqlSession session = MyBatisConnectionFactory.getSession().openSession();
        CommentContainer c;
        
        try {            
            CommentMapper mapper   = session.getMapper( CommentMapper.class);
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
        
        PermissionScheme recommentsScheme  = new PermissionScheme();
    
        recommentsScheme  .addPermissionScheme( Action.GET_MULTIPLE, permissionScheme.getPermissionLevel(Action.GET_MULTIPLE))
                        .addPermissionScheme(Action.POST_MULTIPLE,  permissionScheme.getPermissionLevel(Action.POST_MULTIPLE))
                        .addPermissionScheme(Action.GET_UNIQUE,     permissionScheme.getPermissionLevel(Action.GET_UNIQUE))
                        .addPermissionScheme(Action.DELETE_UNIQUE, Permission.LEVEL_PARENT_OWNER);
    
    
        return new CommentsResource( id, c, uriInfo.getPath(), recommentsScheme);
    }
    
    @Path("/{id}")
    @GET
    @AuthenticationRequired( AuthenticationScheme.AUTHENTICATION_SCHEME_TOKEN)
    @Produces( { RepresentationFactory.HAL_JSON})
    public Comment getComment( @PathParam("id") Long id, @Context SecurityContext sc, @Context UriInfo uriInfo)
    {
        logger.info( "[GET] /comments/" + id);
        Comment comment;
        UserPrincipal up = (UserPrincipal) sc.getUserPrincipal();
        Permission permission = new Permission();
        
        SqlSession session = MyBatisConnectionFactory.getSession().openSession();

        try {
            CommentMapper mapper    = session.getMapper( CommentMapper.class);
            comment                 = mapper.getComment(id);
            
            if( comment == null)
                throw new HTTPErrorException( Response.Status.NOT_FOUND, "comment " + id + " not found");
           
            mapper.getPermissions(up.getId(), comment.getId(), permission);
            
            if( !permissionScheme.isAllowed(Action.GET_UNIQUE, permission.getLevel())) {
                throw new HTTPErrorException( Status.FORBIDDEN, "You are not allowed here");
            }
            
            comment.setPermission(permission);
            
            comment.setSelfLink( uriInfo.getPath());
            
            session.commit();
            
        }catch( HTTPErrorException e) {
            throw e;
        }catch( Exception e) {
            logger.info( e);
            throw new InternalServerErrorException( e.getMessage());
        }finally {
            session.close();
        }
        
        return comment;
    }
    
    @Path("/{id}")
    @DELETE
    @AuthenticationRequired( AuthenticationScheme.AUTHENTICATION_SCHEME_TOKEN)
    public void deleteComment( @PathParam("id") Long id, @Context SecurityContext sc)
    {
        Comment comment;
        UserPrincipal up = (UserPrincipal) sc.getUserPrincipal();
        Permission permission = new Permission();
        
        SqlSession session = MyBatisConnectionFactory.getSession().openSession();
        
        try {
            CommentMapper mapper    = session.getMapper( CommentMapper.class);
            comment                 = mapper.getComment(id);
            
            if( comment == null)
                throw new HTTPErrorException( Response.Status.NOT_FOUND, "comment " + id + " not found");
            
            mapper.getPermissions(up.getId(), comment.getId(), permission);
            
            // If we don't have permissions, throw 403 HTTP error
            if( !permissionScheme.isAllowed(Action.DELETE_UNIQUE, permission.getLevel())) {
                throw new HTTPErrorException( Status.FORBIDDEN, "You are not allowed here");
            }
            //If we own the comment, delete it
            int result = mapper.deleteComment( comment.getId());
            
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
