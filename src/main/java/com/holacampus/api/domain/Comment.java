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
import com.theoryinpractise.halbuilder.jaxrs.HalEmbedded;
import com.theoryinpractise.halbuilder.jaxrs.HalLink;
import com.theoryinpractise.halbuilder.jaxrs.HalProperty;
import com.theoryinpractise.halbuilder.jaxrs.HalRootElement;
import com.theoryinpractise.halbuilder.jaxrs.HalSelfLink;
import java.util.Date;

/**
 *
 *  @author Mikel Corcuera <mik.corcuera@gmail.com>  
 */

@HalRootElement
public class Comment implements Linkable{
    
    @HalProperty( name="id")
    private Long                id;
    
    @CreationNeeded( message="{comment.content.missing}")
    @HalProperty( name="content")
    private String              content;
    
    @HalProperty( name="isRecomment")
    private boolean             isRecomment;
    
    @HalProperty( name="creationDate")
    private Date                creationDate;
    
    @HalEmbedded( "creator")
    private ActiveElement       creator;
    
    @HalEmbedded( "permission")
    private Permission          permission;
    
    private CommentContainer    belongingCommentContainer;
    private CommentContainer    ownCommentContainer;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ActiveElement getCreator() {
        return creator;
    }

    public void setCreator(ActiveElement creator) {
        this.creator = creator;
    }

    public boolean isIsRecomment() {
        return isRecomment;
    }

    public void setIsRecomment(boolean isRecomment) {
        this.isRecomment = isRecomment;
    }

    public Permission getPermission() {
        return permission;
    }

    public void setPermission(Permission permission) {
        this.permission = permission;
    }
    
    public CommentContainer getBelongingCommentContainer() {
        return belongingCommentContainer;
    }

    public void setBelongingCommentContainer(CommentContainer belongingCommentContainer) {
        this.belongingCommentContainer = belongingCommentContainer;
    }

    public CommentContainer getOwnCommentContainer() {
        return ownCommentContainer;
    }

    public void setOwnCommentContainer(CommentContainer ownCommentContainer) {
        this.ownCommentContainer = ownCommentContainer;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        
        this.creationDate = creationDate;
    }

    @Override
    @HalSelfLink
    public String getSelfLink() {
        return Utils.createLink("/comments/" + getId(), null);
    }
    
    @HalLink( "recomments")
    public String getRecommentsLink()
    {
        if( !isRecomment)
            return getSelfLink() + "/recomments";
        return null;
    }

    @Override
    public String toString() {
        return "Comment{" + "id=" + id + ", content=" + content + ", isRecomment=" + isRecomment + ", creationDate=" + creationDate + ", creator=" + creator + '}';
    }
    
    
    
    
    
    
}
