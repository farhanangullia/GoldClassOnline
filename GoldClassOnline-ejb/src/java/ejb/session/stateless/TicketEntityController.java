/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.TicketEntity;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author KERK
 */
@Stateless
public class TicketEntityController implements TicketEntityControllerLocal {

    @PersistenceContext(unitName = "GoldClassOnline-ejbPU")
    private EntityManager em;

    public void persist(Object object) {
        em.persist(object);
    }

    @Override
    public TicketEntity createTicketEntity (TicketEntity ticketEntity) {
        em.persist(ticketEntity);
        em.flush();
        em.refresh(ticketEntity);
        
        return ticketEntity;
    }
    
    @Override
    public void updateTicketEntity (TicketEntity ticketEntity) {
        em.merge(ticketEntity);
    }
}
