/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.holacampus.api.beans;

import java.sql.Date;

/**
 *
 *  @author Mikel Corcuera <mik.corcuera@gmail.com>  
 */
public class Message {
    
    private long            id;
    private Conversation    conversation;
    private ActiveElement   writer;
    private Date            creationDate;
    private String          content;
}
