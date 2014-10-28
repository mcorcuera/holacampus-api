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
 * Clase que representa las fotos de la red social
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

    /**
     *
     * @return fecha de creación de la foto
     */
    public Date getCreationDate() {
        return creationDate;
    }

    /**
     *
     * @param creationDate fecha de creación de la foto
     */
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    /**
     *
     * @return título de la foto
     */
    public String getTitle() {
        return title;
    }

    /**
     *
     * @param title título de la foto
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     *
     * @return url al archivo de imagen de la foto
     */
    public String getUrl() {
        return url;
    }

    /**
     *
     * @param url url al archivo de imagen de la foto
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     *
     * @return url al archivo de imagen en miniatura de la foto
     */
    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    /**
     *
     * @param thumbnailUrl url al archivo de imagen en miniatura de la foto
     */
    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    /**
     *
     * @return creador de la foto
     */
    public ActiveElement getCreator() {
        return creator;
    }

    /**
     *
     * @param creator creador de la foto
     */
    public void setCreator(ActiveElement creator) {
        this.creator = creator;
    }

    /**
     *
     * @return permisos sobre la foto
     */
    public Permission getPermission() {
        return permission;
    }

    /**
     *
     * @param permission permisos sobre la foto
     */
    public void setPermission(Permission permission) {
        this.permission = permission;
    }
    
    /**
     *
     * @return datos binarios de la foto
     */
    public String getData() {
        return data;
    }

    /**
     *
     * @param data datos binarios de la foto
     */
    public void setData(String data) {
        this.data = data;
    }

    /**
     *
     * @return true si se trata de una foto de perfil
     */
    public boolean isProfilePhoto() {
        return profilePhoto;
    }

    /**
     *
     * @param profilePhoto  true si se trata de una foto de perfil
     */
    public void setProfilePhoto(boolean profilePhoto) {
        this.profilePhoto = profilePhoto;
    }
    
    /**
     *
     * @return  contenedor de comentarios de la foto
     */
    public CommentContainer getCommentContainer() {
        return commentContainer;
    }

    /**
     *
     * @param commentContainer contenedor de comentarios de la foto
     */
    public void setCommentContainer(CommentContainer commentContainer) {
        this.commentContainer = commentContainer;
    }

    /**
     *
     * @return contenedor de fotos al que pertenece la foto
     */
    public PhotoContainer getPhotoContainer() {
        return photoContainer;
    }

    /**
     *
     * @param photoContainer contenedor de fotos al que pertenece la foto
     */
    public void setPhotoContainer(PhotoContainer photoContainer) {
        this.photoContainer = photoContainer;
    }

    @Override
    public String toString() {
        return "Photo{" + "id=" + getId() + ", creationDate=" + creationDate + ", title=" + title + ", url=" + url + ", thumbnailUrl=" + thumbnailUrl + ", data=" + data + '}';
    }
    
    @HalSelfLink
    @Override
    public String getSelfLink() {
        return Utils.createLink( selfLink, null);
    }
    
    /**
     *
     * @param l link a la representación de la foto
     */
    public void setSelfLink( String l) {
        selfLink = l;
    }
    
    /**
     *
     * @return link a la representación de los comentarios de la foto
     */
    @HalLink("comments") 
    public String getCommentsLink() {
        if( profilePhoto || selfLink == null)
            return null;
        return getSelfLink() + "/comments";
    }
    
    
    
    
}
