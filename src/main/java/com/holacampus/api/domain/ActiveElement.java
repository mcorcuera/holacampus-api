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

import com.holacampus.api.domain.Conversation;
import com.holacampus.api.security.UserPrincipal;
import com.holacampus.api.validators.CreationNeeded;
import com.theoryinpractise.halbuilder.jaxrs.HalEmbedded;
import com.theoryinpractise.halbuilder.jaxrs.HalProperty;
import java.util.List;
import javax.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Email;

/**
 *
 *  @author Mikel Corcuera <mik.corcuera@gmail.com>  
 */
public class ActiveElement {
    
    /**
     *
     */
    public static final String TYPE_USER    = "USER";

    /**
     *
     */
    public static final String TYPE_UNI     = "UNI";
    
    @HalProperty( name="id")
    private Long                id;
    
    @HalProperty( name="type")
    private String              type;
    
    @CreationNeeded( message="{element.email.missing}")
    @Email( message="{user.email.wrong}")
    @Pattern(regexp=".+@.+\\..+", message="user.email.wrong")
    @HalProperty( name="email")
    private String              email;
    
     
    @CreationNeeded( message="{element.password.missing}")
    @Pattern( regexp="^([a-zA-Z0-9@#$%\\.,\\-\\_\\?\\!]{5,20})$", message="user.password.wrong")
    @HalProperty( name="password", input=true)
    private String              password;
    
    @HalEmbedded( "profilePhoto")
    private Photo               profilePhoto;
    
    
    public ActiveElement()
    {
        
    }
    
    public ActiveElement( UserPrincipal u) {
        this();
        setId( u.getId());
        email = u.getName();
    }
    
    /**
     *
     * @return
     */
    public Long getId() {
        return id;
    }

    /**
     *
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     *
     * @return
     */
    public String getType() {
        return type;
    }
    
    /**
     *
     * @param type
     */
    public void setType(String type) {
        this.type = type;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    
    
    public Photo getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(Photo profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    
}
