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
import com.holacampus.api.mappers.ConversationMapper;
import com.holacampus.api.security.AuthenticationRequired;
import com.holacampus.api.security.AuthenticationScheme;
import com.holacampus.api.security.UserPrincipal;
import com.holacampus.api.subresources.ConversationMemberResource;
import com.holacampus.api.subresources.MessagesResource;
import com.holacampus.api.utils.MyBatisConnectionFactory;
import com.theoryinpractise.halbuilder.api.RepresentationFactory;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * Esta clase se encarga de gestionar las peticiones de la API a un recurso
 * Conversation particualr. Es decir, gestiona las peticiones a la URL 
 * <code>/conversations/{id}</code> donde <code>id</code> es el identificador
 * de la conversación.
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
    
    /**
     * Esta función devuelve la clase que se encargará de gestionar las peticiones
     * relacionadas con los mensajes de la conversación. Esto es, a la URL 
     * <code>/coversations/{id}/messages</code>
     * @return recurso que se encarga de gestionar la petición
     */
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
    
     /**
     * Esta función devuelve la clase que se encargará de gestionar las peticiones
     * relacionadas con los miembros de la conversación. Esto es, a la URL 
     * <code>/coversations/{id}/members</code>
     * @return recurso que se encarga de gestionar la petición
     */
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
    
    /**
     * Esta función gestiona las peticiones GET al recurso 
     * <code>/conversations/{id}</code>. Esta operación devuelve la representación
     * de la conversación identificada por <b>id</b>
     * @return la representación de la conversación
     */
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
