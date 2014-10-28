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

package com.holacampus.api.utils;

import java.util.Map;
import java.util.Map.Entry;
import org.apache.ibatis.session.RowBounds;

/**
 * Esta clase contiene una serie de funciones de gran utilidad en el resto
 * de clases de la aplicación
 *  @author Mikel Corcuera <mik.corcuera@gmail.com>  
 */
public class Utils {
    
    /**
     * URL base de la API
     */
    public static final String BASE_URL             = "http://localhost:8080/api";

    /**
     * Página por defecto en las listas de recursos
     */
    public static final Integer DEFAULT_PAGE        = 0;

    /**
     * Tamaño por defecto de las listas de recursos
     */
    public static final Integer DEFAULT_SIZE        = 15;

    /**
     * Tamaño máximo de las listas de recursos
     */
    public static final Integer MAX_SIZE            = 40;
    
    /**
     * Crea un link a la url establecida por <code>path</code> con los parámetros
     * establecidos en <code>queryParams</code>
     * @param path url base del enlace
     * @param queryParams paramentros del enlace
     * @return enlace creado
     */
    public static String createLink( String path, Map<String,String> queryParams)
    {
        if( path == null)
            return null;
        StringBuilder s = new StringBuilder( BASE_URL);
        path = path.replace("//", "/");
        if( !path.startsWith("/"))
            s.append("/");
        s.append(path);
        if( path.endsWith("/"))
            s.setLength( s.length() - 1);
        if( queryParams != null && queryParams.size() > 0) {
            s.append("/?");
            
            for( Entry<String,String> e : queryParams.entrySet()) {
                s.append( e.getKey());
                s.append("=");
                s.append(e.getValue().replace(' ', '+'));
                s.append("&");
            }
            s.setLength( s.length() - 1);
        }
        
        return s.toString();
    }
    
    /**
     * Obtiene la ruta relativa a partir de la absoluta
     * @param absolute ruta absoluta
     * @return ruta relativa
     */
    public static String getRelativePath( String absolute)
    {
        return absolute.replace( BASE_URL, "");
    }
    
    /**
     * Obtiene una página válida para la lista de recursos
     * @param page página inicial
     * @return página válida
     */
    public static int getValidPage( Integer page)
    {
        return ( page == null || page < 0) ? DEFAULT_PAGE : page;
    }
    
    /**
     * Obtiene un tamaño válido para la lista de recursos
     * @param size tamaño inicial
     * @return tamaño válido
     */
    public static int getValidSize( Integer size)
    {
        return (size == null || size < 0) ? DEFAULT_SIZE : ( size > MAX_SIZE ? MAX_SIZE : size);
    }
    
    /**
     * Crea un objeto {@link RowBounds} para utilizar con la API de MyBatis a partir
     * de la página y el tamaño
     * @param page pagina
     * @param size tamaño
     * @return objeto configurado con los parametros establecidos
     */
    public static RowBounds createRowBounds( int page, int size)
    {
        RowBounds rb = new RowBounds( page*size, size);
        
        return rb;
    }
}
