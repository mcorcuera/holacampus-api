package com.holacampus.api.mappers;

import com.holacampus.api.domain.*;
import org.apache.ibatis.annotations.Param;

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

/**
 *
 * Mapper para la clase {@link PhotoContainer}
 *  @author Mikel Corcuera <mik.corcuera@gmail.com>  
 */

import com.holacampus.api.domain.*;

public interface PhotoContainerMapper {
    
      /**
     * Obtiene los permisos de un usuario sobre un contenedor de fotos en concreto
     * @param userId Identificador del usuario o universidad ({@link ActiveElement})
     * @param containerId Identificador del contenedor
     * @param permission Permisos que tiene el usuario sobre el elemento
     * @throws Exception
     */
    public void getPermissions(  @Param("userId") Long userId, @Param("containerId") Long containerId, @Param("permission") Permission permission) throws Exception;

}
