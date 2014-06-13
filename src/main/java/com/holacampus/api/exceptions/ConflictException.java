/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.holacampus.api.exceptions;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

/**
 *
 *  @author Mikel Corcuera <mik.corcuera@gmail.com>  
 */
public class ConflictException extends WebApplicationException {
    
    /**
     * Create a HTTP 409 (Conflict) exception.
     */
    public ConflictException()
    {
        super( Response.status( Status.CONFLICT).build());
    }
    
    /**
     *
     * @param message
     */
    public ConflictException( String message)
    {
        super( Response.status(Status.CONFLICT).entity(message).type(MediaType.TEXT_PLAIN).build());
    }
}
