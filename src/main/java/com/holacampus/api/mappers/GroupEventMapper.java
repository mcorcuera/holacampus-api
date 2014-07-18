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
import com.holacampus.api.domain.GroupEvent;
import com.holacampus.api.domain.Permission;
import com.holacampus.api.domain.PhotoContainer;
import com.holacampus.api.domain.ProfilePhotoContainer;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

/**
 *
 * @author Mikel Corcuera <mik.corcuera@gmail.com>
 */
public interface GroupEventMapper {
    
    
    public List<GroupEvent> getGroupsEvents( @Param("type") String type, @Param("q") String q, RowBounds rb) throws Exception;
    
    public List<GroupEvent> getGroupsEvents( @Param("type") String type, @Param("q") String q) throws Exception;
    
    public int getTotalGroupsEvents(@Param("type") String type, @Param("q") String q) throws Exception;
    
    public GroupEvent getGroupEvent( @Param("type") String type, @Param( "id") Long id) throws Exception;
    
    public CommentContainer         getCommentContainer( @Param("id") long id) throws Exception;
    
    public PhotoContainer           getPhotoContainer( @Param("id") long id) throws Exception;
    
    public ProfilePhotoContainer    getProfilePhotoContainer( @Param("id") long id) throws Exception;
    
    public void                  getPermissions(  @Param("userId") Long userId, @Param("groupId") Long groupId, @Param("permission") Permission permission) throws Exception;

    
    public void createGroupEvent( @Param( "creatorId") Long creatorId, @Param("group") GroupEvent group) throws Exception;
    
    
}
