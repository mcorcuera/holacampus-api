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
import com.holacampus.api.exceptions.HTTPErrorException;
import com.holacampus.api.hal.HalList;
import com.holacampus.api.mappers.FriendshipMapper;
import com.holacampus.api.security.AuthenticationRequired;
import com.holacampus.api.security.AuthenticationScheme;
import com.holacampus.api.security.UserPrincipal;
import com.holacampus.api.utils.MyBatisConnectionFactory;
import com.holacampus.api.utils.Utils;
import com.theoryinpractise.halbuilder.api.RepresentationFactory;
import java.util.ArrayList;
import java.util.List;
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
 *
 * @author Mikel Corcuera <mik.corcuera@gmail.com>
 */
public class FriendsResource {
    
    private static final Logger logger = LogManager.getLogger( FriendsResource.class.getName());

        
    private Long elId;
    
    public FriendsResource( Long elId)
    {
        this.elId = elId;
    }
    
    @GET
    @AuthenticationRequired( AuthenticationScheme.AUTHENTICATION_SCHEME_TOKEN)
    @Produces( { RepresentationFactory.HAL_JSON})
    public HalList<Friend> getFriends(  @QueryParam("page") Integer page, @QueryParam( "size") Integer size,
            @Context SecurityContext sc, @Context UriInfo uriInfo)
    {
        logger.info( "[GET] " + uriInfo.getPath());
        
        UserPrincipal up = (UserPrincipal) sc.getUserPrincipal();
        
        return getFriends( uriInfo.getPath(), false, false, null, page, size);
    }
    
    @Path("/pending")
    @GET
    @AuthenticationRequired( AuthenticationScheme.AUTHENTICATION_SCHEME_TOKEN)
    @Produces( { RepresentationFactory.HAL_JSON})
    public HalList<Friend> getPendingFriends( @QueryParam("page") Integer page, @QueryParam( "size") Integer size,
            @Context SecurityContext sc, @Context UriInfo uriInfo)
    {
        logger.info( "[GET] " + uriInfo.getPath());
        
        UserPrincipal up = (UserPrincipal) sc.getUserPrincipal();
        
        if( !elId.equals(up.getId())) {
            throw new HTTPErrorException( Status.FORBIDDEN, "You are not allowed here");
        }
        
        return getFriends( uriInfo.getPath(), false, true, null, page, size);
    }
    
    @Path("/unconfirmed")
    @GET
    @AuthenticationRequired( AuthenticationScheme.AUTHENTICATION_SCHEME_TOKEN)
    @Produces( { RepresentationFactory.HAL_JSON})
    public HalList<Friend> getUnconfirmedFriends( @QueryParam("page") Integer page, @QueryParam( "size") Integer size,
            @Context SecurityContext sc, @Context UriInfo uriInfo)
    {
        logger.info( "[GET] " + uriInfo.getPath());
        
        UserPrincipal up = (UserPrincipal) sc.getUserPrincipal();
        
        if( !elId.equals(up.getId())) {
            throw new HTTPErrorException( Status.FORBIDDEN, "You are not allowed here");
        }
        
        return getFriends( uriInfo.getPath(), true, false, null, page, size);
    }
    
    private HalList<Friend> getFriends( String path, boolean unconfirmed, 
            boolean pending, String query, Integer page, Integer size)
    {
        HalList<Friend> friends = new HalList<Friend>();
        page            = Utils.getValidPage(page);
        size            = Utils.getValidSize(size);
        RowBounds rb    = Utils.createRowBounds(page, size);
        
        
        SqlSession session = MyBatisConnectionFactory.getSession().openSession();

        try {
            FriendshipMapper friendMapper   = session.getMapper(FriendshipMapper.class);
            List<Friendship> friendships    = friendMapper.getFriends(elId, null, unconfirmed, pending, rb);
            int total                       = friendMapper.getTotalFriends(elId, null, unconfirmed, pending);
            List<Friend> friendList = null;
            if( total > 0) {
                friendList = new ArrayList<Friend>();
                for( Friendship fs : friendships) {
                    Friend friend = new Friend( fs, elId);
                    friend.setSelfLink( "users/" + elId + "/friends/" + friend.getUser().getId());
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
    
    
    @POST
    @AuthenticationRequired( AuthenticationScheme.AUTHENTICATION_SCHEME_TOKEN)
    @Consumes( { RepresentationFactory.HAL_JSON, MediaType.APPLICATION_JSON})
    @Produces( { RepresentationFactory.HAL_JSON})
    public Friendship createFriendRequest( @Context SecurityContext sc, @Context UriInfo uriInfo)
    {
        return null;
    }
    
    @Path("/{id}")
    @GET
    @AuthenticationRequired( AuthenticationScheme.AUTHENTICATION_SCHEME_TOKEN)
    @Produces( { RepresentationFactory.HAL_JSON})
    public Friend getFriend( @PathParam("id") Long id, @Context SecurityContext sc, @Context UriInfo uriInfo )
    {
        logger.info( "[GET] " + uriInfo.getPath());
        Friend friend = null;
        SqlSession session = MyBatisConnectionFactory.getSession().openSession();

        try {
            FriendshipMapper friendMapper   = session.getMapper(FriendshipMapper.class);
            Friendship f                    = friendMapper.getFriend(elId, id);
            
            if( f == null)
                throw new HTTPErrorException( Status.NOT_FOUND, "friend " + id + " not found");
            
            friend = new Friend( f, elId);
            
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
    
    @Path("/{id}")
    @PUT
    @AuthenticationRequired( AuthenticationScheme.AUTHENTICATION_SCHEME_TOKEN)
    @Consumes( { RepresentationFactory.HAL_JSON, MediaType.APPLICATION_JSON})
    @Produces( { RepresentationFactory.HAL_JSON})
    public Friendship updateFriend( @PathParam("id") Long id, @Context SecurityContext sc, @Context UriInfo uriInfo )
    {
        return null;
    }
    
    @Path("/{id}")
    @DELETE
    @AuthenticationRequired( AuthenticationScheme.AUTHENTICATION_SCHEME_TOKEN)
    @Produces( { RepresentationFactory.HAL_JSON})
    public Friendship deleteFriend( @PathParam("id") Long id, @Context SecurityContext sc, @Context UriInfo uriInfo )
    {
        return null;
    }
   
    
}