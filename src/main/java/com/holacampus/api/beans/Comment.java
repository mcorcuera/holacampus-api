/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.holacampus.api.beans;

/**
 *
 *  @author Mikel Corcuera <mik.corcuera@gmail.com>  
 */
public class Comment {
    private long                id;
    private String              content;
    private ActiveElement       creator;
    private boolean             isRecomment;
    private CommentContainer    belongingContentContainer;
    private CommentContainer    ownCommentContainer;
}
