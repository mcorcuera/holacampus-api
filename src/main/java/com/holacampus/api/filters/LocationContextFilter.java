/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.holacampus.api.filters;

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
        
        if( responseContext.getEntity() != null && ReadableRepresentation.class.isAssignableFrom( responseContext.getEntityClass())) {
            ReadableRepresentation r = (ReadableRepresentation) responseContext.getEntity();
            String location = r.getLinkByRel( "self").getHref();
            if( location != null) {
                responseContext.getHeaders().add("Location", location);
            }
        }
        
    }
    
}
