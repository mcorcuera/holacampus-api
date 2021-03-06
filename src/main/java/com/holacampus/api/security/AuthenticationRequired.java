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

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Anotación utilizada para especificar la autenticación requerida
 * por los métodos que gestionan las peticiones a la API.
 * <br/><br/>
 * Para saber los tipos de autenticación soportados, ver la documentación de
 * {@link AuthenticationScheme}
 * 
 * @author Mikel Corcuera <mik.corcuera@gmail.com>
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target( ElementType.METHOD)
public @interface AuthenticationRequired {
    
    /**
     * Nombre de la autenticación requerida
     * @return
     */
    String value();
}
