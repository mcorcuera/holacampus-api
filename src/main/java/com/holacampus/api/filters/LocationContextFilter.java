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

package com.holacampus.api.filters;

import com.holacampus.api.hal.Linkable;
import com.theoryinpractise.halbuilder.api.ReadableRepresentation;
import java.io.IOException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author Mikel Corcuera <mik.corcuera@gmail.com>
 */

@Provider
public class LocationContextFilter implements ContainerResponseFilter{

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        
        /*
        * Set location header based on self link in HAL representation
        */
        
        if( responseContext.getEntity() != null && Linkable.class.isAssignableFrom( responseContext.getEntityClass())) {
            Linkable r = (Linkable) responseContext.getEntity();
            String location = r.selfLink();
            if( location != null) {
                responseContext.getHeaders().add("Location", location);
            }
        }
        
    }
    
}
