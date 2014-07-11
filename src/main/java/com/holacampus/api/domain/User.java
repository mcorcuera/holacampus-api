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
 *  Bean object for User
 *  @author Mikel Corcuera <mik.corcuera@gmail.com>  
 */

@HalRootElement
public class User extends ActiveElement implements Linkable

{
    
    public static final String TYPE_STUDENT     = "STUDENT";

    public static final String TYPE_UNI_MNG     = "UNI_MNG";

    public static final String TYPE_MNG         = "MNG";
    
    public static final String GENDER_MASCULINE = "M";

    public static final String GENDER_FEMENINE  = "F";
    
    @CreationNeeded( message="{user.name.missing}")
    @Valid
    @HalProperty( name="name")
    private Name                name;
    
    @CreationNeeded( message="{user.type.missing}")
    @HalProperty( name="role")
    private String              userType;
    
    @CreationNeeded( message="{user.birthDate.missing}")
    @HalProperty( name="birthDate")
    private Date                birthDate;
    
   
    
    @CreationNeeded( message="{user.gender.missing}")
    @Pattern( regexp="[MF]", message="user.gender.wrong")
    @HalProperty( name="gender")
    private String              gender;
   
    
    @HalEmbedded( "permission")
    private Permission          permission;
    
    
    private CommentContainer    commentContainer;
    private PhotoContainer      photoContainer;
   
    /**
     *
     */
    
    public User()
    {
        super();
        setType( TYPE_USER);
    }
    
    public User( UserPrincipal u)
    {
        super( u); 
    }
    
    /**
     *
     * @return
     */
    public String getUserType() {
        return userType;
    }

    /**
     *
     * @param userType
     */
    public void setUserType(String userType) {
        this.userType = userType;
    }

    /**
     *
     * @return
     */
    public Name getName() {
        return name;
    }

    /**
     *
     * @param name
     */
    public void setName(Name name) {
        this.name = name;
    }

    /**
     *
     * @return
     */
    public Date getBirthDate() {
        return birthDate;
    }

    /**
     *
     * @param birthDate
     */
    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    

    /**
     *
     * @return
     */
    public String getGender() {
        return gender;
    }

    /**
     *
     * @param gender
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    public Permission getPermission() {
        return permission;
    }

    public void setPermission(Permission permission) {
        this.permission = permission;
    }

    
    /**
     *
     * @return
     */
    public CommentContainer getCommentContainer() {
        return commentContainer;
    }

    /**
     *
     * @param commentContainer
     */
    public void setCommentContainer(CommentContainer commentContainer) {
        this.commentContainer = commentContainer;
    }

    /**
     *
     * @return
     */
    public PhotoContainer getPhotoContainer() {
        return photoContainer;
    }

    /**
     *
     * @param photoContainer
     */
    public void setPhotoContainer(PhotoContainer photoContainer) {
        this.photoContainer = photoContainer;
    }


    @Override 
    public String toString() {
        return "User{" + "userType=" + userType + ", name=" + name + ", birthDate=" + birthDate + ", gender=" + gender + '}';
    }

    

    /**
     *
     * @return
     */
    @HalSelfLink
    @Override
    public String getSelfLink() {
        return Utils.createLink("/users/" + getId(), null);
        
    }
    
    @HalLink("comments")
    public String getCommentsLink() {
        if(  User.TYPE_STUDENT.equals( userType))
            return getSelfLink() + "/comments";
        return null;
    }
    
    @HalLink("photos")
    public String getPhotosLink() {
        if(  User.TYPE_STUDENT.equals( userType))
            return getSelfLink() + "/photos";
        return null;
    }
    
    @HalLink( "friends")
    public String getFriendsLink()  {
        if(  User.TYPE_STUDENT.equals( userType))
            return getSelfLink() + "/friends";
        return null;
    }    
    
    @HalLink("profile-photo")
    public String getProfilePhotoLink() {
        if( User.TYPE_STUDENT.equals( userType))
            return getSelfLink() + "/profile-photo";
        return null;
    }
}
