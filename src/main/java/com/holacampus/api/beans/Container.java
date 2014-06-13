/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.holacampus.api.beans;

/**
 *
 *  @author Mikel Corcuera <mik.corcuera@gmail.com>  
 */
public class Container {
    
    /**
     *
     */
    public static final String TYPE_USER        = "USER";

    /**
     *
     */
    public static final String TYPE_UNIVERSITY  = "UNI";

    /**
     *
     */
    public static final String TYPE_GROUPEVENT  = "GROUP";

    /**
     *
     */
    public static final String TYPE_COMMENT     = "COMMENT";

    /**
     *
     */
    public static final String TYPE_PHOTO       = "PHOTO";
    
    private long id;
    private String type;
}
