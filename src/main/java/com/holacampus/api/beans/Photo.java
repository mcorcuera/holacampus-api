/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.holacampus.api.beans;

import com.holacampus.api.beans.ActiveElement;
import com.holacampus.api.beans.CommentContainer;
import java.sql.Date;

/**
 *
 *  @author Mikel Corcuera <mik.corcuera@gmail.com>  
 */
public class Photo {
    
    private long                id;
    private Date                creationDate;
    private String              title;
    private String              url;
    private String              thumbnailUrl;
    private ActiveElement       creator;
    private CommentContainer    commentContainer;
    private PhotoContainer      photoContainer;
    
}
