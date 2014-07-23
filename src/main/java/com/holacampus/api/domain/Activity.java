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
 *
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

    public String getWhatType() {
        return whatType;
    }

    public void setWhatType(String whatType) {
        this.whatType = whatType;
    }

    public Element getWhat() {
        return what;
    }

    public void setWhat(Element what) {
        this.what = what;
    }

    public String getWhoType() {
        return whoType;
    }

    public void setWhoType(String whoType) {
        this.whoType = whoType;
    }

    public ActiveElement getWho() {
        return who;
    }

    public void setWho(ActiveElement who) {
        this.who = who;
    }

    public String getWhereType() {
        return whereType;
    }

    public void setWhereType(String whereType) {
        this.whereType = whereType;
    }

    public Element getWhere() {
        return where;
    }

    public void setWhere(Element where) {
        this.where = where;
    }

    public Timestamp getWhen() {
        return when;
    }

    public void setWhen(Timestamp when) {
        this.when = when;
    }
    
    
}
