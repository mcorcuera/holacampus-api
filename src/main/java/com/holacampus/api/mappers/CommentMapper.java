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

package com.holacampus.api.mappers;

import com.holacampus.api.domain.Comment;
import com.holacampus.api.domain.CommentContainer;
import com.holacampus.api.domain.Permission;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

/**
 * 
 * Mapper para la clase {@link Comment}
 * @author Mikel Corcuera <mik.corcuera@gmail.com>
 */
public interface CommentMapper {
    
    /**
     * Inserta un nuevo comentario en la base de datos
     * @param comment Comentario a insertar
     * @return Elementos insertados
     * @throws Exception
     */
    public int                  createComment( @Param("comment") Comment comment) throws Exception; 
    
    /**
     * Obtiene una lista con los comentarios de un contenedor en concreto
     * @param id Identificador del comentario
     * @return Lista de los comentarios
     * @throws Exception
     */
    public List<Comment>        getComments( @Param("containerId") Long id) throws Exception;
    
     
    /**
     * Obtiene una lista con los comentarios de un contenedor en concreto
     * @param id Identificador del comentario
     * @param rb limites (inicio y tamaño) del resultado
     * @return Lista de los comentarios
     * @throws Exception
     */
    public List<Comment>        getComments( @Param("containerId") Long id, RowBounds rb) throws Exception;
    
    /**
     * Obtiene el número total de comentarios en un contenedor de comentarios
     * @param id Identificador de comentarios
     * @return Número total de comentarios
     * @throws Exception
     */
    public int                  getTotalComments( @Param("containerId") Long id) throws Exception;
    
    /**
     * Obtiene un comentario en concreto
     * @param id Identificador del comentario
     * @return El comentario
     * @throws Exception
     */
    public Comment              getComment( @Param("id") Long id) throws Exception;
    
    /**
     * Obtiene el contenedor de comentarios del comentario, es decir, el contenedor
     * sobre el cual se realizan las respuestas al comentario
     * @param id Identificador del comentario
     * @return El contenedor de comentarios del comentario
     * @throws Exception
     */
    public CommentContainer     getCommentContainer( @Param("id") Long id) throws Exception;
    
    /**
     * Obtiene los permisos de un usuario sobre un comentario en concreto
     * @param userId Identificador del usuario
     * @param commentId Identificador del comentario
     * @param permission Permisos del usuario sobre el comentario
     * @throws Exception
     */
    public void                  getPermissions(  @Param("userId") Long userId, @Param("commentId") Long commentId, @Param("permission") Permission permission) throws Exception;
    
    /**
     * Elimina un comentario
     * @param id Identificador del comentario a eliminar
     * @return
     * @throws Exception
     */
    public int                  deleteComment( @Param("id") Long id) throws Exception;
}
