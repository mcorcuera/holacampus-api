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
import com.holacampus.api.domain.PhotoContainer;
import com.holacampus.api.domain.ProfilePhotoContainer;
import com.holacampus.api.domain.*;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

/**
 * Mapper para la clase {@link User}
 *  @author Mikel Corcuera <mik.corcuera@gmail.com>  
 */
public interface UserMapper {
    
    /**
     * Obtiene una lista con los usuarios registrados en la red social
     * @param q filtro por nombre de usuario
     * @return lista de usuarios
     * @throws Exception
     */
    public List<User>               getAllUsers(@Param("q")  String q) throws Exception;
    
     /**
     * Obtiene una lista con los usuarios registrados en la red social
     * @param q filtro por nombre de usuario
     * @param rb límites (inicio y tamaño) del resultado
     * @return lista de usuarios
     * @throws Exception
     */
    public List<User>               getAllUsers( @Param("q") String q, RowBounds rb) throws Exception;
        
    /**
     * Obtiene el número total de usuarios registrados en la red social
     * @param q filtro por nombre de usuario
     * @return número total de usuarios
     * @throws Exception
     */
    public int                      getTotalUsers( @Param("q") String q) throws Exception;
  
    /**
     * Obtiene un usuario en concreto
     * @param id identificador del {@link User}
     * @return el usuario, si existe, o null en caso contrario
     * @throws Exception
     */
    public User                     getUser( @Param("id") long id) throws Exception;
    
   /**
     * Obtiene el contenedor de comentarios ({@link CommentContainer} del
     * usuario
     * @param id identificador del {@link User}
     * @return El contenedor de comentarios del usuario
     * @throws Exception
     */
    public CommentContainer         getCommentContainer( @Param("id") long id) throws Exception;
    
    /**
     * Obtiene el contenedor de fotos ({@link PhotoContainer} del
     * usuario
     * @param id identificador del {@link User}
     * @return El contenedor de fotos del usuario
     * @throws Exception
     */
    public PhotoContainer           getPhotoContainer( @Param("id") long id) throws Exception;
    
    /**
     * Obtiene elcontenedor de foto de perfil ({@link ProfilePhotoContainer} del
     * usuario
     * @param id identificador del {@link User}
     * @return El contenedor de fotos del usuario
     * @throws Exception
     */
    public ProfilePhotoContainer    getProfilePhotoContainer( @Param("id") long id) throws Exception;
    
    /**
     * Obtiene los permisos de un {@link ActiveElement} sobre un usuario
     * en concreto
     * @param currentUserId identificador del {@link ActiveElement}
     * @param userId identificador del {@link User}
     * @param permission Permisos del {@link ActiveElement} sobre el usuario
     * @throws Exception
     */
    public void                     getPermissions(  @Param("currentUserId") Long currentUserId, @Param("userId") Long userId, @Param("permission") Permission permission) throws Exception;
    
    /**
     * Crea un nuevo usuario en la red social
     * @param user datos del usuario a crear
     * @return número de elementos creados
     * @throws Exception
     */
    public int                      createUser( @Param("user")User user) throws Exception;
    
    /**
     * Elimina un usuario de la red social
     * @param id identificador del {@link User} a eliminar
     * @return número de elementos eliminados
     * @throws Exception
     */
    public int                      deleteUser( @Param("id") Long id) throws Exception;
        
}
