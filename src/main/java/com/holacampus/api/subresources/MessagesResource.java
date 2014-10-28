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

import com.holacampus.api.domain.*;
import com.holacampus.api.exceptions.HTTPErrorException;
import com.holacampus.api.hal.HalList;
import com.holacampus.api.mappers.ConversationMapper;
import com.holacampus.api.resources.particular.ParticularConversationResource;
import com.holacampus.api.security.AuthenticationRequired;
import com.holacampus.api.security.AuthenticationScheme;
import com.holacampus.api.security.UserPrincipal;
import com.holacampus.api.utils.MyBatisConnectionFactory;
import com.holacampus.api.utils.Utils;
import com.holacampus.api.validators.CreationValid;
import com.theoryinpractise.halbuilder.api.RepresentationFactory;
import java.net.URLDecoder;
import java.util.List;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * Esta clase se encarga de gestionar las peticiones de la API al subrecurso
 * Messages. Es decir, gestiona las peticiones a la URL 
 * <code>/conversations/{cid}/messages</code>. Estos recursos son los mensajes que
 * pertenecen a una conversación
 * @author Mikel Corcuera <mik.corcuera@gmail.com>
 */
public class MessagesResource {
    
    private static final Logger logger = LogManager.getLogger( MessagesResource.class.getName());

    private final Conversation conversation;
    
    /**
     * Contructor del subrecurso de mensajes de la conversación
     * @param conversation conversación a la que pertenece el subrecurso
     */
    public MessagesResource( Conversation conversation)
    {
        this.conversation = conversation;
    }
        
    /**
      * Esta función gestiona las peticiones GET al recurso 
     * <code>/conversations/{cid}/messages</code>. Esta operación devuelve un lista 
     * con los mensajes de la conversación
     * @param page página de los resultados
     * @param size tamaño de los resultados
     * @param sc información de seguridad y autenticación de la petición
     * @param uriInfo  información de la URL de la petición
     * @return lista con los mensajes del tamaño especificado
     */
    @GET
    @AuthenticationRequired( AuthenticationScheme.AUTHENTICATION_SCHEME_TOKEN)
    @Produces( { RepresentationFactory.HAL_JSON})
    public HalList<Message> getMessages(  @QueryParam("page") Integer page, 
            @QueryParam( "size") Integer size, @Context SecurityContext sc, @Context UriInfo uriInfo)
    {
        page                                = Utils.getValidPage(page);
        size                                = Utils.getValidSize(size);
        RowBounds rb                        = Utils.createRowBounds(page, size);
        HalList<Message> messages           = null;
        UserPrincipal up                    = (UserPrincipal) sc.getUserPrincipal();
        SqlSession session                  = MyBatisConnectionFactory.getSession().openSession();

        try {
            ConversationMapper mapper   = session.getMapper( ConversationMapper.class);
            
            checkMembership( mapper, up.getId());
            
            List<Message> messagesList  = mapper.getLastMessages(conversation, rb);
            int total                   = mapper.getTotalMessages( conversation);
            mapper.updateLastSeen( conversation, up.getId());
            messages = new HalList<Message>( messagesList, total);
            messages.setPage(page);
            messages.setSize(size);
            messages.setResourceRelativePath( uriInfo.getPath());
            session.commit();
         }catch( HTTPErrorException e) {
            throw e;
        }catch( Exception e) {
            logger.info( e);
            throw new HTTPErrorException( Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }finally {
            session.close();
        }
        return messages;
    }
    
     /**
      * Esta función gestiona las peticiones POST al recurso 
     * <code>/conversations/{cid}/messages</code>. Esta operación envía un nuevo
     * mensaje a la conversación. El autor del mensaje será el usuario o universidad
     * que realiza la petición
     * @param message mensaje a enviar en la conversación
     * @param sc información de seguridad y autenticación de la petición
     * @param uriInfo  información de la URL de la petición
     * @return representación del mensaje ya enviado a la conversación
     */
    @POST
    @AuthenticationRequired( AuthenticationScheme.AUTHENTICATION_SCHEME_TOKEN)
    @Consumes( { RepresentationFactory.HAL_JSON, MediaType.APPLICATION_JSON})
    @Produces( { RepresentationFactory.HAL_JSON})
    public Message sendMessage( @Valid @CreationValid Message message, @Context SecurityContext sc, @Context UriInfo uriInfo) 
    {
        UserPrincipal up                    = (UserPrincipal) sc.getUserPrincipal();
        SqlSession session                  = MyBatisConnectionFactory.getSession().openSession();

        try {
            ConversationMapper mapper   = session.getMapper( ConversationMapper.class);
            checkMembership( mapper, up.getId());
            
            int result = mapper.sendMessage(conversation, up.getId(), message);
            
            if( result == 0) {
                throw new HTTPErrorException( Response.Status.INTERNAL_SERVER_ERROR, "Error sending message");
            }
            
            message = mapper.getMessage(conversation, message.getId());
            mapper.updateLastSeen( conversation, up.getId());
            session.commit();
         }catch( HTTPErrorException e) {
            throw e;
        }catch( Exception e) {
            logger.info( e);
            throw new HTTPErrorException( Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }finally {
            session.close();
        }
        return message;
    }
    
    /**
      * Esta función gestiona las peticiones GET al recurso 
     * <code>/conversations/{cid}/messages/{id}</code>. Esta operación obtiene un
     * mensaje en concreto de la conversación.
     * @param  id identificador del mensaje
     * @param sc información de seguridad y autenticación de la petición
     * @param uriInfo  información de la URL de la petición
     * @return representación del mensaje
     */
    @Path( "/{id}")
    @GET
    @AuthenticationRequired( AuthenticationScheme.AUTHENTICATION_SCHEME_TOKEN)
    @Produces( { RepresentationFactory.HAL_JSON})
    public Message getMessage( @PathParam( "id") Long id, @Context SecurityContext sc, @Context UriInfo uriInfo)
    {
        Message message = null;
        
        UserPrincipal up                    = (UserPrincipal) sc.getUserPrincipal();
        SqlSession session                  = MyBatisConnectionFactory.getSession().openSession();

        try {
            ConversationMapper mapper   = session.getMapper( ConversationMapper.class);
            checkMembership( mapper, up.getId());
            
            message = mapper.getMessage(conversation, id);
            
            if( message == null) {
                throw new HTTPErrorException( Response.Status.NOT_FOUND, "Message " + id + " not found");
            }
            
            mapper.updateLastSeen( conversation, up.getId());
            session.commit();
         }catch( HTTPErrorException e) {
            throw e;
        }catch( Exception e) {
            logger.info( e);
            throw new HTTPErrorException( Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }finally {
            session.close();
        }
        return message;
    }
    

    private void checkMembership( ConversationMapper mapper, Long userId) throws HTTPErrorException, Exception
    {
        int isMember = mapper.isMemberOfConversation( conversation.getId(), userId);
            
        if( isMember <= 0)
            throw new HTTPErrorException( Response.Status.FORBIDDEN, "You are not a member of the conversation");
            
    }
    
}
