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

import com.holacampus.api.domain.Activity;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

/**
 * Mapper para la clase {@link Activity}
 * @author Mikel Corcuera <mik.corcuera@gmail.com>
 */
public interface ActivityMapper {
    
    /**
     * Obtiene la actividad reciente para un usuario o universidad determinado
     * @param elementId Identificador del usuario o universidad
     * @return Lista con la actividad reciente del usuario
     * @throws Exception
     */
    public List<Activity> getActivity( @Param( "elementId") Long elementId) throws Exception;

    /**
      Obtiene la actividad reciente para un usuario o universidad determinado
     * @param elementId Identificador del usuario o universidad
     * @param rb Limites (inicio y tamaño) del resultado
     * @return Lista con la actividad reciente del usuario.
     * @throws Exception
     */
    public List<Activity> getActivity( @Param( "elementId") Long elementId, RowBounds rb) throws Exception;

    /**
     * Obtiene la cantidad total de actividad reciente de un usuario o universidad
     * @param elementId Identificador del usuario o universidad
     * @return Cantidad de actividad reciente
     * @throws Exception
     */
    public int getTotalActivity( @Param( "elementId") Long elementId) throws Exception;
    
}
