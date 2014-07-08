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

import com.holacampus.api.domain.Friendship;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

/**
 *
 * @author Mikel Corcuera <mik.corcuera@gmail.com>
 */
public interface FriendshipMapper {
    
    public List<Friendship> getFriends( @Param("userId") Long userId, @Param("q") String q, @Param("unconfirmed") boolean confirmed, @Param("pending") boolean pending) throws Exception;
    
    public List<Friendship> getFriends( @Param("userId") Long userId, @Param("q") String q, @Param("unconfirmed") boolean confirmed, @Param("pending") boolean pending, RowBounds rb) throws Exception;
    
    public int              getTotalFriends( @Param("userId") Long userId, @Param("q") String q, @Param("unconfirmed") boolean confirmed, @Param("pending") boolean pending) throws Exception;

    public Friendship       createFriendship( @Param("userId") Long userId, @Param("friendId") Long friendId) throws Exception;
    
    public Friendship       getFriend( @Param("userId") Long userId, @Param("friendId") Long friendId) throws Exception;
    
    public Friendship       updateFriend( @Param("userId") Long userId, @Param("friend") Friendship friend) throws Exception;
    
    public Friendship       deleteFriend( @Param("userId") Long userId, @Param("friendId") Long friendId) throws Exception;
}