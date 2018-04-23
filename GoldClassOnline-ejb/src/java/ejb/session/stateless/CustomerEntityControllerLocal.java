/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CustomerEntity;
import javax.ejb.Local;

/**
 *
 * @author KERK
 */
@Local
public interface CustomerEntityControllerLocal {

    public CustomerEntity createCustomerEntity(CustomerEntity customerEntity);

    public void updateCustomerEntity(CustomerEntity customerEntity);
    
}
