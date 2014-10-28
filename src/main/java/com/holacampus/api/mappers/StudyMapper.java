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

import com.holacampus.api.domain.Study;
import com.holacampus.api.domain.University;
import com.holacampus.api.domain.User;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import com.holacampus.api.domain.*;

/**
 * Mapper para la clase {@link Study}
 * @author Mikel Corcuera <mik.corcuera@gmail.com>
 */
public interface StudyMapper {
    
    
    /**
     * Obtiene una lista con los estudios ofertados por una universidad
     * @param university universidad sobre la que obtener los resultados
     * @param q filtro por nombre de los estudios
     * @return lista con los estudios
     * @throws Exception
     */
    public List<Study>   getStudies( @Param("university") University university, @Param("q") String q) throws Exception;
   
     /**
     * Obtiene una lista con los estudios ofertados por una universidad
     * @param university universidad sobre la que obtener los resultados
     * @param q filtro por nombre de los estudios
     * @param rb límites (inicio y tamaño) del resultado
     * @return lista con los estudios
     * @throws Exception
     */
    public List<Study>   getStudies( @Param("university") University university, @Param("q") String q, RowBounds rb) throws Exception;
    
    /**
     * Número total de estudios ofertados por una universidad
    * @param university universidad sobre la que obtener los resultados
     * @param q filtro por nombre de los estudios
     * @return número total de estudios
     * @throws Exception
     */
    public int          getTotalStudies( @Param("university") University university, @Param("q") String q) throws Exception;
    
     /**
     * Obtiene una lista con los estudiantes que pertenecen a un estudio ofertado
     * por una universidad
     * @param university universidad sobre la que obtener los resultados
     * @param studyId identificador del {@link Study}
     * @param q filtro por nombre del estudiante
     * @return lista con los estudiantes
     * @throws Exception
     */
    public List<User>   getStudents( @Param("university") University university, @Param( "studyId") Long studyId, @Param("q") String q) throws Exception;
    
    /**
     * Obtiene una lista con los estudiantes que pertenecen a un estudio ofertado
     * por una universidad
     * @param university universidad sobre la que obtener los resultados
     * @param studyId identificador del {@link Study}
     * @param q filtro por nombre del estudiante
     * @param rb límites (inicio y tamaño) del resultado
     * @return lista con los estudiantes
     * @throws Exception
     */
    public List<User>   getStudents( @Param("university") University university, @Param( "studyId") Long studyId, @Param("q") String q, RowBounds rb) throws Exception;
    
    /**
     * Número total de estudiantes que pertenecen a un estudio ofertado por una
     * universidad
      * @param university universidad sobre la que obtener los resultados
     * @param studyId identificador del {@link Study}
     * @param q filtro por nombre del estudiante
     * @return número total de estudiantes
     * @throws Exception
     */
    public int          getTotalStudents( @Param("university") University university, @Param( "studyId") Long studyId, @Param("q") String q) throws Exception;
    
    /**
     * Añade un nuevo estudio a los ofertados por una universidad
     * @param university Universidad a la que añadir los estudios
     * @param study datos del estudio a ofertar
     * @return número de elementos insertados
     * @throws Exception
     */
    public int          createStudy( @Param("university") University university, @Param("study") Study study) throws Exception;
    
    /**
     * Obtiene un estuido dentro de los ofertados por la universidad
     * @param university universidad sobre la que obtener los resultados
     * @param studyId identificador del {@link Study}
     * @return el estudio, si existe, null, en caso contrario
     * @throws Exception
     */
    public Study         getStudy( @Param("university") University university, @Param( "studyId") Long studyId) throws Exception;
    
    /**
     * Actualiza los datos de un estudio en concreto dentro de los estudios 
     * ofertados por una universidad
     * @param university Universidad donde actualizar los estudios
     * @param study datos del estudio a actualizar
     * @return número de elementos actualizados
     * @throws Exception
     */
    public int          updateStudy( @Param("university") University university, @Param("study") Study study) throws Exception;
    
    /**
     * Elimina un estudio en concreto dentro de los estudios ofertados por la 
     * universidad
     * @param university universidad del que eliminar la oferta de estudio
     * @param studyId identificador del {@link Study}
     * @return número de elementos elmiminados
     * @throws Exception
     */
    public int          deleteStudy( @Param("university") University university, @Param( "studyId") Long studyId) throws Exception;
}
