/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful;

import ejb.session.stateless.TicketEntityControllerLocal;
import entity.TicketEntity;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBElement;
import ws.restful.datamodel.ErrorRsp;
import ws.restful.datamodel.RemoteCheckoutReq;
import ws.restful.datamodel.RemoteCheckoutRsp;
import ws.restful.datamodel.RetrieveAllTicketsByCustomerRsp;
import ws.restful.datamodel.RetrieveAllTicketsByScreeningScheduleRsp;

/**
 * REST Web Service
 *
 * @author Farhan Angullia
 */
@Path("Ticket")
public class TicketResource {

    @Context
    private UriInfo context;

    private final TicketEntityControllerLocal ticketEntityControllerLocal = lookupTicketEntityControllerLocal();

    /**
     * Creates a new instance of TicketResource
     */
    public TicketResource() {
    }

    @PUT
    @Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    @Produces(MediaType.APPLICATION_JSON)
    public Response remoteCheckout(JAXBElement<RemoteCheckoutReq> jaxbRemoteCheckoutReq) {
        if ((jaxbRemoteCheckoutReq != null) && (jaxbRemoteCheckoutReq.getValue() != null)) {
            try {
                RemoteCheckoutReq remoteCheckoutReq = jaxbRemoteCheckoutReq.getValue();

                System.out.println("DEBUG CHECKOUT");
                String username = remoteCheckoutReq.getUsername();

                System.out.println(username);

                List<TicketEntity> tickets = ticketEntityControllerLocal.buyTicketsFromRemoteCheckoutRequest(username, remoteCheckoutReq.getRemoteCheckoutLineItems());
                System.out.println("DEBUG CHECKOUT 2");

                for (TicketEntity ticket : tickets) {
                    ticket.setCustomerEntity(null);
                    ticket.getScreeningSchedule().getTicketEntities().clear();
                    ticket.getScreeningSchedule().getHallEntity().getScreeningSchedules().clear();
                    ticket.getScreeningSchedule().getHallEntity().getCinemaEntity().getHalls().clear();
                    ticket.getScreeningSchedule().getMovieEntity().getScreeningSchedules().clear();
                    ticket.getScreeningSchedule().getHallEntity().setSeating(null); //dont show redundant seating

                }

                RemoteCheckoutRsp remoteCheckoutRsp = new RemoteCheckoutRsp(tickets);

                return Response.status(Response.Status.OK).entity(remoteCheckoutRsp).build();
            } catch (Exception ex) {
                return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
            }

        } else {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid remote checkout request").build();
        }
    }

    @Path("retrieveAllTicketsByCustomer/{customerId}")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllTicketsByCustomer(@PathParam("customerId") Long customerId) {
        try {

            List<TicketEntity> tickets = ticketEntityControllerLocal.retrieveAllTicketsByCustomer(customerId);

            RetrieveAllTicketsByCustomerRsp retrieveAllTicketsByCustomerRsp = new RetrieveAllTicketsByCustomerRsp(tickets);

            for (TicketEntity ticket : tickets) {
                ticket.setCustomerEntity(null);
                ticket.getScreeningSchedule().getTicketEntities().clear();
                ticket.getScreeningSchedule().getHallEntity().getScreeningSchedules().clear();
                ticket.getScreeningSchedule().getHallEntity().getCinemaEntity().getHalls().clear();
                ticket.getScreeningSchedule().getMovieEntity().getScreeningSchedules().clear();
                ticket.getScreeningSchedule().getHallEntity().setSeating(null); //dont show redundant seating

            }

            return Response.status(Response.Status.OK).entity(retrieveAllTicketsByCustomerRsp).build();

        } catch (Exception ex) {
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());

            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorRsp).build();
        }
    }

    @Path("retrieveAllTicketsByScreeningSchedule/{screeningScheduleId}")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllTicketsByScreeningSchedule(@PathParam("screeningScheduleId") Long screeningScheduleId) {
        try {

            List<TicketEntity> tickets = ticketEntityControllerLocal.retrieveAllTicketsByScreeningSchedule(screeningScheduleId);

            RetrieveAllTicketsByScreeningScheduleRsp retrieveAllTicketsByScreeningScheduleRsp = new RetrieveAllTicketsByScreeningScheduleRsp(tickets);

            for (TicketEntity ticket : tickets) {
                ticket.setCustomerEntity(null);
                ticket.getScreeningSchedule().getTicketEntities().clear();
                ticket.getScreeningSchedule().getHallEntity().getScreeningSchedules().clear();
                ticket.getScreeningSchedule().getHallEntity().getCinemaEntity().getHalls().clear();
                ticket.getScreeningSchedule().getMovieEntity().getScreeningSchedules().clear();
                ticket.getScreeningSchedule().getHallEntity().setSeating(null); //dont show redundant seating

            }

            return Response.status(Response.Status.OK).entity(retrieveAllTicketsByScreeningScheduleRsp).build();

        } catch (Exception ex) {
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());

            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorRsp).build();
        }
    }

    private TicketEntityControllerLocal lookupTicketEntityControllerLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (TicketEntityControllerLocal) c.lookup("java:global/GoldClassOnline/GoldClassOnline-ejb/TicketEntityController!ejb.session.stateless.TicketEntityControllerLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
}
