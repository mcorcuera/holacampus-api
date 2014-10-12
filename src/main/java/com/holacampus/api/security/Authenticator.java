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

import javax.ws.rs.core.MultivaluedMap;

/**
 * Esta interfaz provee de métodos usados para la autenticación de las peticiones
 * del cliente para un tipo de autenticación.
 * <p>
 * Las clases que la implementer deberán proveer la lógica necesaria para autenticar
 * las peticiones del cliente (conexión con base de datos, comprobación de credenciales, etc.)
 * y devolver los credenciales del usuario.
 * </p>
 * @author Mikel Corcuera <mik.corcuera@gmail.com>
 */
public interface Authenticator {
      
    /**
     * Esta función se encarga de comprobar si una petición está autenticada por un
     * método en concreto a partir de la cabecera de ésta.
     * 
     * @param headers Cabeceras HTTP de la petición
     * @return Devuelve los credenciales del usario autenticado
     * @throws AuthenticationFailException Cuando los credenciales de usuario no están 
     * presentes o no son correctos
     * @throws AuthenticationBadSintaxException Cuando las cabeceras necesarias para la petición
     * tienen un formato incorrecto
     */
    public UserPrincipal authenticate( MultivaluedMap<String,String> headers) throws AuthenticationFailException, AuthenticationBadSintaxException;
    
    /**
     * Devuelve el identificador del tipo de autenticación que implementa la clase
     * @return Identificador del tipo de autenticación que implementa la clase
     */
    public String getScheme();
}
