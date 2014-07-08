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

import com.holacampus.api.domain.CommentContainer;
import com.holacampus.api.domain.Permission;
import com.holacampus.api.domain.Photo;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

/**
 *
 * @author Mikel Corcuera <mik.corcuera@gmail.com>
 */
public interface PhotoMapper {
    
     public int                 createPhoto( @Param("photo") Photo photo) throws Exception; 
     
     public List<Photo>         getPhotos( @Param("containerId") Long id) throws Exception;
     
     public List<Photo>         getPhotos( @Param("containerId") Long id, RowBounds rb) throws Exception;
     
     public int                 getTotalPhotos( @Param("containerId") Long id) throws Exception;
     
     public CommentContainer    getCommentContainer( @Param("id") Long id) throws Exception;
     
     public Photo               getPhoto( @Param("photoId") Long photoId) throws Exception;
     
     public void                getPermissions(  @Param("userId") Long userId, @Param("photoId") Long photoId, @Param("permission") Permission permission) throws Exception;
     
     public int                 deletePhoto( @Param( "id") Long id) throws Exception;
     
     public int                 updatePhoto( @Param("photo") Photo photo) throws Exception;
}
