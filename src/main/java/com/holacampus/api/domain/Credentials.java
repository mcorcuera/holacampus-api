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
package com.holacampus.api.domain;

import com.holacampus.api.security.PasswordHash;

/**
 *
 *  @author Mikel Corcuera <mik.corcuera@gmail.com>  
 */
public class Credentials {
    
    private ActiveElement       element;
    private byte[]              salt;
    private byte[]              hashedPassword   ;
    private int                 iterations;
    

    public ActiveElement getElement() {
        return element;
    }

    public void setElement(ActiveElement element) {
        this.element = element;
    }

    public byte[] getSalt() {
        return salt;
    }

    public void setSalt(byte[] salt) {
        this.salt = salt;
    }

    public byte[] getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(byte[] hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public int getIterations() {
        return iterations;
    }

    public void setIterations(int iterations) {
        this.iterations = iterations;
    }

    @Override
    public String toString() {
        return "Credentials{" + "element=" + element + ", salt=" + PasswordHash.toHex(salt) + ", hashedPassword=" + PasswordHash.toHex(hashedPassword) + ", iterations=" + iterations + '}';
    }

    
    
    
}
