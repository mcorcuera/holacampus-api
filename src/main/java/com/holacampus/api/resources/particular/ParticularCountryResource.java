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

package com.holacampus.api.resources.particular;

import com.holacampus.api.domain.Country;
import com.holacampus.api.exceptions.HTTPErrorException;
import com.holacampus.api.mappers.CountryMapper;
import com.holacampus.api.resources.CountriesResource;
import com.holacampus.api.security.AuthenticationRequired;
import com.holacampus.api.security.AuthenticationScheme;
import com.holacampus.api.subresources.CityResource;
import com.holacampus.api.utils.MyBatisConnectionFactory;
import com.theoryinpractise.halbuilder.api.RepresentationFactory;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * Esta clase se encarga de gestionar las peticiones de la API a un recurso
 * Country particualr. Es decir, gestiona las peticiones a la URL 
 * <code>/countries/{id}</code> donde <code>id</code> es el identificador
 * del país.
 * @author Mikel Corcuera <mik.corcuera@gmail.com>
 */

@Path( "/countries/{id}")
public class ParticularCountryResource {
    
    private static final Logger logger = LogManager.getLogger( ParticularCountryResource.class.getName());

    @Context
    private UriInfo uriInfo;
    
    /**
     * Esta función devuelve la clase que se encargará de gestionar las peticiones
     * relacionadas con las ciudades del país. Esto es, a la URL 
     * <code>/countries/{id}/cities</code>
     * @param id identificador del país
     * @return recurso que se encarga de gestionar la petición
     */
    @Path("/cities")
    public CityResource getCityResource( @PathParam("id") Long id) {
        Country country = null;
        SqlSession session = MyBatisConnectionFactory.getSession().openSession();

        try {
            CountryMapper mapper    = session.getMapper( CountryMapper.class);
            country                 = mapper.getCountry(id);
            
            if( country == null) {
                throw new HTTPErrorException( Status.NOT_FOUND, "country " + id + " not found");
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
        
        return new CityResource( country);
    }
    
    /**
     * Esta función gestiona las peticiones GET al recurso 
     * <code>/countries/{id}</code>. Esta operación devuelve la representación
     * del país identificada por <b>id</b>
     * @param id identificador del país
     * @return representación del país
     */
    @GET
    @AuthenticationRequired( AuthenticationScheme.AUTHENTICATION_SCHEME_TOKEN)
    @Produces( { RepresentationFactory.HAL_JSON})
    public Country getCountry( @PathParam( "id") Long id) {
        Country country = null;
        SqlSession session = MyBatisConnectionFactory.getSession().openSession();

        try {
            CountryMapper mapper    = session.getMapper( CountryMapper.class);
            country                 = mapper.getCountry(id);
            
            if( country == null) {
                throw new HTTPErrorException( Status.NOT_FOUND, "country " + id + " not found");
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
        return country;
    }
    
    /**
     * Esta función gestiona las peticiones PUT al recurso 
     * <code>/countries/{id}</code>. Esta operación modifica los datos del país
     * identificada por <b>id</b>
     * @param country datos a modificar del país
     * @param id identificador del país a modificar
     * @return representación del país ya modificado
     */
    @PUT
    @AuthenticationRequired( AuthenticationScheme.AUTHENTICATION_SCHEME_TOKEN)
    @Produces( { RepresentationFactory.HAL_JSON})
    @Consumes( { RepresentationFactory.HAL_JSON, MediaType.APPLICATION_JSON})
    public Country updateCountry( @Valid Country country, @PathParam( "id") Long id) {
        
        logger.info( "[PUT] " + uriInfo.getPath());
        country.setId(id);
        SqlSession session = MyBatisConnectionFactory.getSession().openSession();

        try {
            CountryMapper mapper    = session.getMapper( CountryMapper.class);
            int result              = mapper.updateCountry(country);
            
            if( result == 0) {
                throw new HTTPErrorException( Status.INTERNAL_SERVER_ERROR, "Error updating country");
            }
            country = mapper.getCountry( country.getId());
            session.commit();
         }catch( HTTPErrorException e) {
            throw e;
        }catch( Exception e) {
            logger.info( e);
            throw new HTTPErrorException( Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }finally {
            session.close();
        }
        return country;
    }
    
    /**
      *Esta función gestiona las peticiones DELTE al recurso 
     * <code>/countries/{id}</code>. Esta operación elimina el país
     * representado por <b>id</b>
     * @param id identificador del país
     */
    @DELETE
    @AuthenticationRequired( AuthenticationScheme.AUTHENTICATION_SCHEME_TOKEN)
    public void deleteCountry( @PathParam( "id") Long id) {
        
        logger.info( "[DELETE] " + uriInfo.getPath());
        SqlSession session = MyBatisConnectionFactory.getSession().openSession();

        try {
            CountryMapper mapper    = session.getMapper( CountryMapper.class);
            Country country         = mapper.getCountry(id);
            
            if( country == null) {
                throw new HTTPErrorException( Status.NOT_FOUND, "Country " + id + " not found");
            }
            
            int result              = mapper.deleteCountry(country.getId());
            
            if( result == 0) {
                throw new HTTPErrorException( Status.INTERNAL_SERVER_ERROR, "Error deleting country");
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
