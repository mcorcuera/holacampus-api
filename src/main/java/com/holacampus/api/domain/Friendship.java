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
 * Clase que representa una relación de amistad
 *  @author Mikel Corcuera <mik.corcuera@gmail.com>  
 */
public class Friendship {
    
    /**
     * La amistad está confirmada
     */
    public static final String STATUS_CONFIRMED     = "CONFIRMED";

    /**
     * La amistad no esta confirmada
     */
    public static final String STATUS_UNCONFIRMED   = "UNCONFIRMED";
    
    private User    sender;
    private User    receiver;
    private String  status;
    private Date    petitionDate;
    private Date    statusChangeDate;

    /**
     *
     * @return usuario que envió la petición de amistad
     */
    public User getSender() {
        return sender;
    }

    /**
     *
     * @param sender usuario que recivió la petición de amistad
     */
    public void setSender(User sender) {
        this.sender = sender;
    }

    /**
     *
     * @return usuario que recibió la petición de amistad
     */
    public User getReceiver() {
        return receiver;
    }

    /**
     *
     * @param receiver usuario que recibió la petición de amistad
     */
    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    /**
     *
     * @return estado de la relaci´ón de amistad
     */
    public String getStatus() {
        return status;
    }

    /**
     *
     * @param status estado de la relación de amistad
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     *
     * @return fecha de la petición de amistad
     */
    public Date getPetitionDate() {
        return petitionDate;
    }

    /**
     *
     * @param petitionDate fecha de la petición de amistad
     */
    public void setPetitionDate(Date petitionDate) {
        this.petitionDate = petitionDate;
    }

    /**
     *
     * @return fecha de cambio del estado de la petición de amistad
     */
    public Date getStatusChangeDate() {
        return statusChangeDate;
    }

    /**
     *
     * @param statusChangeDate  fecha de cambio del estado de la petición de amistad
     */
    public void setStatusChangeDate(Date statusChangeDate) {
        this.statusChangeDate = statusChangeDate;
    }
    
    
}
