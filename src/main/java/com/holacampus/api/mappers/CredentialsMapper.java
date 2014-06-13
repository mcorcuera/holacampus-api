/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.holacampus.api.mappers;

import com.holacampus.api.beans.Credentials;
import com.holacampus.api.beans.User;
import org.apache.ibatis.annotations.Param;

/**
 *
 * @author Mikel Corcuera <mik.corcuera@gmail.com>
 */
public interface CredentialsMapper {
    
    
    public int storeCredentialsForUser( @Param("user") User user, @Param( "credentials") Credentials credentials) throws Exception;
    
    public Credentials getCredentialsForUserEmail( @Param("email") String email) throws Exception;
    
    public Credentials getCredentialsForUserId( @Param("id") Long id) throws Exception;
}
