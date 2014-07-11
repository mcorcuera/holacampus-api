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
 *
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

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public ActiveElement getElement() {
        return element;
    }

    public void setElement(ActiveElement element) {
        this.element = element;
    }


    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

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
