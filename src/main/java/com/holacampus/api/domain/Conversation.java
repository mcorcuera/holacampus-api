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
import com.holacampus.api.utils.Utils;
import com.theoryinpractise.halbuilder.jaxrs.*;
import java.sql.Timestamp;
import java.util.List;

/**
 * Clase que representa las conversaciones de la red social
 *  @author Mikel Corcuera <mik.corcuera@gmail.com>  
 */
@HalRootElement
public class Conversation extends Element implements Linkable{
    
    /**
     * Conversación grupal
     */
    public static final String TYPE_GROUP       = "GROUP";

    /**
     * Conversación individual
     */
    public static final String TYPE_INDIVIDUAL  = "INDIVIDUAL";
    
    @HalProperty( name="type")
    private String                      type;
    
    @HalProperty( name="creationDate")
    private Timestamp                   creationDate;
    
    @HalProperty( name="name")
    private String                      name;
    
    @HalProperty( name="unseenMessages")
    private Integer                     unseenMessages;
    
    @HalProperty( name="memberCount")
    private Integer                     memberCount;
    
    @HalEmbedded( "messages")
    private List<Message>               messages;
    
    @HalEmbedded( "members")
    private List<ConversationMember>    members;

    /**
     *
     * @return tipo de conversación
     */
    public String getType() {
        return type;
    }

    /**
     *
     * @param type tipo de conversación
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     *
     * @return fecha de creación de la conversación
     */
    public Timestamp getCreationDate() {
        return creationDate;
    }

    /**
     *
     * @param creationDate fecha de creación de la conversación
     */
    public void setCreationDate( Timestamp creationDate) {
        this.creationDate = creationDate;
    }

    /**
     *
     * @return nombre de la conversación (en caso de conversaciones grupales)
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name nombre de la conversación
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return número total de mensajes no leidos en la conversación
     */
    public Integer getUnseenMessages() {
        return unseenMessages;
    }

    /**
     *
     * @param unseenMessages número total de mensajes no leidos en la conversación
     */
    public void setUnseenMessages(Integer unseenMessages) {
        this.unseenMessages = unseenMessages;
    }

    /**
     *
     * @return lista con los mensajes de la conversación
     */
    public List<Message> getMessages() {
        return messages;
    }

    /**
     *
     * @param messages lista con los mensajes de la conversación
     */
    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    /**
     *
     * @return lista con los miembros de la conversación
     */
    public List<ConversationMember> getMembers() {
        return members;
    }

    /**
     *
     * @param members lista con los miembros de la conversación
     */
    public void setMembers(List<ConversationMember> members) {
        this.members = members;
    }

    /**
     *
     * @return número total de miembros en la conversación
     */
    public Integer getMemberCount() {
        return memberCount;
    }

    /**
     *
     * @param memberCount número total de miembros en la conversación 
     */
    public void setMemberCount(Integer memberCount) {
        this.memberCount = memberCount;
    }

    @Override
    @HalSelfLink
    public String getSelfLink() {
        return Utils.createLink( "/conversations/" + getId(), null);
    }
    
    /**
     *
     * @return enlace a la representación de los mensajes de la conversación
     */
    @HalLink("messages")
    public String getMessagesLink() {
        return getSelfLink() + "/messages";
    }
    
    /**
     *
     * @return enlace  a la representación de los miembros de la conversación
     */
    @HalLink( "members")
    public String getMembersLink() {
        return getSelfLink() + "/members";
    }
    
    

    

}
