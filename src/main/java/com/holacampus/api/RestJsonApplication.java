/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
    
    /**d
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
