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

package com.holacampus.api.domain;
import com.holacampus.api.hal.Linkable;
import com.holacampus.api.utils.Utils;
import com.theoryinpractise.halbuilder.api.Representable;
import com.theoryinpractise.halbuilder.api.Representation;
import com.theoryinpractise.halbuilder.jaxrs.*;
import java.util.Date;



/**
 * Está clase da forma a los tokens de autenticación mediante los cuales se
 * dota de seguridad a la aplicación
 *  @author Mikel Corcuera <mik.corcuera@gmail.com>  
 */

@HalRootElement
public class AuthToken implements Representable, Linkable{
    
    @HalProperty( name="authToken")
    private String      authToken;
    
    @HalEmbedded( "element")
    private ActiveElement        element;
    
    @HalProperty( name="creationDate")
    private Date        creationDate;

    /**
     *
     * @return la cadena de caracteres que representa el token
     */
    public String getAuthToken() {
        return authToken;
    }

    /**
     *
     * @param authToken la cadena de caracteres que representa el token
     */
    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    /**
     *
     * @return elemento al que pertenece el token
     */
    public ActiveElement getElement() {
        return element;
    }

    /**
     *
     * @param element elemento al que pertenece el token
     */
    public void setElement(ActiveElement element) {
        this.element = element;
    }

    /**
     *
     * @return fecha de creación del token
     */
    public Date getCreationDate() {
        return creationDate;
    }

    /**
     *
     * @param creationDate fecha de creación del token
     */
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * Añade kis campos del objeto a la representación HAL
     * @param resource representación HAL a la que añadir los campos
     */
    @Override
    public void representResource(Representation resource) {
        resource.withLink("self", getSelfLink())
                .withProperty("authToken", getAuthToken())
                .withProperty("creationDate", getCreationDate().toString());
    }

    @HalSelfLink
    @Override
    public String getSelfLink() {
        return Utils.createLink("/auth-tokens/" + getAuthToken(), null);
    }
    
    
    
}
