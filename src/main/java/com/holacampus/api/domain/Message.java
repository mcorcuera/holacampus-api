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
import com.holacampus.api.validators.CreationNeeded;
import com.theoryinpractise.halbuilder.jaxrs.*;
import java.sql.Timestamp;

/**
 *  Clase que representa los mensajes de una conversación
 *  @author Mikel Corcuera <mik.corcuera@gmail.com>  
 */
@HalRootElement
public class Message extends Element implements Linkable {
    
    @HalProperty( name="creationDate")
    private Timestamp       creationDate;
    @CreationNeeded( message="{message.content.missing}")
    @HalProperty( name="content")
    private String          content;

    @HalEmbedded( "creator")
    private ActiveElement   creator;
    
    private Conversation    conversation;
    
    /**
     *
     * @return conversación a la que pertenece el mensaje
     */
    public Conversation getConversation() {
        return conversation;
    }

    /**
     *
     * @param conversation conversación a la que pertenece el mensaje
     */
    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
    }

    /**
     *
     * @return creador del mensaje
     */
    public ActiveElement getCreator() {
        return creator;
    }

    /**
     *
     * @param creator creador del mensaje
     */
    public void setCreator(ActiveElement creator) {
        this.creator = creator;
    }

    /**
     *
     * @return fecha de creación del mensaje
     */
    public Timestamp getCreationDate() {
        return creationDate;
    }

    /**
     *
     * @param creationDate fecha de creación del mensaje
     */
    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
    }

    /**
     *
     * @return contenido del mensaje
     */
    public String getContent() {
        return content;
    }

    /**
     *
     * @param content contenido del mensaje
     */
    public void setContent(String content) {
        this.content = content;
    }

    @Override
    @HalSelfLink
    public String getSelfLink() {
        return conversation.getSelfLink() + "/messages/" + getId();
    }
    
    
}
