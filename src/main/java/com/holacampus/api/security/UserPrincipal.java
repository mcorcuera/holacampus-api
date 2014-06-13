/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.holacampus.api.security;

import java.security.Principal;

/**
 *
 * @author Mikel Corcuera <mik.corcuera@gmail.com>
 */
public final class UserPrincipal implements Principal{

    private final String  name;
    private final Long    id;
    private final String  role;
    
    public UserPrincipal( String name, Long id, String role)
    {
        this.name   = name;
        this.id     = id;
        this.role   = role;
    }
    
    @Override
    public String getName() 
    {
         return name;
    }
    
    public Long getId()
    {
        return id;
    }
    
    public String getRole()
    {
        return role;
    }
    
}
