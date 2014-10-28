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
import org.apache.ibatis.annotations.Param;

/**
 * Mapper para la clase {@link ProflePhotoContainer}
 * @author Mikel Corcuera <mik.corcuera@gmail.com>
 */
public interface ProfilePhotoContainerMapper {
    
    /**
     * Establece una foto como foto de portada del {@link ProflePhotoContainer}
     * @param photoId identificador de la {@link Photo} a establecer como 
     *          foto de portada
     * @param profilePhotoId identificador del {@link ProflePhotoContainer}
     * @return número de elementos modificados
     * @throws Exception
     */
    public int setProfilePhoto( @Param("photoId") Long photoId, @Param("profilePhotoId") Long profilePhotoId) throws Exception;

    /**
     * Elimina la foto de portada del {@link ProflePhotoContainer}
     * @param profilePhotoId identificador del {@link ProflePhotoContainer}
     * @return número de elementos modificados
     * @throws Exception
     */
    public int deleteProfilePhoto(@Param("profilePhotoId") Long profilePhotoId) throws Exception;
    
}
