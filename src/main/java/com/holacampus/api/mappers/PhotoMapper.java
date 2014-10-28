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

import com.holacampus.api.domain.CommentContainer;
import com.holacampus.api.domain.Permission;
import com.holacampus.api.domain.*;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

/**
 * Mapper para la clase {@link Photo}
 * @author Mikel Corcuera <mik.corcuera@gmail.com>
 */
public interface PhotoMapper {
    
    
    /**
     * Obtiene una lista con las fotos de un contenedor en concreto
     * @param id identificador del {@link PhotoContainer}
     * @return lista de fotos
     * @throws Exception
     */
    public List<Photo>         getPhotos( @Param("containerId") Long id) throws Exception;
     
     
    /**
     * Obtiene una lista con las fotos de un contenedor en concreto
     * @param id identificador del {@link PhotoContainer}
     * @param rb límites (inicio y tamaño) del resultado
     * @return lista de fotos
     * @throws Exception
     */
    public List<Photo>         getPhotos( @Param("containerId") Long id, RowBounds rb) throws Exception;
     
    /**
     * Obtiene el número total de fotos en un contenedor en concreto
     * @param id identificador del {@link PhotoContainer}
     * @return número total de fotos
     * @throws Exception
     */
    public int                 getTotalPhotos( @Param("containerId") Long id) throws Exception;
     
    
    /**
     * Inserta una nueva foto en la red social
     * @param photo Foto a insertar
     * @return elementos insertados
     * @throws Exception
     */
    public int                 createPhoto( @Param("photo") Photo photo) throws Exception; 
     
    /**
     * Obtiene el identificador del {@link PhotoContainer} al que pertenece
     * la foto
     * @param id identificador de la {@link Photo}
     * @return el identificador del {@link PhotoContainer}
     * @throws Exception
     */
    public long                getPhotoContainerId( Long id) throws Exception;
     
    /**
     * Obtiene el contenedor de comentarios ({@link CommentContainer}) de la 
     * fotos. Es decir, el contenedor de comentarios donde se realizan las respuestas
     * a la foto
     * @param id identificador de la {@link Photo}
     * @return el contenedro de comentarios de la foto
     * @throws Exception
     */
    public CommentContainer    getCommentContainer( @Param("id") Long id) throws Exception;
     
     /**
     * Obtiene una foto en concreto
     * @param photoId identificador de la {@link Photo}
     * @return la foto si existe, null en otro caso
     * @throws Exception
     */
    public Photo               getPhoto( @Param("photoId") Long photoId) throws Exception;
     
    /**
     * Obtiene los permisos de un usuario sobre un comentario en concreto
     * @param userId identificador del {@link ActiveElement}
     * @param photoId identificador de la {@link Photo}
     * @param permission permisos de usuario sobre la foto
     * @throws Exception
     */
    public void                getPermissions(  @Param("userId") Long userId, @Param("photoId") Long photoId, @Param("permission") Permission permission) throws Exception;
     
    /**
     * Elimina una foto
     * @param id identificador de la {@link Photo}
     * @return número de elementos eliminados
     * @throws Exception
     */
    public int                 deletePhoto( @Param( "id") Long id) throws Exception;
     
    /**
     * Actualiza los datos de una foto
     * @param photo identificador de la {@link Photo}
     * @return número de elementos actualizados
     * @throws Exception
     */
    public int                 updatePhoto( @Param("photo") Photo photo) throws Exception;
}
