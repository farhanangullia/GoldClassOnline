/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.MovieEntity;
import entity.ScreeningSchedule;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.MovieNotFoundException;

/**
 *
 * @author 
 */
@Stateless
public class MovieEntityController implements MovieEntityControllerLocal {

    @PersistenceContext(unitName = "GoldClassOnline-ejbPU")
    private EntityManager em;

    @Override
    public MovieEntity createMovieEntity(MovieEntity movieEntity) {
        em.persist(movieEntity);
        em.flush();
        em.refresh(movieEntity);

        return movieEntity;
    }

    @Override
    public void updateMovieEntity(MovieEntity movieEntity) {
        MovieEntity me = retrieveMovieByMovieId(movieEntity.getId());
        me.setCast(movieEntity.getCast());
        me.setDirector(movieEntity.getDirector());
        me.setGenre(movieEntity.getGenre());
        me.setLanguage(movieEntity.getLanguage());
        me.setRatings(movieEntity.getRatings());
        me.setRunningTime(movieEntity.getRunningTime());
        me.setSypnosis(movieEntity.getSypnosis());
        me.setTitle(movieEntity.getTitle());
    }

    @Override
    public MovieEntity retrieveMovieByMovieId(Long movieId) {
        MovieEntity movieEntity = em.find(MovieEntity.class, movieId);

        return movieEntity;

    }

    @Override
    public List<MovieEntity> retrieveAllMovieEntities() {
        Query query = em.createQuery("SELECT m FROM MovieEntity m WHERE m.enabled = 1");

        return query.getResultList();
    }

    @Override
    public List<MovieEntity> retrieveAllMovieEntitiesByCinema(Long cinemaId) { //how
        Query query = em.createQuery("SELECT m FROM MovieEntity m WHERE m.enabled= 1");
        List<MovieEntity> allMovies = query.getResultList();
        List<MovieEntity> moviesInCinema = new ArrayList<>();
        for (MovieEntity movieEntity : allMovies) {
            for (ScreeningSchedule screeningSchedule : movieEntity.getScreeningSchedules()) {
                if (screeningSchedule.getHallEntity().getCinemaEntity().getId().equals(cinemaId)) {

                    moviesInCinema.add(movieEntity);
                    break;
                }

            }
        }

        return moviesInCinema;

    }

    @Override
    public void deleteMovie(Long movieId) throws MovieNotFoundException {
        if (movieId != null) {
            MovieEntity movieEntutyToRemove = retrieveMovieByMovieId(movieId);
            movieEntutyToRemove.setEnabled(Boolean.FALSE);
        } else {
            throw new MovieNotFoundException("Id not provided for Movie to be deleted");
        }
    }
}
