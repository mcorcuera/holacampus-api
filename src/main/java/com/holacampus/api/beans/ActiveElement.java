/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.holacampus.api.beans;

import com.holacampus.api.beans.Conversation;
import java.util.List;

/**
 *
 *  @author Mikel Corcuera <mik.corcuera@gmail.com>  
 */
public class ActiveElement {
    
    /**
     *
     */
    public static final String TYPE_USER    = "USER";

    /**
     *
     */
    public static final String TYPE_UNI     = "UNI";
    
    private Long                id;
    private String              type;
    private Photo               profilePhoto;
    private List<Conversation>  conversations;
    
    /**
     *
     * @return
     */
    public Long getId() {
        return id;
    }

    /**
     *
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     *
     * @return
     */
    public String getType() {
        return type;
    }

    /**
     *
     * @param type
     */
    public void setType(String type) {
        this.type = type;
    }

}
