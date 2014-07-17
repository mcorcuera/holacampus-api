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
import com.holacampus.api.domain.University;
import com.holacampus.api.domain.User;
import com.holacampus.api.exceptions.HTTPErrorException;
import com.holacampus.api.hal.HalList;
import com.holacampus.api.mappers.CredentialsMapper;
import com.holacampus.api.mappers.UniversityMapper;
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
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.Encoded;
import javax.ws.rs.GET;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 *
 * @author Mikel Corcuera <mik.corcuera@gmail.com>
 */

@Path("/universities")
public class UniversitiesResource {
    
    private static final Logger logger = LogManager.getLogger( UsersResource.class.getName());

    @Context
    private UriInfo uriInfo;
    
    @GET
    @AuthenticationRequired( AuthenticationScheme.AUTHENTICATION_SCHEME_TOKEN)
    @Produces( { RepresentationFactory.HAL_JSON})
    @Encoded
    public HalList<University> getUniversities( @Context SecurityContext sc, 
            @QueryParam("page") Integer page, @QueryParam( "size") Integer size, @QueryParam( "q") String q) throws UnsupportedEncodingException
    {
        page = Utils.getValidPage(page);
        size = Utils.getValidSize(size);
        if( q != null) {
            q   = URLDecoder.decode(q, "UTF-8");
        }
        RowBounds rb = Utils.createRowBounds(page, size);
        
        UserPrincipal up = (UserPrincipal) sc.getUserPrincipal();
        
        SqlSession session          = MyBatisConnectionFactory.getSession().openSession();
        HalList<University> universities;
        
        try {            
            UniversityMapper uniMapper  = session.getMapper( UniversityMapper.class);
            List<University> uniList    = uniMapper.getAllUniversities(q, rb);
            int total                   = uniMapper.getTotalUniversities(q);
            session.commit();
            
            for( University university : uniList) {
                Permission permissions = new Permission();
                uniMapper.getPermissions( up.getId(), university.getId(), permissions);
                university.setPermission(permissions);
            }
            
            universities = new HalList( uniList, total);
            
            universities.setResourceRelativePath( uriInfo.getPath());
            universities.setPage(page);
            universities.setSize(size);
            universities.setQuery(q);
            
        }catch( Exception e) {
            logger.error( e.toString());
            throw new InternalServerErrorException();
        }
        finally {
            session.close();
        }
        
        return universities;
    }
    
    @POST
    @AuthenticationRequired( AuthenticationScheme.AUTHENTICATION_SCHEME_NONE)
    @Consumes( { RepresentationFactory.HAL_JSON, MediaType.APPLICATION_JSON})
    @Produces( { RepresentationFactory.HAL_JSON})
    public University createUniversity( @CreationValid @Valid University university)
    {
        logger.info("[POST] /universities");
                    
        SqlSession session = MyBatisConnectionFactory.getSession().openSession();
        
        try {
            UniversityMapper    uniMapper   = session.getMapper( UniversityMapper.class);
            CredentialsMapper   credMapper  = session.getMapper( CredentialsMapper.class);
            /*
            * Insert the user in database
            */
            uniMapper.createUniversity(university);
            
            /*
            * Compute hash from password. Then store hashed password and salt into credentials table
            */
            Credentials credentials = new Credentials();
            String hashedPassword = PasswordHash.createHash( university.getPassword(), credentials);
            logger.info( hashedPassword);
            
            credMapper.storeCredentials( university, credentials);
            
            university = uniMapper.getUniversity( university.getId());
            university.setPermission( new Permission( Permission.LEVEL_OWNER));
            
            session.commit(); 
            
        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            logger.error(ex);
        }catch( HTTPErrorException e) {
            throw e;
        } catch( Exception ex) {
            logger.error(ex);
            throw new HTTPErrorException( Response.Status.CONFLICT, "Email already exists on database");
        } finally {
            session.close();
        }
        
        return university;
    }
}
