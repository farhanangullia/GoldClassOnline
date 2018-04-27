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

        String[][] seating = new String[hallEntity.getRow()][hallEntity.getCol()];
        char row, col;
        for (int i = 0; i < hallEntity.getRow(); i++) {
            for (int j = 0; j < hallEntity.getCol(); j++) {
                row = (char) (i + 65);
                col = (char) (j + 49);
                seating[i][j] = "" + row + col;
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
        String[][] seating = new String[hallEntity.getRow()][hallEntity.getCol()];
        char row, col;
        for (int i = 0; i < hallEntity.getRow(); i++) {
            for (int j = 0; j < hallEntity.getCol(); j++) {
                row = (char) (i + 65);
                col = (char) (j + 49);
                seating[i][j] = "" + row + col;
            }
        }
        he.setSeating(seating);
    }

    @Override
    public void updateHallHandicapSeats(HallEntity hallEntity, List<String> handicapSeats) {
        
        HallEntity he = retrieveHallByHallId(hallEntity.getId());

        String[][] seating = he.getSeating();

        for (int i = 0; i < he.getRow(); i++) {
            for (int j = 0; j < he.getCol(); j++) {
                if (handicapSeats.contains(seating[i][j])) {
                    seating[i][j] = "(H)" + seating[i][j];
                }
            }
        }
        he.setSeating(seating);
    }
    
    @Override
    public void removeHallHandicapSeats(HallEntity hallEntity, List<String> handicapSeats) {
        
        HallEntity he = retrieveHallByHallId(hallEntity.getId());

        String[][] seating = he.getSeating();

        for (int i = 0; i < he.getRow(); i++) {
            for (int j = 0; j < he.getCol(); j++) {
                if (handicapSeats.contains(seating[i][j])) {
                    seating[i][j] = seating[i][j].replace("(H)", "");
                }
            }
        }
        he.setSeating(seating);
    }
    
    

    @Override
    public void updateHallDisabledSeats(HallEntity hallEntity, List<String> disabledSeats) {
        HallEntity he = retrieveHallByHallId(hallEntity.getId());

        String[][] seating = he.getSeating();

        for (int i = 0; i < he.getRow(); i++) {
            for (int j = 0; j < he.getCol(); j++) {
                if (disabledSeats.contains(seating[i][j])) {
                    seating[i][j] = "X";
                }
            }
        }
        he.setSeating(seating);
    }
    
    @Override
    public void removeHallDisabledSeats(HallEntity hallEntity, List<String> handicapSeats) {
        
        HallEntity he = retrieveHallByHallId(hallEntity.getId());

        String[][] seating = he.getSeating();
        for (int i = 0; i < handicapSeats.size(); i++) {
            char rowChar = handicapSeats.get(i).charAt(0);
            int row = ((int) rowChar) - 65;
            char colChar = handicapSeats.get(i).charAt(1);
            int col = Character.getNumericValue(colChar);
            seating[row][col-1] = "" + rowChar + col;
        }
        he.setSeating(seating);
    }

    @Override
    public void updateHallEntityWithCinemaEntity(HallEntity hallEntity, CinemaEntity cinemaEntity) {
        HallEntity he = retrieveHallByHallId(hallEntity.getId());
        he.setRow(hallEntity.getRow());
        he.setCol(hallEntity.getCol());
        he.setName(hallEntity.getName());
        he.setCinemaEntity(cinemaEntity);
        String[][] seating = new String[hallEntity.getRow()][hallEntity.getCol()];
        char row, col;
        for (int i = 0; i < hallEntity.getRow(); i++) {
            for (int j = 0; j < hallEntity.getCol(); j++) {
                row = (char) (i + 65);
                col = (char) (j + 49);
                seating[i][j] = "" + row + col;
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
    public List<HallEntity> retrieveAllHallsForCinemaStaff() {
        Query query = em.createQuery("SELECT h FROM HallEntity h WHERE h.enabled=1");

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
