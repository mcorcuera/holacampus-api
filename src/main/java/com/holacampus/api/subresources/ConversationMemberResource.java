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

import com.holacampus.api.domain.Conversation;
import com.holacampus.api.domain.ConversationMember;
import com.holacampus.api.domain.Message;
import com.holacampus.api.exceptions.HTTPErrorException;
import com.holacampus.api.hal.HalList;
import com.holacampus.api.mappers.ConversationMapper;
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
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * Esta clase se encarga de gestionar las peticiones de la API al subrecurso
 * ConversationMembers. Es decir, gestiona las peticiones a la URL 
 * <code>/conversations/{cid}/members</code>. Estos recursos son los miembros que
 * pertenecen a una conversación
 * @author Mikel Corcuera <mik.corcuera@gmail.com>
 */
public class ConversationMemberResource {
    
    private static final Logger logger = LogManager.getLogger( ConversationMemberResource.class.getName());

    private final Conversation conversation;
    
    /**
     * Contructor del subrecurso de miembros de la conversación
     * @param conversation conversación a la que pertenece el subrecurso
     */
    public ConversationMemberResource( Conversation conversation)
    {
        this.conversation = conversation;
    }

    /**
      * Esta función gestiona las peticiones GET al recurso 
     * <code>/conversations/{cid}/members</code>. Esta operación devuelve un lista 
     * con los miembro de la conversación
     * @param page página de los resultados
     * @param size tamaño de los resultados
     * @param sc información de seguridad y autenticación de la petición
     * @param uriInfo  información de la URL de la petición
     * @return lista con los miembros de la conversación
     */
    @GET
    @AuthenticationRequired( AuthenticationScheme.AUTHENTICATION_SCHEME_TOKEN)
    @Produces( { RepresentationFactory.HAL_JSON})
    public HalList<ConversationMember> getMembers(  @QueryParam("page") Integer page, 
            @QueryParam( "size") Integer size, @Context SecurityContext sc, @Context UriInfo uriInfo)
    {
        page                                = Utils.getValidPage(page);
        size                                = Utils.getValidSize(size);
        RowBounds rb                        = Utils.createRowBounds(page, size);
        HalList<ConversationMember> members = null;
        UserPrincipal up                    = (UserPrincipal) sc.getUserPrincipal();
        SqlSession session                  = MyBatisConnectionFactory.getSession().openSession();

        try {
            ConversationMapper mapper   = session.getMapper( ConversationMapper.class);
            
            checkMembership( mapper, up.getId());
            
            List<ConversationMember> memberList = mapper.getMembers(conversation, rb);
            int total                           = mapper.getTotalMembers(conversation);
            
            members = new HalList<ConversationMember>( memberList, total);
            members.setPage(page);
            members.setSize(size);
            members.setResourceRelativePath( uriInfo.getPath());
            session.commit();
         }catch( HTTPErrorException e) {
            throw e;
        }catch( Exception e) {
            logger.info( e);
            throw new HTTPErrorException( Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }finally {
            session.close();
        }
        return members;
    }
    
    /**
     * Esta función gestiona las peticiones POST al recurso 
     * <code>/conversations/{cid}/members</code>. Esta operación añade un nuevo
     * miembro a la conversación
     * @param member miembro a añadir en la conversación
     * @param sc información de seguridad y autenticación de la petición
     * @param uriInfo  información de la URL de la petición
     * @return reperesentación del miembro recien añadido
     */
    @POST
    @AuthenticationRequired( AuthenticationScheme.AUTHENTICATION_SCHEME_TOKEN)
    @Consumes( { RepresentationFactory.HAL_JSON, MediaType.APPLICATION_JSON})
    @Produces( { RepresentationFactory.HAL_JSON})
    public ConversationMember addMember( @Valid @CreationValid ConversationMember member, 
            @Context SecurityContext sc, @Context UriInfo uriInfo) 
    {
        if( member.getMember().getId() == null)
            throw new HTTPErrorException( Response.Status.BAD_REQUEST, "Malformed request");
        
        if( conversation.getType().equals( Conversation.TYPE_INDIVIDUAL))
            throw new HTTPErrorException( Response.Status.FORBIDDEN, "You can't add members to individual conversation");
        
        UserPrincipal up                    = (UserPrincipal) sc.getUserPrincipal();
        SqlSession session                  = MyBatisConnectionFactory.getSession().openSession();

        try {
            ConversationMapper mapper   = session.getMapper( ConversationMapper.class);
            checkMembership( mapper, up.getId());
            
            int result = mapper.addMemberToConversation(conversation, member.getMember().getId());
            
            if( result == 0) {
                throw new HTTPErrorException( Response.Status.CONFLICT, "User already in conversation");
            }
            
            member = mapper.getMember(conversation, member.getMember().getId());
            mapper.updateLastSeen( conversation, up.getId());
            session.commit();
         }catch( HTTPErrorException e) {
            throw e;
        }catch( Exception e) {
            logger.info( e);
            throw new HTTPErrorException( Response.Status.CONFLICT, "User already in conversation");
        }finally {
            session.close();
        }
        return member;
    }
    
    /**
     * Esta función gestiona las peticiones GET al recurso 
     * <code>/conversations/{cid}/members/{id}</code>. Esta operación obtiene
     * la representación de un miembro en concreto
     * @param id identificador del miembro de la conversación
     * @param sc información de seguridad y autenticación de la petición
     * @param uriInfo  información de la URL de la petición
     * @return representación del miembro de la conversación
     */
    @Path( "/{id}")
    @GET
    @AuthenticationRequired( AuthenticationScheme.AUTHENTICATION_SCHEME_TOKEN)
    @Produces( { RepresentationFactory.HAL_JSON})
    public ConversationMember getMember( @PathParam( "id") Long id, @Context SecurityContext sc, @Context UriInfo uriInfo)
    {
        ConversationMember member = null;
        
        UserPrincipal up                    = (UserPrincipal) sc.getUserPrincipal();
        SqlSession session                  = MyBatisConnectionFactory.getSession().openSession();

        try {
            ConversationMapper mapper   = session.getMapper( ConversationMapper.class);
            checkMembership( mapper, up.getId());
            
            member = mapper.getMember(conversation, id);
            
            if( member == null) {
                throw new HTTPErrorException( Response.Status.NOT_FOUND, "Member " + id + " not found");
            }
            
            session.commit();
         }catch( HTTPErrorException e) {
            throw e;
        }catch( Exception e) {
            logger.info( e);
            throw new HTTPErrorException( Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }finally {
            session.close();
        }
        return member;
    }
    
    /**
     * Esta función gestiona las peticiones DELETE al recurso 
     * <code>/conversations/{cid}/members/{id}</code>. Esta operación elimina a
     *  un miembro en concreto de la conversación
     * @param id identificador del miembro a eliminar
     * @param sc información de seguridad y autenticación de la petición
     * @param uriInfo  información de la URL de la petición
     */
    @Path( "/{id}")
    @DELETE
    @AuthenticationRequired( AuthenticationScheme.AUTHENTICATION_SCHEME_TOKEN)
    @Produces( { RepresentationFactory.HAL_JSON})
    public void deleteMember( @PathParam( "id") Long id, @Context SecurityContext sc, @Context UriInfo uriInfo)
    {
        ConversationMember member = null;
        
        UserPrincipal up                    = (UserPrincipal) sc.getUserPrincipal();
        
        if( conversation.getType().equals( Conversation.TYPE_INDIVIDUAL))
            throw new HTTPErrorException( Response.Status.FORBIDDEN, "You can't delete members from individual conversation");
        
        
        if( !id.equals( up.getId())){
           throw new HTTPErrorException( Response.Status.FORBIDDEN, "You can only delete yourself from the conversation");
        }
        
        SqlSession session                  = MyBatisConnectionFactory.getSession().openSession();

        try {
            ConversationMapper mapper   = session.getMapper( ConversationMapper.class);
            checkMembership( mapper, up.getId());
            
            member = mapper.getMember(conversation, id);
            
            if( member == null) {
                throw new HTTPErrorException( Response.Status.NOT_FOUND, "Member " + id + " not found");
            }
            
            int result = mapper.removeMember(conversation, id);
            
            if( result == 0)
                throw new  HTTPErrorException( Response.Status.INTERNAL_SERVER_ERROR, "Problem deleting member");
            
            session.commit();
         }catch( HTTPErrorException e) {
            throw e;
        }catch( Exception e) {
            logger.info( e);
            throw new HTTPErrorException( Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }finally {
            session.close();
        }
        return;
    }
    
    /**
     * Esta función gestiona las peticiones DELETE al recurso 
     * <code>/conversations/{cid}/members/me</code>. Esta operación elimina al 
     * usuario que realiza la petición de la conversación
     * @param sc información de seguridad y autenticación de la petición
     * @param uriInfo  información de la URL de la petición
     */
    @Path( "/me")
    @DELETE
    @AuthenticationRequired( AuthenticationScheme.AUTHENTICATION_SCHEME_TOKEN)
    @Produces( { RepresentationFactory.HAL_JSON})
    public void deleteMember( @Context SecurityContext sc, @Context UriInfo uriInfo)
    {
        deleteMember( ((UserPrincipal) sc.getUserPrincipal()).getId(), sc, uriInfo);
    }
    
   
    private void checkMembership( ConversationMapper mapper, Long userId) throws HTTPErrorException, Exception
    {
        int isMember = mapper.isMemberOfConversation( conversation.getId(), userId);
            
        if( isMember <= 0)
            throw new HTTPErrorException( Response.Status.FORBIDDEN, "You are not a member of the conversation");
            
    }
}
