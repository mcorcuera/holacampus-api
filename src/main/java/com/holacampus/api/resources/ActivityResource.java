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

import com.holacampus.api.domain.Activity;
import com.holacampus.api.exceptions.HTTPErrorException;
import com.holacampus.api.hal.HalList;
import com.holacampus.api.mappers.ActivityMapper;
import com.holacampus.api.security.AuthenticationRequired;
import com.holacampus.api.security.AuthenticationScheme;
import com.holacampus.api.security.UserPrincipal;
import com.holacampus.api.utils.MyBatisConnectionFactory;
import com.holacampus.api.utils.Utils;
import com.theoryinpractise.halbuilder.api.RepresentationFactory;
import java.net.URLDecoder;
import java.util.List;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
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
@Path( "/activity")
public class ActivityResource {
    
    private static final Logger logger = LogManager.getLogger( ActivityResource.class.getName());

    
    @Context
    private SecurityContext sc;
    
    @Context
    private UriInfo uriInfo;

    @GET
    @AuthenticationRequired( AuthenticationScheme.AUTHENTICATION_SCHEME_TOKEN)
    @Produces( { RepresentationFactory.HAL_JSON})
    public HalList<Activity> getLastActivity( @QueryParam("page") Integer page, @QueryParam( "size") Integer size)
    {
        UserPrincipal up            = (UserPrincipal) sc.getUserPrincipal();
        HalList<Activity> activity  = null;
        SqlSession session          = MyBatisConnectionFactory.getSession().openSession();
        page                        = Utils.getValidPage(page);
        size                        = Utils.getValidSize(size);
        RowBounds rb                = Utils.createRowBounds(page, size);
        try {
            ActivityMapper mapper       = session.getMapper( ActivityMapper.class);
            List<Activity> activityList = mapper.getActivity( up.getId(), rb); 
            int total                   = mapper.getTotalActivity(up.getId());
            activity = new HalList<Activity>( activityList, total);
            activity.setResourceRelativePath("/activity");
            activity.setPage( page);
            activity.setSize( size);
            session.commit();
         }catch( HTTPErrorException e) {
            throw e;
        }catch( Exception e) {
            logger.info( e);
            throw new HTTPErrorException( Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }finally {
            session.close();
        }

        return activity;
    }
}

