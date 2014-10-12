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
import javax.ws.rs.core.SecurityContext;

/**
 * Esta clase implementa a {@link SecurityContext} y es utilizada para proveer
 * de los datos de autenticación a las funciones que se encargan de gestionar las 
 * peticiones a la API para que tengan información sobre el usuario que realiza
 * la petición.
 * @author Mikel Corcuera <mik.corcuera@gmail.com>
 */
public final class MySecurityContext implements SecurityContext{

    private final UserPrincipal     userPrincipal;
    private final String            authenticationScheme;
    private final boolean           secure;
    
    /**
     * Crea una nueva instancia de la clase.
     * @param principal Usuario autenticado
     * @param scheme Esquema usado para la autenticación
     * @param secure Indica si la conexión es segura
     */
    public MySecurityContext( UserPrincipal principal, String scheme, boolean secure)
    {
        this.userPrincipal = principal;
        this.authenticationScheme = scheme;
        this.secure = secure;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public Principal getUserPrincipal() {
        return userPrincipal;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isUserInRole(String role) {
        return role.equals( userPrincipal.getRole());
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSecure() {
        return secure;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public String getAuthenticationScheme() {
        return authenticationScheme;
    }
    
    
}
