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

import com.holacampus.api.domain.Study;
import com.holacampus.api.hal.Linkable;
import com.holacampus.api.utils.Utils;
import com.holacampus.api.validators.CreationNeeded;
import com.theoryinpractise.halbuilder.jaxrs.*;
import java.util.Date;

/**
 * Clase que representa a las etapas dentro de la trayectoria académica de un 
 * estudiante
 *  @author Mikel Corcuera <mik.corcuera@gmail.com>  
 */
@HalRootElement
public class Stage extends Element implements Linkable{
    
    @CreationNeeded
    @HalProperty( name="fromYear")
    private Integer     fromYear;
    @HalProperty( name="toYear")
    private Integer     toYear;
    @HalProperty( name="description")
    private String  description;
    @HalProperty( name="added")
    private Date    added;
    @HalEmbedded( "user")
    private User    user;
    @CreationNeeded
    @HalEmbedded( "study")
    private Study   study;
    
    private String path;

    /**
     *
     * @return usuario al que pertenece la trayectoria académica
     */
    public User getUser() {
        return user;
    }

    /**
     *
     * @param user usuario al que pertenece la trayectoria académica
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     *
     * @return estudios que se realizaron en esta etapa
     */
    public Study getStudy() {
        return study;
    }

    /**
     *
     * @param study estudios que se realizaron en esta etapa
     */
    public void setStudy(Study study) {
        this.study = study;
    }

    /**
     *
     * @return año de inicio de la etapa
     */
    public Integer getFromYear() {
        return fromYear;
    }

    /**
     * 
     * @param fromYear año de inicio de la etapa
     */
    public void setFromYear(Integer fromYear) {
        this.fromYear = fromYear;
    }

    /**
     *
     * @return año de final de la etapa
     */
    public Integer getToYear() {
        return toYear;
    }

    /**
     *
     * @param toYear año de final de la etapa
     */
    public void setToYear(Integer toYear) {
        this.toYear = toYear;
    }

    /**
     *
     * @return descripción de la etapa
     */
    public String getDescription() {
        return description;
    }

    /**
     *
     * @param description descripción de la etapa
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     *
     * @return fecha en la que se añadió la etapa a la trayectoria
     */
    public Date getAdded() {
        return added;
    }

    /**
     *
     * @param added fecha en la que se añadió la etapa a la trayectoria
     */
    public void setAdded(Date added) {
        this.added = added;
    }

    @Override
    @HalSelfLink
    public String getSelfLink() {
        return Utils.createLink(path, null);
    }
    
    /**
     *
     * @param path enlace a la representación del recurso
     */
    public void setPath( String path) {
        this.path = path;
    }
    
    
}
