/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful;

import ejb.session.stateless.MovieEntityControllerLocal;
import entity.MovieEntity;
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
import util.exception.MovieNotFoundException;
import ws.restful.datamodel.ErrorRsp;
import ws.restful.datamodel.RetrieveAllMoviesByCinemaRsp;
import ws.restful.datamodel.RetrieveAllMoviesRsp;
import ws.restful.datamodel.RetrieveMovieRsp;

/**
 * REST Web Service
 *
 * @author Farhan Angullia
 */
@Path("Movie")
public class MovieResource {

    @Context
    private UriInfo context;

    private final MovieEntityControllerLocal movieEntityControllerLocal = lookupMovieEntityControllerLocal();

    /**
     * Creates a new instance of MovieResource
     */
    public MovieResource() {
    }

    @Path("retrieveAllMovies")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllMovies() {

        try {
            List<MovieEntity> movieEntities = movieEntityControllerLocal.retrieveAllMovieEntities();

            for (MovieEntity movieEntity : movieEntities) {

                for (ScreeningSchedule screeningSchedule : movieEntity.getScreeningSchedules()) {

                    screeningSchedule.getHallEntity().getScreeningSchedules().clear();
                    screeningSchedule.getHallEntity().getCinemaEntity().getHalls().clear();
                    screeningSchedule.getTicketEntities().clear();
                    screeningSchedule.setMovieEntity(null);
                   // screeningSchedule.getHallEntity().getCinemaEntity().getStaffEntities().clear();

                }

                //   movieEntity.getScreeningSchedules().clear();
            }

            RetrieveAllMoviesRsp retrieveAllMoviesRsp = new RetrieveAllMoviesRsp(movieEntities);

            return Response.status(Response.Status.OK).entity(retrieveAllMoviesRsp).build();
        } catch (Exception ex) {
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorRsp).build();
        }
    }

    @Path("retrieveAllMoviesByCinema/{cinemaId}")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllMoviesByCinema(@PathParam("cinemaId") Long cinemaId) {
        try {

            List<MovieEntity> movieEntities = movieEntityControllerLocal.retrieveAllMovieEntitiesByCinema(cinemaId);

            for (MovieEntity movieEntity : movieEntities) {

                for (ScreeningSchedule screeningSchedule : movieEntity.getScreeningSchedules()) {

                    screeningSchedule.getHallEntity().getScreeningSchedules().clear();
                    screeningSchedule.getHallEntity().getCinemaEntity().getHalls().clear();
                    screeningSchedule.getTicketEntities().clear();
                    screeningSchedule.setMovieEntity(null);
                   // screeningSchedule.getHallEntity().getCinemaEntity().getStaffEntities().clear();

                }

            }

            RetrieveAllMoviesByCinemaRsp retrieveAllMoviesByCinemaRsp = new RetrieveAllMoviesByCinemaRsp(movieEntities);

            return Response.status(Response.Status.OK).entity(retrieveAllMoviesByCinemaRsp).build();

        } catch (Exception ex) {
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());

            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorRsp).build();
        }
    }

    @Path("retrieveMovie/{movieId}")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveMovie(@PathParam("movieId") Long movieId) {
        try {

            MovieEntity movieEntity = movieEntityControllerLocal.retrieveMovieByMovieId(movieId);

            for (ScreeningSchedule screeningSchedule : movieEntity.getScreeningSchedules()) {

                screeningSchedule.getHallEntity().getScreeningSchedules().clear();
                screeningSchedule.getHallEntity().getCinemaEntity().getHalls().clear();
                screeningSchedule.getTicketEntities().clear();
                screeningSchedule.setMovieEntity(null);
              //  screeningSchedule.getHallEntity().getCinemaEntity().getStaffEntities().clear();

            }

            RetrieveMovieRsp retrieveMovieRsp = new RetrieveMovieRsp(movieEntity);

            return Response.status(Response.Status.OK).entity(retrieveMovieRsp).build();

        } catch (Exception ex) {
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());

            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorRsp).build();
        }
    }

    private MovieEntityControllerLocal lookupMovieEntityControllerLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (MovieEntityControllerLocal) c.lookup("java:global/GoldClassOnline/GoldClassOnline-ejb/MovieEntityController!ejb.session.stateless.MovieEntityControllerLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

}
