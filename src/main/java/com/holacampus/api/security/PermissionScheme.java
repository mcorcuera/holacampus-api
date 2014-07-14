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

import com.holacampus.api.domain.Permission;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Mikel Corcuera <mik.corcuera@gmail.com>
 */
public final class PermissionScheme {
    
    public enum Action {
        GET_UNIQUE, POST_UNIQUE, PUT_UNIQUE, DELETE_UNIQUE,
        GET_MULTIPLE, POST_MULTIPLE, PUT_MULTIPLE, DELETE_MULTIPLE
    }
    
    private static Map<String,Integer> levels = new HashMap<String,Integer>();
    
    static {
        levels.put( Permission.LEVEL_USER, 0);
        levels.put( Permission.LEVEL_MEMBER_REQUESTED, 10);
        levels.put( Permission.LEVEL_MEMBER_PENDING, 10);
        levels.put( Permission.LEVEL_MEMBER, 20);
        levels.put( Permission.LEVEL_PARENT_OWNER, 30);
        levels.put( Permission.LEVEL_OWNER, 40);
    }
    
    private Map<Action,String> permissions;
    
    public PermissionScheme() {
        permissions = new HashMap<Action,String>();
    }
    
    public PermissionScheme addPermissionScheme( Action action, String level) 
    {
        if( permissions.get( action) != null)
            permissions.remove( action);
        
        permissions.put(action, level);
        
        return this;
    }
    
    public String getPermissionLevel( Action action)
    {
        return permissions.get( action);
    }
    
    public boolean isAllowed( Action action, String permission)
    {
        String requiredPermission = permissions.get( action);
        if( requiredPermission == null) {
            return false;
        }
        
        Integer requiredLevel   = levels.get( requiredPermission);
        Integer level           = levels.get( permission);
        
        if( requiredLevel == null || level == null)
            return false;
        
        if( requiredLevel > level)
            return false;
        
        return true;
    }
}
