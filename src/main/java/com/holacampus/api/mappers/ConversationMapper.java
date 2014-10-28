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
import com.holacampus.api.domain.*;

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
     * Obtiene los miembros de una conversación
     * @param conversation Conversación de la que se obtienen los miembros
     * @return lista de miembros de la conversación
     * @throws Exception
     */
    public List<ConversationMember> getMembers( @Param( "conversation") Conversation conversation) throws Exception;
    
    /**
     * Obtiene los miembros de una conversación
     * @param conversation Conversación de la que se obtienen los miembros
     * @param rb limites (inicio y tamaño) del resultado
     * @return lista de miembros de la conversación
     * @throws Exception
     */
    public List<ConversationMember> getMembers( @Param( "conversation") Conversation conversation, RowBounds rb) throws Exception;
    
    /**
     * Devuelve el número total de miembros en una conversación
     * @param conversation conversación de la que se obtiene el número de miembros
     * @return el número de miembros de la conversación
     * @throws Exception
     */
    public int                      getTotalMembers( @Param( "conversation") Conversation conversation) throws Exception;
    
    /**
     * Obtiene un miembro en concreto de una conversacióin
     * @param conversation conversación de la que se quiere obtener el miembro
     * @param member el identificador del {@link ActiveElement} que se quiere obtener
     * @return el miembro de la conversación (null en caso de que no exista)
     * @throws Exception
     */
    public ConversationMember       getMember( @Param( "conversation") Conversation conversation, @Param( "memberId") Long member) throws Exception;

    /**
     * Elimina un miembro de una conversación
     * @param conversation conversación de la que se quiere eliminar el miembro
     * @param member identificador del {@link ActiveElement} a eliminar
     * @return el número de elementos eliminados
     * @throws Exception
     */
    public int                      removeMember( @Param( "conversation") Conversation conversation, @Param( "memberId") Long member) throws Exception;
    
    /**
     * Obtiene una lista con los ultimos mensajes en una conversación
     * @param conversation conversación de la que se obtienen los mensajes
     * @param rb  limites (inicio y tamaño) del resultado
     * @return lista con los últimos mensajes de la conversación
     * @throws Exception
     */
    public List<Message>            getLastMessages( @Param( "conversation") Conversation conversation, RowBounds rb) throws Exception;
    
    /**
     * Devuelve el número total de mensajes en una conversación
     * @param conversation conversación sobre la que se realiza la consulta
     * @return número de mensajes
     * @throws Exception
     */
    public int                      getTotalMessages( @Param( "conversation") Conversation conversation) throws Exception;
    
    /**
     * Envía un mensaje a una conversación
     * @param conversation conversación en la que se envía el mensaje
     * @param creatorId identificador del {@link ActiveElement} que envía el mensaje
     * @param message el mensaje a enviar
     * @return número de mensajes enviados
     * @throws Exception
     */
    public int                      sendMessage( @Param( "conversation") Conversation conversation, @Param("creatorId") Long creatorId, @Param( "message") Message message) throws Exception;
    
    /**
     * Obtiene un mensaje en concreto de una conversación
     * @param conversation conversación de la que obtener el mensaje
     * @param messageId identificador del mensaje
     * @return el mensaje o null en caso de que no exista
     * @throws Exception
     */
    public Message                  getMessage( @Param( "conversation") Conversation conversation, @Param("messageId") Long messageId) throws Exception;
    
    /**
     * Devuelve el número total de mensajes sin leer por un usuario en una conversación
     * @param conversationId identificador de la conversación
     * @param elementId identificador del {@link ActiveElement} para el cual se obtiene la cantidad de mensajes no leidos
     * @return el número de mensajes no leídos.
     * @throws Exception
     */
    public int                      getConversationUnseenMessages( @Param( "conversationId") Long conversationId, @Param( "elementId") Long elementId) throws Exception;
    
    /**
     * Obtiene el número total de mensajes sin leer para un {@link ActiveElement} en todas sus conversaciones
     * @param elementId identificador del {@link ActiveElement}
     * @return el número total de mensajes sin leer
     * @throws Exception
     */
    public int                      getActiveElementUnseenMessages(@Param( "elementId") Long elementId) throws Exception;
    
    /**
     * Actualiza el momento en el cual el {@link ActiveElement} vió por última vez una conversación
     * y así poder obtener el número de mensajes no leídos correctamente
     * @param conversation conversación sobre la cual se quiere actualizar el momento de último acceso
     * @param elementId el identificador del {@link ActiveElement} para el que se quiere actualizar este valor
     * @return número de elementos actualizados
     * @throws Exception
     */
    public int                      updateLastSeen(  @Param( "conversation") Conversation conversation, @Param( "elementId") Long elementId) throws Exception;

}


