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

package com.holacampus.api.hal;

import com.holacampus.api.domain.Location;
import com.holacampus.api.domain.Name;
import com.theoryinpractise.halbuilder.api.ReadableRepresentation;
import com.theoryinpractise.halbuilder.api.RepresentationException;
import com.theoryinpractise.halbuilder.jaxrs.builders.BuilderException;
import com.theoryinpractise.halbuilder.jaxrs.builders.PropertyBuilder;

/**
 * Constructor de un objeto de la clase {@link Location} utilizado por el plugin
 * HAL-JSON para JAX-RS
 * @author Mikel Corcuera <mik.corcuera@gmail.com>
 */
public class LocationBuilder implements PropertyBuilder<Location>{

    @Override
    public Location build(Object s) throws BuilderException{
        Location l = null;
        if( ReadableRepresentation.class.isAssignableFrom( s.getClass())) {
            ReadableRepresentation r = (ReadableRepresentation) s;
            try {
                l = new Location( Float.parseFloat( (String) r.getValue("latitude")), Float.parseFloat((String) r.getValue("longitude")));
            }catch( RepresentationException | NumberFormatException e) {
                throw new BuilderException( e);
            }  
        }
        return l;
    }

    @Override
    public boolean canBuild(Class type) {
        return Location.class.isAssignableFrom(type);
    }

    @Override
    public Class getBuildType() {
        return Location.class;
    }
    
}
