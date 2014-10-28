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

import com.holacampus.api.domain.ActiveElement;
import com.holacampus.api.domain.*;
import com.holacampus.api.domain.Credentials;
import com.holacampus.api.domain.User;
import org.apache.ibatis.annotations.Param;

/**
 * Mapper para la clase {@link Credentials}
 * @author Mikel Corcuera <mik.corcuera@gmail.com>
 */
public interface CredentialsMapper {
    
    /**
     * Almacena los credenciales de usuario para un {@link ActiveElement} en concreto
     * @param element identificador del  {@link ActiveElement} para el que se van a almacenar los credenciales
     * @param credentials credenciales a almacenar
     * @return número de elementos almacenados
     * @throws Exception
     */
    public int storeCredentials( @Param("element") ActiveElement element, @Param( "credentials") Credentials credentials) throws Exception;
    
    /**
     * Obtiene los credenciales para la dirección de email de un  {@link ActiveElement} en concreto
     * @param email dirección de email
     * @return los credenciales relacionados al  {@link ActiveElement} con dicha dirección de email
     * @throws Exception
     */
    public Credentials getCredentialsForEmail( @Param("email") String email) throws Exception;
    
    /**
     * Obtiene los credenciales para el identificador de un  {@link ActiveElement} en concreto
     * @param id identificador del {@link ActiveElement}
     * @return los credenciales relacionados al  {@link ActiveElement} con dicho identificador
     * @throws Exception
     */
    public Credentials getCredentialsForId( @Param("id") Long id) throws Exception;
}
