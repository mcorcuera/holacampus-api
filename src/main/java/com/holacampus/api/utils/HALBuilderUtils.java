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

import com.theoryinpractise.halbuilder.api.Representable;
import com.theoryinpractise.halbuilder.api.Representation;
import com.theoryinpractise.halbuilder.api.RepresentationFactory;
import com.theoryinpractise.halbuilder.standard.StandardRepresentationFactory;
import java.util.List;

/**
 *
 *  @author Mikel Corcuera <mik.corcuera@gmail.com>  
 */
public class HALBuilderUtils {
     private static RepresentationFactory rp;
     
     static {
         rp = new StandardRepresentationFactory()
                    .withFlag( RepresentationFactory.PRETTY_PRINT)
                    .withFlag( RepresentationFactory.SINGLE_ELEM_ARRAYS);
     }
     
    /**
     *
     * @return
     */
    public static RepresentationFactory getRepresentationFactory()
     {
         return rp;
     }
     
    /**
     *
     * @param r
     * @return
     */
    public static Representation fromRepresentable( Representable r)
     {
         return getRepresentationFactory().newRepresentation().withRepresentable(r);
     }
     
    /**
     *
     * @param r
     * @param list
     * @param name
     * @return
     */
    public static Representation addList( Representation r, List list, String name)
     {
         r.withProperty("total", list.size());
         
         for( Object l : list) {
             r.withRepresentation( name, fromRepresentable( (Representable) l));
         }
         
         return r;
     }
}
