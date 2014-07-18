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

/**
 *
 *  @author Mikel Corcuera <mik.corcuera@gmail.com>  
 */
@HalRootElement
public class Message implements Linkable {
    
    @HalProperty( name="id")
    private Long            id;
    @HalProperty( name="creationDate")
    private Timestamp       creationDate;
    @HalProperty( name="content")
    private String          content;

    @HalEmbedded( "creator")
    private ActiveElement   creator;
    
    private Conversation    conversation;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Conversation getConversation() {
        return conversation;
    }

    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
    }

    public ActiveElement getCreator() {
        return creator;
    }

    public void setCreator(ActiveElement creator) {
        this.creator = creator;
    }

    public Timestamp getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    @HalSelfLink
    public String getSelfLink() {
        return Utils.createLink( conversation.getSelfLink() + "/messages/" + id, null);
    }
    
    
}
