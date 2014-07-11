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

package com.holacampus.api.utils;

import java.util.Map;
import java.util.Map.Entry;
import org.apache.ibatis.session.RowBounds;

/**
 *
 *  @author Mikel Corcuera <mik.corcuera@gmail.com>  
 */
public class Utils {
    
    /**
     *
     */
    public static final String BASE_URL             = "http://localhost:8080/api";
    public static final Integer DEFAULT_PAGE        = 0;
    public static final Integer DEFAULT_SIZE        = 15;
    public static final Integer MAX_SIZE            = 40;
    /**
     *
     * @param path
     * @return
     */
    public static String createLink( String path, Map<String,String> queryParams)
    {
        if( path == null)
            return null;
        StringBuilder s = new StringBuilder( BASE_URL);
        path = path.replace("//", "/");
        if( !path.startsWith("/"))
            s.append("/");
        s.append(path);
        if( path.endsWith("/"))
            s.setLength( s.length() - 1);
        if( queryParams != null && queryParams.size() > 0) {
            s.append("/?");
            
            for( Entry<String,String> e : queryParams.entrySet()) {
                s.append( e.getKey());
                s.append("=");
                s.append(e.getValue().replace(' ', '+'));
                s.append("&");
            }
            s.setLength( s.length() - 1);
        }
        
        return s.toString();
    }
    
    public static String getRelativePath( String absolute)
    {
        return absolute.replace( BASE_URL, "");
    }
    
    public static int getValidPage( Integer page)
    {
        return ( page == null || page < 0) ? DEFAULT_PAGE : page;
    }
    
    public static int getValidSize( Integer size)
    {
        return (size == null || size < 0) ? DEFAULT_SIZE : ( size > MAX_SIZE ? MAX_SIZE : size);
    }
    
    public static RowBounds createRowBounds( int page, int size)
    {
        RowBounds rb = new RowBounds( page*size, size);
        
        return rb;
    }
}
