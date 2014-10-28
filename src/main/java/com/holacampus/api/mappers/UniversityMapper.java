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
import com.holacampus.api.domain.CommentContainer;
import com.holacampus.api.domain.Permission;
import com.holacampus.api.domain.PhotoContainer;
import com.holacampus.api.domain.ProfilePhotoContainer;
import com.holacampus.api.domain.University;
import com.holacampus.api.domain.User;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

/**
 * Mapper para la clase {@link University}
 *  @author Mikel Corcuera <mik.corcuera@gmail.com>  
 */
public interface UniversityMapper {
    
    /**
     * Obtiene una lista con las universidades en la red social
     * @param q filtro por nombre de la universidad
     * @return lista de universidades
     * @throws Exception
     */
    public List<University>         getAllUniversities( @Param("q") String q) throws Exception;
    
    /**
     * Obtiene una lista con las universidades en la red social
     * @param q filtro por nombre de la universidad
     * @param rb límites (inicio y tamaño) del resultado
     * @return lista de universidades
     * @throws Exception
     */
    public List<University>         getAllUniversities( @Param("q") String q, RowBounds rb) throws Exception;
    
    /**
     * Obtiene el número total de  universidades en la red social
     * @param q filtro por nombre de la universidad
     * @return número total de  universidades
     * @throws Exception
     */
    public int                      getTotalUniversities( @Param("q") String q) throws Exception;
    
    /**
     * Obtiene una lista con los estudiantes que estudian o han estudiado en 
     * una universidad en concreto
     * @param universityId identificador de la {@link University}
     * @param q filtro por nombre del estudiante
     * @return lista con los estudiantes
     * @throws Exception
     */
    public List<User>               getStudents( @Param("universityId") Long universityId, @Param("q") String q) throws Exception;
    
       /**
     * Obtiene una lista con los estudiantes que estudian o han estudiado en 
     * una universidad en concreto
     * @param universityId identificador de la {@link University}
     * @param q filtro por nombre del estudiante
     * @param rb límites (inicio y tamaño) del resultado
     * @return lista con los estudiantes
     * @throws Exception
     */
    public List<User>               getStudents( @Param("universityId") Long universityId, @Param("q") String q, RowBounds rb) throws Exception;
    
    /**
     * Obtiene el número total de estudiantes que estudian o han estudiado en una
     * universidad en concreto
     * @param universityId identificador de la {@link University}
     * @param q filtro por nombre del estudiante
     * @return número total de estudiantes 
     * @throws Exception
     */
    public int                      getTotalStudents( @Param("universityId") Long universityId, @Param("q") String q) throws Exception;
    
    /**
     * Obtiene una universidad en concreto
     * @param id indetificador de la {@link University}
     * @return la universidad, si existe, o null en caso contrario
     * @throws Exception
     */
    public University               getUniversity( @Param("id") Long id) throws Exception;
    
    /**
     * Crea una nueva universidad en la red social
     * @param university datos de la universidad a crear
     * @throws Exception
     */
    public void                     createUniversity( @Param("university") University university) throws Exception;
   
    /**
     * Obtiene el contenedor de comentarios ({@link CommentContainer} de la
     * universidad
     * @param id identificadorde la {@link University}
     * @return El contenedor de comentarios de la universidad
     * @throws Exception
     */
    public CommentContainer         getCommentContainer( @Param("id") long id) throws Exception;
    
    /**
     * Obtiene el contenedor de fotos ({@link PhotoContainer} de la
     * universidad
     * @param id identificadorde la {@link University}
     * @return El contenedor de fotos de la universidad
     * @throws Exception
     */
    public PhotoContainer           getPhotoContainer( @Param("id") long id) throws Exception;
    
    /**
     * Obtiene el contenedor de foto de perfil ({@link ProfilePhotoContainer} de la
     * universidad
     * @param id identificadorde la {@link University}
     * @return el contenedor de foto de perfil de la universidad
     * @throws Exception
     */
    public ProfilePhotoContainer    getProfilePhotoContainer( @Param("id") long id) throws Exception;

    /**
     * Obtiene los permisos de un {@link ActiveElement} sobre una universidad
     * en concreto
     * @param currentUserId identificador del {@link ActiveElement}
     * @param universityId identificador de la {@link University}
     * @param permission Permisos del {@link ActiveElement} sobre la universidad
     * @throws Exception
     */
    public void                     getPermissions(  @Param("currentUserId") Long currentUserId, @Param("universityId") Long universityId, @Param("permission") Permission permission) throws Exception;
  
    /**
     * Obtiene una lista con las ciudades donde se encuentra la universidad
     * @param universityId identificador de la {@link University}
     * @return lista con las ciudades donde se encuentra la universidad
     * @throws Exception
     */
    public List<City>               getCities( @Param( "universityId") Long universityId) throws Exception;
    
     /**
     * Obtiene una lista con las ciudades donde se encuentra la universidad
     * @param universityId identificador de la {@link University}
     * @param rb límites (inicio y tamaño) del resultado
     * @return lista con las ciudades donde se encuentra la universidad
     * @throws Exception
     */
    public List<City>               getCities( @Param( "universityId") Long universityId, RowBounds rb) throws Exception;
    
    /**
     * Obtiene el número total de ciudades donde se encuentra la universidad
     * @param universityId identificador de la {@link University}
     * @return número total de ciudades
     * @throws Exception
     */
    public int                      getTotalCities(@Param( "universityId") Long universityId) throws Exception;
    
    /**
     * Inserta una nueva ciudad a la lista de ciudades  en donde se encuentra 
     * la universidad
     * @param universityId identificador de la {@link University}
     * @param city ciudad a insertar
     * @return número de elementos insertados
     * @throws Exception
     */
    public int                      insertCity( @Param( "universityId") Long universityId, @Param( "city") City city) throws Exception;
    
    /**
     * Elimina una ciudad en concreto de la lista de ciudades donde se encuentra
     * la universidad
     * @param universityId identificador de la {@link University}
     * @param cityId identificador de la {@link City}
     * @param countryId identificador del {@link Country} al que pertenece la ciudad
     * @return número de elementos eliminados
     * @throws Exception
     */
    public int                      deleteCity( @Param( "universityId") Long universityId, @Param( "cityId") Long cityId, @Param( "countryId") Long countryId) throws Exception;
    

}
