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
import java.util.Date;
import javax.validation.Valid;
import javax.validation.constraints.Size;


/**
 * Clase que represneta a los grupos y eventos de la red social
 *  @author Mikel Corcuera <mik.corcuera@gmail.com>  
 */
@HalRootElement
public class GroupEvent extends Element implements Linkable{
   
    /**
     * Evento
     */
    public static final String TYPE_EVENT = "EVENT";

    /**
     * Grupo
     */
    public static final String TYPE_GROUP = "GROUP";
    
    @CreationNeeded( message="{group.name.missing}")
    @Valid
    @Size( min=1, max=90, message="{group.name.wrong.size}")
    @HalProperty( name="name")
    private String              name;
    @HalProperty( name="creationDate")
    private Date                creationDate;
    @CreationNeeded( message="{group.name.missing}")
    @Valid
    @HalProperty( name="description")
    private String              description;
    @HalProperty( name="location")
    private Location            location;
    @HalProperty( name="locationName")
    private String              locationName;
    @HalProperty( name="eventDate")
    private Timestamp            eventDate;
    @HalProperty( name="members")
    private Integer             members;
    
    @HalEmbedded( "creator")
    private ActiveElement       creator;
    @HalEmbedded( "city")
    private City                city;
    @HalEmbedded( "groupPhoto")
    private Photo               groupPhoto;
    
    @HalEmbedded( "permission")
    private Permission          permission;
    
    @HalProperty( name="type")
    private String              type;

    /**
     *
     * @return nombre del grupo o evento
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name nombre del grupo o evento
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return fecha de creción del grupo o evento
     */
    public Date getCreationDate() {
        return creationDate;
    }

    /**
     *
     * @param creationDate fecha de creación del grupo o evento
     */
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    /**
     *
     * @return descripción del grupo o evento
     */
    public String getDescription() {
        return description;
    }

    /**
     *
     * @param description descripción del grupo o evento
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     *
     * @return localización donde tiene lugar el grupo o evento
     */
    public Location getLocation() {
        return location;
    }

    /**
     *
     * @param location localización donde tiene lugar el grupo o evento
     */
    public void setLocation(Location location) {
        this.location = location;
    }

    /**
     *
     * @return nombre de la  localización donde tiene lugar el grupo o evento
     */
    public String getLocationName() {
        return locationName;
    }

    /**
     *
     * @param locationName nombre de la  localización donde tiene lugar el grupo o evento
     */
    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    /**
     *
     * @return creador del grupo o evento
     */
    public ActiveElement getCreator() {
        return creator;
    }

    /**
     *
     * @param creator creador del grupo o evento
     */
    public void setCreator(ActiveElement creator) {
        this.creator = creator;
    }

    /**
     *
     * @return ciudad donde tiene lugar el grupo o evento
     */
    public City getCity() {
        return city;
    }

    /**
     *
     * @param city ciudad donde tiene lugar el grupo o evento
     */
    public void setCity(City city) {
        this.city = city;
    }

    /**
     *
     * @return tipo: grupo o evento
     */
    public String getType() {
        return type;
    }

    /**
     *
     * @param type tipo: grupo o evento
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     *
     * @return fecha en la que tiene lugar el evento
     */
    public Timestamp getEventDate() {
        return eventDate;
    }

    /**
     *
     * @param eventDate fecha en la que tiene lugar el evento
     */
    public void setEventDate( Timestamp eventDate) {
        this.eventDate = eventDate;
    }

    /**
     *
     * @return foto de perfil del grupo o evento
     */
    public Photo getGroupPhoto() {
        return groupPhoto;
    }

    /**
     *
     * @param groupPhoto foto de perfil del grupo o evento
     */
    public void setGroupPhoto(Photo groupPhoto) {
        this.groupPhoto = groupPhoto;
    }

    /**
     *
     * @return permisos sobre el grupo o evento
     */
    public Permission getPermission() {
        return permission;
    }

    /**
     *
     * @param permission permisos sobre el grupo o evento
     */
    public void setPermission(Permission permission) {
        this.permission = permission;
    }

    /**
     *
     * @return número total de miembros
     */
    public Integer getMembers() {
        return members;
    }

    /**
     *
     * @param members número total de miembros
     */
    public void setMembers(Integer members) {
        this.members = members;
    }

    
    
    @Override
    @HalSelfLink
    public String getSelfLink() {
        String typeUrl = GroupEvent.TYPE_GROUP.equals( type) ? "/groups/" : "/events/";
        return Utils.createLink( typeUrl + getId(), null);
    }
    
    /**
     *
     * @return enlace a la representación de los comentarios del grupo o evento
     */
    @HalLink( "comments")
    public String getCommentsLink()
    {
        return getSelfLink() + "/comments";
    }
    
    /**
     *
     * @return enlace a la representación de las fotos del grupo o evento
     */
    @HalLink( "photos")
    public String getPhotosLink()
    {
        return getSelfLink() + "/photos";
    }
    
    /**
     *
     * @return enlace a la representación de la foto de perfil del grupo o evento
     */
    @HalLink( "group-photo")
    public String getGroupPhotoLink()
    {
        return getSelfLink() + "/group-photo";
    }
    
    /**
     *
     * @return enlace a la representación de los miembros del grupo o evento
     */
    @HalLink( "members")
    public String getMembersLink()
    {
        return getSelfLink() + "/members";
    }
    
    
    
}
