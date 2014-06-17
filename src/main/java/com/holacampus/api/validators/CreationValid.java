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

package com.holacampus.api.validators;

import com.holacampus.api.domain.User;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

/**
 *
 * @author Mikel Corcuera <mik.corcuera@gmail.com>
 */
@Target({ METHOD, FIELD, PARAMETER })
@Retention(RUNTIME)
@Constraint(validatedBy = CreationValid.Validator.class)
public @interface CreationValid {
    
    String message() default "{invalid}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
    
     public class Validator implements ConstraintValidator<CreationValid,Object>{

        @Override
        public void initialize(CreationValid a) {
            
        }

        @Override
        public boolean isValid(Object t, ConstraintValidatorContext cvc) 
        {
            boolean valid = true;
            final Field[] fields    = t.getClass().getDeclaredFields();
            
            cvc.disableDefaultConstraintViolation();
            
            for( Field field : fields) {
                 CreationNeeded c = field.getAnnotation( CreationNeeded.class);
                 if( c != null) {
                     
                     try {
                         Object property = new PropertyDescriptor(field.getName(), t.getClass()).getReadMethod().invoke(t);
                         if( property == null) {
                             valid = false;
                             cvc.buildConstraintViolationWithTemplate(c.message()).addPropertyNode(field.getName()).addConstraintViolation();
                         }
                     } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | IntrospectionException ex) {
                         Logger.getLogger(CreationValid.class.getName()).log(Level.SEVERE, null, ex);
                     }
                 }
             }
            
            return valid;
        }
         
     }
}
