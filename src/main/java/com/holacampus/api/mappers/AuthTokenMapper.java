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

package com.holacampus.api.mappers;

import com.holacampus.api.domain.AuthToken;
import org.apache.ibatis.annotations.Param;
import com.holacampus.api.domain.*;

/**Mapper para la clase {@link Activity}
 * @author Mikel Corcuera <mik.corcuera@gmail.com>
 */
public interface AuthTokenMapper {
    
    /**
     * Almacena un token de autenticación en la base de datos
     * @param auth Token a almacenar
     * @param id id del {@link ActiveElement} al que pertenece el token
     * @return La cantidad de elementos insertados
     * @throws Exception
     */
    public int storeAuthToken( @Param("auth") AuthToken auth, @Param( "id") Long id) throws Exception;
    
    /**
     * Obtiene el token de autenticación a partir de su id
     * @param id id del token de autenticación
     * @return Token de autenticación
     * @throws Exception
     */
    public AuthToken getAuthToken( String id) throws Exception;
    
    /**
     * Obtiene el token de autenticación y los datos del elemento al que esta asociado
     * @param id id del token de autenticación
     * @return Token de autenticación.
     * @throws Exception
     */
    public AuthToken getAuthTokenAndCredentials( String id) throws Exception;
    
    /**
     * Elimina el token de autenticación
     * @param id Identificador del token de autenticación
     * @return Número de elementos eliminados
     * @throws Exception
     */
    public int deleteAuthToken( String id) throws Exception;
    
    /**
     * Elimina todos los tokens de autenticación de un usuario
     * @param id Indentificador del usuario
     * @return Número de elementos eliminados
     * @throws Exception
     */
    public int deleteAllElementTokens( Long id) throws Exception;
}
