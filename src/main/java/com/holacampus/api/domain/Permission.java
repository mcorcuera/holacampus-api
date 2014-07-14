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

package com.holacampus.api.domain;

import com.theoryinpractise.halbuilder.jaxrs.HalProperty;
import com.theoryinpractise.halbuilder.jaxrs.HalRootElement;

/**
 *
 * @author Mikel Corcuera <mik.corcuera@gmail.com>
 */
@HalRootElement
public class Permission {
    
    public static final String LEVEL_OWNER              = "OWNER";
    public static final String LEVEL_PARENT_OWNER       = "PARENT_OWNER";
    public static final String LEVEL_MEMBER             = "MEMBER";
    public static final String LEVEL_MEMBER_REQUESTED   = "MEMBER_REQUESTED";
    public static final String LEVEL_MEMBER_PENDING     = "MEMBER_PENDING";
    public static final String LEVEL_USER               = "USER";
    
    @HalProperty( name="level")
    private String level;

    public Permission() {
        this.level = Permission.LEVEL_USER;
    }
    
    public Permission( String level) {
        this.level = level;
    }
    
     public void setLevel(String level) {
        this.level = level;
    }
     
    public String getLevel() {
        return level;
    }
    
    
}
