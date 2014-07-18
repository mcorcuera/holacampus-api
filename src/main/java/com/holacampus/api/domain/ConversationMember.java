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

package com.holacampus.api.domain;

import com.holacampus.api.hal.Linkable;
import com.theoryinpractise.halbuilder.jaxrs.*;
import java.sql.Timestamp;

/**
 *
 *  @author Mikel Corcuera <mik.corcuera@gmail.com>  
 */

@HalRootElement
public class ConversationMember implements Linkable {
    
    private Timestamp       lastSeen;
    @HalEmbedded( "member")
    private ActiveElement   member;
    private Conversation    conversation;

    public Timestamp getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(Timestamp lastSeen) {
        this.lastSeen = lastSeen;
    }

    public ActiveElement getMember() {
        return member;
    }

    public void setMember(ActiveElement member) {
        this.member = member;
    }

    public Conversation getConversation() {
        return conversation;
    }

    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
    }

    
    
    @Override
    @HalSelfLink
    public String getSelfLink() {
        return conversation.getSelfLink() + "/members/" + member.getId();
    }
    
}
