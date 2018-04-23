/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CinemaEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.CinemaNotFoundException;

/**
 *
 * @author KERK
 */
@Local
public interface CinemaEntityControllerLocal {

    public CinemaEntity createCinemaEntity(CinemaEntity cinemaEntity);

    public void updateCinemaEntity(CinemaEntity cinemaEntity);

    public List<CinemaEntity> retrieveAllCinemaEntities();

    public CinemaEntity retrieveCinemaByCinemaId(Long cinemaId) throws CinemaNotFoundException;

    public void deleteCinema(Long cinemaId) throws CinemaNotFoundException;
    
}
