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

package com.holacampus.api.beans;

import com.holacampus.api.hal.Linkable;
import com.holacampus.api.utils.Utils;
import com.theoryinpractise.halbuilder.api.Representable;
import com.theoryinpractise.halbuilder.api.Representation;
import java.sql.Date;
import java.util.List;

/**
 *  Bean object for User
 *  @author Mikel Corcuera <mik.corcuera@gmail.com>  
 */
public class User extends ActiveElement implements Representable, Linkable

{
    
    /**
     * 
     */
    public static final String TYPE_STUDENT     = "STUDENT";

    /**
     *
     */
    public static final String TYPE_UNI_MNG     = "UNI_MNG";

    /**
     *
     */
    public static final String TYPE_MNG         = "MNG";
    
    /**
     *
     */
    public static final String GENDER_MASCULINE = "M";

    /**
     *
     */
    public static final String GENDER_FEMENINE  = "F";
    
    private String              userType;
    private Name                name;
    private Date                birthDate;    
    private String              email;
    private String              gender;
    private CommentContainer    commentContainer;
    private PhotoContainer      photoContainer;
    private List<Friendship>    friends;
    private Settings            settings;
    private Credentials         credentials;
    private List<Stage>         stages;
    private List<University>    universities;
    private List<Conversation>  conversations;
    private List<Group>         groups;
    private List<Event>         events;
    private List<Group>         ownedGroups;
    private List<Event>         ownedEvents;
    private String              password;
   
    /**
     *
     */
    
    public User()
    {
        super();
        setType( TYPE_USER);
    }
    
    /**
     *
     * @return
     */
    public String getUserType() {
        return userType;
    }

    /**
     *
     * @param userType
     */
    public void setUserType(String userType) {
        this.userType = userType;
    }

    /**
     *
     * @return
     */
    public Name getName() {
        return name;
    }

    /**
     *
     * @param name
     */
    public void setName(Name name) {
        this.name = name;
    }

    /**
     *
     * @return
     */
    public Date getBirthDate() {
        return birthDate;
    }

    /**
     *
     * @param birthDate
     */
    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    /**
     *
     * @return
     */
    public String getEmail() {
        return email;
    }

    /**
     *
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     *
     * @return
     */
    public String getGender() {
        return gender;
    }

    /**
     *
     * @param gender
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     *
     * @return
     */
    public CommentContainer getCommentContainer() {
        return commentContainer;
    }

    /**
     *
     * @param commentContainer
     */
    public void setCommentContainer(CommentContainer commentContainer) {
        this.commentContainer = commentContainer;
    }

    /**
     *
     * @return
     */
    public PhotoContainer getPhotoContainer() {
        return photoContainer;
    }

    /**
     *
     * @param photoContainer
     */
    public void setPhotoContainer(PhotoContainer photoContainer) {
        this.photoContainer = photoContainer;
    }

    /**
     *
     * @return
     */
    public List<Friendship> getFriends() {
        return friends;
    }

    /**
     *
     * @param friends
     */
    public void setFriends(List<Friendship> friends) {
        this.friends = friends;
    }

    /**
     *
     * @return
     */
    public Settings getSettings() {
        return settings;
    }

    /**
     *
     * @param settings
     */
    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    /**
     *
     * @return
     */
    public Credentials getCredentials() {
        return credentials;
    }

    /**
     *
     * @param credentials
     */
    public void setCredentials(Credentials credentials) {
        this.credentials = credentials;
    }

    /**
     *
     * @return
     */
    public List<Stage> getStages() {
        return stages;
    }

    /**
     *
     * @param stages
     */
    public void setStages(List<Stage> stages) {
        this.stages = stages;
    }

    /**
     *
     * @return
     */
    public List<University> getUniversities() {
        return universities;
    }

    /**
     *
     * @param universities
     */
    public void setUniversities(List<University> universities) {
        this.universities = universities;
    }

    /**
     *
     * @return
     */
    public List<Conversation> getConversations() {
        return conversations;
    }

    /**
     *
     * @param conversations
     */
    public void setConversations(List<Conversation> conversations) {
        this.conversations = conversations;
    }

    /**
     *
     * @return
     */
    public List<Group> getGroups() {
        return groups;
    }

    /**
     *
     * @param groups
     */
    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    /**
     *
     * @return
     */
    public List<Event> getEvents() {
        return events;
    }

    /**
     *
     * @param events
     */
    public void setEvents(List<Event> events) {
        this.events = events;
    }

    /**
     *
     * @return
     */
    public List<Group> getOwnedGroups() {
        return ownedGroups;
    }

    /**
     *
     * @param ownedGroups
     */
    public void setOwnedGroups(List<Group> ownedGroups) {
        this.ownedGroups = ownedGroups;
    }

    /**
     *
     * @return
     */
    public List<Event> getOwnedEvents() {
        return ownedEvents;
    }

    /**
     *
     * @param ownedEvents
     */
    public void setOwnedEvents(List<Event> ownedEvents) {
        this.ownedEvents = ownedEvents;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    

    @Override
    public String toString() {
        return "User{" + "userType=" + userType + ", name=" + name + ", birthDate=" + birthDate + ", email=" + email + ", gender=" + gender + '}';
    }

    
    /**
     *
     * @param resource
     */
    @Override
    public void representResource(Representation resource) {
       resource.withLink("self", selfLink())
               .withProperty("id", getId())
               .withProperty( "email", getEmail())
               .withProperty("type", getUserType())
               .withProperty("birthDate", getBirthDate())
               .withProperty("gender", getGender())
               .withProperty("name", getName());
    }

    /**
     *
     * @return
     */
    @Override
    public String selfLink() {
        return Utils.createLink("/users/" + getId());
    }
    
    
    
}
