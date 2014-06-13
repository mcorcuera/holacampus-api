/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.holacampus.api.security;

import com.holacampus.api.beans.User;
import javax.ws.rs.core.MultivaluedMap;

/**
 *
 * @author Mikel Corcuera <mik.corcuera@gmail.com>
 */
public interface Authenticator {
    
    public static final int OK          = 0;
    public static final int AUTH_FAIL   = 401;
    public static final int BAD_SINTAX  = 409;
    
    public int authenticate( MultivaluedMap<String,String> headers, User user);
    
    public String getScheme();
}
