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

import com.holacampus.api.domain.Friend;
import com.holacampus.api.domain.Friendship;
import com.holacampus.api.domain.Permission;
import com.holacampus.api.domain.User;
import com.holacampus.api.exceptions.HTTPErrorException;
import com.holacampus.api.hal.HalList;
import com.holacampus.api.mappers.FriendshipMapper;
import com.holacampus.api.mappers.UserMapper;
import com.holacampus.api.security.AuthenticationRequired;
import com.holacampus.api.security.AuthenticationScheme;
import com.holacampus.api.security.UserPrincipal;
import com.holacampus.api.utils.MyBatisConnectionFactory;
import com.holacampus.api.utils.Utils;
import com.theoryinpractise.halbuilder.api.RepresentationFactory;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * Esta clase se encarga de gestionar las peticiones de la API al subrecurso
 * Friends. Es decir, gestiona las peticiones a la URL 
 * <code>users/{uid}/friends</code>.
 * <br/><br/>
 * Este subrecurso se encarga de gestionar las relaciones de amistad de un usuario.
 * @author Mikel Corcuera <mik.corcuera@gmail.com>
 */
public class FriendsResource {
    
    private static final Logger logger = LogManager.getLogger( FriendsResource.class.getName());

        
    private final Long userId;
    
    /**
     * Constructor del subrecurso de relaciones de amistad
     * @param userId identificador del usuario al que pertenece el subrecurso
     */
    public FriendsResource( Long userId)
    {
        this.userId = userId;
    }
    
    /**
     * Esta función gestiona las peticiones GET al recurso 
     * <code>users/{uid}/friends</code>. Esta operación devuelve un lista 
     * con las relaciones de amistad del usuario. Cabe notar que solo se 
     * devuelven las relaciones de amistad confirmadas (no las pendientes de 
     * confirmación por alguna de las dos partes)
     * @param page página de los resultados
     * @param size tamaño de los resultados
     * @param sc información de seguridad y autenticación de la petición
     * @param uriInfo  información de la URL de la petición
     * @return lista de relaciones de amistad del tamaño especificado
     */
    @GET
    @AuthenticationRequired( AuthenticationScheme.AUTHENTICATION_SCHEME_TOKEN)
    @Produces( { RepresentationFactory.HAL_JSON})
    public HalList<Friend> getFriends(  @QueryParam("page") Integer page, @QueryParam( "size") Integer size,
            @Context SecurityContext sc, @Context UriInfo uriInfo)
    {
        logger.info( "[GET] " + uriInfo.getPath());
        
        UserPrincipal up = (UserPrincipal) sc.getUserPrincipal();
        
        return getFriends( uriInfo.getPath(), up.getId(), false, false, null, page, size);
    }
    
    /**
     * Esta función gestiona las peticiones GET al recurso 
     * <code>users/{uid}/friends/pending</code>. Esta operación devuelve un lista 
     * con las relaciones de amistad del usuario que aún están pendientes de confirmar
     * por el usuario al que le fue enviada la petición
     * @param page página de los resultados
     * @param size tamaño de los resultados
     * @param sc información de seguridad y autenticación de la petición
     * @param uriInfo  información de la URL de la petición
     * @return lista de relaciones de amistad pendientes de confirmación  del tamaño especificado
     */
    @Path("/pending")
    @GET
    @AuthenticationRequired( AuthenticationScheme.AUTHENTICATION_SCHEME_TOKEN)
    @Produces( { RepresentationFactory.HAL_JSON})
    public HalList<Friend> getPendingFriends( @QueryParam("page") Integer page, @QueryParam( "size") Integer size,
            @Context SecurityContext sc, @Context UriInfo uriInfo)
    {
        logger.info( "[GET] " + uriInfo.getPath());
        
        UserPrincipal up = (UserPrincipal) sc.getUserPrincipal();
        
        if( !userId.equals(up.getId())) {
            throw new HTTPErrorException( Status.FORBIDDEN, "You are not allowed here");
        }
        
        return getFriends( uriInfo.getPath(), up.getId(), false, true, null, page, size);
    }
    
     /**
     * Esta función gestiona las peticiones GET al recurso 
     * <code>users/{uid}/friends/uncorfirmed</code>. Esta operación devuelve un lista 
     * con las relaciones de amistad del usuario que aún están pendientes de confirmar
     * por el usuario al que pertenece el subrecurso
     * @param page página de los resultados
     * @param size tamaño de los resultados
     * @param sc información de seguridad y autenticación de la petición
     * @param uriInfo  información de la URL de la petición
     * @return lista de relaciones de amistad pendientes de confirmación  del tamaño especificado
     */
    @Path("/unconfirmed")
    @GET
    @AuthenticationRequired( AuthenticationScheme.AUTHENTICATION_SCHEME_TOKEN)
    @Produces( { RepresentationFactory.HAL_JSON})
    public HalList<Friend> getUnconfirmedFriends( @QueryParam("page") Integer page, @QueryParam( "size") Integer size,
            @Context SecurityContext sc, @Context UriInfo uriInfo)
    {
        logger.info( "[GET] " + uriInfo.getPath());
        
        UserPrincipal up = (UserPrincipal) sc.getUserPrincipal();
        
        if( !userId.equals(up.getId())) {
            throw new HTTPErrorException( Status.FORBIDDEN, "You are not allowed here");
        }
        
        return getFriends( uriInfo.getPath(), up.getId(), true, false, null, page, size);
    }
    
    private HalList<Friend> getFriends( String path, Long currentUser, boolean unconfirmed, 
            boolean pending, String query, Integer page, Integer size)
    {
        HalList<Friend> friends = new HalList<Friend>();
        page            = Utils.getValidPage(page);
        size            = Utils.getValidSize(size);
        RowBounds rb    = Utils.createRowBounds(page, size);
        
        
        SqlSession session = MyBatisConnectionFactory.getSession().openSession();

        try {
            FriendshipMapper friendMapper   = session.getMapper(FriendshipMapper.class);
            UserMapper userMapper           = session.getMapper( UserMapper.class);
            List<Friendship> friendships    = friendMapper.getFriends(userId, null, unconfirmed, pending, rb);
            int total                       = friendMapper.getTotalFriends(userId, null, unconfirmed, pending);
            List<Friend> friendList = null;
            if( total > 0) {
                friendList = new ArrayList<Friend>();
                for( Friendship fs : friendships) {
                    Friend friend = new Friend( fs, userId);
                   
                    User user = userMapper.getUser( friend.getUser().getId());
                    Permission permission = new Permission();
                    userMapper.getPermissions( currentUser, user.getId(), permission);
                    user.setPermission(permission);
                    friend.setUser(user);
                    friend.setSelfLink( "users/" + userId + "/friends/" + friend.getUser().getId());
                    
                    // If the user is getting his friends, put information of who asked who
                    if( userId == currentUser) {
                        if( fs.getSender().getId() == currentUser)
                            friend.setAskedByMe(true);
                        else friend.setAskedByMe(false);
                    }
                    friendList.add(friend);
                }
            }
            
            friends.setResources(friendList);
            friends.setResourceRelativePath( path);
            friends.setTotal( total);
            friends.setPage(page);
            friends.setSize(size);
            
            session.commit();
            
        }catch( HTTPErrorException e) {
            throw e;
        }
        catch( Exception e) {
            logger.info( e);
            throw new HTTPErrorException( Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }finally {
            session.close();
        }
        
        return friends;
    }
    
    /**
     * Esta función gestiona las peticiones POST al recurso 
     * <code>users/{uid}/friends/</code>. Esta operación envía una nueva petición 
     * de amistad al usuario al que pertenece el subrecurso de parte del usuario
     * que realiza la petición
     * @param sc información de seguridad y autenticación de la petición
     * @param uriInfo  información de la URL de la petición
     * @return representación de la relación de amistad recien creada
     */
    @POST
    @AuthenticationRequired( AuthenticationScheme.AUTHENTICATION_SCHEME_TOKEN)
    @Produces( { RepresentationFactory.HAL_JSON})
    public Friend createFriendRequest( @Context SecurityContext sc, @Context UriInfo uriInfo)
    {
        Friend friend = null;
        UserPrincipal up = (UserPrincipal) sc.getUserPrincipal();
        
        if( up.getId().equals( userId)) {
            throw new HTTPErrorException(Status.CONFLICT, "You can't be friends with yourself");
        }
        
        SqlSession session = MyBatisConnectionFactory.getSession().openSession();
        
        try {
            FriendshipMapper mapper = session.getMapper( FriendshipMapper.class);
            UserMapper userMapper           = session.getMapper( UserMapper.class);

            Friendship friendship = mapper.getFriend( up.getId(), userId);
            
            if( friendship != null) {
                throw new HTTPErrorException(Status.CONFLICT, "You are already friend with " + userId);
            }
            
            int result = mapper.createFriendship( up.getId(), userId);
            
            if( result == 0) {
                throw new HTTPErrorException( Status.INTERNAL_SERVER_ERROR, "An error ocurred");
            }
            
            friendship = mapper.getFriend( up.getId(), userId);
            
            friend = new Friend( friendship, userId);
            
            
            User user = userMapper.getUser( friend.getUser().getId());
            Permission permission = new Permission();
            userMapper.getPermissions( up.getId(), user.getId(), permission);
            user.setPermission(permission);
            friend.setUser(user);
            
            if( userId == up.getId()) {
                if( friendship.getSender().getId() == up.getId())
                    friend.setAskedByMe(true);
                else friend.setAskedByMe(false);
            }
            
            friend.setSelfLink( "users/" + up.getId() + "/friends/" + userId);
            
            session.commit();
            
        }catch( HTTPErrorException e) {
            throw e;
        }
        catch( Exception e) {
            logger.info( e);
            throw new HTTPErrorException( Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }finally {
            session.close();
        }
        
        return friend;
    }
    
    /**
     * Esta función gestiona las peticiones GET al recurso 
     * <code>users/{uid}/friends/{id}</code>. Esta operación obtiene la 
     * representación de la relación de amistad identificada por <b>id</b>
     * @param id identificador de la relación de amistad
     * @param sc información de seguridad y autenticación de la petición
     * @param uriInfo  información de la URL de la petición
     * @return representación de la relación de amistad
     */
    @Path("/{id}")
    @GET
    @AuthenticationRequired( AuthenticationScheme.AUTHENTICATION_SCHEME_TOKEN)
    @Produces( { RepresentationFactory.HAL_JSON})
    public Friend getFriend( @PathParam("id") Long id, @Context SecurityContext sc, @Context UriInfo uriInfo )
    {
        logger.info( "[GET] " + uriInfo.getPath());
        Friend friend = null;
        UserPrincipal up = (UserPrincipal) sc.getUserPrincipal();
        
        SqlSession session = MyBatisConnectionFactory.getSession().openSession();

        try {
            FriendshipMapper friendMapper   = session.getMapper(FriendshipMapper.class);
            UserMapper userMapper           = session.getMapper( UserMapper.class);
            Friendship f                    = friendMapper.getFriend(userId, id);
            
            if( f == null)
                throw new HTTPErrorException( Status.NOT_FOUND, "friend " + id + " not found");
            
            friend = new Friend( f, userId);
            
            if( userId == up.getId()) {
                if( f.getSender().getId() == up.getId())
                    friend.setAskedByMe(true);
                else friend.setAskedByMe(false);
            }
            User user = userMapper.getUser( friend.getUser().getId());
            Permission permission = new Permission();
            userMapper.getPermissions( up.getId(), user.getId(), permission);
            user.setPermission(permission);
            friend.setUser(user);
            
            friend.setSelfLink( uriInfo.getPath());
            
        }catch( HTTPErrorException e) {
            throw e;
        }
        catch( Exception e) {
            logger.info( e);
            throw new HTTPErrorException( Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }finally {
            session.close();
        }
        
        return friend;
    }
    
    /**
     * Esta función gestiona las peticiones PUT al recurso 
     * <code>users/{uid}/friends/{id}</code>. Esta operación modifica los
     * datos de la petición de amistad identificada por <b>id</b>. De esta 
     * manera se podrán aceptar o denegar las peticiones de amistad
     * @param friend datos de la amistad a modificar
     * @param id identificador de la relación de amistad
     * @param sc información de seguridad y autenticación de la petición
     * @param uriInfo  información de la URL de la petición
     * @return representación de la relación de amistad ya modificada
     */
    @Path("/{id}")
    @PUT
    @AuthenticationRequired( AuthenticationScheme.AUTHENTICATION_SCHEME_TOKEN)
    @Consumes( { RepresentationFactory.HAL_JSON, MediaType.APPLICATION_JSON})
    @Produces( { RepresentationFactory.HAL_JSON})
    public Friend updateFriend( @Valid Friend friend, @PathParam("id") Long id, @Context SecurityContext sc, @Context UriInfo uriInfo )
    {
        logger.info( "[PUT] " + uriInfo.getPath());
        UserPrincipal up = (UserPrincipal) sc.getUserPrincipal();
        
        // Check if we have permissions. We can only update our friends
        if( !up.getId().equals( userId)) {
            throw new HTTPErrorException( Status.FORBIDDEN, "You are not allowed to delete this friend");
        }
        
        SqlSession session = MyBatisConnectionFactory.getSession().openSession();

        try {
            FriendshipMapper mapper     = session.getMapper( FriendshipMapper.class);
            UserMapper userMapper       = session.getMapper( UserMapper.class);
            if( friend.isConfirmed())
                mapper.acceptFriend( up.getId(), id);
            else
                throw new HTTPErrorException( Status.NOT_MODIFIED, "Friend not modified");
            
            Friendship friendship = mapper.getFriend( up.getId(), id);
            
            friend = new Friend( friendship, userId);
            
            if( userId == up.getId()) {
                if( friendship.getSender().getId() == up.getId())
                    friend.setAskedByMe(true);
                else friend.setAskedByMe(false);
            }
            
            User user = userMapper.getUser( friend.getUser().getId());
            Permission permission = new Permission();
            userMapper.getPermissions( up.getId(), user.getId(), permission);
            user.setPermission(permission);
            friend.setUser(user);
            
            friend.setSelfLink( uriInfo.getPath());
            
            session.commit();
         }catch( HTTPErrorException e) {
            throw e;
        }
        catch( Exception e) {
            logger.info( e);
            throw new HTTPErrorException( Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }finally {
            session.close();
        }
        
        return friend;
    }
    
    /**
     * Esta función gestiona las peticiones DELETE al recurso 
     * <code>users/{uid}/friends/{id}</code>. Esta operación elimina 
     * la petición de amistad identificada por <b>id</b>. 
     * @param id identificador de la relación de amistad
     * @param sc información de seguridad y autenticación de la petición
     * @param uriInfo  información de la URL de la petición
     */
    @Path("/{id}")
    @DELETE
    @AuthenticationRequired( AuthenticationScheme.AUTHENTICATION_SCHEME_TOKEN)
    public void deleteFriend( @PathParam("id") Long id, @Context SecurityContext sc, @Context UriInfo uriInfo )
    {
        UserPrincipal up = (UserPrincipal) sc.getUserPrincipal();
        
        // Check if we have permissions. We can only delete our friends
        if( !up.getId().equals( userId)) {
            throw new HTTPErrorException( Status.FORBIDDEN, "You are not allowed to delete this friend");
        }
        
        SqlSession session = MyBatisConnectionFactory.getSession().openSession();

        try {
            FriendshipMapper mapper    = session.getMapper( FriendshipMapper.class);
            mapper.deleteFriend( up.getId(), id);
            session.commit();
         }catch( HTTPErrorException e) {
            throw e;
        }
        catch( Exception e) {
            logger.info( e);
            throw new HTTPErrorException( Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }finally {
            session.close();
        }
        
    }
   
    
}
