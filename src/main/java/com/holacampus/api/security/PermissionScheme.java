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
 * Esta clase se encarga de gestionar los permisos necesarios para actucar sobre
 * cada recurso.
 * 
 * <p>
 * Así, permite definir permisos dependiendo del método HTTP que se pretende ejecutar
 * sobre el recurso (GET, POST, PUT, DELETE) y dependiendo de si se trata de un recurso
 * individual o de una colección de recursos
 * </p>
 * @author Mikel Corcuera <mik.corcuera@gmail.com>
 */
public final class PermissionScheme {
    
    /**
     * Acción sobre el recurso
     */
    public enum Action {

        /**
         * Petición GET sobre un recurso individual
         */
        GET_UNIQUE,

        /**
         * Petición POST sobre un recurso individual
         */
        POST_UNIQUE,

        /**
         * Petición PUT sobre un recurso individual
         */
        PUT_UNIQUE,

        /**
        * Petición DELETE sobre un recurso individual

         */
        DELETE_UNIQUE,

        /**
        * Petición GET sobre una colección de recursos

         */
        GET_MULTIPLE,

        /**
        * Petición POST sobre una colección de recursos
        *
         */
        POST_MULTIPLE,

        /**
        * Petición PUT sobre una colección de recursos
        *
         */
        PUT_MULTIPLE,

        /**
        * Petición DELETE una colección de recursos
        *
         */
        DELETE_MULTIPLE
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
    
    /**
     * Crea una nueva instancia del objeto.
     */
    public PermissionScheme() {
        permissions = new HashMap<Action,String>();
    }
    
    /**
     * Añade un permiso al esquema de permisos del recurso
     * @param action Acción sobre la que se define el permiso
     * @param level Nivel de autenticación requerido.
     * @return
     */
    public PermissionScheme addPermissionScheme( Action action, String level) 
    {
        if( permissions.get( action) != null)
            permissions.remove( action);
        
        permissions.put(action, level);
        
        return this;
    }
    
    /**
     * Obtiene el nivel de autenticación requerido para realizar la acción sobre
     * el recurso
     * @param action Acción a realizar
     * @return El nivel de autenticación requerido
     */
    public String getPermissionLevel( Action action)
    {
        return permissions.get( action);
    }
    
    /**
     * Comprueba si una acción está permitida para un cierto nivel de permisos
     * @param action Acción a realizar
     * @param permission Permisos del usuario
     * @return true si tiene permisos, false si no
     */
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
