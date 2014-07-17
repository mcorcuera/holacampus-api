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

package com.holacampus.api.subresources;

import com.holacampus.api.domain.City;
import com.holacampus.api.domain.Country;
import com.holacampus.api.exceptions.HTTPErrorException;
import com.holacampus.api.hal.HalList;
import com.holacampus.api.mappers.CityMapper;
import com.holacampus.api.mappers.CountryMapper;
import com.holacampus.api.security.AuthenticationRequired;
import com.holacampus.api.security.AuthenticationScheme;
import com.holacampus.api.utils.MyBatisConnectionFactory;
import com.holacampus.api.utils.Utils;
import com.holacampus.api.validators.CreationValid;
import com.theoryinpractise.halbuilder.api.RepresentationFactory;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.Encoded;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 *
 * @author Mikel Corcuera <mik.corcuera@gmail.com>
 */
public class CityResource {
    
    private static final Logger logger = LogManager.getLogger( CityResource.class.getName());

        
    private Country country;
    
    public CityResource( Country country)
    {
        this.country = country;
    }
    
    @GET
    @AuthenticationRequired( AuthenticationScheme.AUTHENTICATION_SCHEME_TOKEN)
    @Produces( { RepresentationFactory.HAL_JSON})
    @Encoded
    public HalList<City> getCities( @QueryParam("page") Integer page, 
            @QueryParam( "size") Integer size, @QueryParam( "q") String q, 
            @Context UriInfo uriInfo)  throws UnsupportedEncodingException
    {
        logger.info( "[GET] " + uriInfo.getPath());
        
        HalList<City> cities;
        
        page = Utils.getValidPage(page);
        size = Utils.getValidSize(size);
        if( q != null) {
            q   = URLDecoder.decode(q, "UTF-8");
        }
        RowBounds rb = Utils.createRowBounds(page, size);
        
        SqlSession session = MyBatisConnectionFactory.getSession().openSession();

        try {
            CityMapper mapper   = session.getMapper( CityMapper.class);
            List<City> cityList = mapper.getCities( country, q, rb);
            int total           = mapper.getTotalCities( country, q);
            session.commit();
            
            cities = new HalList<City> ( cityList, total);
            cities.setResourceRelativePath( uriInfo.getPath());
            cities.setPage(page);
            cities.setSize(size);
            cities.setQuery(q);
            
         }catch( HTTPErrorException e) {
            throw e;
        }catch( Exception e) {
            logger.info( e);
            throw new HTTPErrorException( Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }finally {
            session.close();
        }

        return cities;
    }
    
    @POST
    @AuthenticationRequired( AuthenticationScheme.AUTHENTICATION_SCHEME_NONE)
    @Consumes( { RepresentationFactory.HAL_JSON, MediaType.APPLICATION_JSON})
    @Produces( { RepresentationFactory.HAL_JSON})
    public City createCity( @CreationValid @Valid City city, @Context UriInfo uriInfo) {
        
        logger.info( "[POST] " + uriInfo.getPath());
        
        SqlSession session = MyBatisConnectionFactory.getSession().openSession();

        try {
            CityMapper mapper       = session.getMapper( CityMapper.class);
            int result              = mapper.createCity(country, city);
            
            if( result == 0) {
                throw new HTTPErrorException( Status.CONFLICT, "could not create city");
            }
            
            city = mapper.getCity(country, city.getId());
            
            session.commit();
         }catch( HTTPErrorException e) {
            throw e;
        }catch( Exception e) {
            logger.info( e);
            throw new HTTPErrorException( Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }finally {
            session.close();
        }
        return city;
    }
    
    /* Particular City */
    
    @Path( "{id}")
    @GET
    @AuthenticationRequired( AuthenticationScheme.AUTHENTICATION_SCHEME_TOKEN)
    @Produces( { RepresentationFactory.HAL_JSON})
    public City getCity( @PathParam( "id") Long id) {
        
        City city = null;
        SqlSession session = MyBatisConnectionFactory.getSession().openSession();

        try {
            CityMapper mapper   = session.getMapper( CityMapper.class);
            city                = mapper.getCity( country, id);
            
            if( city == null) {
                throw new HTTPErrorException( Status.NOT_FOUND, "city " + id + " not found");
            }
            
            session.commit();
         }catch( HTTPErrorException e) {
            throw e;
        }catch( Exception e) {
            logger.info( e);
            throw new HTTPErrorException( Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }finally {
            session.close();
        }
        return city;
    }
    
    @Path( "{id}")
    @PUT
    @AuthenticationRequired( AuthenticationScheme.AUTHENTICATION_SCHEME_TOKEN)
    @Produces( { RepresentationFactory.HAL_JSON})
    @Consumes( { RepresentationFactory.HAL_JSON, MediaType.APPLICATION_JSON})
    public City updateCity( @Valid City city, @PathParam( "id") Long id, @Context UriInfo uriInfo) {
        
        logger.info( "[PUT] " + uriInfo.getPath());
        city.setId(id);
        SqlSession session = MyBatisConnectionFactory.getSession().openSession();

        try {
            CityMapper mapper   = session.getMapper( CityMapper.class);
            int result          = mapper.updateCity(country, city);
            
            if( result == 0) {
                throw new HTTPErrorException( Status.INTERNAL_SERVER_ERROR, "Error updating city");
            }
            city = mapper.getCity(country, city.getId());
            session.commit();
         }catch( HTTPErrorException e) {
            throw e;
        }catch( Exception e) {
            logger.info( e);
            throw new HTTPErrorException( Status.CONFLICT, "city name duplicated");
        }finally {
            session.close();
        }
        return city;
    }
    
    @Path( "{id}")
    @DELETE
    @AuthenticationRequired( AuthenticationScheme.AUTHENTICATION_SCHEME_TOKEN)
    public void deleteCity( @PathParam( "id") Long id, @Context UriInfo uriInfo) {
        
        logger.info( "[DELETE] " + uriInfo.getPath());
        SqlSession session = MyBatisConnectionFactory.getSession().openSession();

        try {
            CityMapper mapper    = session.getMapper( CityMapper.class);
            City city         = mapper.getCity(country, id);
            
            if( city == null) {
                throw new HTTPErrorException( Status.NOT_FOUND, "City " + id + " not found");
            }
            
            int result              = mapper.deleteCity(country, city.getId());
            
            if( result == 0) {
                throw new HTTPErrorException( Status.INTERNAL_SERVER_ERROR, "Error deleting city");
            }
            
            session.commit();
         }catch( HTTPErrorException e) {
            throw e;
        }catch( Exception e) {
            logger.info( e);
            throw new HTTPErrorException( Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }finally {
            session.close();
        }
    }
}
