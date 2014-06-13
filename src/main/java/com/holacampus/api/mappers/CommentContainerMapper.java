/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.holacampus.api.mappers;

import com.holacampus.api.beans.CommentContainer;
import org.apache.ibatis.annotations.Param;

/**
 *
 *  @author Mikel Corcuera <mik.corcuera@gmail.com>  
 */
public interface CommentContainerMapper {
    
    /**
     *
     * @param id
     * @return
     */
    public CommentContainer getCommentContainer( long id);
    
    /**
     *
     * @param cc
     * @return
     */
    public int createCommentContainer( @Param("cc") CommentContainer cc);
    
}
