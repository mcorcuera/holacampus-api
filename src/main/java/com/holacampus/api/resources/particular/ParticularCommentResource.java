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

import com.holacampus.api.domain.Comment;
import com.holacampus.api.domain.CommentContainer;
import com.holacampus.api.domain.Permission;
import com.holacampus.api.exceptions.HTTPErrorException;
import com.holacampus.api.mappers.CommentMapper;
import com.holacampus.api.security.AuthenticationRequired;
import com.holacampus.api.security.AuthenticationScheme;
import com.holacampus.api.security.UserPrincipal;
import com.holacampus.api.subresources.CommentsResource;
import com.holacampus.api.utils.MyBatisConnectionFactory;
import com.theoryinpractise.halbuilder.api.RepresentationFactory;
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
 * @author Mikel Corcuera <mik.corcuera@gmail.com>
 */

@Path("comments/{id}")
public class ParticularCommentResource {
    
    private static final Logger logger = LogManager.getLogger( ParticularCommentResource.class.getName());
    
    @Context
    private UriInfo uriInfo;
    
    @Context
    private SecurityContext sc;
    
    @Path("/recomments")
    public CommentsResource getCommentResource( @PathParam("id") long id) {
        
        SqlSession session = MyBatisConnectionFactory.getSession().openSession();
        CommentContainer c;
        
        try {            
            CommentMapper mapper   = session.getMapper( CommentMapper.class);
            c = mapper.getCommentContainer(id);
            session.commit();            
            
            logger.info( "Comment container get: " + c);
            
            if( c == null) {
                throw new HTTPErrorException( Status.NOT_FOUND, "Comment " + id + " not found");
            }else if( c.getId() == null) {
                throw new HTTPErrorException( Status.METHOD_NOT_ALLOWED, "Comment " + id + " is a recomment");
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
        
        return new CommentsResource( id, c, uriInfo.getPath());
    }
    
    @GET
    @AuthenticationRequired( AuthenticationScheme.AUTHENTICATION_SCHEME_TOKEN)
    @Produces( { RepresentationFactory.HAL_JSON})
    public Comment getComment( @PathParam("id") Long id)
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
                throw new HTTPErrorException( Status.NOT_FOUND, "comment " + id + " not found");
           
            mapper.getPermissions(up.getId(), comment.getId(), permission);
            
            comment.setPermission(permission);
            
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
                throw new HTTPErrorException( Status.NOT_FOUND, "comment " + id + " not found");
            
            mapper.getPermissions(up.getId(), comment.getId(), permission);
            logger.info("Permisos borrar: " + permission.getLevel());
            // If we don't own the comment, throw 403 HTTP erro
            if( !permission.getLevel().equals(Permission.LEVEL_OWNER) && !permission.getLevel().equals(Permission.LEVEL_PARENT_OWNER))
                throw new HTTPErrorException( Status.FORBIDDEN, "you don't have permissions to delete the comment " + id);
            
            //If we own the comment, delete it
            int result = mapper.deleteComment( comment.getId());
            
            if( result <= 0) {
                 throw new HTTPErrorException( Status.CONFLICT, "comment " + id + " could not be deleted");
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
