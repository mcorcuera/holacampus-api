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

import com.holacampus.api.beans.User;
import com.holacampus.api.exceptions.HTTPErrorException;
import com.holacampus.api.security.AuthenticationRequired;
import com.holacampus.api.security.AuthenticationScheme;
import com.holacampus.api.security.Authenticator;
import com.holacampus.api.security.MySecurityContext;
import com.holacampus.api.security.UnknownAuthenticationSchemeException;
import com.holacampus.api.security.UserPrincipal;
import java.io.IOException;
import java.lang.reflect.Method;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.glassfish.jersey.server.ExtendedUriInfo;
import org.jboss.resteasy.core.Headers;
import org.jboss.resteasy.core.ServerResponse;

/**
 *
 * @author Mikel Corcuera <mik.corcuera@gmail.com>
 */
@Provider
public class SecurityContextFilter implements ContainerRequestFilter{

    private static final Logger logger = LogManager.getLogger( SecurityContextFilter.class.getName());
    
    private static final ServerResponse ACCESS_DENIED        = new ServerResponse( "Access denied", 401, new Headers<Object>());
    private static final ServerResponse BAD_REQUEST          = new ServerResponse( "The request cannot be fullfilled due to bad sintax", 400, new Headers<Object>());
    
    @Override
    public void filter(ContainerRequestContext crc) throws IOException {
        logger.info("[AUTH] Security check");   
        
        UserPrincipal       principal;
        MySecurityContext   sc = null;        
      
        /*
        * Get the authentication scheme required by the invoked method. If the annotation
        * is not present, use default scheme.
        */
        ExtendedUriInfo uriInfo = (ExtendedUriInfo) crc.getUriInfo();
        
        Method method = uriInfo.getMatchedResourceMethod().getInvocable().getDefinitionMethod();
        
        String authenticationScheme = AuthenticationScheme.getDefaultScheme();
        
        if( method.isAnnotationPresent( AuthenticationRequired.class)) {
            
            AuthenticationRequired authentication = method.getAnnotation( AuthenticationRequired.class);
            
            authenticationScheme = authentication.scheme();
        }
        
        logger.info("[AUTH] Scheme " + authenticationScheme);   
        
        if( !authenticationScheme.equals(AuthenticationScheme.AUTHENTICATION_SCHEME_NONE)) {
           
            try {
                User user = new User();
                
                int result = AuthenticationScheme.authenticate(authenticationScheme, crc.getHeaders(), user);
               
                if( result == Authenticator.OK) {
                    principal = new UserPrincipal( user.getEmail(), user.getId(), User.TYPE_STUDENT);
                    sc = new MySecurityContext( principal, authenticationScheme, false);
                }else if( result == Authenticator.BAD_SINTAX) {
                    throw new HTTPErrorException( Response.Status.BAD_REQUEST, "Bad sintax in authentication headers");
                }else if ( result == Authenticator.AUTH_FAIL) {
                    throw new HTTPErrorException( Response.Status.UNAUTHORIZED, "Unauthorized user");
                }
                 
            } catch (UnknownAuthenticationSchemeException ex) {
                logger.error(ex);
                throw new InternalServerErrorException();
            }
        }
        crc.setSecurityContext( sc);
    }
    
}

