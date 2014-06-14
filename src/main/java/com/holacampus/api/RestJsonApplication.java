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

package com.holacampus.api;

import com.holacampus.api.security.AuthenticationScheme;
import com.holacampus.api.security.BasicAuthenticator;
import com.holacampus.api.security.TokenAuthenticator;
import java.net.URL;
import javax.ws.rs.ApplicationPath;
import org.apache.log4j.PropertyConfigurator;
import org.glassfish.jersey.server.ResourceConfig;


/**
 * 
 *  @author Mikel Corcuera <mik.corcuera@gmail.com>  
 */

@ApplicationPath("/")
public class RestJsonApplication extends ResourceConfig{
    
    /**
     *
     */
    public RestJsonApplication() {
        
        packages( "com.holacampus.api.resources", 
                "com.theoryinpractise.halbuilder.jaxrs",
                "com.holacampus.api.filters");
        
        
        // Log4j Configuration from file
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        URL url = loader.getResource("log4j.properties");
        PropertyConfigurator.configure(url);
        
        AuthenticationScheme.registerAuthenticator( new BasicAuthenticator());
        AuthenticationScheme.registerAuthenticator( new TokenAuthenticator());
        
    }
    
}
