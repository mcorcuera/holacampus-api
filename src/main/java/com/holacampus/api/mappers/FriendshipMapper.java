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

import com.holacampus.api.domain.Friendship;
import java.util.List;
import com.holacampus.api.domain.*;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

/**
 * Mapper para la clase {@link Friendship}
 * @author Mikel Corcuera <mik.corcuera@gmail.com>
 */
public interface FriendshipMapper {
    
    /**
     * Obtiene las relaciones de amistad de un {@link ActiveElement} en concreto.
     * Se podrán filtrar estos resultados por estado de la amistad y por nombre
     * @param elementId identificador del  {@link ActiveElement} del que se obtienen las amistades
     * @param q filtro por nombre del usuario
     * @param unconfirmed Si es verdadero, obtiene solo las amistades no confirmadas
     * @param pending Si es verdadero, obtiene solo las amistades pendientes de confirmación
     * @return lista de amistades que cumplen con los criterios
     * @throws Exception
     */
    public List<Friendship> getFriends( @Param("userId") Long elementId, @Param("q") String q, @Param("unconfirmed") boolean unconfirmed, @Param("pending") boolean pending) throws Exception;
    
        /**
     * Obtiene las relaciones de amistad de un {@link ActiveElement} en concreto.
     * Se podrán filtrar estos resultados por estado de la amistad y por nombre
     * @param elementId identificador del  {@link ActiveElement} del que se obtienen las amistades
     * @param q filtro por nombre del usuario
     * @param unconfirmed Si es verdadero, obtiene solo las amistades no confirmadas
     * @param pending Si es verdadero, obtiene solo las amistades pendientes de confirmación
     * @param rb limites (inicio y tamaño) del resultado
     * @return lista de amistades que cumplen con los criterios
     * @throws Exception
     */
    public List<Friendship> getFriends( @Param("userId") Long elementId, @Param("q") String q, @Param("unconfirmed") boolean unconfirmed, @Param("pending") boolean pending, RowBounds rb) throws Exception;
    
    /**
     * Devuelve el número total de relaciones de amistad de un {@link ActiveElement} en concreto.
     * Se podrán filtrar estos resultados por estado de la amistad y por nombre
     * @param elementId identificador del  {@link ActiveElement} del que se obtienen las amistades
     * @param q filtro por nombre del usuario
     * @param unconfirmed Si es verdadero, obtiene solo las amistades no confirmadas
     * @param pending Si es verdadero, obtiene solo las amistades pendientes de confirmación
     * @return número total de  amistades que cumplen con los criterios
     * @throws Exception
     */
    public int              getTotalFriends( @Param("userId") Long elementId, @Param("q") String q, @Param("unconfirmed") boolean unconfirmed, @Param("pending") boolean pending) throws Exception;

    /**
     * Añade una nueva relación de amistad entre dos usuarios. Esta relación
     * se encontrará en estado de no confirmada hasta que el usuario que recibe la
     * petición la acepte
     * @param senderId identificador del {@link User} que envía la petición de amistad
     * @param receiverId identificador del {@link User} que recibe la petición de amistad
     * @return número de elementos creados
     * @throws Exception
     */
    public int              createFriendship( @Param("senderId") Long senderId, @Param("receiverId") Long receiverId) throws Exception;
    
    /**
     * Obtiene una realción de amistad concreta de un {@link User}
     * @param userId identificador del {@link User} sobre el que se realiza la consulta
     * @param friendId identificador de la amistad que se quiere obtener
     * @return si existe, la relación de amistad. Null en caso contrario
     * @throws Exception
     */
    public Friendship       getFriend( @Param("userId") Long userId, @Param("friendId") Long friendId) throws Exception;
    
    /**
     * Acepta una petición de amistad de otro usuario
     * @param userId identificador del {@link User} que acepta la petición de amistad
     * @param friendId identificador de la amistad
     * @return número de elementos aceptados
     * @throws Exception
     */
    public int              acceptFriend( @Param("currentUserId") Long userId, @Param("friendId") Long friendId) throws Exception;
    
    /**
     * Eliminina una relación de amistad con otro usuario
     * @param userId identificador del {@link User} que elimina la relación de amistad
     * @param friendId identificador del {@link User} que se quiere eliminar
     * @return número de elementos eliminados
     * @throws Exception
     */
    public int              deleteFriend( @Param("currentUserId") Long userId, @Param("friendId") Long friendId) throws Exception;
}
