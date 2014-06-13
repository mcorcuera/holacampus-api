/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.holacampus.api.hal.builders;

import com.holacampus.api.beans.Name;
import com.holacampus.api.beans.User;
import com.theoryinpractise.halbuilder.api.ReadableRepresentation;
import java.util.Map;

/**
 *
 *  @author Mikel Corcuera <mik.corcuera@gmail.com>  
 */
public  class UserHALBuilder {
    
    /**
     *
     * @param r
     * @return
     */
    public static User buildUser( ReadableRepresentation r)
    {
        User user = new User();
        Map<String,Object> props = r.getProperties();
        
        user.setEmail( (String) props.get( "email"));
        
        user.setName( new Name( (String) props.get("firstName"), (String) props.get("lastName")));
        user.setGender( (String) props.get( "gender"));
        user.setBirthDate( java.sql.Date.valueOf((String) props.get("birthDate")));
        user.setPassword((String) props.get( "password"));
        user.setId( props.containsKey("id") ? Long.parseLong( (String) props.get("id")) : null);
        
        
        return user;
    }
}
