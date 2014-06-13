/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.holacampus.api.hal.contracts;

import com.theoryinpractise.halbuilder.api.Contract;
import com.theoryinpractise.halbuilder.api.ReadableRepresentation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 *
 *  @author Mikel Corcuera <mik.corcuera@gmail.com>  
 */
public class UserContract{

    /**
     *
     */
    public static class UserCreateContract implements Contract {

        /**
         *
         * @param representation
         * @return
         */
        @Override
        public boolean isSatisfiedBy(ReadableRepresentation representation) {
            boolean satisfied;

            Map<String,Object> props = representation.getProperties();

            List<String> req =  Arrays.asList( "email", "password", "firstName", "birthDate");

            satisfied = ContractChecker.isRequiredSatisfiedBy(representation, req);

            return satisfied;     
        }
    }
    
}
