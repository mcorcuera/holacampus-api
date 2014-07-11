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

package com.holacampus.api.subresources;

import com.holacampus.api.domain.Permission;
import com.holacampus.api.domain.Photo;
import com.holacampus.api.domain.PhotoContainer;
import com.holacampus.api.domain.ProfilePhotoContainer;
import com.holacampus.api.exceptions.HTTPErrorException;
import com.holacampus.api.mappers.PhotoContainerMapper;
import com.holacampus.api.mappers.PhotoMapper;
import com.holacampus.api.mappers.ProfilePhotoContainerMapper;
import com.holacampus.api.resources.particular.ParticularUserResource;
import com.holacampus.api.security.AuthenticationRequired;
import com.holacampus.api.security.AuthenticationScheme;
import com.holacampus.api.security.UserPrincipal;
import com.holacampus.api.utils.MyBatisConnectionFactory;
import com.theoryinpractise.halbuilder.api.ReadableRepresentation;
import com.theoryinpractise.halbuilder.api.RepresentationFactory;
import com.theoryinpractise.halbuilder.jaxrs.HalContext;
import java.io.StringReader;
import java.util.Map;
import java.util.Map.Entry;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.SecurityContext;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 *
 * @author Mikel Corcuera <mik.corcuera@gmail.com>
 */
public class ProfilePhotoResource {
 
    private static final Logger logger = LogManager.getLogger( ParticularUserResource.class.getName());

    
    private PhotoContainer          photoContainer;
    private ProfilePhotoContainer   profilePhotoContainer;
    private String                  path;
    private Long                    parentId;
    
    public ProfilePhotoResource( PhotoContainer photoContainer, 
            ProfilePhotoContainer profilePhotoContainer, Long parentId, 
            String path) 
    {
        this.photoContainer         = photoContainer;
        this.profilePhotoContainer  = profilePhotoContainer;
        this.path                   = path;
        this.parentId               = parentId;
        
    }
    
    @GET
    @AuthenticationRequired( AuthenticationScheme.AUTHENTICATION_SCHEME_TOKEN)
    @Produces( { RepresentationFactory.HAL_JSON})
    public Photo getProfilePhoto() {
        Photo photo = null;
        SqlSession session = MyBatisConnectionFactory.getSession().openSession();

        try {
            PhotoMapper photoMapper    = session.getMapper( PhotoMapper.class);
            
            if( profilePhotoContainer.getPhoto() == null) {
                throw new HTTPErrorException( Status.NOT_FOUND, "Profile photo not found");
            }
            
            photo = photoMapper.getPhoto( profilePhotoContainer.getPhoto().getId());
            
            photo.setProfilePhoto(true);
            photo.setSelfLink(path);
            session.commit();
         }catch( HTTPErrorException e) {
            throw e;
        }catch( Exception e) {
            logger.info( e);
            throw new HTTPErrorException( Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }finally {
            session.close();
        }
        return photo;
    }
    
    @PUT
    @Produces( { RepresentationFactory.HAL_JSON})
    @Consumes( {MediaType.APPLICATION_JSON})
    public Photo changeProfilePhoto( String data, @Context SecurityContext sc) {
        
        ReadableRepresentation r = HalContext.getRepresentationFactory().readRepresentation( new StringReader(data));
        
        Photo photo;
        Long desiredPhotoId = null;
        UserPrincipal up = (UserPrincipal) sc.getUserPrincipal();
        Map<String,Object> properties = r.getProperties();
        
        for( Entry<String,Object> e : properties.entrySet()) {
            logger.info( "Propertie: " + e.getKey());
        }
        
        try {
            desiredPhotoId = Long.parseLong( (String) r.getValue("photoId"));
        }catch( Exception e) {
            throw new HTTPErrorException( Status.BAD_REQUEST, "Bad profile photo request: " + e.getMessage());
        }
        
        SqlSession session = MyBatisConnectionFactory.getSession().openSession();

        try {
            PhotoMapper photoMapper                     = session.getMapper( PhotoMapper.class);
            ProfilePhotoContainerMapper profileMapper   = session.getMapper( ProfilePhotoContainerMapper.class);
            PhotoContainerMapper containerMapper        = session.getMapper( PhotoContainerMapper.class);
            
            long photoContainerId = photoMapper.getPhotoContainerId( desiredPhotoId);
            
            if( !photoContainer.getId().equals( photoContainerId)) {
                throw new HTTPErrorException( Status.FORBIDDEN, "You must own the photo");
            }
            Permission permission = new Permission();
            
            containerMapper.getPermissions( up.getId(), photoContainerId, permission);
            
            if( !permission.getLevel().equals( Permission.LEVEL_PARENT_OWNER)) {
                throw new HTTPErrorException( Status.FORBIDDEN, "You must own the element");
            }
            
            profileMapper.setProfilePhoto( desiredPhotoId, profilePhotoContainer.getId());
            
            photo = photoMapper.getPhoto( desiredPhotoId);
            photo.setProfilePhoto(true);
            photo.setSelfLink(path);
            session.commit();
         }catch( HTTPErrorException e) {
            throw e;
        }catch( Exception e) {
            logger.info( e);
            throw new HTTPErrorException( Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }finally {
            session.close();
        }
        
        return photo;
    }
    
    @DELETE
    @AuthenticationRequired( AuthenticationScheme.AUTHENTICATION_SCHEME_TOKEN)
    public void deleteProfilePhoto( @Context SecurityContext sc) {
        
        Photo photo = null;
        UserPrincipal up = (UserPrincipal) sc.getUserPrincipal();
        SqlSession session = MyBatisConnectionFactory.getSession().openSession();

        try {
            PhotoMapper photoMapper                     = session.getMapper( PhotoMapper.class);
            PhotoContainerMapper containerMapper        = session.getMapper( PhotoContainerMapper.class);  
            ProfilePhotoContainerMapper profileMapper   = session.getMapper( ProfilePhotoContainerMapper.class);
            
            if( profilePhotoContainer.getPhoto() == null) {
                return;
            }
            
            photo = photoMapper.getPhoto( profilePhotoContainer.getPhoto().getId());
            
            long photoContainerId = photoMapper.getPhotoContainerId( photo.getId());
            
            Permission permission = new Permission();
            
            containerMapper.getPermissions( up.getId(), photoContainerId, permission);
            
            if( !permission.getLevel().equals( Permission.LEVEL_PARENT_OWNER)) {
                throw new HTTPErrorException( Status.FORBIDDEN, "You must own the element");
            }
             
            profileMapper.deleteProfilePhoto(profilePhotoContainer.getId());
            
            session.commit();
            
            session.commit();
         }catch( HTTPErrorException e) {
            throw e;
        }catch( Exception e) {
            logger.info( e);
            throw new HTTPErrorException( Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }finally {
            session.close();
        }
    }
}
