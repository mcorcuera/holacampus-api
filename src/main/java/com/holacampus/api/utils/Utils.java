/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.holacampus.api.utils;

/**
 *
 *  @author Mikel Corcuera <mik.corcuera@gmail.com>  
 */
public class Utils {
    
    /**
     *
     */
    public static final String BASE_URL = "http://localhost:8080/api";
    
    /**
     *
     * @param path
     * @return
     */
    public static String createLink( String path)
    {
        return BASE_URL + path;
    }
}
