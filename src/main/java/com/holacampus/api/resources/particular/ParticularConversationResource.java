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

import com.holacampus.api.domain.Conversation;
import com.holacampus.api.exceptions.HTTPErrorException;
import com.holacampus.api.hal.HalList;
import com.holacampus.api.mappers.ConversationMapper;
import com.holacampus.api.resources.ConversationResource;
import com.holacampus.api.security.AuthenticationRequired;
import com.holacampus.api.security.AuthenticationScheme;
import com.holacampus.api.security.UserPrincipal;
import com.holacampus.api.subresources.ConversationMemberResource;
import com.holacampus.api.subresources.MessagesResource;
import com.holacampus.api.utils.MyBatisConnectionFactory;
import com.theoryinpractise.halbuilder.api.RepresentationFactory;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
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
@Path( "/conversations/{id}")
public class ParticularConversationResource {
    
    
    private static final Logger logger = LogManager.getLogger( ParticularConversationResource.class.getName());
    
    @Context
    private SecurityContext sc;
    
    @Context
    private UriInfo uriInfo;
    
    @PathParam( "id")
    private Long id;
    
    @Path( "/messages")
    public MessagesResource getMessagesResource()
    {
        Conversation conversation = null;
        
        SqlSession session                  = MyBatisConnectionFactory.getSession().openSession();

        try {
            ConversationMapper mapper   = session.getMapper( ConversationMapper.class);
            
            conversation                = mapper.getPlainConversation( id);
            
            if( conversation == null)
                throw new HTTPErrorException( Status.NOT_FOUND, "Conversation " + id + " not found");
            
            session.commit();
         }catch( HTTPErrorException e) {
            throw e;
        }catch( Exception e) {
            logger.info( e);
            throw new HTTPErrorException( Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }finally {
            session.close();
        }
        
        return new MessagesResource( conversation);
    }
    
     @Path( "/members")
    public ConversationMemberResource getConversationMemberResource()
    {
        Conversation conversation = null;
        
        SqlSession session                  = MyBatisConnectionFactory.getSession().openSession();

        try {
            ConversationMapper mapper   = session.getMapper( ConversationMapper.class);
            
            conversation                = mapper.getPlainConversation( id);
            
            if( conversation == null)
                throw new HTTPErrorException( Status.NOT_FOUND, "Conversation " + id + " not found");
            
            session.commit();
         }catch( HTTPErrorException e) {
            throw e;
        }catch( Exception e) {
            logger.info( e);
            throw new HTTPErrorException( Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }finally {
            session.close();
        }
        
        return new ConversationMemberResource( conversation);
    }
    
    @GET
    @AuthenticationRequired( AuthenticationScheme.AUTHENTICATION_SCHEME_TOKEN)
    @Produces( { RepresentationFactory.HAL_JSON})
    public Conversation getConversation( )
    {
        Conversation conversation = null;
        
        UserPrincipal up                    = (UserPrincipal) sc.getUserPrincipal();
        SqlSession session                  = MyBatisConnectionFactory.getSession().openSession();

        try {
            ConversationMapper mapper   = session.getMapper( ConversationMapper.class);
            
            conversation                = mapper.getConversation( id, up.getId());
            
            if( conversation == null)
                throw new HTTPErrorException( Status.NOT_FOUND, "Conversation " + id + " not found");
            
            int isMember = mapper.isMemberOfConversation( conversation.getId(), up.getId());
            
            if( isMember <= 0)
                throw new HTTPErrorException( Status.FORBIDDEN, "You are not a member of the conversation");
            
            session.commit();
         }catch( HTTPErrorException e) {
            throw e;
        }catch( Exception e) {
            logger.info( e);
            throw new HTTPErrorException( Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }finally {
            session.close();
        }
        return conversation;
    }
}
