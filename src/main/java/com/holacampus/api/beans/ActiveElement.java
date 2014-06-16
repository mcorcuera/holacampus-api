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

package com.holacampus.api.beans;

import com.holacampus.api.beans.Conversation;
import com.theoryinpractise.halbuilder.jaxrs.HalProperty;
import java.util.List;

/**
 *
 *  @author Mikel Corcuera <mik.corcuera@gmail.com>  
 */
public class ActiveElement {
    
    /**
     *
     */
    public static final String TYPE_USER    = "USER";

    /**
     *
     */
    public static final String TYPE_UNI     = "UNI";
    
    @HalProperty( name="id")
    private Long                id;
    private String              type;
    private Photo               profilePhoto;
    private List<Conversation>  conversations;
    
    /**
     *
     * @return
     */
    public Long getId() {
        return id;
    }

    /**
     *
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     *
     * @return
     */
    public String getType() {
        return type;
    }

    /**
     *
     * @param type
     */
    public void setType(String type) {
        this.type = type;
    }

}
