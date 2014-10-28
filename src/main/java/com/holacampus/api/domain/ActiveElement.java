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
import com.theoryinpractise.halbuilder.jaxrs.HalProperty;
import com.theoryinpractise.halbuilder.jaxrs.HalSelfLink;
import javax.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Email;

/**
 *  Esta clase identifica a los elementos de la red social que son capaces de 
 * modificar los elementos de esta. Dicho de otra manera, los elementos que 
 * tienen un rol activo en la red social
 *  @author Mikel Corcuera <mik.corcuera@gmail.com>  
 */
public class ActiveElement extends Element implements Linkable{
    
    /**
     * El tipo de elemento activo para los usuarios
     */
    public static final String TYPE_USER    = "USER";

    /**
     * El tiepo de elemento activo para las universidades
     */
    public static final String TYPE_UNI     = "UNI";
        
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
    
    /**
     * Contructor por defecto
     */
    public ActiveElement()
    {
        
    }
    
    /**
     * Crea un nuevo usuario a partir de un {@link UserPrincipal} a partir
     * de los datos contenidos en este.
     * @param u UserPrincipal donde se encuentran los datos
     */
    public ActiveElement( UserPrincipal u) {
        this();
        setId( u.getId());
        email = u.getName();
    }
    

    /**
     * 
     * @return el tipo de elemento activo
     */
    public String getType() {
        return type;
    }
    
    /**
     * @param type tipo de elemento activo a establecer
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return email del elemento activo
     */
    public String getEmail() {
        return email;
    }

    /**
     *
     * @param email email a establecer
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     *
     * @return contraseña de acceso del elemento activo
     */
    public String getPassword() {
        return password;
    }

    /**
     *
     * @param password contraseña a establecer
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     *
     * @return foto de perfil del elemento activo
     */
    public Photo getProfilePhoto() {
        return profilePhoto;
    }

    /**
     *
     * @param profilePhoto foto de perfil a establecer
     */
    public void setProfilePhoto(Photo profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    @HalSelfLink
    @Override
    public String getSelfLink() {
        if( ActiveElement.TYPE_USER.equals(type))
            return Utils.createLink("/users/" + getId(), null);
        return Utils.createLink("/universities/" + getId(), null); 
    }

    
}
