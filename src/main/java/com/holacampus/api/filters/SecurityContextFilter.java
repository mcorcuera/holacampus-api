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

import com.holacampus.api.domain.User;
import com.holacampus.api.exceptions.HTTPErrorException;
import com.holacampus.api.security.AuthenticationBadSintaxException;
import com.holacampus.api.security.AuthenticationFailException;
import com.holacampus.api.security.AuthenticationRequired;
import com.holacampus.api.security.AuthenticationScheme;
import com.holacampus.api.security.Authenticator;
import com.holacampus.api.security.MySecurityContext;
import com.holacampus.api.security.UnknownAuthenticationSchemeException;
import com.holacampus.api.security.UserPrincipal;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.glassfish.jersey.server.ExtendedUriInfo;

/**
 * Esta clase se encarga de filtrar todas las peticiones a la API de holacampus
 * y gestionar la autenticación de estas.
 * <p>
 * Para ello hace uso de la anotación {@link AuthenticationRequired} que especifica
 * el nivel de autorización requerido.
 * </p>
 * <p>
 * Para realizar la autenticación hace uso de los {@link Authenticator} o autenticadores
 * registrados en {@link AuthenticationScheme}. Así, contamos con tres tipos de autenticación
 * requerida:
 * <ul>
 *  <li> Sin autenticación </li>
 *  <li> Basic Authentication: lógica implementada en {@link BasicAuthenticator} </li>
 *  <li> Token Authentication {@link TokenAuthenticator}</li>
 * </ul>
 * </p>
 * <p>
 * Dependiendo de los parametros especificados en la anotación {@link AuthenticationRequired}
 * se escogerá un {@link Authenticator} que será el encargado de gestionar la autenticación
 * de la petición del cliente. En caso de error de autenticación se devolverá al cliente
 * una respuesta con código de error <b>404 - Not Authorized </b>
 * </p>
 * @author Mikel Corcuera <mik.corcuera@gmail.com>
 */
@Provider
public class SecurityContextFilter implements ContainerRequestFilter{

    private static final Logger logger = LogManager.getLogger( SecurityContextFilter.class.getName());
    
    /**
     * {@inheritDoc}
     */
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
            
            authenticationScheme = authentication.value();
        }
        
        logger.info("[AUTH] Scheme " + authenticationScheme);   
        
        if( !authenticationScheme.equals(AuthenticationScheme.AUTHENTICATION_SCHEME_NONE)) {
           
            try {
                principal = AuthenticationScheme.authenticate(authenticationScheme, crc.getHeaders());
                sc = new MySecurityContext( principal, authenticationScheme, false);
                 
            } catch (UnknownAuthenticationSchemeException ex) {
                logger.error(ex);
                throw new InternalServerErrorException();
            } catch (AuthenticationFailException ex) {
                 throw new HTTPErrorException( Response.Status.UNAUTHORIZED, "Unauthorized user");
            } catch (AuthenticationBadSintaxException ex) {
                throw new HTTPErrorException( Response.Status.BAD_REQUEST, "Bad sintax in authentication headers");
            }
        }
        crc.setSecurityContext( sc);
    }
    
}

