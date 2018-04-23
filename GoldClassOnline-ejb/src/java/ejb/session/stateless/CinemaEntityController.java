/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;


import entity.CinemaEntity;
import entity.HallEntity;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.CinemaNotFoundException;

/**
 *
 * @author KERK
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
    public void updateCinemaEntity(CinemaEntity cinemaEntity){
           em.merge(cinemaEntity);
    }
       
    @Override
    public CinemaEntity retrieveCinemaByCinemaId(Long cinemaId) throws CinemaNotFoundException
    {
        CinemaEntity cinemaEntity = em.find(CinemaEntity.class, cinemaId);
        
        if(cinemaEntity != null)
        {
            return cinemaEntity;
        }
        else
        {
            throw new CinemaNotFoundException("Cinema ID " + cinemaId + " does not exist!");
        }               
    }
    
    @Override
    public List<CinemaEntity> retrieveAllCinemaEntities() {
        Query query = em.createQuery("SELECT c FROM CinemaEntity c WHERE c.enabled = 1");
        
        return query.getResultList();
    }
    
     @Override
    public void deleteCinema(Long cinemaId) throws CinemaNotFoundException
    {
        if (cinemaId != null) {
            CinemaEntity cinemaEntityToRemove = retrieveCinemaByCinemaId(cinemaId);
            cinemaEntityToRemove.setEnabled(Boolean.FALSE);
        } else {
            throw new CinemaNotFoundException("Id not provided for Cinema to be deleted");
        }
    }
        
        
}