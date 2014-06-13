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
 * @author Mikel Corcuera <mik.corcuera@gmail.com>
 */
public class HTTPErrorException extends WebApplicationException{
    
    public HTTPErrorException( Status status)
    {
        super( Response.status(status).build());
    }
    
    public HTTPErrorException( Status status, String message)
    {
        super( Response.status( status).entity(status).entity(message).type(MediaType.TEXT_PLAIN).build());
    }
    
}
