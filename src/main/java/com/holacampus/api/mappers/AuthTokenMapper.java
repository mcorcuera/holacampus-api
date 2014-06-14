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

import com.holacampus.api.beans.AuthToken;
import org.apache.ibatis.annotations.Param;

/**
 *
 * @author Mikel Corcuera <mik.corcuera@gmail.com>
 */
public interface AuthTokenMapper {
    
    
    public int storeAuthTokenForUserId( @Param("auth") AuthToken auth, @Param( "id") Long id) throws Exception;
    
    public AuthToken getAuthToken( String id) throws Exception;
    
    public AuthToken getAuthTokenAndCredentials( String id) throws Exception;
    
    public int deleteAuthToken( String id) throws Exception;
    
    public int deleteAllUserTokens( Long id) throws Exception;
}
