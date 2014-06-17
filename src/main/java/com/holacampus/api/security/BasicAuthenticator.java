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
 *
 * @author Mikel Corcuera <mik.corcuera@gmail.com>
 */
public final class BasicAuthenticator implements Authenticator{

    private static final Logger logger = LogManager.getLogger( BasicAuthenticator.class.getName());
    
    private static final String AUTHENTICATION_HEADER = "Authorization";
    private static final String SCHEME = "[Bb]asic";
    
    @Override
    public final int authenticate(MultivaluedMap<String, String> headers, User user) 
    {
        String authHeader = null;
        
        /*
         * If authentication header is not present, return 401 error
        */
        if( ( authHeader = headers.getFirst(AUTHENTICATION_HEADER)) == null) {
            return Authenticator.AUTH_FAIL;
        }
        
        /*
         * Get user credentials from Authentication header
        */
        int result = getUserCredentials( authHeader, user);
        
        if( result != Authenticator.OK) {
            return result;
        }
        
        /*
         * Retrieve credentials from database
        */
        logger.info("[AUTH] Credentials: " + user.getEmail() + ":" + user.getPassword()); 

        SqlSession session = MyBatisConnectionFactory.getSession().openSession();
                    
        try {
            CredentialsMapper   credMapper    = session.getMapper( CredentialsMapper.class);

            /*
            * Get Credentials from database and compare
            */
            Credentials credentials = credMapper.getCredentialsForUserEmail(user.getEmail());

            if( credentials != null && PasswordHash.validatePassword(user.getPassword(), credentials)) {
               user.setId( credentials.getUser().getId());
            }else {
                return Authenticator.AUTH_FAIL;
            }

        } catch( Exception ex) {
            logger.error(ex);
            throw new InternalServerErrorException();
        } finally {
            session.close();
        }
        
        return Authenticator.OK;
    }
    
    private int getUserCredentials( String authHeader, User user)
    {
        String encodedUserPassword = null;
        
        try{
            encodedUserPassword = authHeader.split( SCHEME + " ")[ 1];
        }catch( ArrayIndexOutOfBoundsException e) {
            logger.error( e.toString());
            return Authenticator.BAD_SINTAX;
        }
        
        String userPassword = null;
        
        try {
            userPassword = new String( Base64.decode( encodedUserPassword.getBytes()));
        }catch ( Exception e) {
            logger.error( e.toString());
            return Authenticator.BAD_SINTAX;
        }
        
        if( userPassword.split(":").length > 1) {
            String email    = userPassword.split(":")[ 0];
            String pass     = userPassword.split(":")[ 1];
                        
            user.setEmail( email);
            user.setPassword( pass);
        }else {
            return Authenticator.BAD_SINTAX;
        }
        
        return Authenticator.OK;
    }

    @Override
    public final String getScheme() {
        return AuthenticationScheme.AUTHENTICATION_SCHEME_BASIC;
    }
    
}
