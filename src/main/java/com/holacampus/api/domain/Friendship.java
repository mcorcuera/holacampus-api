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
package com.holacampus.api.domain;

import java.util.Date;


/**
 *
 *  @author Mikel Corcuera <mik.corcuera@gmail.com>  
 */
public class Friendship {
    
    public static final String STATUS_CONFIRMED     = "CONFIRMED";
    public static final String STATUS_UNCONFIRMED   = "UNCONFIRMED";
    
    private User    sender;
    private User    receiver;
    private String  status;
    private Date    petitionDate;
    private Date    statusChangeDate;

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getPetitionDate() {
        return petitionDate;
    }

    public void setPetitionDate(Date petitionDate) {
        this.petitionDate = petitionDate;
    }

    public Date getStatusChangeDate() {
        return statusChangeDate;
    }

    public void setStatusChangeDate(Date statusChangeDate) {
        this.statusChangeDate = statusChangeDate;
    }
    
    
}
