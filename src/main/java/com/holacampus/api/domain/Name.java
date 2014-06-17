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

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 *  @author Mikel Corcuera <mik.corcuera@gmail.com>  
 */
public class Name {
    
    @NotNull( message="{name.firstName.missing}")
    @Size( min=1, max=45, message="{name.firstName.wrong.size}")
    private String firstName;
    
    @NotNull( message="{name.lastName.missing}")
    @Size( min=1, max=90,  message="{name.lastName.wrong.size}")
    private String lastName;

    /**
     *
     */
    public Name()
    {
        
    }
    
    /**
     *
     * @param firstName
     * @param lastName
     */
    public Name(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
    
    /**
     *
     * @return
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     *
     * @param firstName
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     *
     * @return
     */
    public String getLastName() {
        return lastName;
    }

    /**
     *
     * @param lastName
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return "Name{" + "firstName=" + firstName + ", lastName=" + lastName + '}';
    }
    
    
}
