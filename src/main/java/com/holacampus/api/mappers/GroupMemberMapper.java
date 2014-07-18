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

import com.holacampus.api.domain.ActiveElement;
import com.holacampus.api.domain.GroupEvent;
import com.holacampus.api.domain.GroupMember;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

/**
 *
 * @author Mikel Corcuera <mik.corcuera@gmail.com>
 */
public interface GroupMemberMapper {
    
    
    public List<GroupMember> getMembers( @Param("parent") GroupEvent parent, @Param("q") String q, RowBounds rb) throws Exception;
    
    public List<GroupMember> getMembers( @Param("parent") GroupEvent parent, @Param("q") String q) throws Exception;
    
    public int getTotalMembers( @Param("parent") GroupEvent parent, @Param("q") String q) throws Exception;
    
    public GroupMember getMember( @Param("parent") GroupEvent parent, @Param("memberId") Long memberId) throws Exception;
    
    public int addMember( @Param("parent") GroupEvent parent, @Param("activeElement") ActiveElement activeElement ) throws Exception;
    
    public int deleteMember( @Param("parent") GroupEvent parent, @Param("memberId") Long memberId) throws Exception;
    
}
