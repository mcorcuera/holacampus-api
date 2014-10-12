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

import com.holacampus.api.domain.ActiveElement;
import com.holacampus.api.domain.Conversation;
import com.holacampus.api.exceptions.HTTPErrorException;
import com.holacampus.api.hal.HalList;
import com.holacampus.api.mappers.ConversationMapper;
import com.holacampus.api.resources.ConversationResource;
import com.holacampus.api.security.AuthenticationRequired;
import com.holacampus.api.security.AuthenticationScheme;
import com.holacampus.api.security.UserPrincipal;
import com.holacampus.api.utils.MyBatisConnectionFactory;
import com.holacampus.api.utils.Utils;
import com.theoryinpractise.halbuilder.api.RepresentationFactory;
import java.util.List;
import javax.ws.rs.*;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
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
public class ConversationsResource {
    
    private static final Logger logger = LogManager.getLogger( ConversationsResource.class.getName());

    
    private ActiveElement activeElement;
    
    public ConversationsResource ( ActiveElement e)
    {
        this.activeElement = e;
    }
    
    @GET
    @AuthenticationRequired( AuthenticationScheme.AUTHENTICATION_SCHEME_TOKEN)
    @Produces( { RepresentationFactory.HAL_JSON})
    public HalList<Conversation> getConversations(@QueryParam("page") Integer page, 
            @QueryParam( "size") Integer size, @Context SecurityContext sc, @Context UriInfo uriInfo)
    {
        page                                = Utils.getValidPage(page);
        size                                = Utils.getValidSize(size);
        RowBounds rb                        = Utils.createRowBounds(page, size);
        UserPrincipal up                    = (UserPrincipal) sc.getUserPrincipal();
        if( !up.getId().equals( activeElement.getId()))
            throw new HTTPErrorException( Status.FORBIDDEN, "You are not allowed here");
        HalList<Conversation> conversations = null;
        SqlSession session                  = MyBatisConnectionFactory.getSession().openSession();

        try {
            ConversationMapper mapper               = session.getMapper( ConversationMapper.class);
            List<Conversation> conversationsList    = mapper.getConversationsForActiveElement( up.getId(), rb);
            int total                               = mapper.getTotalConversationsForActiveElement( up.getId());
            session.commit();
            conversations = new HalList<Conversation>( conversationsList, total);
            conversations.setPage(page);
            conversations.setSize(size);
            conversations.setResourceRelativePath( uriInfo.getPath());
         }catch( HTTPErrorException e) {
            throw e;
        }catch( Exception e) {
            logger.info( e);
            throw new HTTPErrorException( Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }finally {
            session.close();
        }
        return conversations;
    }
    
    @Path( "/with-me")
    @GET
    @AuthenticationRequired( AuthenticationScheme.AUTHENTICATION_SCHEME_TOKEN)
    @Produces( { RepresentationFactory.HAL_JSON})
    public Conversation getConversationWithMe(  @Context SecurityContext sc, @Context UriInfo uriInfo)
    {
        Conversation conversation = null;
        UserPrincipal up                    = (UserPrincipal) sc.getUserPrincipal();
        if( up.getId().equals( activeElement.getId()))
            throw new HTTPErrorException( Response.Status.CONFLICT, "You can't create a conversation with yourself");
        SqlSession session                  = MyBatisConnectionFactory.getSession().openSession();
        
         try {
            ConversationMapper mapper   = session.getMapper( ConversationMapper.class);
            conversation                = mapper.getIndividualConversation( up.getId(), activeElement.getId());
            
            if( conversation == null) {
                conversation = new Conversation();
                conversation.setType( Conversation.TYPE_INDIVIDUAL);
                logger.info( conversation.getType());
                int result = mapper.createConversation(conversation);
                if( result == 0)
                    throw new HTTPErrorException( Response.Status.CONFLICT, "Problem creating conversation");
                mapper.addMemberToConversation(conversation, up.getId());
                mapper.addMemberToConversation(conversation, activeElement.getId());
                conversation = mapper.getIndividualConversation(up.getId(), activeElement.getId());
            }
            session.commit();
         }catch( HTTPErrorException e) {
            throw e;
        }catch( Exception e) {
            logger.info( e);
            throw new HTTPErrorException( Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }finally {
            session.close();
        }
        
        return conversation;
    }
}
