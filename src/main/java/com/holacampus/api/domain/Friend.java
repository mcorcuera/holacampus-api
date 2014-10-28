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
 * Clase que representa las amistades de la red social
 * @author Mikel Corcuera <mik.corcuera@gmail.com>
 */

@HalRootElement
public class Friend implements Linkable{
    
    @HalEmbedded( "user")
    private User    user;
    
    @HalProperty( name="confirmed")
    private Boolean confirmed;
    
    @HalProperty( name="statusChangeDate")
    private Date    statusChangeDate;
    
    @HalProperty( name="askedByMe")
    private Boolean askedByMe;
    
    private String  selfLink;

    /**
     * Constructor por defecto
     */
    public Friend() {
        
    }
    
    /**
     * Crea una nueva amistad a partir de una relación de amistad para uno de los
     * involucrados en dicha relación. Así, se determina la situación de la amistad
     * relativa a uno de llos
     * @param fs relación de amistad
     * @param ofUser indentificador del usuario para el que se creará la amistad
     */
    public Friend( Friendship fs, Long ofUser) 
    {
        if( fs.getSender().getId().equals(ofUser)) {
            user = fs.getReceiver();
        }else if( fs.getReceiver().getId().equals(ofUser)) {
            user = fs.getSender();
        }
        
        if( fs.getStatus().equals( Friendship.STATUS_CONFIRMED))
            confirmed = true;
        else
            confirmed = false;
        
        askedByMe = null;
        
        statusChangeDate = fs.getStatusChangeDate();
    }
    
    /**
     * 
     * @return el usuario que es amistad
     */
    public User getUser() {
        return user;
    }

    /**
     *
     * @param user el usuario que es amistad
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     *
     * @return si la amistad está confirmada o no
     */
    public boolean isConfirmed() {
        return confirmed;
    }

    /**
     *
     * @param confirmed si la amistad está confirmada o no
     */
    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }

    /**
     *
     * @return fecha de cambio del estado de la amistad
     */
    public Date getStatusChangeDate() {
        return statusChangeDate;
    }

    /**
     *
     * @param statusChangeDate fecha de cambio del estado de la amistad
     */
    public void setStatusChangeDate(Date statusChangeDate) {
        this.statusChangeDate = statusChangeDate;
    }

    /**
     *
     * @return si la petición de amistad fue realizada por el usuario que tiene
     * la amistad con este
     */
    public Boolean isAskedByMe() {
        return askedByMe;
    }

    /**
     *
     * @param askedByMe si la petición de amistad fue realizada por el usuario que tiene
     * la amistad con este
     */
    public void setAskedByMe(Boolean askedByMe) {
        this.askedByMe = askedByMe;
    }
    
    /**
     *
     * @param l el link que enlaza a la representación del recurso
     */
    public void setSelfLink( String l) {
        selfLink = l;
    }
    
    @Override
    @HalSelfLink
    public String getSelfLink() {
        return Utils.createLink( selfLink, null);
    }
}
