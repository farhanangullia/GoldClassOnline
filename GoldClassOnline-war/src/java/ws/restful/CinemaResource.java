/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful;

import ejb.session.stateless.CinemaEntityControllerLocal;
import entity.CinemaEntity;
import entity.HallEntity;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.GET;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import ws.restful.datamodel.ErrorRsp;
import ws.restful.datamodel.RetrieveAllCinemasRsp;

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
        System.out.println("YA 1");
        try {
            List<CinemaEntity> cinemaEntities = cinemaEntityControllerLocal.retrieveAllCinemaEntities();

            System.out.println("YA");

            for (CinemaEntity cinemaEntity : cinemaEntities) {
                cinemaEntity.getHalls().clear();

            }

            RetrieveAllCinemasRsp retrieveAllCinemasRsp = new RetrieveAllCinemasRsp(cinemaEntities);

            return Response.status(Response.Status.OK).entity(retrieveAllCinemasRsp).build();
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
