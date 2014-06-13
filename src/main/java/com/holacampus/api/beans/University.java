/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.holacampus.api.beans;

import java.util.List;

/**
 *
 *  @author Mikel Corcuera <mik.corcuera@gmail.com>  
 */
public class University extends ActiveElement{
    
    private String              name;
    private User                manager;
    private CommentContainer    commentContainer;
    private PhotoContainer      photoContainer;
    private List<Study>         studies;
    private List<City>          cities;
    private List<User>          students;
    private List<Conversation>  conversations;
    private List<Group>         groups;
    private List<Event>         events;
    private List<Group>         ownedGroups;
    private List<Event>         ownedEvents;
    
    /**
     *
     */
    public University()
    {
        super();
        setType( TYPE_UNI);
    }
}
