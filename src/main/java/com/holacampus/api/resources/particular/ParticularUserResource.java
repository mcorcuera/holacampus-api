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
import com.holacampus.api.hal.HalList;
import com.holacampus.api.mappers.GroupEventMapper;
import com.holacampus.api.mappers.UserMapper;
import com.holacampus.api.security.AuthenticationRequired;
import com.holacampus.api.security.AuthenticationScheme;
import com.holacampus.api.security.PermissionScheme;
import com.holacampus.api.security.PermissionScheme.Action;
import com.holacampus.api.security.UserPrincipal;
import com.holacampus.api.subresources.ConversationsResource;
import com.holacampus.api.subresources.CommentsResource;
import com.holacampus.api.subresources.FriendsResource;
import com.holacampus.api.subresources.PhotosResource;
import com.holacampus.api.subresources.ProfilePhotoResource;
import com.holacampus.api.subresources.StagesResource;
import com.holacampus.api.utils.MyBatisConnectionFactory;
import com.holacampus.api.utils.Utils;
import com.theoryinpractise.halbuilder.api.RepresentationFactory;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Objects;
import javax.ws.rs.DELETE;
import javax.ws.rs.Encoded;
import javax.ws.rs.GET;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * Esta clase se encarga de gestionar las peticiones de la API a un recurso
 * User particualr. Es decir, gestiona las peticiones a la URL 
 * <code>/users/{id}</code> donde <code>id</code> es el identificador
 * del usuario
 *  @author Mikel Corcuera <mik.corcuera@gmail.com>  
 */

@Path( "/users")
public class ParticularUserResource {
    
    private static final Logger logger = LogManager.getLogger( ParticularUserResource.class.getName());
    
    private static final PermissionScheme commentsScheme        = new PermissionScheme();
    private static final PermissionScheme photosScheme          = new PermissionScheme();
    private static final PermissionScheme profilephotoScheme    = new PermissionScheme();
    private static final PermissionScheme photosCommentScheme   = new PermissionScheme();
    
    static {
        commentsScheme  .addPermissionScheme( Action.GET_MULTIPLE, Permission.LEVEL_MEMBER)
                        .addPermissionScheme(Action.POST_MULTIPLE, Permission.LEVEL_MEMBER)
                        .addPermissionScheme(Action.GET_UNIQUE, Permission.LEVEL_MEMBER)
                        .addPermissionScheme(Action.DELETE_UNIQUE, Permission.LEVEL_PARENT_OWNER);
        
        photosScheme    .addPermissionScheme( Action.GET_MULTIPLE, Permission.LEVEL_MEMBER)
                        .addPermissionScheme(Action.POST_MULTIPLE, Permission.LEVEL_PARENT_OWNER)
                        .addPermissionScheme(Action.GET_UNIQUE, Permission.LEVEL_MEMBER)
                        .addPermissionScheme(Action.PUT_UNIQUE, Permission.LEVEL_OWNER)
                        .addPermissionScheme(Action.DELETE_UNIQUE, Permission.LEVEL_PARENT_OWNER);
        
        
        photosCommentScheme     .addPermissionScheme( Action.GET_MULTIPLE, Permission.LEVEL_MEMBER)
                                .addPermissionScheme(Action.POST_MULTIPLE, Permission.LEVEL_MEMBER)
                                .addPermissionScheme(Action.GET_UNIQUE, Permission.LEVEL_MEMBER)
                                .addPermissionScheme(Action.DELETE_UNIQUE, Permission.LEVEL_PARENT_OWNER);
    }
    
    
    @Context
    private UriInfo uriInfo;
    @Context
    private SecurityContext sc;
    
    /**
     * Esta función devuelve la clase que se encargará de gestionar las peticiones
     * relacionadas con los comentarios del usuario. Esto es, a la URL 
     * <code>/users/{id}/comments</code>
     * @param id identificador del usuario
     * @return recurso que se encarga de gestionar la petición
     */
    @Path("/{id}/comments")
    public CommentsResource getCommentsResource( @PathParam("id") long id) 
    {
        SqlSession session = MyBatisConnectionFactory.getSession().openSession();
        CommentContainer c;
        try {            
            UserMapper userMapper   = session.getMapper( UserMapper.class);
            c = userMapper.getCommentContainer(id);
            session.commit();            
            
            if( c == null) {
                throw new HTTPErrorException( Status.NOT_FOUND, "User " + id + " not found");
            }
        } catch( Exception e) {
            logger.error( e.toString());
            throw new HTTPErrorException( Status.NOT_FOUND, "User " + id + " not found");
        } finally {
            session.close();
        }
        
        return new CommentsResource( c, uriInfo.getPath(), ParticularUserResource.commentsScheme);
    }
    
    /**
     * Esta función devuelve la clase que se encargará de gestionar las peticiones
     * relacionadas con las fotos del usuario. Esto es, a la URL 
     * <code>/users/{id}/photos</code>
     * @param id identificador del usuario
     * @return recurso que se encarga de gestionar la petición
     */
    @Path( "/{id}/photos")
    public PhotosResource getPhotosResource( @PathParam("id") long id)
    {
        SqlSession session = MyBatisConnectionFactory.getSession().openSession();
        PhotoContainer c;
        try {            
            UserMapper userMapper   = session.getMapper( UserMapper.class);
            c = userMapper.getPhotoContainer(id);
            session.commit();            
            
            if( c == null) {
                throw new HTTPErrorException( Status.NOT_FOUND, "User " + id + " not found");
            }
        } catch( Exception e) {
            logger.error( e.toString());
            throw new HTTPErrorException( Status.NOT_FOUND, "User " + id + " not found");
        } finally {
            session.close();
        }  
        
        return new PhotosResource( c, uriInfo.getPath(), photosScheme, photosCommentScheme);
    }
    
     /**
     * Esta función devuelve la clase que se encargará de gestionar las peticiones
     * relacionadas con la foto de perfil del usuario. Esto es, a la URL 
     * <code>/users/{id}/profile-photo</code>
     * @param id identificador del usuario
     * @return recurso que se encarga de gestionar la petición
     */
    @Path( "/{id}/profile-photo")
    public ProfilePhotoResource getProfilePhotoResource( @PathParam("id") long id)
    {
        SqlSession session = MyBatisConnectionFactory.getSession().openSession();
        PhotoContainer pc;
        ProfilePhotoContainer ppc;
        try {            
            UserMapper userMapper   = session.getMapper( UserMapper.class);
            pc                      = userMapper.getPhotoContainer(id);
            ppc                     = userMapper.getProfilePhotoContainer(id);
            
            if( pc == null) {
                throw new HTTPErrorException( Status.NOT_FOUND, "User " + id + " not found");
            }
            
            session.commit();            

        } catch( Exception e) {
            logger.error( e.toString());
            throw new HTTPErrorException( Status.NOT_FOUND, "User " + id + " not found");
        } finally {
            session.close();
        }  
        
        return new ProfilePhotoResource( pc, ppc, uriInfo.getPath());
    }
    
     /**
     * Esta función devuelve la clase que se encargará de gestionar las peticiones
     * relacionadas con los amigos del usuario. Esto es, a la URL 
     * <code>/users/{id}/friends</code>
     * @param id identificador del usuario
     * @return recurso que se encarga de gestionar la petición
     */
    @Path( "/{id}/friends")
    public FriendsResource getFriendsResource( @PathParam("id") Long id)
    {
        SqlSession session = MyBatisConnectionFactory.getSession().openSession();

        try {
            UserMapper mapper    = session.getMapper( UserMapper.class);
            User user = mapper.getUser(id);
            if( user == null)
                throw new HTTPErrorException( Status.NOT_FOUND, "User not found");
            session.commit();
        }catch( HTTPErrorException e) {
            throw e;
        }catch( Exception e) {
            logger.info( e);
            throw new InternalServerErrorException( e.getMessage());
        }finally {
            session.close();
        }

        return new FriendsResource( id);
    }
    
   /**
     * Esta función devuelve la clase que se encargará de gestionar las peticiones
     * relacionadas con las etapas dentro de la trayectoria académica
     * del usuario. Esto es, a la URL 
     * <code>/users/{id}/stages</code>
     * @param id identificador del usuario
     * @return recurso que se encarga de gestionar la petición
     */
    @Path( "/{id}/stages")
    public StagesResource getStagesResource( @PathParam("id") Long id)
    {
        SqlSession session = MyBatisConnectionFactory.getSession().openSession();
        User user = null;
        try {
            UserMapper mapper    = session.getMapper( UserMapper.class);
            user = mapper.getUser(id);
            if( user == null)
                throw new HTTPErrorException( Status.NOT_FOUND, "User not found");
            session.commit();
        }catch( HTTPErrorException e) {
            throw e;
        }catch( Exception e) {
            logger.info( e);
            throw new InternalServerErrorException( e.getMessage());
        }finally {
            session.close();
        }

        return new StagesResource( user);
    }
    
      /**
     * Esta función devuelve la clase que se encargará de gestionar las peticiones
     * relacionadas con las conversaciones del usuario. Esto es, a la URL 
     * <code>/users/{id}/conversations</code>
     * @param id identificador del usuario
     * @return recurso que se encarga de gestionar la petición
     */
    @Path( "/{id}/conversations")
    public ConversationsResource getConversationsResource( @PathParam("id") Long id)
    {
        SqlSession session = MyBatisConnectionFactory.getSession().openSession();
        User user = null;
        try {
            UserMapper mapper    = session.getMapper( UserMapper.class);
            user = mapper.getUser(id);
            if( user == null)
                throw new HTTPErrorException( Status.NOT_FOUND, "User not found");
            session.commit();
        }catch( HTTPErrorException e) {
            throw e;
        }catch( Exception e) {
            logger.info( e);
            throw new InternalServerErrorException( e.getMessage());
        }finally {
            session.close();
        }

        return new ConversationsResource( user);
    }
    
      /**
     * Esta función gestiona las peticiones GET al recurso 
     * <code>/users/{id}</code>. Esta operación devuelve la representación
     * del usuario identificado por <b>id</b>
     * @param id el identificador del usuario
     * @return la representación del usuario
     */
    @GET
    @Path("/{id}")
    @AuthenticationRequired( AuthenticationScheme.AUTHENTICATION_SCHEME_TOKEN)
    @Produces( { RepresentationFactory.HAL_JSON})
    public User getUser( @PathParam("id") long id)
    {
        logger.info( "[GET] " + uriInfo.getPath());
        
        User user;
        UserPrincipal up = (UserPrincipal) sc.getUserPrincipal();
        
        SqlSession session = MyBatisConnectionFactory.getSession().openSession();
        
        try {            
            UserMapper userMapper   = session.getMapper( UserMapper.class);
            user = userMapper.getUser(id);      
            
            if( user == null) {
                throw new HTTPErrorException( Status.NOT_FOUND, "User not found");
            }
            
            if( user.getProfilePhoto() != null) {
                user.getProfilePhoto().setProfilePhoto( true);
                user.getProfilePhoto().setSelfLink( uriInfo.getPath() + "/profile-photo");
            }
            
            Permission permission = new Permission();
            
            userMapper.getPermissions( up.getId(), id, permission);
            user.setPermission(permission);
            session.commit();      
        } catch( Exception e) {
            logger.error( e.toString());
            throw new HTTPErrorException( Status.NOT_FOUND, "User not found");
        } finally {
            session.close();
        }   
        return user;
    }
    
      
   /**
     * Esta función gestiona las peticiones DELETE al recurso 
     * <code>/users/{id}</code>. Esta operación elimina al
     * usuario identificado por <b>id</b>
     * @param id el identificador del usuario
     */
    @DELETE
    @Path("/{id}")
    @AuthenticationRequired( AuthenticationScheme.AUTHENTICATION_SCHEME_BASIC)
    public void deleteUser( @PathParam( "id") Long id)
    {
        /*
        * Only the user authenticated can delete himself, so we check it here
        */
        if( Objects.equals(id, ((UserPrincipal) sc.getUserPrincipal()).getId())) {
            SqlSession session = MyBatisConnectionFactory.getSession().openSession();
            try {
                UserMapper mapper    = session.getMapper( UserMapper.class);
                int result           = mapper.deleteUser(id);
                
                if( result == 0) {
                    throw new HTTPErrorException( Status.INTERNAL_SERVER_ERROR);
                }
                session.commit();
            }catch( Exception e) {
                logger.info( e);
                throw new InternalServerErrorException( e.getMessage());
            }finally {
                session.close();
            }
        }else {
            throw new HTTPErrorException( Status.UNAUTHORIZED, "You are not authorized");
        }
    }
    
    /**
      * Esta función gestiona las peticiones GET al recurso 
     * <code>/users/{id}/groups|events</code>. Esta operación devuelve una 
     * lista con los grupos o eventos a los que pertenece el 
     * usuario identificado por <b>id</b>
     * @param id el identificador del usuario
     * @param type identifica si se trata de una petición de grupos o eventos
     * @param page página de los resultados
     * @param size tamaño de los resultados
     * @param q cadena de caracteres que sirve para filtrar por nombre los
     * resultados
     * @return lista con los grupos o eventos del tamaño especificado y filtrada
     * por nombre
     * @throws UnsupportedEncodingException
     */
    @Path( "/{id}/{type:groups|events}")
    @GET
    @Produces( { RepresentationFactory.HAL_JSON})
    @AuthenticationRequired( AuthenticationScheme.AUTHENTICATION_SCHEME_TOKEN)
    @Encoded
    public HalList<GroupEvent> getGroupsEvents( @PathParam("id") Long id, @PathParam("type") String type, 
            @QueryParam("page") Integer page, @QueryParam( "size") Integer size, @QueryParam( "q") String q) throws UnsupportedEncodingException
    {
        return getGroupsEvents(id, type, page, size, q, false);
    }
    
    /**
      * Esta función gestiona las peticiones GET al recurso 
     * <code>/users/{id}/events/past</code>. Esta operación devuelve una 
     * lista con los eventos ya pasados a los que pertenece el 
     * usuario identificado por <b>id</b>
     * @param id el identificador del usuario
     * @param page página de los resultados
     * @param size tamaño de los resultados
     * @param q cadena de caracteres que sirve para filtrar por nombre los
     * resultados
     * @return lista con los eventos pasados del tamaño especificado y filtrada
     * por nombre
     * @throws UnsupportedEncodingException
     */
    @Path( "/{id}/events/past")
    @GET
    @Produces( { RepresentationFactory.HAL_JSON})
    @AuthenticationRequired( AuthenticationScheme.AUTHENTICATION_SCHEME_TOKEN)
    @Encoded
    public HalList<GroupEvent> getPastEvents( @PathParam("id") Long id, 
            @QueryParam("page") Integer page, @QueryParam( "size") Integer size, @QueryParam( "q") String q) throws UnsupportedEncodingException
    {
        return getGroupsEvents(id, GroupEvent.TYPE_EVENT, page, size, q, false);
    }
    
    private HalList<GroupEvent> getGroupsEvents( Long id,  String type,  
            Integer page, Integer size,  String q, Boolean pending) throws UnsupportedEncodingException
    {
        page = Utils.getValidPage(page);
        size = Utils.getValidSize(size);
        if( q != null) {
            q   = URLDecoder.decode(q, "UTF-8");
        }
        String elementType = null;
        if( "groups".equals(type))
            elementType = GroupEvent.TYPE_GROUP;
        else
            elementType = GroupEvent.TYPE_EVENT;
        
        RowBounds rb                = Utils.createRowBounds(page, size);
        UserPrincipal up            = (UserPrincipal) sc.getUserPrincipal(); 
        SqlSession session          = MyBatisConnectionFactory.getSession().openSession();
        HalList<GroupEvent> groups  = null;
        
        try {            
            GroupEventMapper mapper     = session.getMapper( GroupEventMapper.class);
            UserMapper userMapper       = session.getMapper( UserMapper.class);
            
            User user = userMapper.getUser(id);
            if( user == null)
                throw new HTTPErrorException( Status.NOT_FOUND, "User not found");
            
            Permission permission = new Permission();
            
            userMapper.getPermissions( up.getId(), id, permission);
            
            if( Permission.LEVEL_USER.equals( permission.getLevel()))
                throw new HTTPErrorException( Status.FORBIDDEN, "You are not allowed here");
            
            List<GroupEvent> groupList  = mapper.getGroupsEventsForActiveElement(elementType, q, id, pending, rb);
            int total                   = mapper.getTotalGroupsEventsForActiveElement(elementType, q, id, pending);
            session.commit();
            
            for( GroupEvent group : groupList) {
                Permission permissions = new Permission();
                mapper.getPermissions( up.getId(), group.getId(), permissions);
                group.setPermission(permissions); 
            }
            
            groups = new HalList( groupList, total);
            
            groups.setResourceRelativePath( uriInfo.getPath());
            groups.setPage(page);
            groups.setSize(size);
            groups.setQuery(q);
            
        }catch( Exception e) {
            logger.error( e.toString());
            throw new InternalServerErrorException();
        }
        finally {
            session.close();
        }
        
        return groups;
    }
    
    
   
}
