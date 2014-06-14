/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.holacampus.api.resources;

import com.holacampus.api.beans.Credentials;
import com.holacampus.api.beans.User;
import com.holacampus.api.exceptions.ConflictException;
import com.holacampus.api.exceptions.HTTPErrorException;
import com.holacampus.api.hal.builders.UserHALBuilder;
import com.holacampus.api.hal.contracts.UserContract.UserCreateContract;
import com.holacampus.api.mappers.CredentialsMapper;
import com.holacampus.api.mappers.UserMapper;
import com.holacampus.api.security.AuthenticationRequired;
import com.holacampus.api.security.AuthenticationScheme;
import com.holacampus.api.security.PasswordHash;
import com.holacampus.api.utils.HALBuilderUtils;
import com.holacampus.api.utils.MyBatisConnectionFactory;
import com.holacampus.api.utils.Utils;
import com.theoryinpractise.halbuilder.api.ReadableRepresentation;
import com.theoryinpractise.halbuilder.api.Representation;
import com.theoryinpractise.halbuilder.api.RepresentationFactory;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;
import javax.ws.rs.*;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.SecurityContext;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 *
 *  @author Mikel Corcuera <mik.corcuera@gmail.com>  
 */
@Path("/users")
public class UsersResource {
    
    private static final Logger logger = LogManager.getLogger( UsersResource.class.getName());
    
    /**
     *
     * @param sc
     * @return
     */    
    
    @GET
    @AuthenticationRequired( scheme=AuthenticationScheme.AUTHENTICATION_SCHEME_TOKEN)
    @Produces( { RepresentationFactory.HAL_JSON})
    public ReadableRepresentation getUsers( @Context SecurityContext sc)
    {
        logger.info( "[GET] /users");
        logger.info( "Accessed by" + sc.getUserPrincipal().getName());
        SqlSession session          = MyBatisConnectionFactory.getSession().openSession();
        RepresentationFactory rf    = HALBuilderUtils.getRepresentationFactory();
        Representation usersRep     = null;
        
        try {            
            UserMapper userMapper   = session.getMapper( UserMapper.class);
            List<User> users        = userMapper.getAllUsers( );
            session.commit();
            
            usersRep = rf.newRepresentation( Utils.createLink("/users"));
            HALBuilderUtils.addList(usersRep, users, "users");
        }catch( Exception e) {
            logger.error( e.toString());
            throw new InternalServerErrorException();
        }
        finally {
            session.close();
        }
        
        return usersRep;
    }
    
    /**
     *
     * @param input
     * @return
     */
    @POST
    @AuthenticationRequired( scheme=AuthenticationScheme.AUTHENTICATION_SCHEME_NONE)
    @Consumes( { RepresentationFactory.HAL_JSON, MediaType.APPLICATION_JSON})
    @Produces( { RepresentationFactory.HAL_JSON})
    public ReadableRepresentation createUser( ReadableRepresentation input)
    {
        logger.info("[POST] /users");
        
        RepresentationFactory r = HALBuilderUtils.getRepresentationFactory();
         
        /*
        * Check if required fields are present. If not throws a HTTP 409 (BadRequest) Error  
        */
        if( !input.isSatisfiedBy( new UserCreateContract())) {
            throw new HTTPErrorException( Status.BAD_REQUEST, "Invalid content for creating user");
        }
        
        User user = UserHALBuilder.buildUser(input);
       
        user.setUserType( User.TYPE_STUDENT);
        
        SqlSession session = MyBatisConnectionFactory.getSession().openSession();
        
        try {
            UserMapper          userMapper    = session.getMapper( UserMapper.class);
            CredentialsMapper   credMapper    = session.getMapper( CredentialsMapper.class);
            /*
            * Insert the user in database
            */
            userMapper.createUser(user);
            
            /*
            * Compute hash from password. Then store hashed password and salt into credentials table
            */
            Credentials credentials = new Credentials();
            String hashedPassword = PasswordHash.createHash( user.getPassword(), credentials);
            logger.info( hashedPassword);
            
            credMapper.storeCredentialsForUser(user, credentials);
            
            session.commit(); 
            
        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            logger.error(ex);
        } catch( Exception ex) {
            logger.error(ex);
            throw new HTTPErrorException( Status.CONFLICT, "Email already exists on database");
        } finally {
            session.close();
        }
        return HALBuilderUtils.fromRepresentable(user);
    }
}
