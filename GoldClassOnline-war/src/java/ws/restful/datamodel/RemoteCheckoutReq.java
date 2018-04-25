/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful.datamodel;

import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author Farhan Angullia
 */
@XmlRootElement
@XmlType(name = "remoteCheckoutReq", propOrder = {
    "username", 
    "remoteCheckoutLineItems"
})
public class RemoteCheckoutReq {

    private String username;
    private List<RemoteCheckoutLineItem> remoteCheckoutLineItems;

    public RemoteCheckoutReq() {
    }

    public RemoteCheckoutReq(String username, List<RemoteCheckoutLineItem> remoteCheckoutLineItems) {
        this.username = username;
        this.remoteCheckoutLineItems = remoteCheckoutLineItems;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    @XmlElements({
        @XmlElement(name = "remoteCheckoutLineItem", type = RemoteCheckoutLineItem.class)
    })
    @XmlElementWrapper(name = "remoteCheckoutLineItems")
    public List<RemoteCheckoutLineItem> getRemoteCheckoutLineItems() {
        return remoteCheckoutLineItems;
    }

    public void setRemoteCheckoutLineItems(List<RemoteCheckoutLineItem> remoteCheckoutLineItems) {
        this.remoteCheckoutLineItems = remoteCheckoutLineItems;
    }

}
