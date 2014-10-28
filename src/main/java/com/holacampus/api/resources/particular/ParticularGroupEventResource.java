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

package com.holacampus.api.resources.particular;

import com.holacampus.api.domain.CommentContainer;
import com.holacampus.api.domain.GroupEvent;
import com.holacampus.api.domain.Permission;
import com.holacampus.api.domain.PhotoContainer;
import com.holacampus.api.domain.ProfilePhotoContainer;
import com.holacampus.api.domain.User;
import com.holacampus.api.exceptions.HTTPErrorException;
import com.holacampus.api.mappers.GroupEventMapper;
import com.holacampus.api.mappers.UserMapper;
import com.holacampus.api.security.AuthenticationRequired;
import com.holacampus.api.security.AuthenticationScheme;
import com.holacampus.api.security.PermissionScheme;
import com.holacampus.api.security.UserPrincipal;
import com.holacampus.api.subresources.CommentsResource;
import com.holacampus.api.subresources.MembersResource;
import com.holacampus.api.subresources.PhotosResource;
import com.holacampus.api.subresources.ProfilePhotoResource;
import com.holacampus.api.utils.MyBatisConnectionFactory;
import com.theoryinpractise.halbuilder.api.RepresentationFactory;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * Esta clase se encarga de gestionar las peticiones de la API a un recurso
 * GroupEvent particualr. Es decir, gestiona las peticiones a la URL 
 * <code>/groups|events/{id}</code> donde <code>id</code> es el identificador
 * del grupo o evento.
 * @author Mikel Corcuera <mik.corcuera@gmail.com>
 */

@Path("/{type:groups|events}/{id}")
public class ParticularGroupEventResource {
    
    private static final Logger logger = LogManager.getLogger( ParticularGroupEventResource.class.getName());
    
    private static final PermissionScheme commentsScheme        = new PermissionScheme();
    private static final PermissionScheme photosScheme          = new PermissionScheme();
    private static final PermissionScheme photosCommentScheme   = new PermissionScheme();
    
    static {
        commentsScheme  .addPermissionScheme( PermissionScheme.Action.GET_MULTIPLE, Permission.LEVEL_MEMBER)
                        .addPermissionScheme(PermissionScheme.Action.POST_MULTIPLE, Permission.LEVEL_MEMBER)
                        .addPermissionScheme(PermissionScheme.Action.GET_UNIQUE, Permission.LEVEL_MEMBER)
                        .addPermissionScheme(PermissionScheme.Action.DELETE_UNIQUE, Permission.LEVEL_PARENT_OWNER);
        
        photosScheme    .addPermissionScheme( PermissionScheme.Action.GET_MULTIPLE, Permission.LEVEL_MEMBER)
                        .addPermissionScheme(PermissionScheme.Action.POST_MULTIPLE, Permission.LEVEL_MEMBER)
                        .addPermissionScheme(PermissionScheme.Action.GET_UNIQUE, Permission.LEVEL_MEMBER)
                        .addPermissionScheme(PermissionScheme.Action.PUT_UNIQUE, Permission.LEVEL_OWNER)
                        .addPermissionScheme(PermissionScheme.Action.DELETE_UNIQUE, Permission.LEVEL_PARENT_OWNER);
        
        photosCommentScheme     .addPermissionScheme( PermissionScheme.Action.GET_MULTIPLE, Permission.LEVEL_MEMBER)
                                .addPermissionScheme(PermissionScheme.Action.POST_MULTIPLE, Permission.LEVEL_MEMBER)
                                .addPermissionScheme(PermissionScheme.Action.GET_UNIQUE, Permission.LEVEL_MEMBER)
                                .addPermissionScheme(PermissionScheme.Action.DELETE_UNIQUE, Permission.LEVEL_PARENT_OWNER);
    }
    
    @Context
    private UriInfo uriInfo;
    @Context
    private SecurityContext sc;
    
    @PathParam( "type")
    private String type;
    @PathParam( "id")
    private Long id;
    
    /**
     * Esta función devuelve la clase que se encargará de gestionar las peticiones
     * relacionadas con los comentarios del grupo o evento. Esto es, a la URL 
     * <code>/groups|events/{id}/comments</code>
     * @return recurso que se encarga de gestionar la petición
     */
    @Path("/comments")
    public CommentsResource getCommentResource() 
    {
        SqlSession session = MyBatisConnectionFactory.getSession().openSession();
        CommentContainer c;
        try {            
            GroupEventMapper mapper   = session.getMapper( GroupEventMapper.class);
            c = mapper.getCommentContainer(id);
            session.commit();            
            
            if( c == null) {
                throw new HTTPErrorException( Response.Status.NOT_FOUND, "Group/Event " + id + " not found");
            }
        } catch( Exception e) {
            logger.error( e.toString());
            throw new HTTPErrorException( Response.Status.NOT_FOUND, "Group/Event " + id + " not found");
        } finally {
            session.close();
        }
        
        return new CommentsResource( c, uriInfo.getPath(), ParticularGroupEventResource.commentsScheme);
    }
    
     /**
     * Esta función devuelve la clase que se encargará de gestionar las peticiones
     * relacionadas con las fotos del grupo o evento. Esto es, a la URL 
     * <code>/groups|events/{id}/photos</code>
     * @return recurso que se encarga de gestionar la petición
     */
    @Path( "/photos")
    public PhotosResource getPhotosResource()
    {
        SqlSession session = MyBatisConnectionFactory.getSession().openSession();
        PhotoContainer c;
        try {            
            GroupEventMapper mapper   = session.getMapper( GroupEventMapper.class);
            c = mapper.getPhotoContainer(id);
            session.commit();            
            
            if( c == null) {
                throw new HTTPErrorException( Response.Status.NOT_FOUND, "Group/Event " + id + " not found");
            }
        } catch( Exception e) {
            logger.error( e.toString());
            throw new HTTPErrorException( Response.Status.NOT_FOUND, "Group/Event " + id + " not found");
        } finally {
            session.close();
        }  
        
        return new PhotosResource( c, uriInfo.getPath(), ParticularGroupEventResource.photosScheme, ParticularGroupEventResource.photosCommentScheme);
    }
    
      
     /**
     * Esta función devuelve la clase que se encargará de gestionar las peticiones
     * relacionadas con la foto de perfil del grupo o evento. Esto es, a la URL 
     * <code>/groups|events/{id}/group-photo</code>
     * @return recurso que se encarga de gestionar la petición
     */
    @Path( "/group-photo")
    public ProfilePhotoResource getProfilePhotoResource()
    {
        SqlSession session = MyBatisConnectionFactory.getSession().openSession();
        PhotoContainer pc;
        ProfilePhotoContainer ppc;
        try {            
            GroupEventMapper mapper = session.getMapper( GroupEventMapper.class);
            pc                      = mapper.getPhotoContainer(id);
            ppc                     = mapper.getProfilePhotoContainer(id);
            
            if( pc == null) {
                throw new HTTPErrorException( Response.Status.NOT_FOUND, "Group/Event " + id + " not found");
            }
            
            session.commit();            

        } catch( Exception e) {
            logger.error( e.toString());
            throw new HTTPErrorException( Response.Status.NOT_FOUND, "Group/Event " + id + " not found");
        } finally {
            session.close();
        }  
        return new ProfilePhotoResource( pc, ppc, uriInfo.getPath());
    }
    
    /**
     * Esta función devuelve la clase que se encargará de gestionar las peticiones
     * relacionadas con los miembros del grupo o evento. Esto es, a la URL 
     * <code>/groups|events/{id}/members</code>
     * @return recurso que se encarga de gestionar la petición
     */
    @Path( "/members")
    public MembersResource getMembersResource()
    {
        SqlSession session = MyBatisConnectionFactory.getSession().openSession();
        GroupEvent parent = null;
        
        String elementType = null;
        if( "groups".equals(type))
            elementType = GroupEvent.TYPE_GROUP;
        else
            elementType = GroupEvent.TYPE_EVENT;
        
        try {            
            GroupEventMapper mapper = session.getMapper( GroupEventMapper.class);
            parent = mapper.getGroupEvent( elementType, id);
            
            if( parent == null) {
                throw new HTTPErrorException( Response.Status.NOT_FOUND, "Group/Event " + id + " not found");
            }
            
            session.commit();            

        } catch( Exception e) {
            logger.error( e.toString());
            throw new HTTPErrorException( Response.Status.NOT_FOUND, "Group/Event " + id + " not found");
        } finally {
            session.close();
        }  
        return new MembersResource( parent);
    }
    
    /**
     * Esta función gestiona las peticiones GET al recurso 
     * <code>/groups|events/{id}</code>. Esta operación devuelve la representación
     * del grupo o evento identificado por <b>id</b>
     * @return representación del grupo o evento
     */
    @GET
    @AuthenticationRequired( AuthenticationScheme.AUTHENTICATION_SCHEME_TOKEN)
    @Produces( { RepresentationFactory.HAL_JSON})
    public GroupEvent getGroupEvent()
    {
        logger.info( "[GET] " + uriInfo.getPath());
        String elementType = null;
        if( "groups".equals(type))
            elementType = GroupEvent.TYPE_GROUP;
        else
            elementType = GroupEvent.TYPE_EVENT;
        
        GroupEvent group;
        UserPrincipal up = (UserPrincipal) sc.getUserPrincipal();
        
        SqlSession session = MyBatisConnectionFactory.getSession().openSession();
        
        try {            
            GroupEventMapper mapper   = session.getMapper( GroupEventMapper.class);
            group = mapper.getGroupEvent( elementType, id);      
            
            if( group == null) {
                throw new HTTPErrorException( Response.Status.NOT_FOUND, "Group/Event not found");
            }
            
            if( group.getGroupPhoto()!= null) {
                group.getGroupPhoto().setProfilePhoto( true);
                group.getGroupPhoto().setSelfLink( uriInfo.getPath() + "/group-photo");
            }
            
            Permission permission = new Permission();
            
            mapper.getPermissions( up.getId(), id, permission);
            group.setPermission(permission);
            session.commit();      
        } catch( Exception e) {
            logger.error( e.toString());
            throw new HTTPErrorException( Response.Status.NOT_FOUND, "Group/Event not found");
        } finally {
            session.close();
        }   
        return group;
    }
}
