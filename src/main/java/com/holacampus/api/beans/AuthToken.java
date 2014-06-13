/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.holacampus.api.beans;

import com.holacampus.api.hal.Linkable;
import com.holacampus.api.utils.Utils;
import com.theoryinpractise.halbuilder.api.Representable;
import com.theoryinpractise.halbuilder.api.Representation;
import java.util.Date;


/**
 *
 *  @author Mikel Corcuera <mik.corcuera@gmail.com>  
 */
public class AuthToken implements Representable, Linkable{
    
    private String      authToken;
    private User        user;
    private Date        creationDate;

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public void representResource(Representation resource) {
        resource.withLink("self", selfLink())
                .withProperty("authToken", getAuthToken())
                .withProperty("creationDate", getCreationDate().toString());
    }

    @Override
    public String selfLink() {
        return Utils.createLink("/users/" + getUser().getId() + "/auth-tokens/" + getAuthToken());
    }
    
    
    
}
