/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.MovieEntity;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.MovieNotFoundException;

/**
 *
 * @author KERK
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
        em.merge(movieEntity);
    }
    
    @Override
    public MovieEntity retrieveMovieByMovieId(Long movieId) throws MovieNotFoundException
    {
        MovieEntity movieEntity = em.find(MovieEntity.class, movieId);
        
        if(movieEntity != null)
        {
            return movieEntity;
        }
        else
        {
            throw new MovieNotFoundException("Cinema ID " + movieId + " does not exist!");
        }               
    }
    
    @Override
    public List<MovieEntity> retrieveAllMovieEntities() {
        Query query = em.createQuery("SELECT m FROM MovieEntity m WHERE m.enabled = 1");
        
        return query.getResultList();
    }
    
    @Override
    public void deleteMovie(Long movieId) throws MovieNotFoundException
    {
        if (movieId != null) {
            MovieEntity movieEntutyToRemove = retrieveMovieByMovieId(movieId);
            movieEntutyToRemove.setEnabled(Boolean.FALSE);
        } else {
            throw new MovieNotFoundException("Id not provided for Movie to be deleted");
        }
    }
}
