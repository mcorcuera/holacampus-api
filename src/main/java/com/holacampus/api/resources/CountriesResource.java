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

import com.holacampus.api.domain.Country;
import com.holacampus.api.exceptions.HTTPErrorException;
import com.holacampus.api.hal.HalList;
import com.holacampus.api.mappers.CountryMapper;
import com.holacampus.api.security.AuthenticationRequired;
import com.holacampus.api.security.AuthenticationScheme;
import com.holacampus.api.utils.MyBatisConnectionFactory;
import com.holacampus.api.utils.Utils;
import com.holacampus.api.validators.CreationNeeded;
import com.holacampus.api.validators.CreationValid;
import com.theoryinpractise.halbuilder.api.RepresentationFactory;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.Encoded;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
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
 * Esta clase se encarga de gestionar las peticiones de la API al recurso
 * Countries. Es decir, gestiona las peticiones a la URL 
 * <code>/countries</code>.
 * <br/><br/>
 * Este recurso representa a los paises registrados en la red social
 * @author Mikel Corcuera <mik.corcuera@gmail.com>
 */

@Path( "/countries")
public class CountriesResource {
    
    private static final Logger logger = LogManager.getLogger( CountriesResource.class.getName());

    @Context
    private UriInfo uriInfo;
      
    /**
     * Esta función gestiona las peticiones GET al recurso 
     * <code>/countries</code>. Esta operación devuelve un lista con los
     * países de la red social
     * @param page página de los resultados
     * @param size tamaño de los resultados
     * @param q cadena de caracteres que sirve para filtrar por nombre los
     * resultados
     * @return lista con los países del tamaño especificado y filtrada.
     * @throws UnsupportedEncodingException
     */
    @GET
    @AuthenticationRequired( AuthenticationScheme.AUTHENTICATION_SCHEME_TOKEN)
    @Produces( { RepresentationFactory.HAL_JSON})
    @Encoded
    public HalList<Country> getCountries( @QueryParam("page") Integer page, 
            @QueryParam( "size") Integer size, @QueryParam( "q") String q)  throws UnsupportedEncodingException
    {
        logger.info( "[GET] " + uriInfo.getPath());
        
        HalList<Country> countries;
        
        page = Utils.getValidPage(page);
        size = Utils.getValidSize(size);
        if( q != null) {
            q   = URLDecoder.decode(q, "UTF-8");
        }
        RowBounds rb = Utils.createRowBounds(page, size);
        
        SqlSession session = MyBatisConnectionFactory.getSession().openSession();

        try {
            CountryMapper mapper        = session.getMapper( CountryMapper.class);
            List<Country> countryList   = mapper.getCountries(q, rb);
            int total                   = mapper.getTotalCountries(q);
            
            session.commit();
            
            countries = new HalList<Country> ( countryList, total);
            countries.setResourceRelativePath("/countries");
            countries.setPage(page);
            countries.setSize(size);
            countries.setQuery(q);
            
         }catch( HTTPErrorException e) {
            throw e;
        }catch( Exception e) {
            logger.info( e);
            throw new HTTPErrorException( Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }finally {
            session.close();
        }

        return countries;
    }
    
    /**
     * Esta función gestiona las peticiones POST al recurso 
     * <code>/countries</code>. Esta operación crea un nuevo país en la 
     * base de datos
     * @param country datos del país a crear
     * @return representación del país recién creado
     */
    @POST
    @AuthenticationRequired( AuthenticationScheme.AUTHENTICATION_SCHEME_TOKEN)
    @Consumes( { RepresentationFactory.HAL_JSON, MediaType.APPLICATION_JSON})
    @Produces( { RepresentationFactory.HAL_JSON})
    public Country createCountry( @CreationValid @Valid Country country) {
        
        logger.info( "[GET] " + uriInfo.getPath());
        
        SqlSession session = MyBatisConnectionFactory.getSession().openSession();

        try {
            CountryMapper mapper    = session.getMapper( CountryMapper.class);
            int result              = mapper.createCountry(country);
            
            if( result == 0) {
                throw new HTTPErrorException( Status.CONFLICT, "could not create country");
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
    
}
