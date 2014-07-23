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
import javax.validation.Valid;
import javax.validation.constraints.Size;

/**
 *
 *  @author Mikel Corcuera <mik.corcuera@gmail.com>  
 */

@HalRootElement
public class Photo extends Element implements Linkable{
    
    @HalProperty( name="creationDate")
    private Date                creationDate;
   
    @Valid
    @Size( min=1, max=150, message="{photo.title.wrong.size}")
    @HalProperty( name="title")
    private String              title;
   
    @HalProperty( name="url")
    private String              url;
    
    @HalProperty( name="thumbnailUrl")
    private String              thumbnailUrl;
   
    @CreationNeeded( message="{photo.data.missing}")
    @HalProperty( name="data", input=true)
    private String              data;
    
   
    
    @HalEmbedded( "creator")
    private ActiveElement       creator;
    
    @HalEmbedded( "permission")
    private Permission          permission;
    
    private boolean             profilePhoto;
    
    private CommentContainer    commentContainer;
    private PhotoContainer      photoContainer;
    
    private String selfLink;


    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public ActiveElement getCreator() {
        return creator;
    }

    public void setCreator(ActiveElement creator) {
        this.creator = creator;
    }

    public Permission getPermission() {
        return permission;
    }

    public void setPermission(Permission permission) {
        this.permission = permission;
    }
    
    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public boolean isProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(boolean profilePhoto) {
        this.profilePhoto = profilePhoto;
    }
    
    public CommentContainer getCommentContainer() {
        return commentContainer;
    }

    public void setCommentContainer(CommentContainer commentContainer) {
        this.commentContainer = commentContainer;
    }

    public PhotoContainer getPhotoContainer() {
        return photoContainer;
    }

    public void setPhotoContainer(PhotoContainer photoContainer) {
        this.photoContainer = photoContainer;
    }

    @Override
    public String toString() {
        return "Photo{" + "id=" + getId() + ", creationDate=" + creationDate + ", title=" + title + ", url=" + url + ", thumbnailUrl=" + thumbnailUrl + ", data=" + data + '}';
    }
    
    @HalSelfLink
    public String getSelfLink() {
        return Utils.createLink( selfLink, null);
    }
    
    public void setSelfLink( String l) {
        selfLink = l;
    }
    
    @HalLink("comments") 
    public String getCommentsLink() {
        if( profilePhoto || selfLink == null)
            return null;
        return getSelfLink() + "/comments";
    }
    
    
    
    
}
