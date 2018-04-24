/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful;

import ejb.session.stateless.CustomerEntityControllerLocal;
import entity.CustomerEntity;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBElement;
import util.exception.CreateCustomerException;
import util.exception.InvalidLoginCredentialException;
import ws.restful.datamodel.CreateCustomerReq;
import ws.restful.datamodel.CreateCustomerRsp;
import ws.restful.datamodel.ErrorRsp;
import ws.restful.datamodel.GetCustomerRsp;

/**
 * REST Web Service
 *
 * @author Farhan Angullia
 */
@Path("Customer")
public class CustomerResource {

    @Context
    private UriInfo context;

    private final CustomerEntityControllerLocal customerEntityControllerLocal = lookupCustomerControllerLocal();

    /**
     * Creates a new instance of CustomerResource
     */
    public CustomerResource() {
    }

    @Path("getCustomer")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCustomer(@QueryParam("username") String username,
            @QueryParam("password") String password) {
        try {
            CustomerEntity customer = customerEntityControllerLocal.customerLogin(username, password);
            System.out.println("********** CustomerResource.getCustomer(): Customer " + customer.getUsername() + " login remotely via web service");

            customer.setPassword(null);       //for security purposes on client side
            customer.setSalt(null);

            customer.getTicketEntities().clear();

            return Response.status(Response.Status.OK).entity(new GetCustomerRsp(customer)).build();
        } catch (InvalidLoginCredentialException ex) {
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());

            return Response.status(Response.Status.UNAUTHORIZED).entity(errorRsp).build();
        } catch (Exception ex) {
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());

            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorRsp).build();
        }
    }


    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createCustomer(JAXBElement<CreateCustomerReq> jaxbCreateCustomerReq) {
        System.out.println("HERE asdsad");
        if ((jaxbCreateCustomerReq != null) && (jaxbCreateCustomerReq.getValue() != null)) {
            try {
                System.out.println("HERE");

                CreateCustomerReq createCustomerReq = jaxbCreateCustomerReq.getValue();
                System.out.println("HERE E E");
                Long id = customerEntityControllerLocal.createCustomerEntity(createCustomerReq.getCustomerEntity());
                System.out.println("HERE 2");
                CreateCustomerRsp createCustomerRsp = new CreateCustomerRsp(id);
                System.out.println("HERE 3");
                return Response.status(Response.Status.OK).entity(createCustomerRsp).build();
            } catch (CreateCustomerException ex) {
                ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());

                return Response.status(Response.Status.BAD_REQUEST).entity(errorRsp).build();
            } catch (Exception ex) {
                ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());

                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorRsp).build();
            }
        } else {
            ErrorRsp errorRsp = new ErrorRsp("Invalid create customer request");

            return Response.status(Response.Status.BAD_REQUEST).entity(errorRsp).build();
        }
    }

    private CustomerEntityControllerLocal lookupCustomerControllerLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (CustomerEntityControllerLocal) c.lookup("java:global/GoldClassOnline/GoldClassOnline-ejb/CustomerEntityController!ejb.session.stateless.CustomerEntityControllerLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

}
