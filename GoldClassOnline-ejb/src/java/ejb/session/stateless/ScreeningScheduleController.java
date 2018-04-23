/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.ScreeningSchedule;
import java.util.List;
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

    @PersistenceContext(unitName = "GoldClassOnline-ejbPU")
    private EntityManager em;

    public void persist(Object object) {
        em.persist(object);
    }

    @Override
    public ScreeningSchedule createScreeningSchedule(ScreeningSchedule screeningSchedule) {
        em.persist(screeningSchedule);
        em.flush();
        em.refresh(screeningSchedule);

        return screeningSchedule;
    }

    @Override
    public void updateScreeningSchedule(ScreeningSchedule screeningSchedule) {
        em.merge(screeningSchedule);
    }

    @Override
    public List<ScreeningSchedule> retrieveAllScreeningSchedules() {
        Query query = em.createQuery("SELECT s FROM ScreeningSchedule s");

        return query.getResultList();
    }

    @Override
    public List<ScreeningSchedule> retrieveAllScreeningSchedulesByHall(Long hallId) {
        Query query = em.createQuery("SELECT s FROM ScreeningSchedule s WHERE s.hallEntity.id = :inHallId");
        query.setParameter("inHallId", hallId);

        return query.getResultList();
    }

    @Override
    public List<ScreeningSchedule> retrieveAllScreeningSchedulesByHallAndMovie(Long hallId, Long movieId) {
        Query query = em.createQuery("SELECT s FROM ScreeningSchedule s WHERE s.hallEntity.id = :inHallId AND s.movieEntity.id=:inMovieId");
        query.setParameter("inHallId", hallId);
        query.setParameter("inMovieId", movieId);

        return query.getResultList();
    }

}
