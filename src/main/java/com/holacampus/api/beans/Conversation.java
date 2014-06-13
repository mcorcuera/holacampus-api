/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.holacampus.api.beans;

import java.sql.Date;
import java.util.List;

/**
 *
 *  @author Mikel Corcuera <mik.corcuera@gmail.com>  
 */
public class Conversation {
    
    /**
     *
     */
    public static final String TYPE_GROUP       = "GROUP";

    /**
     *
     */
    public static final String TYPE_INDIVIDUAL  = "INDIVIDUAL";
    
    private long                        id;
    private String                      type;
    private Date                        creationDate;
    private String                      name;
    private List<Message>               messages;
    private List<ConversationMember>    members;
}
