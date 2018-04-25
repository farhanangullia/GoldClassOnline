/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CustomerEntity;
import javax.ejb.Local;
import util.exception.CreateCustomerException;
import util.exception.CustomerNotFoundException;
import util.exception.InvalidLoginCredentialException;

/**
 *
 * @author KERK
 */
@Local
public interface CustomerEntityControllerLocal {

    public void updateCustomerEntity(CustomerEntity customerEntity);

    public CustomerEntity retrieveCustomerByUsername(String username) throws CustomerNotFoundException;

    public CustomerEntity customerLogin(String username, String password) throws InvalidLoginCredentialException;

    public Long createCustomerEntity(CustomerEntity newCustomer) throws CreateCustomerException;
    
}
