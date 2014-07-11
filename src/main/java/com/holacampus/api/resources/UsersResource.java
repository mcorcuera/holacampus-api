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

package com.holacampus.api.resources;

import com.holacampus.api.domain.Credentials;
import com.holacampus.api.domain.Permission;
import com.holacampus.api.domain.User;
import com.holacampus.api.exceptions.HTTPErrorException;
import com.holacampus.api.hal.HalList;
import com.holacampus.api.mappers.CredentialsMapper;
import com.holacampus.api.mappers.UserMapper;
import com.holacampus.api.security.AuthenticationRequired;
import com.holacampus.api.security.AuthenticationScheme;
import com.holacampus.api.security.PasswordHash;
import com.holacampus.api.security.UserPrincipal;
import com.holacampus.api.utils.HALBuilderUtils;
import com.holacampus.api.utils.MyBatisConnectionFactory;
import com.holacampus.api.utils.Utils;
import com.holacampus.api.validators.CreationValid;
import com.theoryinpractise.halbuilder.api.RepresentationFactory;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;
import java.util.Objects;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import org.apache.ibatis.session.RowBounds;
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
    
    @Context
    private UriInfo uriInfo;
    
    @GET
    @AuthenticationRequired( AuthenticationScheme.AUTHENTICATION_SCHEME_TOKEN)
    @Produces( { RepresentationFactory.HAL_JSON})
    public HalList<User> getUsers( @Context SecurityContext sc, 
            @QueryParam("page") Integer page, @QueryParam( "size") Integer size, @QueryParam( "q") String q) throws UnsupportedEncodingException
    {
        logger.info( "[GET] " + uriInfo.getPath());
        
        page = Utils.getValidPage(page);
        size = Utils.getValidSize(size);
        if( q != null)
            q   = URLDecoder.decode(q, "UTF-8");
        
        RowBounds rb = Utils.createRowBounds(page, size);
        
        UserPrincipal up = (UserPrincipal) sc.getUserPrincipal();
        
        SqlSession session          = MyBatisConnectionFactory.getSession().openSession();
        RepresentationFactory rf    = HALBuilderUtils.getRepresentationFactory();
        HalList<User> users;
        
        try {            
            UserMapper userMapper   = session.getMapper( UserMapper.class);
            List<User> usersList    = userMapper.getAllUsers( q, rb);
            int total               = userMapper.getTotalUsers(q);
            session.commit();
            
            for( User user : usersList) {
                Permission permissions = new Permission();
                userMapper.getPermissions( up.getId(), user.getId(), permissions);
                user.setPermission(permissions);
            }
            
            users = new HalList( usersList, total);
            
            users.setResourceRelativePath("/users");
            users.setPage(page);
            users.setSize(size);
            users.setQuery(q);
            
        }catch( Exception e) {
            logger.error( e.toString());
            throw new InternalServerErrorException();
        }
        finally {
            session.close();
        }
        
        return users;
    }
    
    @POST
    @AuthenticationRequired( AuthenticationScheme.AUTHENTICATION_SCHEME_NONE)
    @Consumes( { RepresentationFactory.HAL_JSON, MediaType.APPLICATION_JSON})
    @Produces( { RepresentationFactory.HAL_JSON})
    public User createUser( @CreationValid @Valid User user)
    {
        logger.info("[POST] /users");
                    
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
            
            credMapper.storeCredentials(user, credentials);
            
            user = userMapper.getUser( user.getId());
            user.setPermission( new Permission( Permission.LEVEL_OWNER));
            
            session.commit(); 
            
        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            logger.error(ex);
        }catch( HTTPErrorException e) {
            throw e;
        } catch( Exception ex) {
            logger.error(ex);
            throw new HTTPErrorException( Status.CONFLICT, "Email already exists on database");
        } finally {
            session.close();
        }
        
        return user;
    }
}
