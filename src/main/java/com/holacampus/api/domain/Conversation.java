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
 *
 *  @author Mikel Corcuera <mik.corcuera@gmail.com>  
 */
@HalRootElement
public class Conversation implements Linkable{
    
    /**
     *
     */
    public static final String TYPE_GROUP       = "GROUP";

    /**
     *
     */
    public static final String TYPE_INDIVIDUAL  = "INDIVIDUAL";
    
    
    @HalProperty( name="id")
    private Long                        id;
    
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Timestamp getCreationDate() {
        return creationDate;
    }

    public void setCreationDate( Timestamp creationDate) {
        this.creationDate = creationDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getUnseenMessages() {
        return unseenMessages;
    }

    public void setUnseenMessages(Integer unseenMessages) {
        this.unseenMessages = unseenMessages;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public List<ConversationMember> getMembers() {
        return members;
    }

    public void setMembers(List<ConversationMember> members) {
        this.members = members;
    }

    public Integer getMemberCount() {
        return memberCount;
    }

    public void setMemberCount(Integer memberCount) {
        this.memberCount = memberCount;
    }

    @Override
    @HalSelfLink
    public String getSelfLink() {
        return Utils.createLink( "/conversations/" + id, null);
    }
    
    @HalLink("messages")
    public String getMessagesLink() {
        return getSelfLink() + "/messages";
    }
    
    @HalLink( "members")
    public String getMembersLink() {
        return getSelfLink() + "/members";
    }
    
    

    

}
