/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful.datamodel;

import entity.CustomerEntity;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author Farhan Angullia
 */
@XmlRootElement
@XmlType(name = "getCustomerRsp", propOrder = {
    "customerEntity"
})
public class GetCustomerRsp {

    private CustomerEntity customerEntity;

    public GetCustomerRsp() {
    }

    public GetCustomerRsp(CustomerEntity customerEntity) {
        this.customerEntity = customerEntity;
    }

    /**
     * @return the customerEntity
     */
    public CustomerEntity getCustomerEntity() {
        return customerEntity;
    }

    /**
     * @param customerEntity the customerEntity to set
     */
    public void setCustomerEntity(CustomerEntity customerEntity) {
        this.customerEntity = customerEntity;
    }


   

}
