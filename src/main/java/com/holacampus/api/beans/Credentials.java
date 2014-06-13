/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.holacampus.api.beans;

import com.holacampus.api.security.PasswordHash;

/**
 *
 *  @author Mikel Corcuera <mik.corcuera@gmail.com>  
 */
public class Credentials {
    
    private User    user;
    private byte[]  salt             ;
    private byte[]  hashedPassword   ;
    private int     iterations;
    

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
        return "Credentials{" + "user=" + user + ", salt=" + PasswordHash.toHex(salt) + ", hashedPassword=" + PasswordHash.toHex(hashedPassword) + ", iterations=" + iterations + '}';
    }

    
    
    
}
