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
 *
 *  @author Mikel Corcuera <mik.corcuera@gmail.com>  
 */
@HalRootElement
public class GroupEvent implements Linkable{
   
    public static final String TYPE_EVENT = "EVENT";

    public static final String TYPE_GROUP = "GROUP";
    
    @HalProperty( name="id")
    private Long                id;
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
    @HalEmbedded( "group-photo")
    private Photo               groupPhoto;
    
    @HalEmbedded( "permission")
    private Permission          permission;
    
    private String              type;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public ActiveElement getCreator() {
        return creator;
    }

    public void setCreator(ActiveElement creator) {
        this.creator = creator;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Timestamp getEventDate() {
        return eventDate;
    }

    public void setEventDate( Timestamp eventDate) {
        this.eventDate = eventDate;
    }

    public Photo getGroupPhoto() {
        return groupPhoto;
    }

    public void setGroupPhoto(Photo groupPhoto) {
        this.groupPhoto = groupPhoto;
    }

    public Permission getPermission() {
        return permission;
    }

    public void setPermission(Permission permission) {
        this.permission = permission;
    }

    public Integer getMembers() {
        return members;
    }

    public void setMembers(Integer members) {
        this.members = members;
    }

    
    
    @Override
    @HalSelfLink
    public String getSelfLink() {
        String typeUrl = GroupEvent.TYPE_GROUP.equals( type) ? "/groups/" : "/events/";
        return Utils.createLink( typeUrl + id, null);
    }
    
    @HalLink( "comments")
    public String getCommentsLink()
    {
        return getSelfLink() + "/comments";
    }
    
    
    @HalLink( "photos")
    public String getPhotosLink()
    {
        return getSelfLink() + "/photos";
    }
    
    
    @HalLink( "group-photo")
    public String getGroupPhotoLink()
    {
        return getSelfLink() + "/group-photo";
    }
    
    @HalLink( "members")
    public String getMembersLink()
    {
        return getSelfLink() + "/members";
    }
    
    
    
}
