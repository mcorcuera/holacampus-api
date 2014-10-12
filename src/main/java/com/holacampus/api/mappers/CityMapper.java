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

import com.holacampus.api.domain.City;
import com.holacampus.api.domain.Country;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

/**
 *Mapper para la clase {@link City}
 * @author Mikel Corcuera <mik.corcuera@gmail.com>
 */
public interface CityMapper {
    
    /**
     * Obtiene las ciudades presentes en la base de datos para un país en concreto
     * @param country País sobre el que se obtiene las ciudades
     * @param q Filtro por nombre de la ciudad
     * @param rb imites (inicio y tamaño) del resultado
     * @return Lista con las ciudades
     * @throws Exception
     */
    public List<City>   getCities( @Param("country") Country country, @Param("q") String q, RowBounds rb) throws Exception;
    
     /**
     * Obtiene las ciudades presentes en la base de datos para un país en concreto
     * @param country País sobre el que se obtiene las ciudades
     * @param q Filtro por nombre de la ciudad
     * @return Lista con las ciudades
     * @throws Exception
     */
    public List<City>   getCities( @Param("country") Country country, @Param("q") String q) throws Exception;
    
    /**
     * Devuelve el número total de ciudades presentes en la base de datos para un país en concreto
     * @param country País sobre el que se obtiene las ciudades
     * @param q Filtro por nombre de la ciudad
     * @return número total de ciudades
     * @throws Exception
     */
    public int          getTotalCities( @Param("country") Country country, @Param("q") String q) throws Exception;
    
    /**
     * Inserta una nueva ciudad en un país determinado
     * @param country País al que pertenece la ciudad
     * @param city Ciudad a insertar
     * @return Número de elementos insertados
     * @throws Exception
     */
    public int          createCity( @Param("country") Country country, @Param("city") City city) throws Exception;
    
    /**
     * Obtiene una ciudad en concreto de un país en concreto
     * @param country País al que pertenece la ciudad
     * @param id Identificador de la ciudad
     * @return La ciudad solicitada
     * @throws Exception
     */
    public City         getCity( @Param("country") Country country, @Param( "id") Long id) throws Exception;
    
    /**
     * Modifica los datos de una ciudad
     * @param country País al que pertenece la ciudad
     * @param city La ciudad con los datos a modificar
     * @return número de elementos modificados
     * @throws Exception
     */
    public int          updateCity( @Param("country") Country country, @Param("city") City city) throws Exception;
    
    /**
     * Elimina una ciudad del sistema
     * @param country País al que pertenece la ciudad
     * @param id Identificador de la ciudad a eliminar
     * @return número de elementos eliminados
     * @throws Exception
     */
    public int          deleteCity( @Param("country") Country country, @Param( "id") Long id) throws Exception;
}
