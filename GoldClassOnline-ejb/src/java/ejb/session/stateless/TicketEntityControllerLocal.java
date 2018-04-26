/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.TicketEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.CustomerNotFoundException;
import util.exception.PurchaseTicketException;
import ws.restful.datamodel.RemoteCheckoutLineItem;

/**
 *
 * @author 
 */
@Local
public interface TicketEntityControllerLocal {

    public TicketEntity createTicketEntity(TicketEntity ticketEntity);

    public void updateTicketEntity(TicketEntity ticketEntity);

    public List<TicketEntity> buyTicketsFromRemoteCheckoutRequest(String username, List<RemoteCheckoutLineItem> remoteCheckoutLineItems) throws PurchaseTicketException;

    public List<TicketEntity> retrieveAllTicketsByCustomer(Long id) throws CustomerNotFoundException;

    public List<TicketEntity> retrieveAllTicketsByScreeningSchedule(Long id);
    
}
