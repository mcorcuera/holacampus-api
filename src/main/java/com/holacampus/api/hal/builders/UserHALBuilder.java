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

package com.holacampus.api.hal.builders;

import com.holacampus.api.domain.Name;
import com.holacampus.api.domain.User;
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
