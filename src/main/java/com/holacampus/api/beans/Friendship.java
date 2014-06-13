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
public class Friendship {
    
    private User    sender;
    private User    receiver;
    private String  status;
    private Date    petitionDate;
    private Date    statusChangeDate;
    private boolean askedByMe;
}
