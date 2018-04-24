/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CustomerEntity;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.exception.CreateCustomerException;
import util.exception.CustomerNotFoundException;
import util.exception.InvalidLoginCredentialException;
import util.security.CryptographicHelper;

/**
 *
 * @author KERK
 */
@Stateless
public class CustomerEntityController implements CustomerEntityControllerLocal {

    @PersistenceContext(unitName = "GoldClassOnline-ejbPU")
    private EntityManager em;

    public void persist(Object object) {
        em.persist(object);
    }

    @Override
    public Long createCustomerEntity(CustomerEntity newCustomer) throws CreateCustomerException {
        try {

            em.persist(newCustomer);
            em.flush();
            em.refresh(newCustomer);

            return newCustomer.getId();
        } catch (PersistenceException ex) {
            if (ex.getCause() != null
                    && ex.getCause().getCause() != null
                    && ex.getCause().getCause().getClass().getSimpleName().equals("MySQLIntegrityConstraintViolationException")) {
                throw new CreateCustomerException("Customer with same username already exist");
            } else {
                throw new CreateCustomerException("An unexpected error has occurred: " + ex.getMessage());
            }
        } catch (Exception ex) {
            throw new CreateCustomerException("An unexpected error has occurred: " + ex.getMessage());
        }

    }

    @Override
    public void updateCustomerEntity(CustomerEntity customerEntity) {
        em.merge(customerEntity);
    }

    @Override
    public CustomerEntity retrieveCustomerByUsername(String username) throws CustomerNotFoundException {
        Query query = em.createQuery("SELECT c FROM CustomerEntity c WHERE c.username = :inUsername");
        query.setParameter("inUsername", username);

        try {
            CustomerEntity customer = (CustomerEntity) query.getSingleResult();

            return customer;
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new CustomerNotFoundException("Customer username " + username + " does not exist!");
        }
    }

    @Override
    public CustomerEntity customerLogin(String username, String password) throws InvalidLoginCredentialException {
        try {
            CustomerEntity customer = retrieveCustomerByUsername(username);
            String passwordHash = CryptographicHelper.getInstance().byteArrayToHexString(CryptographicHelper.getInstance().doMD5Hashing(password + customer.getSalt()));

            if (customer.getPassword().equals(passwordHash)) {

                return customer;
            } else {
                throw new InvalidLoginCredentialException("Username does not exist or invalid password!");
            }
        } catch (CustomerNotFoundException ex) {
            throw new InvalidLoginCredentialException("Username does not exist or invalid password!");
        }
    }

}
