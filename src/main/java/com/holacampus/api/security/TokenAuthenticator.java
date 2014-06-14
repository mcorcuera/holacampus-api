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

import com.holacampus.api.beans.AuthToken;
import com.holacampus.api.beans.Credentials;
import com.holacampus.api.beans.User;
import com.holacampus.api.mappers.AuthTokenMapper;
import com.holacampus.api.mappers.CredentialsMapper;
import com.holacampus.api.utils.MyBatisConnectionFactory;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.core.MultivaluedMap;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 *
 * @author Mikel Corcuera <mik.corcuera@gmail.com>
 */


public class TokenAuthenticator implements Authenticator{

    private static final Logger logger = LogManager.getLogger( TokenAuthenticator.class.getName());

    
    private static final String AUTHENTICATION_HEADER = "X-Auth-Token";
    
    @Override
    public int authenticate(MultivaluedMap<String, String> headers, User user) 
    {
        String token = null;
        
         /*
         * If authentication header is not present, return 401 error
        */
        if( ( token = headers.getFirst(AUTHENTICATION_HEADER)) == null) {
            return Authenticator.AUTH_FAIL;
        }
        
        AuthToken auth;
       /*
        * Check token with the one stored on the database
        */
        SqlSession session = MyBatisConnectionFactory.getSession().openSession();
                    
        try {
            AuthTokenMapper authMapper = session.getMapper( AuthTokenMapper.class);

            /*
            * Get Token from database and compare
            */
            auth = authMapper.getAuthTokenAndCredentials( token);

            if( auth != null) {
               user.setId( auth.getUser().getId());
               user.setEmail( auth.getUser().getEmail());
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

    @Override
    public String getScheme() {
        return AuthenticationScheme.AUTHENTICATION_SCHEME_TOKEN;
    }
    
}
