/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.ScreeningSchedule;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author KERK
 */
@Local
public interface ScreeningScheduleControllerLocal {

    public ScreeningSchedule createScreeningSchedule(ScreeningSchedule screeningSchedule);

    public void updateScreeningSchedule(ScreeningSchedule screeningSchedule);

    public List<ScreeningSchedule> retrieveAllScreeningSchedulesByHallAndMovie(Long hallId, Long movieId);

    public List<ScreeningSchedule> retrieveAllScreeningSchedulesByHall(Long hallId);

    public List<ScreeningSchedule> retrieveAllScreeningSchedules();
    
}
