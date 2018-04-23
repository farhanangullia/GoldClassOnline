/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.StaffEntity;
import javax.ejb.Local;
import util.exception.InvalidLoginCredentialException;
import util.exception.StaffNotFoundException;

/**
 *
 * @author KERK
 */
@Local
public interface StaffEntityControllerLocal {

    public StaffEntity createStaffEntity(StaffEntity staffEntity);

    public void updateStaffEntity(StaffEntity staffEntity);

    public StaffEntity retrieveStaffByUsername(String username) throws StaffNotFoundException;

    public StaffEntity staffLogin(String username, String password) throws InvalidLoginCredentialException;
    
}
