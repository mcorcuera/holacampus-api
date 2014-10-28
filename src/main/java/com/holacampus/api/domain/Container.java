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

/**
 * Superclase para los distintos contenedores (de comentarios, fotos, etc.)
 *  @author Mikel Corcuera <mik.corcuera@gmail.com>  
 */
public class Container {
    
    /**
     * Tipo de elemento al que puede pertenecer el contenedor
     */
    public static enum ElementType{

        /**
         * Usuario
         */
        USER,

        /**
         * Universidad
         */
        UNI,

        /**
         * Grupo (o evento)
         */
        GROUP,

        /**
         * Comentario
         */
        COMMENT,

        /**
         * Foto
         */
        PHOTO
    }
    
    private Long        id;
    private ElementType type;
    private Long        ownerId;

    /**
     *
     * @return identificador del contenedor
     */
    public Long getId() {
        return id;
    }

    /**
     *
     * @param id identificador del contenedor
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     *
     * @return tipo de elemento al que pertenece el contendor
     */
    public ElementType getType() {
        return type;
    }

    /**
     *
     * @param type tipo de elemento al que pertenece el contenedor
     */
    public void setType(ElementType type) {
        this.type = type;
    }

    /**
     *
     * @return identificador del tipo de elemento al que pertenece el contenedor
     */
    public Long getOwnerId() {
        return ownerId;
    }

    /**
     *
     * @param ownnerId identificador del tipo de elemento al que pertenece el contenedor
     */
    public void setOwnerId(Long ownnerId) {
        this.ownerId = ownnerId;
    }
    
    
    
    
}
