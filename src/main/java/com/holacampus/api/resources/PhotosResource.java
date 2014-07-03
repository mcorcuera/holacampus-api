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

import com.holacampus.api.exceptions.HTTPErrorException;
import com.holacampus.api.security.AuthenticationRequired;
import com.holacampus.api.security.AuthenticationScheme;
import com.holacampus.api.utils.PhotoUploader;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

/**
 *
 * @author Mikel Corcuera <mik.corcuera@gmail.com>
 */

@Path("/photos")
public class PhotosResource {
    
    @POST
    @AuthenticationRequired( AuthenticationScheme.AUTHENTICATION_SCHEME_NONE)
    @Consumes( MediaType.TEXT_PLAIN)
    @Produces( MediaType.TEXT_PLAIN)
    public String postPhoto( String photo) {
        String url = null;
        try {
            url = PhotoUploader.uploadPhoto(photo);
            if( url == null) {
                throw new HTTPErrorException( Status.BAD_REQUEST, "unknown photo format");
            }           
        } catch (Exception e) {
            throw new HTTPErrorException( Status.INTERNAL_SERVER_ERROR, "Internal server error: " + e);
        }
        return url;
    }
}
