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

import com.holacampus.api.hal.Linkable;
import com.holacampus.api.utils.Utils;
import com.theoryinpractise.halbuilder.jaxrs.*;
import java.util.Date;

/**
 *
 * @author Mikel Corcuera <mik.corcuera@gmail.com>
 */

@HalRootElement
public class Friend implements Linkable{
    
    @HalEmbedded( "user")
    private User    user;
    
    @HalProperty( name="confirmed")
    private boolean confirmed;
    
    @HalProperty( name="statusChangeDate")
    private Date    statusChangeDate;
    
    @HalProperty( name="askedByMe")
    private Boolean askedByMe;
    
    private String  selfLink;

    public Friend() {
        
    }
    
    public Friend( Friendship fs, Long userId) 
    {
        if( fs.getSender().getId().equals(userId)) {
            user = fs.getReceiver();
        }else if( fs.getReceiver().getId().equals(userId)) {
            user = fs.getSender();
        }
        
        if( fs.getStatus().equals( Friendship.STATUS_CONFIRMED))
            confirmed = true;
        else
            confirmed = false;
        
        statusChangeDate = fs.getStatusChangeDate();
    }
    
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }

    public Date getStatusChangeDate() {
        return statusChangeDate;
    }

    public void setStatusChangeDate(Date statusChangeDate) {
        this.statusChangeDate = statusChangeDate;
    }

    public Boolean isAskedByMe() {
        return askedByMe;
    }

    public void setAskedByMe(Boolean askedByMe) {
        this.askedByMe = askedByMe;
    }
    
    

    public void setSelfLink( String l) {
        selfLink = l;
    }
    
    @Override
    @HalSelfLink
    public String getSelfLink() {
        return Utils.createLink( selfLink, null);
    }
}
