/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
