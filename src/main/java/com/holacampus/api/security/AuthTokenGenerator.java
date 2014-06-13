/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.holacampus.api.security;

import org.apache.commons.lang.RandomStringUtils;

/**
 *
 * @author Mikel Corcuera <mik.corcuera@gmail.com>
 */
public class AuthTokenGenerator {
    
    public static final int DEFAULT_SIZE = 48;
    
    public static String getAuthToken( int size)
    {
        return RandomStringUtils.random( size, true, true);
    }
    
    public static String getAuthToken()
    {
        return getAuthToken( DEFAULT_SIZE);
    }
}
