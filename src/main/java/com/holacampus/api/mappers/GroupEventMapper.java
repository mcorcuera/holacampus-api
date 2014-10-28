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

import com.holacampus.api.domain.*;
import com.holacampus.api.domain.GroupEvent;
import com.holacampus.api.domain.Permission;
import com.holacampus.api.domain.PhotoContainer;
import com.holacampus.api.domain.ProfilePhotoContainer;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

/**
 * Mapper para la clase {@link GroupEvent}
 * @author Mikel Corcuera <mik.corcuera@gmail.com>
 */
public interface GroupEventMapper {
    
    
    /**
     * Obtiene una lista con los grupos o eventos de la red social.
     * @param type {@link GroupEvent#TYPE_EVENT} para eventos y {@link GroupEvent#TYPE_GROUP}
     *              para grupos
     * @param q filtro por nombre
     * @param pending cuando es <code>true</code> obtiene los eventos por llegar,
     * cuando es <code>false</code> los eventos pasados y si es <code>null</code> obtiene
     * todos. Ignorado para el caso de grupos.
     * @return lista con los grupos o eventos
     * @throws Exception
     */
    public List<GroupEvent> getGroupsEvents( @Param("type") String type, @Param("q") String q, @Param("pending") Boolean pending) throws Exception;
    
    
    /**
     * Obtiene una lista con los grupos o eventos de la red social.
     * @param type {@link GroupEvent#TYPE_EVENT} para eventos y {@link GroupEvent#TYPE_GROUP}
     *              para grupos
     * @param q filtro por nombre
     * @param pending cuando es <code>true</code> obtiene los eventos por llegar,
     * cuando es  <code>false</code> los eventos pasados y si es <code>null</code> obtiene
     * todos. Ignorado para el caso de grupos.
     * @param rb límites (inicio y tamaño) del resultado
     * @return lista con los grupos o eventos
     * @throws Exception
     */
    public List<GroupEvent> getGroupsEvents( @Param("type") String type, @Param("q") String q, @Param("pending") Boolean pending, RowBounds rb) throws Exception;
    
    /**
     * Devuelve el número total de grupos o eventos de la red social
     * @param type {@link GroupEvent#TYPE_EVENT} para eventos y {@link GroupEvent#TYPE_GROUP}
     *              para grupos
     * @param q filtro por nombre
     * @param pending cuando es <code>true</code> obtiene los eventos por llegar,
     * cuando es  <code>false</code> los eventos pasados y si es <code>null</code> obtiene
     * todos. Ignorado para el caso de grupos.
     * @return número de grupos o eventos.
     * @throws Exception
     */
    public int getTotalGroupsEvents(@Param("type") String type, @Param("q") String q, @Param("pending") Boolean pending) throws Exception;
    
    
     /**
     * Obtiene una lista con los grupos o eventos de la red social asociados a un 
     * {@link ActiveElement}
     * @param type {@link GroupEvent#TYPE_EVENT} para eventos y {@link GroupEvent#TYPE_GROUP}
     *              para grupos
     * @param q filtro por nombre
     * @param pending cuando es <code>true</code> obtiene los eventos por llegar,
     * cuando es  <code>false</code> los eventos pasados y si es <code>null</code> obtiene
     * todos. Ignorado para el caso de grupos.
     * @param elementId identificador del {@link ActiveElement}
     * @return lista con los grupos o eventos
     * @throws Exception
     */
    public List<GroupEvent> getGroupsEventsForActiveElement( @Param("type") String type, @Param("q") String q, @Param( "elementId") Long elementId, @Param("pending") Boolean pending) throws Exception;
    
    
     /**
     * Obtiene una lista con los grupos o eventos de la red social asociados a un 
     * {@link ActiveElement}
     * @param type {@link GroupEvent#TYPE_EVENT} para eventos y {@link GroupEvent#TYPE_GROUP}
     *              para grupos
     * @param q filtro por nombre
     * @param elementId identificador del {@link ActiveElement}
     * @param pending cuando es <code>true</code> obtiene los eventos por llegar,
     * cuando es  <code>false</code> los eventos pasados y si es <code>null</code> obtiene
     * todos. Ignorado para el caso de grupos.
     * @param rb límites (inicio y tamaño) del resultado
     * @return lista con los grupos o eventos
     * @throws Exception
     */
    public List<GroupEvent> getGroupsEventsForActiveElement( @Param("type") String type, @Param("q") String q, @Param( "elementId") Long elementId, @Param("pending") Boolean pending, RowBounds rb) throws Exception;
    
     /**
     * Devuelve el número total de grupos o eventos de la red social asociados a un 
     * {@link ActiveElement}
     * @param type {@link GroupEvent#TYPE_EVENT} para eventos y {@link GroupEvent#TYPE_GROUP}
     *              para grupos
     * @param q filtro por nombre
     * @param elementId identificador del {@link ActiveElement}
     * @param pending cuando es <code>true</code> obtiene los eventos por llegar,
     * cuando es  <code>false</code> los eventos pasados y si es <code>null</code> obtiene
     * todos. Ignorado para el caso de grupos.
     * @return número de grupos o eventos.
     * @throws Exception
     */
    public int getTotalGroupsEventsForActiveElement(@Param("type") String type, @Param("q") String q, @Param( "elementId") Long elementId,  @Param("pending") Boolean pending) throws Exception;
    
    /**
     * Obtiene un grupo o evento en concreto
     * @param type {@link GroupEvent#TYPE_EVENT} para eventos y {@link GroupEvent#TYPE_GROUP}
     *              para grupos
     * @param id identificador del grupo
     * @return el grupo o evento si existe, null en caso contrario.
     * @throws Exception
     */
    public GroupEvent getGroupEvent( @Param("type") String type, @Param( "id") Long id) throws Exception;
    
    /**
     *
     * Obtiene el contenedor de comentarios ({@link CommentContainer} del grupo o evento.
     * @param id identificador del grupo o evento
     * @return El contenedor de comentarios del grupo o evento
     * @throws Exception
     */
    public CommentContainer         getCommentContainer( @Param("id") long id) throws Exception;
    
     /**
     *
     * Obtiene el contenedor de fotos ({@link PhotoContainer} del grupo o evento.
     * @param id identificador del grupo o evento
     * @return El contenedor de fotos del grupo o evento
     * @throws Exception
     */
    public PhotoContainer           getPhotoContainer( @Param("id") long id) throws Exception;
    
    /**
     *
     * Obtiene el contenedor de foto de perfil ({@link ProfilePhotoContainer} del grupo o evento.
     * @param id identificador del grupo o evento
     * @return El contenedor de foto de perfil del grupo o evento
     * @throws Exception
     */
    public ProfilePhotoContainer    getProfilePhotoContainer( @Param("id") long id) throws Exception;
    
    /**
     * Obtiene los permisos de un {@link ActiveElement} sobre un grupo o evento en concreto
     * @param elementId identificador del {@link ActiveElement}
     * @param groupId identificador ddel grupo o evento
     * @param permission Permisos del usuario sobre el grupo o evento
     * @throws Exception
     */
    public void                  getPermissions(  @Param("userId") Long elementId, @Param("groupId") Long groupId, @Param("permission") Permission permission) throws Exception;

    /**
     * Crea un nuevo grupo o evento en la red social.
     * @param creatorId el identificador del {@link ActiveElement} que crea el grupo o evento
     * @param group datos del grupo o evento a crear
     * @throws Exception
     */
    public void createGroupEvent( @Param( "creatorId") Long creatorId, @Param("group") GroupEvent group) throws Exception;
    
    
}
