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

package com.holacampus.api.resources;

import com.holacampus.api.domain.GroupEvent;
import com.holacampus.api.domain.Permission;
import com.holacampus.api.domain.University;
import com.holacampus.api.exceptions.HTTPErrorException;
import com.holacampus.api.hal.HalList;
import com.holacampus.api.mappers.GroupEventMapper;
import com.holacampus.api.mappers.UniversityMapper;
import com.holacampus.api.security.AuthenticationRequired;
import com.holacampus.api.security.AuthenticationScheme;
import com.holacampus.api.security.UserPrincipal;
import com.holacampus.api.utils.MyBatisConnectionFactory;
import com.holacampus.api.utils.Utils;
import com.holacampus.api.validators.CreationValid;
import com.theoryinpractise.halbuilder.api.RepresentationFactory;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import javax.validation.Valid;
import javax.ws.rs.*;
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
@Path("/{type:groups|events}")
public class GroupsEventsResource {
    
    private static final Logger logger = LogManager.getLogger( GroupsEventsResource.class.getName());
     
    @PathParam( "type")
    private String type;
    
    @Context
    private UriInfo uriInfo;
    
    @Context
    private SecurityContext sc;
    
    @GET
    @Produces( { RepresentationFactory.HAL_JSON})
    @AuthenticationRequired( AuthenticationScheme.AUTHENTICATION_SCHEME_TOKEN)
    @Encoded
    public HalList<GroupEvent> getGroupsEvents( @QueryParam("page") Integer page, 
            @QueryParam( "size") Integer size, @QueryParam( "q") String q) throws UnsupportedEncodingException
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
            List<GroupEvent> groupList  = mapper.getGroupsEvents( elementType, q, rb);
            int total                   = mapper.getTotalGroupsEvents( elementType, q);
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
    
    @POST
    @AuthenticationRequired( AuthenticationScheme.AUTHENTICATION_SCHEME_TOKEN)
    @Consumes( { RepresentationFactory.HAL_JSON, MediaType.APPLICATION_JSON})
    @Produces( { RepresentationFactory.HAL_JSON})
    public GroupEvent createGroupEvent( @CreationValid @Valid GroupEvent group)
    {
        logger.info("[GET] " + uriInfo.getPath());
        if( "groups".equals(type))
            group.setType( GroupEvent.TYPE_GROUP);
        else
            group.setType( GroupEvent.TYPE_EVENT);
        
        if( GroupEvent.TYPE_EVENT.equals( group.getType()) && group.getEventDate() == null)
            throw new HTTPErrorException( Status.BAD_REQUEST, "{event.date.missing}");
        
        UserPrincipal up    = (UserPrincipal) sc.getUserPrincipal();
        SqlSession session  = MyBatisConnectionFactory.getSession().openSession();
        
        try {
            GroupEventMapper mapper    = session.getMapper( GroupEventMapper.class);
            mapper.createGroupEvent( up.getId(), group);
            group = mapper.getGroupEvent( group.getType(), group.getId());
            group.setPermission( new Permission( Permission.LEVEL_OWNER));
            session.commit();
         }catch( HTTPErrorException e) {
            throw e;
        }catch( Exception e) {
            logger.info( e);
            throw new HTTPErrorException( Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }finally {
            session.close();
        }
        return group;
    }
    
}
