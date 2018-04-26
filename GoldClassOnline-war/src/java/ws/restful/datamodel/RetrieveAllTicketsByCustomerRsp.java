/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful.datamodel;

import entity.TicketEntity;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author Farhan Angullia
 */
@XmlRootElement
@XmlType(name = "retrieveAllTicketsByCustomer", propOrder = {
    "tickets"
})
public class RetrieveAllTicketsByCustomerRsp {
    private List<TicketEntity> tickets;

    public RetrieveAllTicketsByCustomerRsp() {
    }

    public RetrieveAllTicketsByCustomerRsp(List<TicketEntity> tickets) {
        this.tickets = tickets;
    }

    /**
     * @return the tickets
     */
    public List<TicketEntity> getTickets() {
        return tickets;
    }

    /**
     * @param tickets the tickets to set
     */
    public void setTickets(List<TicketEntity> tickets) {
        this.tickets = tickets;
    }
    
    
    
}
