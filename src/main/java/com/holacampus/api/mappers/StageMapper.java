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
import com.holacampus.api.domain.User;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

/**
 * Mapper para la clase {@link Stage}
 * @author Mikel Corcuera <mik.corcuera@gmail.com>
 */
public interface StageMapper {
    
    /**
     * Obtiene una lista con las etapas en la trayectoria académica de un usuario 
     * ({@link User})
     * @param user usuario sobre el que obtener los resultados
     * @return lista de las etapas
     * @throws Exception
     */
    public List<Stage>      getStages( @Param("user") User user) throws Exception;
    
    
   /**
     * Obtiene una lista con las etapas en la trayectoria académica de un usuario 
     * ({@link User})
     * @param user usuario sobre el que obtener los resultados
     * @param rb límites (inicio y tamaño) del resultado
     * @return lista de las etapas
     * @throws Exception
     */
    public List<Stage>      getStages( @Param("user") User user, RowBounds rb) throws Exception;
    
    
    /**
     * Número total de etapas en la trayectoría académica de un usuario
     * @param user usuario sobre el que obtener los resultados
     * @return número total de etapas
     * @throws Exception
     */
    public int              getTotalStages( @Param("user") User user) throws Exception;
    
    /**
     * Añade una nueva etapa a la trayectoria académica del usuario
     * @param user usuario en el que añadir la etapa
     * @param stage datos de la etapa académica
     * @return número total de elementos insertados
     * @throws Exception
     */
    public int              createStage( @Param("user") User user, @Param("stage") Stage stage) throws Exception;
    
    /**
     * Obtiene una etapa en concreto de la trayectoria académica del usuario
     * @param user usuario sobre el que obtener los resultados
     * @param stageId identificador de la etapa ({@link Stage})
     * @return la etapa, si exsite, null, en caso contrario
     * @throws Exception
     */
    public Stage            getStage( @Param("user") User user, @Param( "stageId") Long stageId) throws Exception;
    
    /**
     * Actualiza los datos de una etapa en concreto de la trayectoria académica del usuario
     * @param user usuario sobre el que realizar las modificaciones
     * @param stage etapa con los datos modificados
     * @return número de elementos modificados
     * @throws Exception
     */
    public int              updateStage( @Param("user") User user, @Param("stage") Stage stage) throws Exception;
    
    /**
     * Elimina una etapa en concreto dentro de la trayectoria académica de un usuario
     * @param user usuario del que eliminar la etapa
     * @param stageId identificador de la etapa ({@link Stage})
     * @return número de elementos eliminados
     * @throws Exception
     */
    public int              deleteStage( @Param("user") User user, @Param( "stageId") Long stageId) throws Exception;
}
