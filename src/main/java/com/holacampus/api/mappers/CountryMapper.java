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

import com.holacampus.api.domain.Country;
import com.holacampus.api.domain.*;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

/**
 * Mapper para la clase {@link Country}
 * @author Mikel Corcuera <mik.corcuera@gmail.com>
 */
public interface CountryMapper {
    
    
    /**
     * Obtiene una lista con los países ({@link Country}) registrados en el sistema
     * @param q filtro por nombre del país
     * @return lista con los países 
     * @throws Exception
     */
    public List<Country> getCountries( @Param("q") String q) throws Exception;
    
    
    /**
     * Obtiene una lista con los países ({@link Country}) registrados en el sistema
     * @param q filtro por nombre del país
     * @param rb  límites (inicio y tamaño) del resultado
     * @return lista con los países 
     * @throws Exception
     */
    public List<Country> getCountries( @Param("q") String q, RowBounds rb) throws Exception;
    
    /**
     * Devuelve el número total de países registrados en el sistema.
     * @param q filtro por nombre del país
     * @return número total de países
     * @throws Exception
     */
    public int getTotalCountries( @Param("q") String q) throws Exception;
    
    /**
     * Añade una nueva ciudad en la base de datos
     * @param country país con los datos a añadir
     * @return el número de elementos insertados
     * @throws Exception
     */
    public int createCountry( @Param("country") Country country) throws Exception;
    
    /**
     * Obtiene un páis en concreto de la base de datos
     * @param id identificador del país a obtener
     * @return el país representado por id o null en caso de no existir
     * @throws Exception
     */
    public Country getCountry( @Param( "id") Long id) throws Exception;
    
    /**
     * Actualiza los datos de un país en concreto
     * @param country país con los datos a modificar
     * @return número de elementos actualizados
     * @throws Exception
     */
    public int updateCountry( @Param("country") Country country) throws Exception;
    
    /**
     * Elimina un país en concreto de la base de datos
     * @param id identificador del país a eliminar
     * @return número de elementos eliminados
     * @throws Exception
     */
    public int deleteCountry( @Param( "id") Long id) throws Exception;
}
