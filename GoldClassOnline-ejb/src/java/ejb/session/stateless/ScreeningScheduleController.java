/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.HallEntity;
import entity.MovieEntity;
import entity.ScreeningSchedule;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author KERK
 */
@Stateless
public class ScreeningScheduleController implements ScreeningScheduleControllerLocal {

    @EJB
    private HallEntityControllerLocal hallEntityControllerLocal;

    @PersistenceContext(unitName = "GoldClassOnline-ejbPU")
    private EntityManager em;

    public void persist(Object object) {
        em.persist(object);
    }

    @Override
    public ScreeningSchedule createScreeningSchedule(ScreeningSchedule screeningSchedule, MovieEntity movieEntity,Long hallEntityID) {
        HallEntity hallEntity = hallEntityControllerLocal.retrieveHallByHallId(hallEntityID);
        screeningSchedule.setHallEntity(hallEntity);
        hallEntity.getScreeningSchedules().add(screeningSchedule);
        screeningSchedule.setMovieEntity(movieEntity);
        movieEntity.getScreeningSchedules().add(screeningSchedule);
        
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(screeningSchedule.getScreeningTime());
        calendar.add(Calendar.MINUTE, movieEntity.getRunningTime());
        
        Date endTime = calendar.getTime();
        screeningSchedule.setScreeningEndTime(endTime);
 
        em.persist(screeningSchedule);
        em.flush();
        em.refresh(screeningSchedule);

        return screeningSchedule;
    }

    @Override
    public void updateScreeningSchedule(ScreeningSchedule screeningSchedule, MovieEntity movieEntity,Long hallEntityID) {
        
        ScreeningSchedule ss = em.find(ScreeningSchedule.class, screeningSchedule.getId());
        ss.setScreeningTime(screeningSchedule.getScreeningTime());
        ss.setMovieEntity(movieEntity);
        movieEntity.getScreeningSchedules().add(ss);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(ss.getScreeningTime());
        calendar.add(Calendar.MINUTE, movieEntity.getRunningTime());
        Date endTime = calendar.getTime();
        ss.setScreeningEndTime(endTime);
 
    }

    @Override
    public List<ScreeningSchedule> retrieveAllScreeningSchedules(Long hallId) {
        Query query = em.createQuery("SELECT s FROM ScreeningSchedule s WHERE s.hallEntity.id = :inHallId AND s.enabled=1");
        query.setParameter("inHallId", hallId);

        return query.getResultList();
    }

    @Override
    public ScreeningSchedule retrieveScreeningScheduleById(Long ssId) {
        ScreeningSchedule ssEntity = em.find(ScreeningSchedule.class, ssId);

        return ssEntity;
    }

    @Override
    public void deleteScreeningSchedule(Long hallId) {
        ScreeningSchedule ssEntityToRemove = retrieveScreeningScheduleById(hallId);
        ssEntityToRemove.setEnabled(Boolean.FALSE);
    }
}
