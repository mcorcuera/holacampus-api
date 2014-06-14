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

import com.holacampus.api.beans.User;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

/**
 *
 *  @author Mikel Corcuera <mik.corcuera@gmail.com>  
 */
public interface UserMapper {
    
    /**
     *
     * @param rb
     * @return
     * @throws Exception
     */
    public List<User> getAllUsers( RowBounds rb) throws Exception;
    
    /**
     *
     * @return
     * @throws Exception
     */
    public List<User> getAllUsers() throws Exception;
    
    /**
     *
     * @param user
     * @return
     * @throws Exception
     */
    public int createUser( @Param("user")User user) throws Exception;
    
    /**
     *
     * @param id
     * @return
     * @throws Exception
     */
    public User getUser( @Param("id") long id) throws Exception;
}
