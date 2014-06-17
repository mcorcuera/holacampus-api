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

import com.holacampus.api.subresources.CommentsResource;
import com.holacampus.api.domain.CommentContainer;
import com.holacampus.api.domain.User;
import com.holacampus.api.exceptions.HTTPErrorException;
import com.holacampus.api.mappers.UserMapper;
import com.holacampus.api.security.AuthenticationRequired;
import com.holacampus.api.security.AuthenticationScheme;
import com.holacampus.api.utils.MyBatisConnectionFactory;
import com.theoryinpractise.halbuilder.api.RepresentationFactory;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response.Status;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 *
 *  @author Mikel Corcuera <mik.corcuera@gmail.com>  
 */

@Path( "/users/{id}")
public class ParticularUserResource {
    
    private static final Logger logger = LogManager.getLogger( ParticularUserResource.class.getName());
    
    @Path("/comments")
    public CommentsResource getCommentResource( @PathParam("id") long id) {
        
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
        
        return new CommentsResource( id, c, "/users");
    }
    
    @GET
    @AuthenticationRequired( AuthenticationScheme.AUTHENTICATION_SCHEME_TOKEN)
    @Produces( { RepresentationFactory.HAL_JSON})
    public User getUser( @PathParam("id") long id)
    {
        logger.info( "[GET] /users/\n" + id);
        
        User user;
        SqlSession session = MyBatisConnectionFactory.getSession().openSession();
        
        try {            
            UserMapper userMapper   = session.getMapper( UserMapper.class);
            user = userMapper.getUser(id);
            session.commit();            
            
            if( user == null) {
                throw new HTTPErrorException( Status.NOT_FOUND, "User not found");
            }
            
        } catch( Exception e) {
            logger.error( e.toString());
            throw new HTTPErrorException( Status.NOT_FOUND, "User not found");
        } finally {
            session.close();
        }   
        return user;
    }
}
