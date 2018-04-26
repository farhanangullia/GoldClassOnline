/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.MovieEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.MovieNotFoundException;

/**
 *
 * @author 
 */
@Local
public interface MovieEntityControllerLocal {

    public MovieEntity createMovieEntity(MovieEntity movieEntity);

    public void updateMovieEntity(MovieEntity movieEntity);

    public List<MovieEntity> retrieveAllMovieEntities();

    public MovieEntity retrieveMovieByMovieId(Long movieId);

    public void deleteMovie(Long movieId) throws MovieNotFoundException;

    public List<MovieEntity> retrieveAllMovieEntitiesByCinema(Long cinemaId);

    public MovieEntity retrieveMovieByTitle(String title);
    
}
