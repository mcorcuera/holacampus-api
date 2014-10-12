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

package com.holacampus.api.exceptions;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

/**
 * Excepción que sirve para devolver respuestas al cliente con un código de error
 * determinado
 * @author Mikel Corcuera <mik.corcuera@gmail.com>
 */
public class HTTPErrorException extends WebApplicationException{
    
    /**
     * Crea un HTTPErrorException con un código de error determinado
     * @param status Código de estado HTTP
     */
    public HTTPErrorException( Status status)
    {
        super( Response.status(status).build());
    }
    
    /**
     * Crea un HTTPErrorException con un código de error y mensaje determinado
     * @param status Código de estado HTTP
     * @param message Mensaje incluido en la respuesta HTTP
     */
    public HTTPErrorException( Status status, String message)
    {
        super( Response.status( status).entity(status).entity(message).type(MediaType.TEXT_PLAIN).build());
    }
    
}
