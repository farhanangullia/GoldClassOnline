/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.ScreeningSchedule;
import javax.ejb.Local;

/**
 *
 * @author KERK
 */
@Local
public interface EJBTimerSessionBeanLocal {
    
    public void createTimer(ScreeningSchedule screeningSchedule);
    
}
