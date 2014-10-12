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

package com.holacampus.api.mappers;

import com.holacampus.api.domain.Conversation;
import com.holacampus.api.domain.ConversationMember;
import com.holacampus.api.domain.Message;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

/**
 * Mapper para la clase {@link Conversation}
 * @author Mikel Corcuera <mik.corcuera@gmail.com>
 */
public interface ConversationMapper {
    
    /**
     * Obtiene las conversaciones de un {@link ActiveElement} en concreto
     * @param elementId Identificador del {@link ActiveElement}
     * @return lista con las conversaciones del {@link ActiveElement}
     * @throws Exception
     */
    public List<Conversation>       getConversationsForActiveElement( @Param( "elementId") Long elementId) throws Exception;
    
    /**
     * Obtiene las conversaciones de un {@link ActiveElement} en concreto
     * @param elementId Identificador del {@link ActiveElement}
     * @param rowBounds  limites (inicio y tamaño) del resultado
     * @return lista con las conversaciones del {@link ActiveElement}
     * @throws Exception
     */
    public List<Conversation>       getConversationsForActiveElement( @Param( "elementId") Long elementId, RowBounds rowBounds) throws Exception;

    /**
     * Devuelve el número total de conversaciones de un {@link ActiveElement} en concreto
     * @param elementId Identificador del {@link ActiveElement} 
     * @return El número total de conversaciones
     * @throws Exception
     */
    public int                      getTotalConversationsForActiveElement( @Param( "elementId") Long elementId) throws Exception;
    
    /** 
     * Obtiene una conversación en concreto de un {@link ActiveElement} 
     * @param id identificador de la conversación
     * @param elementId identificador del {@link ActiveElement} 
     * @return la conversació solicitada
     * @throws Exception
     */
    public Conversation             getConversation( @Param( "id") Long id, @Param( "elementId") Long elementId) throws Exception;
    
    /**
     Obtiene una conversación en concreto de un {@link ActiveElement} 
     * @param id identificador de la conversación
     * @return la conversació solicitada
     * @throws Exception
     */
    public Conversation             getPlainConversation( @Param( "id") Long id) throws Exception;
    
    /**
     * Comprueba si un {@link ActiveElement} es miembro de la conversación
     * @param id identificador de la conversación
     * @param elementId identificador del {@link ActiveElement} 
     * @return 1 si el {@link ActiveElement}  es miembro, 0 si no lo es
     * @throws Exception
     */
    public int                      isMemberOfConversation( @Param( "conversationId") Long id, @Param( "elementId") Long elementId) throws Exception;
    
    /**
     * Elimina una conversación de la base de datos
     * @param id identificador de la conversación
     * @return numero de elementos eliminados
     * @throws Exception
     */
    public Conversation             deleteConversation( @Param( "id") Long id) throws Exception;
    
    /**
     * Crea una nueva conversación
     * @param conversation conversación a crear
     * @return número de elementos creados
     * @throws Exception
     */
    public int                      createConversation( @Param("conversation") Conversation conversation) throws Exception;
    
    /**
     * Obtiene una conversación con un usuario en concreto
     * @param me Usuario que solicita la conversación
     * @param with Usuario con el cual se realiza la conversación
     * @return la conversación con el usuario
     * @throws Exception
     */
    public Conversation             getIndividualConversation( @Param( "me") Long me, @Param( "with") Long with) throws Exception;
    
    /**
     * Añade un miembro a una conversación
     * @param conversation Conversación a la que se quiere añadir el miembro
     * @param memberId identificador del {@link ActiveElement}  a añadir
     * @return número de elementos añadidos
     * @throws Exception
     */
    public int                      addMemberToConversation( @Param( "conversation") Conversation conversation, @Param( "memberId") Long memberId) throws Exception;
    
    /**
     *
     * @param conversation
     * @return
     * @throws Exception
     */
    public List<ConversationMember> getMembers( @Param( "conversation") Conversation conversation) throws Exception;
    
    /**
     *
     * @param conversation
     * @param rb
     * @return
     * @throws Exception
     */
    public List<ConversationMember> getMembers( @Param( "conversation") Conversation conversation, RowBounds rb) throws Exception;
    
    /**
     *
     * @param conversation
     * @return
     * @throws Exception
     */
    public int                      getTotalMembers( @Param( "conversation") Conversation conversation) throws Exception;
    
    /**
     *
     * @param conversation
     * @param member
     * @return
     * @throws Exception
     */
    public ConversationMember       getMember( @Param( "conversation") Conversation conversation, @Param( "memberId") Long member) throws Exception;

    /**
     *
     * @param conversation
     * @param member
     * @return
     * @throws Exception
     */
    public int                      removeMember( @Param( "conversation") Conversation conversation, @Param( "memberId") Long member) throws Exception;
    
    /**
     *
     * @param conversation
     * @param rb
     * @return
     * @throws Exception
     */
    public List<Message>            getLastMessages( @Param( "conversation") Conversation conversation, RowBounds rb) throws Exception;
    
    /**
     *
     * @param conversation
     * @return
     * @throws Exception
     */
    public int                      getTotalMessages( @Param( "conversation") Conversation conversation) throws Exception;
    
    /**
     *
     * @param conversation
     * @param creatorId
     * @param message
     * @return
     * @throws Exception
     */
    public int                      sendMessage( @Param( "conversation") Conversation conversation, @Param("creatorId") Long creatorId, @Param( "message") Message message) throws Exception;
    
    /**
     *
     * @param conversation
     * @param messageId
     * @return
     * @throws Exception
     */
    public Message                  getMessage( @Param( "conversation") Conversation conversation, @Param("messageId") Long messageId) throws Exception;
    
    /**
     *
     * @param conversationId
     * @param elementId
     * @return
     * @throws Exception
     */
    public int                      getConversationUnseenMessages( @Param( "conversationId") Long conversationId, @Param( "elementId") Long elementId) throws Exception;
    
    /**
     *
     * @param elementId
     * @return
     * @throws Exception
     */
    public int                      getActiveElementUnseenMessages(@Param( "elementId") Long elementId) throws Exception;
    
    /**
     *
     * @param conversation
     * @param elementId
     * @return
     * @throws Exception
     */
    public int                      updateLastSeen(  @Param( "conversation") Conversation conversation, @Param( "elementId") Long elementId) throws Exception;

}


