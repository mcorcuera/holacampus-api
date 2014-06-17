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

package com.holacampus.api.subresources;

import com.holacampus.api.domain.Comment;
import com.holacampus.api.domain.CommentContainer;
import com.holacampus.api.domain.Container;
import com.holacampus.api.domain.User;
import com.holacampus.api.hal.HalList;
import com.holacampus.api.mappers.CommentMapper;
import com.holacampus.api.mappers.UserMapper;
import com.holacampus.api.resources.particular.ParticularUserResource;
import com.holacampus.api.security.AuthenticationRequired;
import com.holacampus.api.security.AuthenticationScheme;
import com.holacampus.api.security.UserPrincipal;
import com.holacampus.api.utils.MyBatisConnectionFactory;
import com.holacampus.api.utils.Utils;
import com.holacampus.api.validators.CreationValid;
import com.theoryinpractise.halbuilder.api.RepresentationFactory;
import java.util.List;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 *
 * @author Mikel Corcuera <mik.corcuera@gmail.com>
 */
public class CommentsResource {
    
    private static final Logger logger = LogManager.getLogger( ParticularUserResource.class.getName());
 
    private final CommentContainer  container;
    private final String            path;
    private Long                    elId;
    
    public CommentsResource( long id, CommentContainer c, String path)
    {
        this.elId       = id;
        this.container  = c;
        this.path       = path;
    }
    
    @GET
    @AuthenticationRequired( AuthenticationScheme.AUTHENTICATION_SCHEME_TOKEN)
    @Produces( { RepresentationFactory.HAL_JSON})
    public HalList<Comment> getComments( @Context SecurityContext sc, @QueryParam("page") Integer page, @QueryParam( "size") Integer size)
    {
        logger.info("[GET] " + path + "/"  + elId + "/comments");
        
        HalList<Comment> comments = null;

        page            = Utils.getValidPage(page);
        size            = Utils.getValidSize(size);
        RowBounds rb    = Utils.createRowBounds(page, size);
        
        SqlSession session = MyBatisConnectionFactory.getSession().openSession();

        try {
            CommentMapper mapper        = session.getMapper( CommentMapper.class);
            List<Comment> commentList   = mapper.getCommentsWithCreator( container.getId(), rb);
            int total                   = mapper.getTotalComments( container.getId());
            session.commit();
            
            comments = new HalList<Comment>( commentList, total);
            comments.setPage(page);
            comments.setSize(size);
        }catch( Exception e) {
            logger.info( e);
            throw new InternalServerErrorException( e.getMessage());
        }finally {
            session.close();
        }
        
        return comments;
    }
    
    @POST
    @AuthenticationRequired( AuthenticationScheme.AUTHENTICATION_SCHEME_TOKEN)
    @Consumes( { RepresentationFactory.HAL_JSON, MediaType.APPLICATION_JSON})
    @Produces( { RepresentationFactory.HAL_JSON})
    public Comment postNewComment( @CreationValid @Valid Comment comment, @Context SecurityContext sc)
    {
        logger.info("[POST] " + path + "/"  + elId + "/comments");
        
        comment.setCreator( new User( (UserPrincipal) sc.getUserPrincipal()));
        comment.setBelongingCommentContainer(container);
        
        /*
        * Check if the comment is being made over another comment
        */
        if( container.getType() == Container.ElementType.COMMENT)
            comment.setIsRecomment(true);
        else
            comment.setIsRecomment(false);
        
        SqlSession session = MyBatisConnectionFactory.getSession().openSession();

        try {
            CommentMapper mapper    = session.getMapper( CommentMapper.class);
            UserMapper userMapper   = session.getMapper( UserMapper.class);
            int result              = mapper.createComment(comment);
            comment.setCreator( userMapper.getUser( elId));
            session.commit();
            
            if( result == 0 || comment.getId() == null) {
                logger.info( "Problem creating comment");
                throw new Exception( "Error while creating the comment");
            }
        }catch( Exception e) {
            logger.info( e);
            throw new InternalServerErrorException( e.getMessage());
        }finally {
            session.close();
        }
        
        return comment;
    }
}
