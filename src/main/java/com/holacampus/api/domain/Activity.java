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

import com.theoryinpractise.halbuilder.jaxrs.*;
import java.sql.Timestamp;

/**
 * Esta clase define una actividad en concreto en la red social. Establece 
 * de que actividad se trata, quien la realiza y dónde, entre otros datos.
 * @author Mikel Corcuera <mik.corcuera@gmail.com>
 */
@HalRootElement
public class Activity {
    
    @HalProperty( name="whatType")
    private String whatType;
    
    @HalEmbedded( "what")
    private Element what;
    
    @HalProperty( name="whoType")
    private String whoType;
    
    @HalEmbedded( "who")
    private ActiveElement who;
    
    @HalProperty( name="whereType")
    private String whereType;
    
    @HalEmbedded( "where")
    private Element where;
    
    @HalProperty( name="when")
    private Timestamp when;

    /**
     *
     * @return el tipo de actividad
     */
    public String getWhatType() {
        return whatType;
    }

    /**
     *
     * @param whatType el tipo de actividad a establecer
     */
    public void setWhatType(String whatType) {
        this.whatType = whatType;
    }

    /**
     *
     * @return el elemento en el que consiste la actividad. Puede ser un comentario,
     * una foto, etc.
     */
    public Element getWhat() {
        return what;
    }

    /**
     *
     * @param what el elemento en el que consiste la actividad
     */
    public void setWhat(Element what) {
        this.what = what;
    }

    /**
     *
     * @return el tipo de elemento activo que realiza la actividad
     */
    public String getWhoType() {
        return whoType;
    }

    /**
     *
     * @param whoType el tipo de elemento activo que realiza la actividad
     */
    public void setWhoType(String whoType) {
        this.whoType = whoType;
    }

    /**
     *
     * @return el elemento activo que realiza la actividad
     */
    public ActiveElement getWho() {
        return who;
    }

    /**
     *
     * @param who el elemento activo que realiza la actividad
     */
    public void setWho(ActiveElement who) {
        this.who = who;
    }

    /**
     *
     * @return el tipo de elemento donde se realiza la activad (comentario, grupor,...)
     */
    public String getWhereType() {
        return whereType;
    }

    /**
     *
     * @param whereType el tipo de elemento donde se realiza la actividad.
     */
    public void setWhereType(String whereType) {
        this.whereType = whereType;
    }

    /**
     *
     * @return el elemento donde se realiza la actividad (comentario, grupo, etc..)
     */
    public Element getWhere() {
        return where;
    }

    /**
     *
     * @param where el elemento donde se realiza la actividad
     */
    public void setWhere(Element where) {
        this.where = where;
    }

    /**
     *
     * @return momento en el que se realizó la actividad
     */
    public Timestamp getWhen() {
        return when;
    }

    /**
     *
     * @param when momento en el que se realizó la actividad
     */
    public void setWhen(Timestamp when) {
        this.when = when;
    }
    
    
}
