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

/**
 *
 * @author KERK
 */
@Stateless
public class HallEntityController implements HallEntityControllerLocal {
    
    @PersistenceContext(unitName = "GoldClassOnline-ejbPU")
    private EntityManager em;
    
    @Override
    public HallEntity createHallEntity(HallEntity hallEntity, CinemaEntity cinemaEntity) {
        em.persist(hallEntity);
        em.flush();
        em.refresh(hallEntity);
        hallEntity.setCinemaEntity(cinemaEntity);
        cinemaEntity.getHalls().add(hallEntity);
        
        return hallEntity;
    }
    
    @Override
    public void updateHallEntity(HallEntity hallEntity) {
        em.merge(hallEntity);
    }
    
    @Override
    public List<HallEntity> retrieveAllHalls(Long cinemaId) {
        Query query = em.createQuery("SELECT h FROM HallEntity h WHERE h.cinemaEntity.id = :inCinemaId");
        query.setParameter("inCinemaId", cinemaId);
        
        return query.getResultList();
    }

   
}
