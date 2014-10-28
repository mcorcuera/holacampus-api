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

package com.holacampus.api.resources.particular;

import com.holacampus.api.domain.City;
import com.holacampus.api.domain.CommentContainer;
import com.holacampus.api.domain.GroupEvent;
import com.holacampus.api.domain.Permission;
import com.holacampus.api.domain.PhotoContainer;
import com.holacampus.api.domain.ProfilePhotoContainer;
import com.holacampus.api.domain.University;
import com.holacampus.api.domain.User;
import com.holacampus.api.exceptions.HTTPErrorException;
import com.holacampus.api.hal.HalList;
import com.holacampus.api.mappers.CityMapper;
import com.holacampus.api.mappers.GroupEventMapper;
import com.holacampus.api.mappers.UniversityMapper;
import com.holacampus.api.mappers.UserMapper;
import com.holacampus.api.security.AuthenticationRequired;
import com.holacampus.api.security.AuthenticationScheme;
import com.holacampus.api.security.PermissionScheme;
import com.holacampus.api.security.PermissionScheme.Action;
import com.holacampus.api.security.UserPrincipal;
import com.holacampus.api.subresources.ConversationsResource;
import com.holacampus.api.subresources.CommentsResource;
import com.holacampus.api.subresources.PhotosResource;
import com.holacampus.api.subresources.ProfilePhotoResource;
import com.holacampus.api.subresources.StudiesResource;
import com.holacampus.api.utils.MyBatisConnectionFactory;
import com.holacampus.api.utils.Utils;
import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;
import com.theoryinpractise.halbuilder.api.RepresentationFactory;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.Encoded;
import javax.ws.rs.GET;
import javax.ws.rs.InternalServerErrorException;
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
 * Esta clase se encarga de gestionar las peticiones de la API a un recurso
 * University particualr. Es decir, gestiona las peticiones a la URL 
 * <code>/universities/{id}</code> donde <code>id</code> es el identificador
 * de la universidad
 * @author Mikel Corcuera <mik.corcuera@gmail.com>
 */

@Path( "/universities/{id}")
public class ParticularUniversityResource {
    
    private static final Logger logger = LogManager.getLogger( ParticularUniversityResource.class.getName());

    @Context
    private SecurityContext sc;
    
    @Context
    private UriInfo uriInfo;
    
    @PathParam( "id")
    private Long id;
    
    private static final PermissionScheme commentsScheme  = new PermissionScheme();
    private static final PermissionScheme photosScheme    = new PermissionScheme();
    private static final PermissionScheme photosCommentScheme   = new PermissionScheme();
    
    static {
        commentsScheme  .addPermissionScheme( Action.GET_MULTIPLE, Permission.LEVEL_USER)
                        .addPermissionScheme(Action.POST_MULTIPLE, Permission.LEVEL_USER)
                        .addPermissionScheme(Action.GET_UNIQUE, Permission.LEVEL_USER)
                        .addPermissionScheme(Action.DELETE_UNIQUE, Permission.LEVEL_PARENT_OWNER);
        
        photosScheme    .addPermissionScheme( Action.GET_MULTIPLE, Permission.LEVEL_USER)
                        .addPermissionScheme(Action.POST_MULTIPLE, Permission.LEVEL_PARENT_OWNER)
                        .addPermissionScheme(Action.GET_UNIQUE, Permission.LEVEL_USER)
                        .addPermissionScheme(Action.PUT_UNIQUE, Permission.LEVEL_OWNER)
                        .addPermissionScheme(Action.DELETE_UNIQUE, Permission.LEVEL_PARENT_OWNER);
        
        photosCommentScheme     .addPermissionScheme( Action.GET_MULTIPLE, Permission.LEVEL_USER)
                                .addPermissionScheme(Action.POST_MULTIPLE, Permission.LEVEL_USER)
                                .addPermissionScheme(Action.GET_UNIQUE, Permission.LEVEL_USER)
                                .addPermissionScheme(Action.DELETE_UNIQUE, Permission.LEVEL_PARENT_OWNER);
    }
    
    /**
     * Esta función devuelve la clase que se encargará de gestionar las peticiones
     * relacionadas con los comentarios de la universidad. Esto es, a la URL 
     * <code>/universities/{id}/comments</code>
     * @return recurso que se encarga de gestionar la petición
     */
    @Path("/comments")
    public CommentsResource getCommentResource() 
    {
        SqlSession session = MyBatisConnectionFactory.getSession().openSession();
        CommentContainer c;
        try {            
            UniversityMapper uniMapper   = session.getMapper( UniversityMapper.class);
            c = uniMapper.getCommentContainer(id);
            session.commit();            
            
            if( c == null) {
                throw new HTTPErrorException( Response.Status.NOT_FOUND, "University " + id + " not found");
            }
        } catch( Exception e) {
            logger.error( e.toString());
            throw new HTTPErrorException( Response.Status.NOT_FOUND, "University " + id + " not found");
        } finally {
            session.close();
        }
        
        return new CommentsResource( c, uriInfo.getPath(), commentsScheme);
    }
    
     /**
     * Esta función devuelve la clase que se encargará de gestionar las peticiones
     * relacionadas con las fotos de la universidad. Esto es, a la URL 
     * <code>/universities/{id}/photos</code>
     * @return recurso que se encarga de gestionar la petición
     */
    @Path( "/photos")
    public PhotosResource getPhotosResource( )
    {
        SqlSession session = MyBatisConnectionFactory.getSession().openSession();
        PhotoContainer c;
        try {            
             UniversityMapper uniMapper   = session.getMapper( UniversityMapper.class);
            c = uniMapper.getPhotoContainer(id);
            session.commit();            
            
            if( c == null) {
                throw new HTTPErrorException( Response.Status.NOT_FOUND, "University " + id + " not found");
            }
        } catch( Exception e) {
            logger.error( e.toString());
            throw new HTTPErrorException( Response.Status.NOT_FOUND, "University " + id + " not found");
        } finally {
            session.close();
        }  
        
        return new PhotosResource( c, uriInfo.getPath(), photosScheme, photosCommentScheme);
    }
    
     /**
     * Esta función devuelve la clase que se encargará de gestionar las peticiones
     * relacionadas con la foto de perfil de la universidad. Esto es, a la URL 
     * <code>/universities/{id}/profile-photo</code>
     * @return recurso que se encarga de gestionar la petición
     */
    @Path( "/profile-photo")
    public ProfilePhotoResource getProfilePhotoResource()
    {
        SqlSession session = MyBatisConnectionFactory.getSession().openSession();
        PhotoContainer pc;
        ProfilePhotoContainer ppc;
        try {            
            UniversityMapper uniMapper  = session.getMapper( UniversityMapper.class);
            pc                          = uniMapper.getPhotoContainer(id);
            ppc                         = uniMapper.getProfilePhotoContainer(id);
            
            if( pc == null) {
                throw new HTTPErrorException( Response.Status.NOT_FOUND, "University " + id + " not found");
            }
            
            session.commit();            

        } catch( Exception e) {
            logger.error( e.toString());
            throw new HTTPErrorException( Response.Status.NOT_FOUND, "University " + id + " not found");
        } finally {
            session.close();
        }  
        
        return new ProfilePhotoResource( pc, ppc, uriInfo.getPath());
    }
    
     /**
     * Esta función devuelve la clase que se encargará de gestionar las peticiones
     * relacionadas con los estudios de la universidad. Esto es, a la URL 
     * <code>/universities/{id}/studies</code>
     * @return recurso que se encarga de gestionar la petición
     */
    @Path("/studies")
    public StudiesResource getStudiesResource()    
    {
        SqlSession session = MyBatisConnectionFactory.getSession().openSession();
        University university;
        try {            
            UniversityMapper uniMapper  = session.getMapper( UniversityMapper.class);
            university                  = uniMapper.getUniversity(id);
            
            if( university == null) {
                throw new HTTPErrorException( Response.Status.NOT_FOUND, "University " + id + " not found");
            }
            
            session.commit();            

        } catch( Exception e) {
            logger.error( e.toString());
            throw new HTTPErrorException( Response.Status.NOT_FOUND, "University " + id + " not found");
        } finally {
            session.close();
        }  
        
        return new StudiesResource( university);
    }
    
    /**
     * Esta función devuelve la clase que se encargará de gestionar las peticiones
     * relacionadas con las conversaciones de la universidad. Esto es, a la URL 
     * <code>/universities/{id}/conversations</code>
     * @return recurso que se encarga de gestionar la petición
     */
    @Path("/conversations")
    public ConversationsResource getConversationsResource()    
    {
        SqlSession session = MyBatisConnectionFactory.getSession().openSession();
        University university;
        try {            
            UniversityMapper uniMapper  = session.getMapper( UniversityMapper.class);
            university                  = uniMapper.getUniversity(id);
            
            if( university == null) {
                throw new HTTPErrorException( Response.Status.NOT_FOUND, "University " + id + " not found");
            }
            
            session.commit();            

        } catch( Exception e) {
            logger.error( e.toString());
            throw new HTTPErrorException( Response.Status.NOT_FOUND, "University " + id + " not found");
        } finally {
            session.close();
        }  
        
        return new ConversationsResource( university);
    }
    
    /**
     * Esta función gestiona las peticiones GET al recurso 
     * <code>/universities/{id}</code>. Esta operación devuelve la representación
     * de la universidad identificada por <b>id</b>
     * @return la representación de la universidad
     */
    @GET
    @AuthenticationRequired( AuthenticationScheme.AUTHENTICATION_SCHEME_TOKEN)
    @Produces( { RepresentationFactory.HAL_JSON})
    public University getUniversity( )
    {
        logger.info( "[GET] " + uriInfo.getPath());
        
        University university;
        UserPrincipal up = (UserPrincipal) sc.getUserPrincipal();
        
        SqlSession session = MyBatisConnectionFactory.getSession().openSession();
        
        try {            
            UniversityMapper uniMapper   = session.getMapper( UniversityMapper.class);
            university = uniMapper.getUniversity(id);      
            
            if( university == null) {
                throw new HTTPErrorException( Response.Status.NOT_FOUND, "University not found");
            }
            
            if( university.getProfilePhoto() != null) {
                university.getProfilePhoto().setProfilePhoto( true);
                university.getProfilePhoto().setSelfLink( uriInfo.getPath() + "/profile-photo");
            }
            
            Permission permission = new Permission();
            
            uniMapper.getPermissions( up.getId(), id, permission);
            university.setPermission(permission);
            
            session.commit();      
        }catch( HTTPErrorException e) {
            throw e;
        }catch( Exception e) {
            logger.error( e.toString());
            throw new HTTPErrorException( Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
        } finally {
            session.close();
        }   
        return university;
    }

    /**
      * Esta función gestiona las peticiones GET al recurso 
     * <code>/universities/{id}/groups|events</code>. Esta operación devuelve una 
     * lista con los grupos o eventos a los que pertenece la 
     * universidad identificada por <b>id</b>
     * @param type identifica si se trata de una petición de grupos o eventos
     * @param page página de los resultados
     * @param size tamaño de los resultados
     * @param q cadena de caracteres que sirve para filtrar por nombre los
     * resultados
     * @return lista con los grupos o eventos del tamaño especificado y filtrada
     * por nombre
     * @throws UnsupportedEncodingException
     */
    @Path( "/{type:groups|events}")
    @GET
    @Produces( { RepresentationFactory.HAL_JSON})
    @AuthenticationRequired( AuthenticationScheme.AUTHENTICATION_SCHEME_TOKEN)
    @Encoded
    public HalList<GroupEvent> getGroupsEvents( @PathParam("type") String type, 
            @QueryParam("page") Integer page, @QueryParam( "size") Integer size, @QueryParam( "q") String q) throws UnsupportedEncodingException
    {
        return getGroupsEvents( type, page, size, q, true);
    }
    
    /**
      * Esta función gestiona las peticiones GET al recurso 
     * <code>/universities/{id}/events/past</code>. Esta operación devuelve una 
     * lista con los eventos ya pasados a los que pertenece la 
     * universidad identificada por <b>id</b>
     * @param page página de los resultados
     * @param size tamaño de los resultados
     * @param q cadena de caracteres que sirve para filtrar por nombre los
     * resultados
     * @return lista con los eventos pasados del tamaño especificado y filtrada
     * por nombre
     * @throws UnsupportedEncodingException
     */
    @Path( "/events/past")  
    @GET
    @Produces( { RepresentationFactory.HAL_JSON})
    @AuthenticationRequired( AuthenticationScheme.AUTHENTICATION_SCHEME_TOKEN)
    @Encoded
    public HalList<GroupEvent> getPastEvents( @QueryParam("page") Integer page, @QueryParam( "size") Integer size, @QueryParam( "q") String q) throws UnsupportedEncodingException
    {
        return getGroupsEvents( GroupEvent.TYPE_EVENT, page, size, q, false);
    }
    
    private HalList<GroupEvent> getGroupsEvents( String type, Integer page,  Integer size,  String q, Boolean pending) throws UnsupportedEncodingException
    {
        page = Utils.getValidPage(page);
        size = Utils.getValidSize(size);
        if( q != null) {
            q   = URLDecoder.decode(q, "UTF-8");
        }
        String elementType = null;
        if( "groups".equals(type))
            elementType = GroupEvent.TYPE_GROUP;
        else
            elementType = GroupEvent.TYPE_EVENT;
        
        RowBounds rb                = Utils.createRowBounds(page, size);
        UserPrincipal up            = (UserPrincipal) sc.getUserPrincipal(); 
        SqlSession session          = MyBatisConnectionFactory.getSession().openSession();
        HalList<GroupEvent> groups  = null;
        
        try {            
            GroupEventMapper mapper     = session.getMapper( GroupEventMapper.class);
            UniversityMapper uniMapper  = session.getMapper( UniversityMapper.class);
            
            University uni = uniMapper.getUniversity(id);
            if( uni == null)
                throw new HTTPErrorException( Status.NOT_FOUND, "University not found");
           
            List<GroupEvent> groupList  = mapper.getGroupsEventsForActiveElement(elementType, q, id, pending, rb);
            int total                   = mapper.getTotalGroupsEventsForActiveElement(elementType, q, id, pending);
            session.commit();
            
            for( GroupEvent group : groupList) {
                Permission permissions = new Permission();
                mapper.getPermissions( up.getId(), group.getId(), permissions);
                group.setPermission(permissions); 
            }
            
            groups = new HalList( groupList, total);
            
            groups.setResourceRelativePath( uriInfo.getPath());
            groups.setPage(page);
            groups.setSize(size);
            groups.setQuery(q);
            
        }catch( Exception e) {
            logger.error( e.toString());
            throw new InternalServerErrorException();
        }
        finally {
            session.close();
        }
        
        return groups;
    }
    
  
    
    /**
     * Esta función gestiona las peticiones GET al recurso 
     * <code>/universities/{id}/students</code>. Esta operación devuelve una 
     * lista con los estudiantes de la  
     * universidad identificada por <b>id</b>
     * @param page página de los resultados
     * @param size tamaño de los resultados
     * @param q cadena de caracteres que sirve para filtrar por nombre los
     * resultados
     * @return lista con los estudiantes del tamaño especificado y filtrada
     * por nombre
     * @throws UnsupportedEncodingException
     */
    @Path("/students")
    @GET
    @AuthenticationRequired( AuthenticationScheme.AUTHENTICATION_SCHEME_TOKEN)
    @Produces( { RepresentationFactory.HAL_JSON})
    @Encoded
    public HalList<User> getStudents(@QueryParam("page") Integer page, 
            @QueryParam( "size") Integer size, @QueryParam( "q") String q )  throws UnsupportedEncodingException
    {
        
        logger.info( "[GET] " + uriInfo.getPath());
        
        HalList<User> users = null; 
        
        page = Utils.getValidPage(page);
        size = Utils.getValidSize(size);
        if( q != null) {
            q   = URLDecoder.decode(q, "UTF-8");
        }
        RowBounds rb = Utils.createRowBounds(page, size);
        UserPrincipal up = (UserPrincipal) sc.getUserPrincipal();
        SqlSession session = MyBatisConnectionFactory.getSession().openSession();

        try {
            UniversityMapper mapper  = session.getMapper( UniversityMapper.class);
            UserMapper userMapper   = session.getMapper( UserMapper.class);
            List<User> usersList    = mapper.getStudents( id, q, rb);
            int total               = mapper.getTotalStudents(id, q);
            
            for( User user : usersList) {
                Permission permissions = new Permission();
                userMapper.getPermissions( up.getId(), user.getId(), permissions);
                user.setPermission(permissions);
            }
            
            users = new HalList( usersList, total);
            
            users.setResourceRelativePath( uriInfo.getPath());
            users.setPage(page);
            users.setSize(size);
            users.setQuery(q);
            
            session.commit();
         }catch( HTTPErrorException e) {
            throw e;
        }catch( Exception e) {
            logger.info( e);
            throw new HTTPErrorException( Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }finally {
            session.close();
        }
        return users;
    }
    
    /**
     * Esta función gestiona las peticiones GET al recurso 
     * <code>/universities/{id}/cities</code>. Esta operación devuelve una 
     * lista con las ciudades en donde se encuentra la 
     * universidad identificada por <b>id</b>
     * @param page página de los resultados
     * @param size tamaño de los resultados
     * @return lista con los estudiantes del tamaño especificado
     */
    @Path( "/cities")
    @GET
    @AuthenticationRequired( AuthenticationScheme.AUTHENTICATION_SCHEME_TOKEN)
    @Produces( { RepresentationFactory.HAL_JSON})
    public HalList<City> getUniversityCities(@QueryParam("page") Integer page, 
            @QueryParam( "size") Integer size)
    {
        HalList<City> cities;
         
        page = Utils.getValidPage(page);
        size = Utils.getValidSize(size);
        RowBounds rb = Utils.createRowBounds(page, size);
        
        SqlSession session = MyBatisConnectionFactory.getSession().openSession();

        try {
            UniversityMapper uniMapper      = session.getMapper( UniversityMapper.class);
            University university           = uniMapper.getUniversity(id);
            
            if( university == null)
                throw new HTTPErrorException( Status.NOT_FOUND, "University " + id + " not found");
            
            List<City> cityList = uniMapper.getCities(id, rb);
            int total           = uniMapper.getTotalCities( id);
            session.commit();
            
            cities = new HalList<City> ( cityList, total);
            cities.setResourceRelativePath( uriInfo.getPath());
            cities.setPage(page);
            cities.setSize(size);
        }catch( HTTPErrorException e) {
            throw e;
        }catch( Exception e) {
            logger.info( e);
             throw new HTTPErrorException( Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }finally {
            session.close();
        }

        return cities;
    }
    
     /**
     * Esta función gestiona las peticiones POST al recurso 
     * <code>/universities/{id}/cities</code>. Esta operación añade una nueva
     * ciudad a la lista de ciudades en donde se encuentra la universidad 
     * identificada por <b>id</b>
     * @param city datos de la ciudad a añadir
     * @return representación de la ciudad añadida
     */
    @Path( "/cities")
    @POST
    @AuthenticationRequired( AuthenticationScheme.AUTHENTICATION_SCHEME_TOKEN)
    @Consumes( { RepresentationFactory.HAL_JSON, MediaType.APPLICATION_JSON})
    @Produces( { RepresentationFactory.HAL_JSON})
    public City addCity( City city)
    {
        if( city.getCountry() == null || city.getCountry().getId() == null)
            throw new HTTPErrorException( Status.BAD_REQUEST, "You must provide the city belonging country");
        
        UserPrincipal up = (UserPrincipal) sc.getUserPrincipal();
        
        if( !up.getId().equals( id))
            throw new HTTPErrorException( Status.FORBIDDEN, "You are not allowed here");
        
        SqlSession session = MyBatisConnectionFactory.getSession().openSession();

        try {
            UniversityMapper    uniMapper   = session.getMapper( UniversityMapper.class);
            CityMapper          cityMapper  = session.getMapper( CityMapper.class);    
            int result                      = uniMapper.insertCity(id, city);
            
            if( result == 0)
                throw new HTTPErrorException( Status.INTERNAL_SERVER_ERROR, "Problem inserting city");
            city = cityMapper.getCity( city.getCountry(), city.getId());
            session.commit();
         }catch( MySQLIntegrityConstraintViolationException e) {
            throw new HTTPErrorException( Status.CONFLICT, "City already exists in database");
        }catch( HTTPErrorException e) {
            throw e;
        }catch( Exception e) {
            logger.info( e);
            throw new HTTPErrorException( Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }finally {
            session.close();
        }
        
        return city;
    }
    
    /**
     * Esta función gestiona las peticiones DELETE al recurso 
     * <code>/universities/{id}/cities</code>. Esta operación elimina una
     * ciudad de la lista de ciudades en donde se encuentra la universidad 
     * identificada por <b>id</b>
     * @param cityId identificador de la ciudad a eliminar
     * @param countryId identificador del país al que pertenece la ciudad a 
     * eliminar
     */
    @Path( "/cities")
    @DELETE
    @AuthenticationRequired( AuthenticationScheme.AUTHENTICATION_SCHEME_TOKEN)
    @Consumes( { RepresentationFactory.HAL_JSON, MediaType.APPLICATION_JSON})
    public void deleteCity( @QueryParam("city") long cityId, @QueryParam("country") long countryId)
    {
        UserPrincipal up = (UserPrincipal) sc.getUserPrincipal();
        
        if( !up.getId().equals( id))
            throw new HTTPErrorException( Status.FORBIDDEN, "You are not allowed here");
        
        SqlSession session = MyBatisConnectionFactory.getSession().openSession();

        try {
            UniversityMapper    uniMapper   = session.getMapper( UniversityMapper.class);
            int result                      = uniMapper.deleteCity(id, cityId, countryId);
            
            if( result == 0)
                throw new HTTPErrorException( Status.INTERNAL_SERVER_ERROR, "Problem deleting city");
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
