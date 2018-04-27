/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.ScreeningSchedule;
import java.util.Calendar;
import java.util.Date;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.ScheduleExpression;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author KERK
 */
@Stateless
public class EJBTimerSessionBean implements EJBTimerSessionBeanLocal {

    @Resource
    private SessionContext sessionContext;
    @PersistenceContext(unitName = "GoldClassOnline-ejbPU")
    private EntityManager em;
    
    @EJB
    private ScreeningScheduleControllerLocal screeningScheduleControllerLocal;
    
    @Override
    public void createTimer(ScreeningSchedule screeningSchedule) {
        TimerService timerService = sessionContext.getTimerService();
        ScheduleExpression schedule = new ScheduleExpression();
        Date date = screeningSchedule.getScreeningTime();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        schedule.year(calendar.get(Calendar.YEAR)).month(calendar.get(Calendar.MONTH) + 1).dayOfMonth(calendar.get(Calendar.DAY_OF_MONTH)).hour(calendar.get(Calendar.HOUR_OF_DAY)).minute(calendar.get(Calendar.MINUTE));
        Object[] myObj = {screeningSchedule.getId(), 0L};
        timerService.createCalendarTimer(schedule, new TimerConfig(myObj, true));

    }
    
    @Timeout
    public void handleTimeout(Timer timer) {
         Object[] myObj = (Object[]) timer.getInfo();

        Long id = (Long) myObj[0];
        Long type = (Long) myObj[1];
        Long comparison = 0L;
        
        if (type.equals(comparison)) {
            ScreeningSchedule ss = em.find(ScreeningSchedule.class, id);
            ss.setEnabled(Boolean.FALSE);
    }
    }
}

