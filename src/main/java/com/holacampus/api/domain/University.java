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
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.Size;

/**
 * Clase que representa a las universidades de la red social
 *  @author Mikel Corcuera <mik.corcuera@gmail.com>  
 */
@HalRootElement
public class University extends ActiveElement implements Linkable{
    
    @CreationNeeded( message="{university.name.missing")
    @Valid
    @Size( min=1, max=150, message="{university.name.wrong.size}")
    @HalProperty( name="name")
    private String              name;
    
    
    @HalEmbedded( "permission")
    private Permission          permission;
    
    @HalEmbedded( "cities")
    private List<City> cities;
    
    /**
     * Constructor por defecto
     */
    public University()
    {
        super();
        setType( TYPE_UNI);
    }

    /**
     *
     * @return nombre de la universidad
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name nombre de la universidad
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return permisos sobre la universidad
     */
    public Permission getPermission() {
        return permission;
    }

    /**
     *
     * @param permission permisos sobre la universidad
     */
    public void setPermission(Permission permission) {
        this.permission = permission;
    }

    /**
     *
     * @return ciudades en las que se encuentra la universidad
     */
    public List<City> getCities() {
        return cities;
    }

    /**
     *
     * @param cities ciudades en las que se encuentra la universidad
     */
    public void setCities(List<City> cities) {
        this.cities = cities;
    }
    

    @Override
    @HalSelfLink
    public String getSelfLink() {
        return Utils.createLink("/universities/" + getId(), null);
    }
    
    /**
     *
     * @return enlace a la representación de los comentarios de la universidad
     */
    @HalLink("comments")
    public String getCommentsLink() {
        return getSelfLink() + "/comments";
    }
    
    /**
     *
     * @return enlace a la representación de las fotos de la universidad
     */
    @HalLink("photos")
    public String getPhotosLink() {
        return getSelfLink() + "/photos";
    }
    
    /**
     *
     * @return enlace a la representación de la foto de perfil de la universidad
     */
    @HalLink("profile-photo")
    public String getProfilePhotoLink() {
        return getSelfLink() + "/profile-photo";
    }
    
    /**
     *
     * @return enlace a la representación de las ciudades en las que se 
     * encuentra la universidad
     */
    @HalLink( "cities")
    public String getCitiesLink() {
        return getSelfLink() + "/cities";
    }
    
    /**
     *
     * @return enlace a la representación de los estudios ofertados por la
     * universidad
     */
    @HalLink( "studies")
    public String getStudiesLink() {
        return getSelfLink() + "/studies";
    }
    
    /**
     *
     * @return enlace a la representación de los estudiantes de la universidad
     */
    @HalLink( "students")
    public String getStudentsLink() {
        return getSelfLink() + "/students";
    }
    
    /**
     *
     * @return enlace a la representación de las conversaciones de la universidad
     */
    @HalLink( "conversations")
    public String getConversationsLink() {
        return getSelfLink() + "/conversations";
    }
    
    /**
     *
     * @return enlace a la representación de la conversación de un usuario o 
     * universidad con esta universidad
     */
    @HalLink( "conversationWithMe")
    public String getConversationWithMeLink() {
        return getSelfLink() + "/conversations/with-me";
    }
    
    /**
     *
     * @return enlace a la representación de los grupos a los que pertenece
     * la universidad
     */
    @HalLink( "groups")
    public String getGroupsLink() {
        return getSelfLink() + "/groups";
    }
    
    /**
     *
     * @return enlace a la representación de los eventos a los que pertenece
     * la universidad
     */
    @HalLink( "events")
    public String getEventsLink() {
        return getSelfLink() + "/events";
    }
}
