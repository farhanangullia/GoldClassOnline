/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CustomerEntity;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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
    public CustomerEntity createCustomerEntity (CustomerEntity customerEntity) {
        em.persist(customerEntity);
        em.flush();
        em.refresh(customerEntity);
        
        return customerEntity;
    }
    
    @Override
    public void updateCustomerEntity(CustomerEntity customerEntity) {
        em.merge(customerEntity);
    }
}
