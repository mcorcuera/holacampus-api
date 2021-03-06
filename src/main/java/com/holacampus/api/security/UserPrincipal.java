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

import java.security.Principal;

/**
 * Esta clase contiene los datos del usuario que realiza la petición.
 * @author Mikel Corcuera <mik.corcuera@gmail.com>
 */
public final class UserPrincipal implements Principal{

    private final String  name;
    private final Long    id;
    private final String  role;
    
    /**
     * Crea una nueva instancia a partir del nombre de usuario (email), el id
     * y el rol de este
     * @param name
     * @param id
     * @param role
     */
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
    
    /**
     * Obtiene el id del usuario
     * @return
     */
    public Long getId()
    {
        return id;
    }
    
    /**
     * Obtiene el rol del usuario
     * @return
     */
    public String getRole()
    {
        return role;
    }
    
}
