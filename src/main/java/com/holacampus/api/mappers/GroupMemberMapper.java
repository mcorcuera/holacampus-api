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
import com.holacampus.api.domain.GroupMember;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

/**
 * Mapper para la clase {@link GroupMember}
 * @author Mikel Corcuera <mik.corcuera@gmail.com>
 */
public interface GroupMemberMapper {
    
    /**
     * Obtiene los miembros de un grupo o evento en concreto
     * @param parent grupo o evento sobre el que se quieren obtener los resultados
     * @param q filtro por nombre del miembro
     * @return lista con los miembros
     * @throws Exception
     */
    public List<GroupMember> getMembers( @Param("parent") GroupEvent parent, @Param("q") String q) throws Exception;
    
    
     /**
     * Obtiene los miembros de un grupo o evento en concreto
     * @param parent grupo o evento sobre el que se quieren obtener los resultados
     * @param q filtro por nombre del miembro
     * @param rb límites (inicio y tamaño) del resultado
     * @return lista con los miembros
     * @throws Exception
     */
    public List<GroupMember> getMembers( @Param("parent") GroupEvent parent, @Param("q") String q, RowBounds rb) throws Exception;
    
    
    /**
     * Devuelve el número total de miembros de un grupo o evento
     * @param parent grupo o evento sobre el que se quieren obtener los resultados
     * @param q filtro por nombre del miembro
     * @return número de miembros de un grupo
     * @throws Exception
     */
    public int getTotalMembers( @Param("parent") GroupEvent parent, @Param("q") String q) throws Exception;
    
    /**
     *
     * @param parent
     * @param activeElementId
     * @return
     * @throws Exception
     */
    public List<ActiveElement> getEligibles( @Param("parent") GroupEvent parent, @Param("currentId") Long activeElementId) throws Exception;
    
    /**
     * Obtiene una lista con lo miembros de la red social que el usuario actual de la red
     * social qpuede añadir a un grupo concreto
     * @param parent grupo o evento sobre que que se quieren obtener los resultados
     * @param activeElementId identificador del {@link ActiveElment} que quiere obtener los resultados
     * @return lista con los posibles miembros a añadir
     * @throws Exception
     */
    public int getTotalEligibles( @Param("parent") GroupEvent parent, @Param("currentId") Long activeElementId) throws Exception;
    
    
    
    /**
     * Obtiene una lista con lo miembros de la red social que el usuario actual de la red
     * social qpuede añadir a un grupo concreto
     * @param parent grupo o evento sobre que que se quieren obtener los resultados
     * @param activeElementId identificador del {@link ActiveElment} que quiere obtener los resultados
     * @param rb  límites (inicio y tamaño) del resultado
     * @return lista con los posibles miembros a añadir
     * @throws Exception
     */
    public List<ActiveElement> getEligibles( @Param("parent") GroupEvent parent, @Param("currentId") Long activeElementId, RowBounds rb) throws Exception;
    
    /**
     * Obtiene un miembro concreto de un grupo o evento en concreto
     * @param parent grupo o evento sobre que que se quieren obtener los resultados
     * @param memberId identificador del miembreo
     * @return el miembro, si existe, null, en caso contrario
     * @throws Exception
     */
    public GroupMember getMember( @Param("parent") GroupEvent parent, @Param("memberId") Long memberId) throws Exception;
    
    /**
     * Añade un nuevo miembreo {@link ActiveElement} a un grupo o evento en concreto
     * @param parent grupo o evento al que se quiere añadir un nuevo miembro
     * @param activeElement miembro a añadir
     * @return número de miembros añadidos
     * @throws Exception
     */
    public int addMember( @Param("parent") GroupEvent parent, @Param("activeElement") ActiveElement activeElement ) throws Exception;
    
    /**
     * Elimina a un miembro de un grupo o evento
     * @param parent grupo o evento del que se quiere eliminar al miembro
     * @param memberId identificador del {@link ActiveElement} que se quiere eliminar
     * @return número de elementos eliminados
     * @throws Exception
     */
    public int deleteMember( @Param("parent") GroupEvent parent, @Param("memberId") Long memberId) throws Exception;
    
}
