/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CinemaEntity;
import entity.HallEntity;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author KERK
 */
@Local
public interface HallEntityControllerLocal {

    public void updateHallEntity(HallEntity hallEntity);

    public List<HallEntity> retrieveAllHalls(Long cinemaId);

    public HallEntity createHallEntity(HallEntity hallEntity, CinemaEntity cinemaEntity);
    
}
