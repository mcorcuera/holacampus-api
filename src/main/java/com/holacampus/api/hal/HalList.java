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
 *
 * @author Mikel Corcuera <mik.corcuera@gmail.com>
 */

@HalRootElement
public class HalList<T>{
    
    @HalEmbedded( "resources")
    private List<T> resources;
    
    @HalProperty( name="total")
    private int total;
    
    private String resourceRelativePath;
    private int page;
    private int size;
    private String query;
    
    public HalList()
    {
    }
    
    public HalList( List<T> resources)
    {
        this.resources = resources;
    }
    
    public HalList( List<T> resources, int total)
    {
        this.resources = resources;
        this.total = total;
    }
    
    public List<T> getResources() {
        return resources;
    }

    public void setResources(List<T> resources) {
        this.resources = resources;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }


    public String getResourceRelativePath() {
        return resourceRelativePath;
    }

    public void setResourceRelativePath(String resourceName) {
        this.resourceRelativePath = resourceName;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }
    
    
    
    @HalSelfLink
    public String selfLink() {
        
        HashMap<String,String> params = new HashMap<String,String>();
        
        if( page != Utils.DEFAULT_PAGE || size != Utils.DEFAULT_SIZE) {
            params.put("page", Integer.toString(page));
            params.put("size", Integer.toString(size));
        }    
        
        if( query != null)
            params.put("q", query);
        
        return Utils.createLink(resourceRelativePath, params);
    }
    
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
