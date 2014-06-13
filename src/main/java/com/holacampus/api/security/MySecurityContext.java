/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.holacampus.api.security;

import java.security.Principal;
import javax.ws.rs.core.SecurityContext;

/**
 *
 * @author Mikel Corcuera <mik.corcuera@gmail.com>
 */
public final class MySecurityContext implements SecurityContext{

    private final UserPrincipal     userPrincipal;
    private final String            authenticationScheme;
    private final boolean           secure;
    
    
    public MySecurityContext( UserPrincipal principal, String scheme, boolean secure)
    {
        this.userPrincipal = principal;
        this.authenticationScheme = scheme;
        this.secure = secure;
    }
    
    @Override
    public Principal getUserPrincipal() {
        return userPrincipal;
    }
    

    @Override
    public boolean isUserInRole(String role) {
        return role.equals( userPrincipal.getRole());
    }

    @Override
    public boolean isSecure() {
        return secure;
    }

    @Override
    public String getAuthenticationScheme() {
        return authenticationScheme;
    }
    
    
}
