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

package com.theoryinpractise.halbuilder.jaxrs;

import com.holacampus.api.exceptions.HTTPErrorException;
import com.holacampus.api.utils.HALBuilderUtils;
import com.theoryinpractise.halbuilder.api.ReadableRepresentation;
import com.theoryinpractise.halbuilder.api.RepresentationException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 *
 * @author  
 */
@Provider
public class JaxRsHalBuilderSupport implements MessageBodyWriter, MessageBodyReader<ReadableRepresentation> {

    private static final MediaType HAL_JSON_TYPE = new MediaType("application", "hal+json");

    private static final MediaType HAL_XML_TYPE = new MediaType("application", "hal+xml");
    
    private static final MediaType JSON_TYPE = new MediaType( "application", "json");

    private static final Logger logger = LogManager.getLogger( JaxRsHalBuilderSupport.class.getName());

    /**
     *
     * @param aClass
     * @param type
     * @param annotations
     * @param mediaType
     * @return
     */
    @Override
    public boolean isWriteable(Class aClass, Type type, Annotation[] annotations, MediaType mediaType) {
        
        return ReadableRepresentation.class.isAssignableFrom(aClass) 
                && (mediaType.isCompatible(HAL_JSON_TYPE) || mediaType.isCompatible(HAL_XML_TYPE));
    }

    /**
     *
     * @param o
     * @param aClass
     * @param type
     * @param annotations
     * @param mediaType
     * @return
     */
    @Override
    public long getSize(Object o, Class aClass, Type type, Annotation[] annotations, MediaType mediaType) {
        ReadableRepresentation representation = (ReadableRepresentation) o;
        return representation.toString(mediaType.toString()).length();
    }

    /**
     *
     * @param o
     * @param aClass
     * @param type
     * @param annotations
     * @param mediaType
     * @param multivaluedMap
     * @param outputStream
     * @throws IOException
     * @throws WebApplicationException
     */
    @Override
    public void writeTo(Object o, Class aClass, Type type, Annotation[] annotations, MediaType mediaType, MultivaluedMap multivaluedMap, OutputStream outputStream) throws IOException, WebApplicationException {
        
        ReadableRepresentation representation = (ReadableRepresentation) o;
        representation.toString(mediaType.toString(), new OutputStreamWriter(outputStream));
    }

    /**
     *
     * @param aClass
     * @param type
     * @param antns
     * @param mediaType
     * @return
     */
    @Override
    public boolean isReadable(Class<?> aClass, Type type, Annotation[] antns, MediaType mediaType) {
        return ReadableRepresentation.class.isAssignableFrom(aClass) 
                && (mediaType.isCompatible(HAL_JSON_TYPE) || mediaType.isCompatible(HAL_XML_TYPE) || mediaType.isCompatible( JSON_TYPE));
    }

    /**
     *
     * @param type
     * @param type1
     * @param antns
     * @param mt
     * @param mm
     * @param in
     * @return
     * @throws IOException
     * @throws WebApplicationException
     */
    @Override
    public ReadableRepresentation readFrom(Class<ReadableRepresentation> type, Type type1, Annotation[] antns, MediaType mt, MultivaluedMap<String, String> mm, InputStream in) throws IOException, WebApplicationException {
        
        ReadableRepresentation r;
                
        try {
            r = HALBuilderUtils.getRepresentationFactory().readRepresentation(new InputStreamReader(in));
        }catch( RepresentationException e) {
            throw new HTTPErrorException( Response.Status.BAD_REQUEST, "Bad sintax in message body");
        }
        
        return r;
    }

}
