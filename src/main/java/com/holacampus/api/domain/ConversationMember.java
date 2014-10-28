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
 * Clase que representa a los miembros de una conversación
 *  @author Mikel Corcuera <mik.corcuera@gmail.com>  
 */

@HalRootElement
public class ConversationMember implements Linkable {
    
    private Timestamp       lastSeen;
    @HalEmbedded( "member")
    private ActiveElement   member;
    private Conversation    conversation;

    /**
     *
     * @return ultima vez que el miembro ha visto los mensajes de la conversación
     */
    public Timestamp getLastSeen() {
        return lastSeen;
    }

    /**
     *
     * @param lastSeen ultima vez que el miembro ha visto los mensajes de la conversación
     */
    public void setLastSeen(Timestamp lastSeen) {
        this.lastSeen = lastSeen;
    }

    /**
     *
     * @return  el elemento activo que es miembro de la conversación
     */
    public ActiveElement getMember() {
        return member;
    }

    /**
     *
     * @param member el elemento activo que es miembro de la conversación
     */
    public void setMember(ActiveElement member) {
        this.member = member;
    }

    /**
     *
     * @return la conversación de la que el miembro es miembro
     */
    public Conversation getConversation() {
        return conversation;
    }

    /**
     *
     * @param conversation la conversación de la que el miembro es miembro
     */
    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
    }

    @Override
    @HalSelfLink
    public String getSelfLink() {
        return conversation.getSelfLink() + "/members/" + member.getId();
    }
    
}
