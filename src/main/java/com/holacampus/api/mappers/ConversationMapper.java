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
 *
 * @author Mikel Corcuera <mik.corcuera@gmail.com>
 */
public interface ConversationMapper {
    
    public List<Conversation> getConversationsForActiveElement( @Param( "elementId") Long elementId) throws Exception;
    
    public List<Conversation> getConversationsForActiveElement( @Param( "elementId") Long elementId, RowBounds rowBounds) throws Exception;

    public int getTotalConversationsForActiveElement( @Param( "elementId") Long elementId) throws Exception;
    
    public Conversation getConversation( @Param( "id") Long id, @Param( "elementId") Long elementId) throws Exception;
    
    public int isMemberOfConversation( @Param( "conversationId") Long id, @Param( "elementId") Long elementId) throws Exception;
    
    public Conversation deleteConversation( @Param( "id") Long id) throws Exception;
    
    public int createConversation( @Param("conversation") Conversation conversation) throws Exception;
    
    public Conversation getIndividualConversation( @Param( "me") Long me, @Param( "with") Long with) throws Exception;
    
    public int addMemberToConversation( @Param( "conversation") Conversation conversation, @Param( "memberId") Long member) throws Exception;
    
    public List<ConversationMember> getMembers( @Param( "conversation") Conversation conversation) throws Exception;
    
    public List<ConversationMember> getMembers( @Param( "conversation") Conversation conversation, RowBounds rb) throws Exception;
    
    public int getTotalMembers( @Param( "conversation") Conversation conversation) throws Exception;
    
    public ConversationMember getMember( @Param( "conversation") Conversation conversation, @Param( "memberId") Long member) throws Exception;

    public List<Message> getLastMessages( @Param( "conversation") Conversation conversation, RowBounds rb) throws Exception;
    
    public List<Message> getTotalMessages( @Param( "conversation") Conversation conversation, RowBounds rb) throws Exception;
    
    public int sendMessage( @Param( "conversation") Conversation conversation, @Param("creatorId") Long creatorId, @Param( "message") Message message) throws Exception;
    
    public Message getMessage( @Param( "conversation") Conversation conversation, @Param("messageId") Long messageId) throws Exception;
    
    public int getConversationUnseenMessages( @Param( "conversationId") Long conversationId, @Param( "elementId") Long elementId) throws Exception;
    
    public int getActiveElementUnseenMessages(@Param( "elementId") Long elementId) throws Exception;
    
    public int updateLastSeen(  @Param( "conversationId") Long conversationId, @Param( "elementId") Long elementId) throws Exception;

}


