/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.holacampus.api.resources;

import com.holacampus.api.beans.User;
import com.holacampus.api.exceptions.HTTPErrorException;
import com.holacampus.api.mappers.UserMapper;
import com.holacampus.api.utils.HALBuilderUtils;
import com.holacampus.api.utils.MyBatisConnectionFactory;
import com.theoryinpractise.halbuilder.api.ReadableRepresentation;
import com.theoryinpractise.halbuilder.api.RepresentationFactory;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 *
 *  @author Mikel Corcuera <mik.corcuera@gmail.com>  
 */

@Path( "/users/{id}")
public class UserResource {
    
    private static final Logger logger = LogManager.getLogger( UserResource.class.getName());
    
    /**
     *
     * @param id 
     * @return
     */
    @GET
    @Produces( { RepresentationFactory.HAL_JSON})
    public ReadableRepresentation getUser( @PathParam("id") long id)
    {
        logger.info( "[GET] /users/\n" + id);
        
        User user;
        SqlSession session = MyBatisConnectionFactory.getSession().openSession();
        
        try {            
            UserMapper userMapper   = session.getMapper( UserMapper.class);
            user = userMapper.getUser(id);
            session.commit();            
            
            if( user == null) {
                throw new HTTPErrorException( Status.NOT_FOUND, "User not found");
            }
            
        } catch( Exception e) {
            logger.error( e.toString());
            throw new HTTPErrorException( Status.NOT_FOUND, "User not found");
        } finally {
            session.close();
        }   
        return HALBuilderUtils.fromRepresentable(user);
    }
}
