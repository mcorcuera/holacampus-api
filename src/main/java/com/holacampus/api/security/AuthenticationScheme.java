/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
