/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.ScreeningSchedule;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author KERK
 */
@Stateless
public class ScreeningScheduleController implements ScreeningScheduleControllerLocal {

    @PersistenceContext(unitName = "GoldClassOnline-ejbPU")
    private EntityManager em;

    public void persist(Object object) {
        em.persist(object);
    }

    
    @Override
    public ScreeningSchedule createScreeningSchedule (ScreeningSchedule screeningSchedule) {
        em.persist(screeningSchedule);
        em.flush();
        em.refresh(screeningSchedule);
        
        return screeningSchedule;       
    }
    
    @Override
    public void updateScreeningSchedule (ScreeningSchedule screeningSchedule) {
        em.merge(screeningSchedule);
    }
}
