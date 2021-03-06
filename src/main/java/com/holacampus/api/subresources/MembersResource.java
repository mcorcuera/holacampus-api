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

import com.holacampus.api.domain.ActiveElement;
import com.holacampus.api.domain.GroupEvent;
import com.holacampus.api.domain.GroupMember;
import com.holacampus.api.domain.Permission;
import com.holacampus.api.domain.University;
import com.holacampus.api.domain.User;
import com.holacampus.api.exceptions.HTTPErrorException;
import com.holacampus.api.hal.HalList;
import com.holacampus.api.mappers.GroupEventMapper;
import com.holacampus.api.mappers.GroupMemberMapper;
import com.holacampus.api.mappers.UniversityMapper;
import com.holacampus.api.mappers.UserMapper;
import com.holacampus.api.security.AuthenticationRequired;
import com.holacampus.api.security.AuthenticationScheme;
import com.holacampus.api.security.UserPrincipal;
import com.holacampus.api.utils.MyBatisConnectionFactory;
import com.holacampus.api.utils.Utils;
import com.theoryinpractise.halbuilder.api.RepresentationFactory;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.Encoded;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * Esta clase se encarga de gestionar las peticiones de la API al subrecurso
 * Members. Es decir, gestiona las peticiones a la URL 
 * <code>/groups|events/{cid}/members</code>. Estos recursos son los miembros que
 * pertenecen a un grupo o evento
 * @author Mikel Corcuera <mik.corcuera@gmail.com>
 */
public class MembersResource {

    private static final Logger logger = LogManager.getLogger( MembersResource.class.getName());

    private final GroupEvent parent;
    
    /**
     * Contructor del subrecurso de miembros del grupo o evento
     * @param parent grupo o evento al que pertenece el subrecurso
     */
    public MembersResource( GroupEvent parent)
    {
        this.parent = parent;
    }
    
    /**
      * Esta función gestiona las peticiones GET al recurso 
     * <code>/groups|events/{cid}/members</code>. Esta operación devuelve un lista
     * con los miembro del grupo o evento
     * @param page página de los resultados
     * @param size tamaño de los resultados
     * @param q cadena de caracteres que sirve para filtrar por nombre los
     * resultados
     * @param sc información de seguridad y autenticación de la petición
     * @param uriInfo  información de la URL de la petición
     * @return lista con los miembros del tamaño especificado y filtrada.
     * @throws UnsupportedEncodingException
     */
    @GET
    @AuthenticationRequired( AuthenticationScheme.AUTHENTICATION_SCHEME_TOKEN)
    @Produces( { RepresentationFactory.HAL_JSON})
    @Encoded
    public HalList<GroupMember> getMembers( @QueryParam("page") Integer page, 
            @QueryParam( "size") Integer size, @QueryParam( "q") String q, 
            @Context UriInfo uriInfo, @Context SecurityContext sc)  throws UnsupportedEncodingException
    {
        logger.info( "[GET] " + uriInfo.getPath());
        
        HalList<GroupMember> members;
        
        page = Utils.getValidPage(page);
        size = Utils.getValidSize(size);
        if( q != null) {
            q   = URLDecoder.decode(q, "UTF-8");
        }
        RowBounds rb = Utils.createRowBounds(page, size);
        UserPrincipal up = (UserPrincipal) sc.getUserPrincipal();
        SqlSession session = MyBatisConnectionFactory.getSession().openSession();
        
          try {
            GroupMemberMapper memberMapper  = session.getMapper( GroupMemberMapper.class);
            GroupEventMapper groupMapper    = session.getMapper( GroupEventMapper.class);
            UserMapper  userMapper          = session.getMapper( UserMapper.class);
            UniversityMapper uniMapper      = session.getMapper( UniversityMapper.class);
            Permission groupPermission = new Permission();
            
           groupMapper.getPermissions( up.getId(), parent.getId(), groupPermission);
           
           if( !groupPermission.getLevel().equals( Permission.LEVEL_MEMBER) && !groupPermission.getLevel().equals( Permission.LEVEL_OWNER))
               throw new HTTPErrorException( Status.FORBIDDEN, "You are not allowed here");
            
            List<GroupMember> memberList = memberMapper.getMembers(parent, q, rb);
            int total                    = memberMapper.getTotalMembers(parent, q);
            
            session.commit();
            
            for( GroupMember member : memberList) {
                member.setParent(parent);
                Permission permissions = new Permission();
                ActiveElement element = member.getMember();
                
                if( element.getType().equals( ActiveElement.TYPE_USER)) {
                    User user = (User) element;
                    userMapper.getPermissions( up.getId(), user.getId(), permissions);
                    user.setPermission(permissions);
                }else {
                    University university = (University) element;
                    uniMapper.getPermissions( up.getId(), university.getId(), permissions);
                    university.setPermission(permissions);
                }
            }
            
            members = new HalList<GroupMember> ( memberList, total);
            members.setResourceRelativePath( uriInfo.getPath());
            members.setPage(page);
            members.setSize(size);
            members.setQuery(q);
            
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
     * <code>/groups|events/{cid}/members</code>. Esta operación añade un 
     * miembro al grupo o evento
     * @param ae Usuario o Universidad a añadir
     * @param sc información de seguridad y autenticación de la petición
     * @param uriInfo  información de la URL de la petición
     * @return representación del miembro ya añadido al grupo o evento
     */
    @POST
    @AuthenticationRequired( AuthenticationScheme.AUTHENTICATION_SCHEME_TOKEN)
    @Consumes( { RepresentationFactory.HAL_JSON, MediaType.APPLICATION_JSON})
    @Produces( { RepresentationFactory.HAL_JSON})
    public GroupMember addMember( ActiveElement ae, @Context UriInfo uriInfo, @Context SecurityContext sc) {
        
        logger.info( "[POST] " + uriInfo.getPath());
        
        if( ae.getId() == null)
            throw new HTTPErrorException( Status.BAD_REQUEST, "Bad Request");
        
        UserPrincipal up    = (UserPrincipal) sc.getUserPrincipal();
        GroupMember member  = null;
        SqlSession session  = MyBatisConnectionFactory.getSession().openSession();

        try {
            GroupMemberMapper memberMapper  = session.getMapper( GroupMemberMapper.class);
            GroupEventMapper groupMapper    = session.getMapper( GroupEventMapper.class);
            
            Permission groupPermission = new Permission();
            
            groupMapper.getPermissions( up.getId(), parent.getId(), groupPermission);
            
            // We must be members or owners, or we are adding ourselves
            if( !(groupPermission.getLevel().equals( Permission.LEVEL_MEMBER) || groupPermission.getLevel().equals( Permission.LEVEL_OWNER))
                    && !up.getId().equals( ae.getId()))
               throw new HTTPErrorException( Status.FORBIDDEN, "You are not allowed here");
            
            int result = memberMapper.addMember(parent, ae);
            
            if( result == 0)
                throw new HTTPErrorException( Status.CONFLICT, "This user is already member of the group");
            
            member = memberMapper.getMember(parent, ae.getId());
            session.commit();
         }catch( HTTPErrorException e) {
            throw e;
        }catch( Exception e) {
            logger.info( e);
            throw new HTTPErrorException( Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }finally {
            session.close();
        }
        return member;
    }
    
    /**
     * Esta función gestiona las peticiones GET al recurso 
     * <code>/groups|events/{cid}/members/elegibles</code>. Esta operación devuelve un lista
     * con los posibles {@link ActiveElement} que pueden ser añadidos al grupo
     * por el usuario que realiza la petición
     * @param page página de los resultados
     * @param size tamaño de los resultados
     * @param sc información de seguridad y autenticación de la petición
     * @param uriInfo  información de la URL de la petición
     * @return lista de {@link ActiveElement} que pueden ser añadidos como miembros
     * al grupo o evento
     * @throws UnsupportedEncodingException
     */
    @Path("/eligible")
    @GET
    @AuthenticationRequired( AuthenticationScheme.AUTHENTICATION_SCHEME_TOKEN)
    @Produces( { RepresentationFactory.HAL_JSON})
    @Encoded
    public HalList<ActiveElement> getElegible( @QueryParam("page") Integer page, 
            @QueryParam( "size") Integer size,
            @Context UriInfo uriInfo, @Context SecurityContext sc)  throws UnsupportedEncodingException
    {
        
        logger.info( "[GET] " + uriInfo.getPath());
        
        HalList<ActiveElement> eligibles = null; 
        
        page = Utils.getValidPage(page);
        size = Utils.getValidSize(size);
        RowBounds rb = Utils.createRowBounds(page, size);
        
        UserPrincipal up = (UserPrincipal) sc.getUserPrincipal();
        SqlSession session = MyBatisConnectionFactory.getSession().openSession();

        try {
            GroupMemberMapper memberMapper  = session.getMapper( GroupMemberMapper.class);
            GroupEventMapper groupMapper    = session.getMapper( GroupEventMapper.class);
            
            Permission groupPermission = new Permission();
            
            groupMapper.getPermissions( up.getId(), parent.getId(), groupPermission);
            
            // We must be members or owners, or we are adding ourselves
            if( !(groupPermission.getLevel().equals( Permission.LEVEL_MEMBER) 
                    || groupPermission.getLevel().equals( Permission.LEVEL_OWNER)))
               throw new HTTPErrorException( Status.FORBIDDEN, "You are not allowed here");
            
            List<ActiveElement> eligibleList    = memberMapper.getEligibles(parent, up.getId(), rb);
            int total                           = memberMapper.getTotalEligibles(parent, up.getId());
            
            eligibles = new HalList( eligibleList, total);
            
            eligibles.setResourceRelativePath( uriInfo.getPath());
            eligibles.setPage(page);
            eligibles.setSize(size);
            
            session.commit();
         }catch( HTTPErrorException e) {
            throw e;
        }catch( Exception e) {
            logger.info( e);
            throw new HTTPErrorException( Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }finally {
            session.close();
        }
        return eligibles;
    }
   
    /**
     * Esta función gestiona las peticiones GET al recurso 
     * <code>/groups|events/{cid}/members/{id}</code>. Esta operación devuelve 
     * el miembro identificado por <b>id</b> del grupo o evento 
     * @param id identificador del miembro
     * @param sc información de seguridad y autenticación de la petición
     * @return la representación del miembro
     */
    @Path( "{id}")
    @GET
    @AuthenticationRequired( AuthenticationScheme.AUTHENTICATION_SCHEME_TOKEN)
    @Produces( { RepresentationFactory.HAL_JSON})
    public GroupMember getMember( @PathParam( "id") Long id,  @Context SecurityContext sc) {
        
        GroupMember member = null;
        UserPrincipal up   = (UserPrincipal) sc.getUserPrincipal();
        SqlSession session = MyBatisConnectionFactory.getSession().openSession();

        try {
            GroupMemberMapper memberMapper  = session.getMapper( GroupMemberMapper.class);
            GroupEventMapper groupMapper    = session.getMapper( GroupEventMapper.class);
            
            Permission groupPermission = new Permission();
            
            groupMapper.getPermissions( up.getId(), parent.getId(), groupPermission);
           
            if( !groupPermission.getLevel().equals( Permission.LEVEL_MEMBER) && !groupPermission.getLevel().equals( Permission.LEVEL_OWNER))
                throw new HTTPErrorException( Status.FORBIDDEN, "You are not allowed here");
            
            member = memberMapper.getMember(parent, id);
            
            if( member == null) {
                throw new HTTPErrorException( Status.NOT_FOUND, "member " + id + " not found");
            }
            
            session.commit();
         }catch( HTTPErrorException e) {
            throw e;
        }catch( Exception e) {
            logger.info( e);
            throw new HTTPErrorException( Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }finally {
            session.close();
        }
        return member;
    }
    
    /**
     * Esta función gestiona las peticiones DELETE al recurso 
     * <code>/groups|events/{cid}/members/{id}</code>. Esta operación elimina 
     * el miembro identificado por <b>id</b> del grupo o evento
     * @param id identificador del miembro
     * @param sc información de seguridad y autenticación de la petición
     */
    @Path( "{id}")
    @DELETE
    @AuthenticationRequired( AuthenticationScheme.AUTHENTICATION_SCHEME_TOKEN)
    public void deleteMember( @PathParam( "id") Long id, @Context SecurityContext sc) {
        
        GroupMember member = null;
        UserPrincipal up   = (UserPrincipal) sc.getUserPrincipal();
        SqlSession session = MyBatisConnectionFactory.getSession().openSession();

        try {
            GroupMemberMapper memberMapper  = session.getMapper( GroupMemberMapper.class);
            GroupEventMapper groupMapper    = session.getMapper( GroupEventMapper.class);
            
            Permission groupPermission = new Permission();
            
            groupMapper.getPermissions( up.getId(), parent.getId(), groupPermission);
           
            if( !groupPermission.getLevel().equals( Permission.LEVEL_OWNER) && !up.getId().equals( id))
                throw new HTTPErrorException( Status.FORBIDDEN, "You are not allowed here");
            
            member = memberMapper.getMember(parent, id);
            
            if( member == null) {
                throw new HTTPErrorException( Status.NOT_FOUND, "member " + id + " not found");
            }
            
            memberMapper.deleteMember(parent, id);
            
            session.commit();
         }catch( HTTPErrorException e) {
            throw e;
        }catch( Exception e) {
            logger.info( e);
            throw new HTTPErrorException( Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }finally {
            session.close();
        }
    }
}
