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
import util.exception.HallNotFoundException;

/**
 *
 * @author
 */
@Stateless
public class HallEntityController implements HallEntityControllerLocal {

    @PersistenceContext(unitName = "GoldClassOnline-ejbPU")
    private EntityManager em;

    @Override
    public HallEntity createHallEntity(HallEntity hallEntity, Long cinemaEntityId) {

        CinemaEntity cinemaEntity = em.find(CinemaEntity.class, cinemaEntityId);
        hallEntity.setCinemaEntity(cinemaEntity);
        em.persist(hallEntity);
        cinemaEntity.getHalls().add(hallEntity);
        em.flush();
        em.refresh(hallEntity);

        char[][] seating = new char[hallEntity.getRow()][hallEntity.getCol()];
        for (int i = 0; i < hallEntity.getRow(); i++) {
            for (int j = 0; j < hallEntity.getCol(); j++) {   
                seating[i][j] = 'x';
            }
        }
        hallEntity.setSeating(seating);

        return hallEntity;
    }

    @Override
    public void updateHallEntity(HallEntity hallEntity) {
        HallEntity he = retrieveHallByHallId(hallEntity.getId());
        he.setRow(hallEntity.getRow());
        he.setCol(hallEntity.getCol());
        he.setName(hallEntity.getName());
        char[][] seating = new char[hallEntity.getRow()][hallEntity.getCol()];
        for (int i = 0; i < hallEntity.getRow(); i++) {
            for (int j = 0; j < hallEntity.getCol(); j++) {
                seating[i][j] = 'o';
            }
        }
        he.setSeating(seating);
    }

    @Override
    public List<HallEntity> retrieveAllHalls(Long cinemaId) {
        Query query = em.createQuery("SELECT h FROM HallEntity h WHERE h.cinemaEntity.id = :inCinemaId AND h.enabled=1");
        query.setParameter("inCinemaId", cinemaId);

        return query.getResultList();
    }

    @Override
    public HallEntity retrieveHallByHallId(Long hallId) {
        HallEntity hallEntity = em.find(HallEntity.class, hallId);

        return hallEntity;
    }

    @Override
    public void deleteHall(Long hallId) throws HallNotFoundException {
        if (hallId != null) {
            HallEntity hallEntityToRemove = retrieveHallByHallId(hallId);
            hallEntityToRemove.setEnabled(Boolean.FALSE);
        } else {
            throw new HallNotFoundException("Id not provided for Hall to be deleted");
        }
    }
}
