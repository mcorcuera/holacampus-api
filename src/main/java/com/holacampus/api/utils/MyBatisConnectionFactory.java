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

import java.io.IOException;
import java.io.Reader;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;


/**
 *
 *  @author Mikel Corcuera <mik.corcuera@gmail.com>  
 */
public class MyBatisConnectionFactory {
    
    private static SqlSessionFactory    sqlMapper;
    private static Reader               reader;
    
    static{
        try{
            reader      = Resources.getResourceAsReader( "mybatis.xml");
            sqlMapper   = new SqlSessionFactoryBuilder().build( reader);
        }catch( IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     *
     * @return
     */
    public static SqlSessionFactory getSession() {
       return sqlMapper; 
    }
}
