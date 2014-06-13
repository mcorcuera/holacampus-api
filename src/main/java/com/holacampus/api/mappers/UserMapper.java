/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
