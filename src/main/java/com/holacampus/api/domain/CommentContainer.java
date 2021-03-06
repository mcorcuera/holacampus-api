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

import java.util.List;

/**
 *  Clase que representa a los contenedores de comentarios
 *  @author Mikel Corcuera <mik.corcuera@gmail.com>  
 */
public class CommentContainer extends Container{
   
    private List<Comment> comments;

    /**
     *
     * @return lista de comentarios existentes en el contenedor
     */
    public List<Comment> getComments() {
        return comments;
    }

    /**
     *
     * @param comments lista de comentarios en el contenedor
     */
    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
    
    
    
}
