/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful;

import ejb.session.stateless.CinemaEntityControllerLocal;
import entity.CinemaEntity;
import entity.HallEntity;
import entity.ScreeningSchedule;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import ws.restful.datamodel.ErrorRsp;
import ws.restful.datamodel.RetrieveAllCinemasByMovieRsp;
import ws.restful.datamodel.RetrieveAllCinemasRsp;
import ws.restful.datamodel.RetrieveCinemaRsp;

/**
 * REST Web Service
 *
 * @author Farhan Angullia
 */
@Path("Cinema")
public class CinemaResource {

    @Context
    private UriInfo context;

    private final CinemaEntityControllerLocal cinemaEntityControllerLocal = lookupCinemaEntityControllerLocal();

    /**
     * Creates a new instance of CinemaResource
     */
    public CinemaResource() {
    }

    @Path("retrieveAllCinemas")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllCinemas() {

        try {
            List<CinemaEntity> cinemaEntities = cinemaEntityControllerLocal.retrieveAllCinemaEntities();

            for (CinemaEntity cinemaEntity : cinemaEntities) {
                cinemaEntity.getHalls().clear();
               // cinemaEntity.getStaffEntities().clear();

            }

            RetrieveAllCinemasRsp retrieveAllCinemasRsp = new RetrieveAllCinemasRsp(cinemaEntities);

            return Response.status(Response.Status.OK).entity(retrieveAllCinemasRsp).build();
        } catch (Exception ex) {
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorRsp).build();
        }
    }

    @Path("retrieveAllCinemasByMovie/{movieId}")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllCinemasByMovie(@PathParam("movieId") Long movieId) {
        try {

            List<CinemaEntity> cinemaEntities = cinemaEntityControllerLocal.retrieveAllCinemaEntitiesByMovie(movieId);

            for (CinemaEntity cinemaEntity : cinemaEntities) {
             
               // cinemaEntity.getStaffEntities().clear();
                
                for(HallEntity hall:cinemaEntity.getHalls())
                {
                    hall.setCinemaEntity(null);
                   for(ScreeningSchedule screeningSchedule:hall.getScreeningSchedules())
                   {
                       screeningSchedule.getTicketEntities().clear();
                       screeningSchedule.setHallEntity(null);
                       screeningSchedule.setMovieEntity(null);
                       
                   }
                    
                }

            }

            RetrieveAllCinemasByMovieRsp retrieveAllCinemasByMovieRsp = new RetrieveAllCinemasByMovieRsp(cinemaEntities);

            return Response.status(Response.Status.OK).entity(retrieveAllCinemasByMovieRsp).build();

        } catch (Exception ex) {
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());

            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorRsp).build();
        }
    }
    
    
     @Path("retrieveCinema/{cinemaId}")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveCinema(@PathParam("cinemaId") Long cinemaId) {
        try {

            CinemaEntity cinemaEntity = cinemaEntityControllerLocal.retrieveCinemaByCinemaId(cinemaId);

       
            
             cinemaEntity.getStaffEntities().clear();
                
                for(HallEntity hall:cinemaEntity.getHalls())
                {
                    hall.setCinemaEntity(null);
                   for(ScreeningSchedule screeningSchedule:hall.getScreeningSchedules())
                   {
                       screeningSchedule.getTicketEntities().clear();
                       screeningSchedule.setHallEntity(null);
                       screeningSchedule.setMovieEntity(null);
                       
                   }
                    
                }
            

            RetrieveCinemaRsp retrieveCinemaRsp = new RetrieveCinemaRsp(cinemaEntity);

            return Response.status(Response.Status.OK).entity(retrieveCinemaRsp).build();

        } catch (Exception ex) {
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());

            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorRsp).build();
        }
    }
    
    
    
    
    
    
    
    
    
    
    
    

    private CinemaEntityControllerLocal lookupCinemaEntityControllerLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (CinemaEntityControllerLocal) c.lookup("java:global/GoldClassOnline/GoldClassOnline-ejb/CinemaEntityController!ejb.session.stateless.CinemaEntityControllerLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

}
