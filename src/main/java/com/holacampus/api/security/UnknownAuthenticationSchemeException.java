/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.holacampus.api.security;

/**
 *
 * @author Mikel Corcuera <mik.corcuera@gmail.com>
 */
public class UnknownAuthenticationSchemeException extends Exception{
    
    public UnknownAuthenticationSchemeException( String message)
    {
        super(message);
    }
}
