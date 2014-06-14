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

import com.holacampus.api.beans.User;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.core.MultivaluedMap;

/**
 *
 * @author Mikel Corcuera <mik.corcuera@gmail.com>
 */
public class AuthenticationScheme {
    
    public static final String AUTHENTICATION_SCHEME_BASIC    = "Basic";
    public static final String AUTHENTICATION_SCHEME_TOKEN    = "Token";
    public static final String AUTHENTICATION_SCHEME_NONE     = "None";
    
    private static String defaultScheme = AUTHENTICATION_SCHEME_TOKEN;
    
    private static Map<String,Authenticator> authenticators = new HashMap<String,Authenticator>(); 
    
    public static void registerAuthenticator( Authenticator authenticator)
    {
        if( authenticators.get( authenticator.getScheme()) == null)
            authenticators.put( authenticator.getScheme(), authenticator);
    }
    
    public static Authenticator getAuthenticator( String scheme)
    {
        return authenticators.get( scheme);
    }
    
    public static int authenticate( String scheme,  MultivaluedMap<String,String> headers, User user) throws UnknownAuthenticationSchemeException
    {
        Authenticator authenticator = getAuthenticator( scheme);
        
        if( authenticator == null) {
            throw new UnknownAuthenticationSchemeException( "Unknown " + scheme + "scheme");
        }
        
        return authenticator.authenticate(headers, user);
    }
    
    public static void setDefaultScheme( String scheme)
    {
        defaultScheme = scheme;
    }
    
    public static String getDefaultScheme()
    {
        return defaultScheme;
    }
}
