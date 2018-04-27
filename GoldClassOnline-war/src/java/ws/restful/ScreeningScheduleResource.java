/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful;

import ejb.session.stateless.ScreeningScheduleControllerLocal;
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
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import ws.restful.datamodel.ErrorRsp;
import ws.restful.datamodel.RetrieveAllScreeningSchedulesByCinemaAndMovieRsp;
import ws.restful.datamodel.RetrieveAllScreeningSchedulesByMovieRsp;

/**
 * REST Web Service
 *
 * @author Farhan Angullia
 */
@Path("ScreeningSchedule")
public class ScreeningScheduleResource {

    @Context
    private UriInfo context;

    private final ScreeningScheduleControllerLocal screeningScheduleControllerLocal = lookupScreeningScheduleControllerLocal();

    /**
     * Creates a new instance of ScreeningScheduleResource
     */
    public ScreeningScheduleResource() {
    }

    @Path("retrieveAllScreeningSchedulesByMovie")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllScreeningSchedulesByMovie(@QueryParam("movieId") Long movieId) {
        try {

            List<ScreeningSchedule> screeningSchedules = screeningScheduleControllerLocal.retrieveAllScreeningSchedulesByMovie(movieId);

            for (ScreeningSchedule ss : screeningSchedules) {

                ss.setHallEntity(null);
                ss.getTicketEntities().clear();
                ss.setMovieEntity(null);

            }

            RetrieveAllScreeningSchedulesByMovieRsp retrieveAllScreeningSchedulesByMovieRsp = new RetrieveAllScreeningSchedulesByMovieRsp(screeningSchedules);

            return Response.status(Response.Status.OK).entity(retrieveAllScreeningSchedulesByMovieRsp).build();
        } catch (Exception ex) {
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());

            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorRsp).build();
        }
    }

    @Path("retrieveAllScreeningSchedulesByCinemaAndMovie")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllScreeningSchedulesByCinemaAndMovie(@QueryParam("movieId") Long movieId, @QueryParam("cinemaId") Long cinemaId) {
        try {

            List<ScreeningSchedule> screeningSchedules = screeningScheduleControllerLocal.retrieveAllScreeningSchedulesByCinemaAndMovie(movieId, cinemaId);

            RetrieveAllScreeningSchedulesByCinemaAndMovieRsp retrieveAllScreeningSchedulesByCinemaAndMovieRsp = new RetrieveAllScreeningSchedulesByCinemaAndMovieRsp(screeningSchedules);

            for (ScreeningSchedule ss : screeningSchedules) {

                ss.setHallEntity(null);
                ss.getTicketEntities().clear();
                ss.setMovieEntity(null);
            }

            return Response.status(Response.Status.OK).entity(retrieveAllScreeningSchedulesByCinemaAndMovieRsp).build();
        } catch (Exception ex) {
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());

            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorRsp).build();
        }
    }

    private ScreeningScheduleControllerLocal lookupScreeningScheduleControllerLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (ScreeningScheduleControllerLocal) c.lookup("java:global/GoldClassOnline/GoldClassOnline-ejb/ScreeningScheduleController!ejb.session.stateless.ScreeningScheduleControllerLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

}
