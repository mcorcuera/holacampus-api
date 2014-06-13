/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.holacampus.api.hal.contracts;

import com.theoryinpractise.halbuilder.api.ReadableRepresentation;
import java.util.List;
import java.util.Map;

/**
 *
 *  @author Mikel Corcuera <mik.corcuera@gmail.com>  
 */
public class ContractChecker {
    
    /**
     *
     * @param r
     * @param required
     * @return
     */
    public static boolean isRequiredSatisfiedBy( ReadableRepresentation r, List<String> required)
    {
        Map<String,Object> p    = r.getProperties();
        for( String req : required) {
            if( !p.containsKey( req))
                return false;
        }
        
        return true;
    }
    
}
