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
 *
 *  @author Mikel Corcuera <mik.corcuera@gmail.com>  
 */

@HalRootElement
public class Study implements Linkable{
    
    @HalProperty( name="id")
    private Long            id;
    
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public University getUniversity() {
        return university;
    }

    public void setUniversity(University university) {
        this.university = university;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    @HalSelfLink
    public String getSelfLink() {
        if( university != null)
            return university.getSelfLink() + "/studies/" + id;
        return null;
    }
    
    @HalLink("students")
    public String getStudentsLink() {
        return getSelfLink() + "/students";
    }
    
    
}
