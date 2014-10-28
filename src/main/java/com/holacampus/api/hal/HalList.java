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

package com.holacampus.api.hal;

import com.holacampus.api.utils.Utils;
import com.theoryinpractise.halbuilder.jaxrs.*;
import java.util.HashMap;
import java.util.List;

/**
 * Clase que funciona como contenedor de una lista de recursos para que sea posible
 * su representación en formato HAL+JSON
 * @author Mikel Corcuera <mik.corcuera@gmail.com>
 * @param <T>
 */

@HalRootElement
public class HalList<T> implements Linkable{
    
    @HalEmbedded( "resources")
    private List<T> resources;
    
    @HalProperty( name="total")
    private int total;
    
    private String resourceRelativePath;
    private int page;
    private int size;
    private String query = null;
    
    /**
     * Constructor por defecto
     */
    public HalList()
    {
    }
    
    /**
     *
     * @param resources recursos con los que inicializar la lista
     */
    public HalList( List<T> resources)
    {
        this.resources = resources;
    }
    
    /**
     *
     * @param resources recursos con los que inicializar la lista
     * @param total número total de recursos disponibles en la base de datos
     */
    public HalList( List<T> resources, int total)
    {
        this.resources = resources;
        this.total = total;
    }
    
    /**
     *
     * @return recursos disponibles en la lista
     */
    public List<T> getResources() {
        return resources;
    }

    /**
     *
     * @param resources recursos de la lista
     */
    public void setResources(List<T> resources) {
        this.resources = resources;
    }

    /**
     *
     * @return número total de recursos disponibles en la base de datos
     */
    public int getTotal() {
        return total;
    }

    /**
     *
     * @param total número total de recursos disponibles en la base de datos
     */
    public void setTotal(int total) {
        this.total = total;
    }

    /**
     *
     * @return ruta relativa al recurso
     */
    public String getResourceRelativePath() {
        return resourceRelativePath;
    }

    /**
     *
     * @param relativePath ruta relativa al recurso 
     */
    public void setResourceRelativePath(String relativePath) {
        if( relativePath.endsWith("/"))
            relativePath = relativePath.substring(0, relativePath.length() -1);
        this.resourceRelativePath = relativePath;
    }

    /**
     *
     * @return página que representa esta lista dentro del total de recursos
     */
    public int getPage() {
        return page;
    }

    /**
     *
     * @param page página que representa esta lista dentro del total de recursos
     */
    public void setPage(int page) {
        this.page = page;
    }

    /**
     *
     * @return tamañao de la lista (no de la tabla en la base de datos)
     */
    public int getSize() {
        return size;
    }

    /**
     *
     * @param size tamañao de la lista (no de la tabla en la base de datos)
     */
    public void setSize(int size) {
        this.size = size;
    }

    /**
     *
     * @return parametros de la URL que representan a este recurso
     */
    public String getQuery() {
        return query;
    }

    /**
     *
     * @param query parametros de la URL que representan a este recurso
     */
    public void setQuery(String query) {
        this.query = query;
    }
    
    @Override 
    @HalSelfLink
    public String getSelfLink() {
        
        HashMap<String,String> params = new HashMap<String,String>();
        
        if( page != Utils.DEFAULT_PAGE || size != Utils.DEFAULT_SIZE) {
            params.put("page", Integer.toString(page));
            params.put("size", Integer.toString(size));
        }    
        
        if( query != null)
            params.put("q", query);
        
        return Utils.createLink(resourceRelativePath, params);
    }
    
    /**
     *
     * @return enlace a la representación de los siguientes elementos de la lista
     */
    @HalLink("next")
    public String nextLink() {
        if( (page + 1)*size < total) {
            HashMap<String,String> params = new HashMap<String,String>();
            params.put("page", Integer.toString( page + 1));
            params.put("size", Integer.toString( size));
            
            if( query != null)
                params.put("q", query);
        
            return Utils.createLink(resourceRelativePath, params);
        }
        return null;
    }
    
    /**
     *
     * @return enlace a la representación a los elementos anteriores de la lista
     */
    @HalLink("previous")
    public String previousLink() {
        if( page != 0) {
            HashMap<String,String> params = new HashMap<String,String>();
            params.put("page", Integer.toString( page - 1));
            params.put("size", Integer.toString( size));
            
            if( query != null)
                params.put("q", query);
        
            return Utils.createLink(resourceRelativePath, params);
        }
        return null;
    }
    
}
