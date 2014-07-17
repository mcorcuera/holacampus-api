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

import com.holacampus.api.hal.Linkable;
import com.holacampus.api.utils.Utils;
import com.theoryinpractise.halbuilder.jaxrs.HalEmbedded;
import com.theoryinpractise.halbuilder.jaxrs.HalProperty;
import com.theoryinpractise.halbuilder.jaxrs.HalRootElement;
import com.theoryinpractise.halbuilder.jaxrs.HalSelfLink;
import java.sql.Timestamp;


/**
 *
 *  @author Mikel Corcuera <mik.corcuera@gmail.com>  
 */

@HalRootElement
public class GroupMember implements Linkable{
    
    @HalProperty( name="joinDate")
    private Timestamp       joinDate;
    
    @HalEmbedded( "member")
    private ActiveElement   member;

    private GroupEvent parent;
    
    @HalSelfLink
    public String getSelfLink() {
        return parent.getSelfLink() + "/members/" + member.getId();
    }

    public Timestamp getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(Timestamp joinDate) {
        this.joinDate = joinDate;
    }

    public ActiveElement getMember() {
        return member;
    }

    public void setMember(ActiveElement member) {
        this.member = member;
    }

    public GroupEvent getParent() {
        return parent;
    }

    public void setParent(GroupEvent parent) {
        this.parent = parent;
    }
    
    
}
