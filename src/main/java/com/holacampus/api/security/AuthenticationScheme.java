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

package com.holacampus.api.security;

import com.holacampus.api.domain.User;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.core.MultivaluedMap;

/**
 * Esta clase se encarga de gestionar los diferentes metodos o esquemas de autenticación
 * soportados por la aplicación.
 * <p>
 * Para ello permite registrar y obtener autenticadores (clases que implementan {@link Authenticator})
 * para un determinado tipo de autenticación
 * </p>
 * @author Mikel Corcuera <mik.corcuera@gmail.com>
 */
public class AuthenticationScheme {
    
    /**
     * Esquema de autenticación básico
     */
    public static final String AUTHENTICATION_SCHEME_BASIC    = "Basic";

    /**
     * Esquema de autenticación basado en tokens de autenticación
     */
    public static final String AUTHENTICATION_SCHEME_TOKEN    = "Token";

    /**
     * Sin autenticación
     */
    public static final String AUTHENTICATION_SCHEME_NONE     = "None";
    
    private static String defaultScheme = AUTHENTICATION_SCHEME_TOKEN;
    
    private static Map<String,Authenticator> authenticators = new HashMap<String,Authenticator>(); 
    
    /**
     * Resgistra un nuevo autenticador en la aplicación.
     * @param authenticator Autenticador a registrar
     */
    public static void registerAuthenticator( Authenticator authenticator)
    {
        if( authenticators.get( authenticator.getScheme()) == null)
            authenticators.put( authenticator.getScheme(), authenticator);
    }
    
    /**
     * Obtiene un autenticador (si existe) para un tipo de esquema determinado
     * @param scheme Esquema para el cual se quiere obtener el autenticador
     * @return El autenticador para el esquema si existe, null si no existe.
     */
    public static Authenticator getAuthenticator( String scheme)
    {
        return authenticators.get( scheme);
    }
    
    /**
     * Autentica una petición HTTP a partir de sus cabeceras y el esquema requerido.
     * <p>
     *  Para ello hace uso de los autenticadores registrados previamente.
     * </p>
     * @param scheme Esquema requerido
     * @param headers Cabeceras HTTP
     * Devuelve los credenciales del usario autenticado
     * @return objeto con los datos del usuario autenticado
     * @throws AuthenticationFailException Cuando los credenciales de usuario no están 
     * presentes o no son correctos
     * @throws AuthenticationBadSintaxException Cuando las cabeceras necesarias para la petición
     * tienen un formato incorrecto
     * @throws UnknownAuthenticationSchemeException No hay un autenticador registrado
     * para el esquema requerido
     */
    public static UserPrincipal authenticate( String scheme,  MultivaluedMap<String,String> headers) throws UnknownAuthenticationSchemeException, AuthenticationFailException, AuthenticationBadSintaxException
    {
        Authenticator authenticator = getAuthenticator( scheme);
        
        if( authenticator == null) {
            throw new UnknownAuthenticationSchemeException( "Unknown " + scheme + "scheme");
        }
        
        return authenticator.authenticate(headers);
    }
    
    /**
     * Establece el esquema por defecto de la aplicación. Es decir, el usado 
     * cuando la anotación {@link AuthenticationRequired} no está presente.
     * @param scheme Esquema por defecto
     */
    public static void setDefaultScheme( String scheme)
    {
        defaultScheme = scheme;
    }
    
    /**
     * Devuelve el esquema por defecto de la aplicación. Es decir, el usado 
     * cuando la anotación {@link AuthenticationRequired} no está presente.
     * @return Esquema por defecto
     */
    public static String getDefaultScheme()
    {
        return defaultScheme;
    }
}
