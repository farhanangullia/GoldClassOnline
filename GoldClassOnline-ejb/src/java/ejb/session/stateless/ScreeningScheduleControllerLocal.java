/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.MovieEntity;
import entity.ScreeningSchedule;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author KERK
 */
@Local
public interface ScreeningScheduleControllerLocal {

    public List<ScreeningSchedule> retrieveAllScreeningSchedules(Long hallId);

    public ScreeningSchedule retrieveScreeningScheduleById(Long ssId);

    public void deleteScreeningSchedule(Long hallId);

    public ScreeningSchedule createScreeningSchedule(ScreeningSchedule screeningSchedule, MovieEntity movieEntity, Long hallEntityID);

    public void updateScreeningSchedule(ScreeningSchedule screeningSchedule, MovieEntity movieEntity, Long hallEntityID);
    
}
