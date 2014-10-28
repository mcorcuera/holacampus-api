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
 * Clase que representa a los comentarios de la red social
 *  @author Mikel Corcuera <mik.corcuera@gmail.com>  
 */

@HalRootElement
public class Comment extends Element implements Linkable{
    
    
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
    
    private String              selfLink;

    /**
     *
     * @return contenido del comentario
     */
    public String getContent() {
        return content;
    }

    /**
     *
     * @param content contenido del comentario
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     *
     * @return creador del comentario
     */
    public ActiveElement getCreator() {
        return creator;
    }

    /**
     *
     * @param creator creador del comentario
     */
    public void setCreator(ActiveElement creator) {
        this.creator = creator;
    }

    /**
     *
     * @return true si se trata de una respuesta al comentario, false en caso contrario
     */
    public boolean isIsRecomment() {
        return isRecomment;
    }

    /**
     *
     * @param isRecomment true, establece el comentario como respuesta a otro comentario,
     * 
     */
    public void setIsRecomment(boolean isRecomment) {
        this.isRecomment = isRecomment;
    }

    /**
     *
     * @return  los permisos sobre el comentario
     */
    public Permission getPermission() {
        return permission;
    }

    /**
     *
     * @param permission los permisos sobre el comentario
     */
    public void setPermission(Permission permission) {
        this.permission = permission;
    }
    
    /**
     *
     * @return el contenedor de comentarios al que pertenece
     */
    public CommentContainer getBelongingCommentContainer() {
        return belongingCommentContainer;
    }

    /**
     *
     * @param belongingCommentContainer el contenedor de comentarios al que pertenece
     */
    public void setBelongingCommentContainer(CommentContainer belongingCommentContainer) {
        this.belongingCommentContainer = belongingCommentContainer;
    }

    /**
     *
     * @return el contenedor de comentarios para las respuestas al comentarios
     */
    public CommentContainer getOwnCommentContainer() {
        return ownCommentContainer;
    }

    /**
     *
     * @param ownCommentContainer el contenedor de comentarios para las respuestas al comentarios
     */
    public void setOwnCommentContainer(CommentContainer ownCommentContainer) {
        this.ownCommentContainer = ownCommentContainer;
    }

    /**
     *
     * @return fecha de creación del comentario
     */
    public Date getCreationDate() {
        return creationDate;
    }

    /**
     *
     * @param creationDate fecha de creación del comentario
     */
    public void setCreationDate(Date creationDate) {
        
        this.creationDate = creationDate;
    }

    @Override
    @HalSelfLink
    public String getSelfLink() {
        return Utils.createLink( selfLink, null);
    }
    
    /**
     *
     * @param l el enlace a la representación del objeto
     */
    public void setSelfLink( String l) {
        selfLink = l;
    }
    
    /**
     *
     * @return enlace a la representación de las respuestas al comentario
     */
    @HalLink( "recomments")
    public String getRecommentsLink()
    {
        if( !isRecomment)
            return getSelfLink() + "/recomments";
        return null;
    }

    @Override
    public String toString() {
        return "Comment{" + "id=" + getId() + ", content=" + content + ", isRecomment=" + isRecomment + ", creationDate=" + creationDate + ", creator=" + creator + '}';
    }
    
    
    
    
    
    
}
