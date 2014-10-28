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
import com.holacampus.api.security.UserPrincipal;
import com.holacampus.api.utils.Utils;
import com.holacampus.api.validators.CreationNeeded;
import com.theoryinpractise.halbuilder.jaxrs.HalEmbedded;
import com.theoryinpractise.halbuilder.jaxrs.HalLink;
import com.theoryinpractise.halbuilder.jaxrs.HalProperty;
import com.theoryinpractise.halbuilder.jaxrs.HalRootElement;
import com.theoryinpractise.halbuilder.jaxrs.HalSelfLink;
import java.sql.Date;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;

/**
 * Clase que representa a los usuarios de la red social
 *  @author Mikel Corcuera <mik.corcuera@gmail.com>  
 */

@HalRootElement
public class User extends ActiveElement implements Linkable

{
    
    /**
     * Estudiante 
     */
    public static final String TYPE_STUDENT     = "STUDENT";

    
    /**
     * Género masculino
     */
    public static final String GENDER_MASCULINE = "M";

    /**
     * Género femenino
     */
    public static final String GENDER_FEMENINE  = "F";
    
    @CreationNeeded( message="{user.name.missing}")
    @Valid
    @HalProperty( name="name")
    private Name                name;
    
    @HalProperty( name="role")
    private String              userType;
    
    @CreationNeeded( message="{user.birthDate.missing}")
    @HalProperty( name="birthDate")
    private Date                birthDate;
    
   
    
    @CreationNeeded( message="{user.gender.missing}")
    @Pattern( regexp="[MF]", message="user.gender.wrong")
    @HalProperty( name="gender")
    private String              gender;
   
    @HalProperty( name="friendCount")
    private Integer             friendCount;
    
    @HalEmbedded( "permission")
    private Permission          permission;
    
    
    private CommentContainer    commentContainer;
    private PhotoContainer      photoContainer;
   
    /**
     * Constructor por defecto
     */
    
    public User()
    {
        super();
        setType( TYPE_USER);
    }
    
      /**
     * Crea un nuevo usuario a partir de un {@link UserPrincipal} a partir
     * de los datos contenidos en este.
     * @param u UserPrincipal donde se encuentran los datos
     */
    public User( UserPrincipal u)
    {
        super( u); 
    }
    
    /**
     *
     * @return tipo de usuario
     */
    public String getUserType() {
        return userType;
    }

    /**
     *
     * @param userType tipo de usuario
     */
    public void setUserType(String userType) {
        this.userType = userType;
    }

    /**
     *
     * @return nombre del usuario
     */
    public Name getName() {
        return name;
    }

    /**
     *
     * @param name nombre del usuario
     */
    public void setName(Name name) {
        this.name = name;
    }

    /**
     *
     * @return fecha de nacimiento del usuario
     */
    public Date getBirthDate() {
        return birthDate;
    }

    /**
     *
     * @param birthDate fecha de nacimiento del usuario
     */
    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    

    /**
     *
     * @return género del usuario
     */
    public String getGender() {
        return gender;
    }

    /**
     *
     * @param gender género del usuario
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     *
     * @return permisos sobre el usuario
     */
    public Permission getPermission() {
        return permission;
    }

    /**
     *
     * @param permission permisos sobre el usuario
     */
    public void setPermission(Permission permission) {
        this.permission = permission;
    }

    
    /**
     *
     * @return contenedor de comentarios del usuario
     */
    public CommentContainer getCommentContainer() {
        return commentContainer;
    }

    /**
     *
     * @param commentContainer contenedor de comentarios del usuario
     */
    public void setCommentContainer(CommentContainer commentContainer) {
        this.commentContainer = commentContainer;
    }

    /**
     *
     * @return contenedor de fotos del usuario
     */
    public PhotoContainer getPhotoContainer() {
        return photoContainer;
    }

    /**
     *
     * @param photoContainer contenedor de fotos del usuario
     */
    public void setPhotoContainer(PhotoContainer photoContainer) {
        this.photoContainer = photoContainer;
    }

    /**
     *
     * @return número de amistades
     */
    public Integer getFriendCount() {
        return friendCount;
    }

    /**
     *
     * @param friendCount número de amistades
     */
    public void setFriendCount(Integer friendCount) {
        this.friendCount = friendCount;
    }

    @Override 
    public String toString() {
        return "User{" + "userType=" + userType + ", name=" + name + ", birthDate=" + birthDate + ", gender=" + gender + '}';
    }

    

    @HalSelfLink
    @Override
    public String getSelfLink() {
        return Utils.createLink("/users/" + getId(), null);
        
    }
    
    /**
     *
     * @return enlace a la representación de los comentarios del usuario
     */
    @HalLink("comments")
    public String getCommentsLink() {
        if(  User.TYPE_STUDENT.equals( userType))
            return getSelfLink() + "/comments";
        return null;
    }
    
    /**
     *
     * @return enlace a la representación de las fotos del usuario
     */
    @HalLink("photos")
    public String getPhotosLink() {
        if(  User.TYPE_STUDENT.equals( userType))
            return getSelfLink() + "/photos";
        return null;
    }
    
    /**
     *
     * @return enlace a la representación de las amistades del usuario
     */
    @HalLink( "friends")
    public String getFriendsLink()  {
        if(  User.TYPE_STUDENT.equals( userType))
            return getSelfLink() + "/friends";
        return null;
    }    
    
    /**
     *
     * @return enlace a la representación de la foto de perfil del usuario
     */
    @HalLink("profile-photo")
    public String getProfilePhotoLink() {
        if( User.TYPE_STUDENT.equals( userType))
            return getSelfLink() + "/profile-photo";
        return null;
    }
    
    /**
     *
     * @return enlace a la representación de las etapas en la trayectoria académica
     * del usuario
     */
    @HalLink( "stages")
    public String getStagesLink() {
        if( User.TYPE_STUDENT.equals( userType))
            return getSelfLink() + "/stages";
        return null;
    }
    
    /**
     *
     * @return enlace a la representación de las conversaciones del usuario
     */
    @HalLink( "conversations")
    public String getConversationsLink() {
        return getSelfLink() + "/conversations";
    }
    
    /**
     *
     * @return enlace a la representación de la conversación con el usuario
     */
    @HalLink( "conversationWithMe")
    public String getConversationWithMeLink() {
        return getSelfLink() + "/conversations/with-me";
    }
    
    /**
     *
     * @return enlace a la representación de los grupos a los que pertenece el 
     * usuario
     */
    @HalLink( "groups")
    public String getGroupsLink() {
        return getSelfLink() + "/groups";
    }
    
    /**
     *
     * @return enlace a la representación de los eventos a los que pertenece el 
     * usuario
     */
    @HalLink( "events")
    public String getEventsLink() {
        return getSelfLink() + "/events";
    }
}
