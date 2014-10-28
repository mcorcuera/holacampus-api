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
import com.holacampus.api.validators.CreationNeeded;
import com.theoryinpractise.halbuilder.jaxrs.*;
import javax.validation.Valid;
import javax.validation.constraints.Size;

/**
 * Clase que representa los estudios ofertados por una universidad
 *  @author Mikel Corcuera <mik.corcuera@gmail.com>  
 */

@HalRootElement
public class Study extends Element implements Linkable{
    
    @CreationNeeded( message="{study.name.missing}")
    @Valid
    @Size( min=1, max=90, message="{study.name.wrong.size}")
    @HalProperty( name="name")
    private String          name;
        
    @CreationNeeded( message="{study.description.missing}")
    @Valid
    @Size( min=1, message="{study.description.wrong.size}")
    @HalProperty( name="description")
    private String          description;
    
    @HalEmbedded( "university")
    private University      university;

    /**
     *
     * @return universidad a la que pertenecen los estudios
     */
    public University getUniversity() {
        return university;
    }

    /**
     *
     * @param university universidad a la que pertenecen los estudios
     */
    public void setUniversity(University university) {
        this.university = university;
    }

    /**
     *
     * @return nombre de los estudios
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name nombre de los estudios
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return descripción de los estudios
     */
    public String getDescription() {
        return description;
    }

    /**
     *
     * @param description descripción de los estudios
     */
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    @HalSelfLink
    public String getSelfLink() {
        if( university != null)
            return university.getSelfLink() + "/studies/" + getId();
        return null;
    }
    
    /**
     *
     * @return enlace a la representación de la lista de estudiantes del 
     * estudio
     */
    @HalLink("students")
    public String getStudentsLink() {
        return getSelfLink() + "/students";
    }
    
    
}
