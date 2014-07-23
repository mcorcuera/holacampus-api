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
import com.holacampus.api.validators.CreationNeeded;
import com.theoryinpractise.halbuilder.jaxrs.*;
import javax.validation.Valid;
import javax.validation.constraints.Size;

/**
 *
 *  @author Mikel Corcuera <mik.corcuera@gmail.com>  
 */

@HalRootElement
public class Country extends Element implements Linkable {
        
    @CreationNeeded( message="{country.name.missing}")
    @Valid
    @Size( min=1, max=45, message="{country.name.wrong.size}")
    @HalProperty( name="name")
    private String      name;
    
    @CreationNeeded
    @Valid
    @HalProperty( name="location")
    private Location    location;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    @HalSelfLink
    public String getSelfLink() {
        return Utils.createLink("/countries/" + getId(), null);
    }
    
    @HalLink( "cities")
    public String getCitiesLink() {
        return getSelfLink() + "/cities";
    }
    
    
}
