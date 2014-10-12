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

import org.apache.commons.lang.RandomStringUtils;

/**
 * Esta clase se encarga de generar un nuevo token aleatorio de un tamaño especificado
 * @author Mikel Corcuera <mik.corcuera@gmail.com>
 */
public class AuthTokenGenerator {
    
    /**
     * Tamaño por defecto del token
     */
    public static final int DEFAULT_SIZE = 48;
    
    /**
     * Genera un token aleatorio del tamaño especificado
     * @param size Tamaño del token
     * @return Token (cadena de caracteres aleatorios)
     */
    public static String getAuthToken( int size)
    {
        return RandomStringUtils.random( size, true, true);
    }
    
    /**
     *Genera un token aleatorio del tamaño por defecto
     * @return Token (cadena de caracteres aleatorios)
     */
    public static String getAuthToken()
    {
        return getAuthToken( DEFAULT_SIZE);
    }
}
