/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
