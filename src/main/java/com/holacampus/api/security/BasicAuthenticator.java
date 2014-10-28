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

package com.holacampus.api.security;

import com.holacampus.api.domain.Credentials;
import com.holacampus.api.domain.User;
import com.holacampus.api.filters.SecurityContextFilter;
import com.holacampus.api.mappers.CredentialsMapper;
import com.holacampus.api.utils.MyBatisConnectionFactory;
import java.io.IOException;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.core.MultivaluedMap;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.glassfish.jersey.internal.util.Base64;

/**
 * Esta clase implementa la interfaz {@link Authenticator} y provee la lógica para
 * autenticar las peticiones que deben ser autenticadas con el esquema de autenticación 
 * <a href="http://tools.ietf.org/html/rfc2617">basico</a>
 * 
 * <p>
 * Para ello analiza la cabecera Basic de la petición para obtener de esta el nombre
 * de usuario y la contraseña que deberán ser comprobados con los datos almacenados en el
 * servidor.
 * </p>
 * @author Mikel Corcuera <mik.corcuera@gmail.com>
 */
public final class BasicAuthenticator implements Authenticator{

    private static final Logger logger = LogManager.getLogger( BasicAuthenticator.class.getName());
    
    private static final String AUTHENTICATION_HEADER = "Authorization";
    private static final String SCHEME = "[Bb]asic";
    
    
    @Override
    public final UserPrincipal authenticate(MultivaluedMap<String, String> headers) throws AuthenticationFailException, AuthenticationBadSintaxException
    {
        String authHeader = null;
        UserPrincipal up;
        /*
         * If authentication header is not present, return 401 error
        */
        if( ( authHeader = headers.getFirst(AUTHENTICATION_HEADER)) == null) {
            throw new AuthenticationFailException();
        }
        
        /*
         * Get user credentials from Authentication header
        */
        ProvidedCredentials providedCredentials = getUserCredentials( authHeader);
        
        /*
         * Retrieve credentials from database
        */
        logger.info("[AUTH] Credentials: " + providedCredentials.email + ":" + providedCredentials.password); 

        SqlSession session = MyBatisConnectionFactory.getSession().openSession();
                    
        try {
            CredentialsMapper   credMapper    = session.getMapper( CredentialsMapper.class);

            /*
            * Get Credentials from database and compare
            */
            Credentials credentials = credMapper.getCredentialsForEmail( providedCredentials.email);

            if( credentials != null && PasswordHash.validatePassword( providedCredentials.password, credentials)) {
               up = new UserPrincipal( providedCredentials.email, credentials.getElement().getId(), null);
            }else {
                throw new AuthenticationFailException();
            }

        } catch( AuthenticationFailException e) {
            throw e;
        }catch( Exception ex) {
            logger.error(ex);
            throw new InternalServerErrorException();
        } finally {
            session.close();
        }
        
        return up;
    }
    
    private ProvidedCredentials getUserCredentials( String authHeader) throws AuthenticationFailException, AuthenticationBadSintaxException
    {
        String encodedUserPassword  = null;
        ProvidedCredentials credentials     = new ProvidedCredentials();
        
        try{
            encodedUserPassword = authHeader.split( SCHEME + " ")[ 1];
        }catch( ArrayIndexOutOfBoundsException e) {
            logger.error( e.toString());
            throw new AuthenticationBadSintaxException();
        }
        
        String userPassword = null;
        
        try {
            userPassword = new String( Base64.decode( encodedUserPassword.getBytes()));
        }catch ( Exception e) {
            logger.error( e.toString());
            throw new AuthenticationBadSintaxException();
        }
        
        if( userPassword.split(":").length > 1) {
            String email    = userPassword.split(":")[ 0];
            String pass     = userPassword.split(":")[ 1];
                        
            credentials.email =  email;
            credentials.password = pass;
        }else {
            throw new AuthenticationBadSintaxException();
        }
        
        return credentials;
    }
  
    @Override
    public final String getScheme() {
        return AuthenticationScheme.AUTHENTICATION_SCHEME_BASIC;
    }
    
    class ProvidedCredentials {
        public String email;
        public String password;
    }
    
}
