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
 * Clase que representa los permisos que tiene un {@link ActiveElement} sobre
 * un recurso de la red social
 * @author Mikel Corcuera <mik.corcuera@gmail.com>
 */
@HalRootElement
public class Permission {
    
    /**
     * Dueño o creador del recurso
     */
    public static final String LEVEL_OWNER              = "OWNER";

    /**
     * Dueño o creador del elemento al que pertenece el recurso
     */
    public static final String LEVEL_PARENT_OWNER       = "PARENT_OWNER";

    /**
     * Miembro del recurso (por ejemplo, miembro de un grupo)
     */
    public static final String LEVEL_MEMBER             = "MEMBER";

    /**
     * Miembro (solicitado) del recurso
     */
    public static final String LEVEL_MEMBER_REQUESTED   = "MEMBER_REQUESTED";

    /**
     * Miembro (pendiente de aprobación por parte del {@link ActiveElement}
     * del recurso
     */
    public static final String LEVEL_MEMBER_PENDING     = "MEMBER_PENDING";

    /**
     * Ningún tipo de permiso sobre el recurso
     */
    public static final String LEVEL_USER               = "USER";
    
    @HalProperty( name="level")
    private String level;

    /**
     * Constructor por defecto. El nivel de permisos se establece a {@link Permission#LEVEL_USER}
     */
    public Permission() {
        this.level = Permission.LEVEL_USER;
    }
    
    /**
     * 
     * @param level nivel de permisos sobre el recurso
     */
    public Permission( String level) {
        this.level = level;
    }
    
    /**
     *
     * @param level nivel de permisos sobre el recurso
     */
    public void setLevel(String level) {
        this.level = level;
    }
     
    /**
     *
     * @return nivel de permisos sobre el recurso
     */
    public String getLevel() {
        return level;
    }
    
    
}
