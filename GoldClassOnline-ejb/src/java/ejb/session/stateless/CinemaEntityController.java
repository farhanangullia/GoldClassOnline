/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CinemaEntity;
import entity.HallEntity;
import entity.ScreeningSchedule;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.CinemaNotFoundException;

/**
 *
 * @author 
 */
@Stateless
public class CinemaEntityController implements CinemaEntityControllerLocal {

    @PersistenceContext(unitName = "GoldClassOnline-ejbPU")
    private EntityManager em;

    @Override
    public CinemaEntity createCinemaEntity(CinemaEntity cinemaEntity) {
        em.persist(cinemaEntity);
        em.flush();
        em.refresh(cinemaEntity);

        return cinemaEntity;
    }

    @Override
    public void updateCinemaEntity(CinemaEntity cinemaEntity) {
        em.merge(cinemaEntity);
    }

    @Override
    public CinemaEntity retrieveCinemaByCinemaId(Long cinemaId) {
        CinemaEntity cinemaEntity = em.find(CinemaEntity.class, cinemaId);

        return cinemaEntity;
    }

    @Override
    public List<CinemaEntity> retrieveAllCinemaEntities() {
        Query query = em.createQuery("SELECT c FROM CinemaEntity c WHERE c.enabled = 1");

        return query.getResultList();
    }

    @Override
    public void deleteCinema(Long cinemaId) throws CinemaNotFoundException {
        if (cinemaId != null) {
            CinemaEntity cinemaEntityToRemove = retrieveCinemaByCinemaId(cinemaId);
            cinemaEntityToRemove.setEnabled(Boolean.FALSE);
        } else {
            throw new CinemaNotFoundException("Id not provided for Cinema to be deleted");
        }
    }

    @Override
    public List<CinemaEntity> retrieveAllCinemaEntitiesByMovie(Long movieId) { //how
        Query query = em.createQuery("SELECT c FROM CinemaEntity c WHERE c.enabled= 1");
        List<CinemaEntity> cinemaEntities = query.getResultList();
        List<CinemaEntity> cinemasWithMovie = new ArrayList<>();

        for (CinemaEntity cinemaEntity : cinemaEntities) {

            for (HallEntity hall : cinemaEntity.getHalls()) {
                for (ScreeningSchedule screeningSchedule : hall.getScreeningSchedules()) {
                    if (screeningSchedule.getMovieEntity().getId().equals(movieId)) {
                        if (cinemasWithMovie.contains(cinemaEntity)) {
                            break;
                        }

                        cinemasWithMovie.add(cinemaEntity);
                        break;
                    }

                }

            }

        }

        return cinemasWithMovie;

    }

}
