/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CustomerEntity;
import entity.ScreeningSchedule;
import entity.TicketEntity;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.CustomerNotFoundException;
import util.exception.PurchaseTicketException;
import ws.restful.datamodel.RemoteCheckoutLineItem;

/**
 *
 * @author
 */
@Stateless
public class TicketEntityController implements TicketEntityControllerLocal {

    @PersistenceContext(unitName = "GoldClassOnline-ejbPU")
    private EntityManager em;

    @Resource
    private EJBContext eJBContext;

    @EJB
    private CustomerEntityControllerLocal customerEntityControllerLocal;

    @EJB
    private ScreeningScheduleControllerLocal screeningScheduleControllerLocal;

    @Override
    public TicketEntity createTicketEntity(TicketEntity ticketEntity) {
        em.persist(ticketEntity);
        em.flush();
        em.refresh(ticketEntity);

        return ticketEntity;
    }

    @Override
    public void updateTicketEntity(TicketEntity ticketEntity) {
        em.merge(ticketEntity);
    }

    @Override
    public List<TicketEntity> buyTicketsFromRemoteCheckoutRequest(String username, List<RemoteCheckoutLineItem> remoteCheckoutLineItems) throws PurchaseTicketException {

        System.out.println("CHECKOUTREQ CONTROLLER");

        if ((username != null) && (remoteCheckoutLineItems != null) && (!remoteCheckoutLineItems.isEmpty())) {
            try {
                System.out.println("CHECKOUTREQ CONTROLLER 1");
                CustomerEntity customer = customerEntityControllerLocal.retrieveCustomerByUsername(username);
                List<TicketEntity> tickets = new ArrayList<>();

              //  BigDecimal price = new BigDecimal("10");
                System.out.println("CHECKOUTREQ CONTROLLER  2");
                for (RemoteCheckoutLineItem remoteCheckoutLineItem : remoteCheckoutLineItems) {
                    for (String seat : remoteCheckoutLineItem.getSeats()) {
                        //Integer quantity = 0;

                        ScreeningSchedule screeningSchedule = screeningScheduleControllerLocal.retrieveScreeningScheduleById(remoteCheckoutLineItem.getScreeningScheduleId());

                        //   quantity = remoteCheckoutLineItem.getQuantity();
                 
                        TicketEntity ticket = new TicketEntity(screeningSchedule.getPrice(), seat, screeningSchedule, customer);

                        em.persist(ticket);
                        customer.getTicketEntities().add(ticket);
                        screeningSchedule.getTicketEntities().add(ticket);
                        em.merge(screeningSchedule);
                        em.merge(customer);

                        tickets.add(ticket);

                    }

                }
                em.flush();

                System.out.println("CHECKOUTREQ CONTROLLER  3");
                return tickets;
            } catch (Exception exc) {
                System.out.println(exc.getMessage());
                throw new PurchaseTicketException("Nothing to checkout dsdremotely!");
            }

        } else {
            throw new PurchaseTicketException("Nothing to checkout remotely!");

        }

    }

    @Override
    public List<TicketEntity> retrieveAllTicketsByCustomer(Long id) throws CustomerNotFoundException {

        try {

            Query query = em.createQuery("SELECT t FROM TicketEntity t WHERE t.customerEntity.id = :inCustomerId");
            query.setParameter("inCustomerId", id);
            return query.getResultList();

        } catch (Exception e) {
            throw new CustomerNotFoundException("Customer " + id + " does not exist");
        }

    }

    @Override
    public List<TicketEntity> retrieveAllTicketsByScreeningSchedule(Long id) {

        Query query = em.createQuery("SELECT t FROM TicketEntity t WHERE t.screeningSchedule.id = :inScreeningScheduleId");
        query.setParameter("inScreeningScheduleId", id);
        return query.getResultList();

    }

}
