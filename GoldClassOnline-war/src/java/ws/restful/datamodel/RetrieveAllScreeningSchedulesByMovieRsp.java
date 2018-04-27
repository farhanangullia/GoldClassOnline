/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful.datamodel;

import entity.ScreeningSchedule;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author Farhan Angullia
 */
@XmlRootElement
@XmlType(name = "retrieveAllScreeningSchedulesByMovieRsp", propOrder = {
    "screeningSchedules"
})
public class RetrieveAllScreeningSchedulesByMovieRsp {
    
    private List<ScreeningSchedule> screeningSchedules;

    public RetrieveAllScreeningSchedulesByMovieRsp() {
    }

    public RetrieveAllScreeningSchedulesByMovieRsp(List<ScreeningSchedule> screeningSchedules) {
        this.screeningSchedules = screeningSchedules;
    }

    /**
     * @return the screeningSchedules
     */
    public List<ScreeningSchedule> getScreeningSchedules() {
        return screeningSchedules;
    }

    /**
     * @param screeningSchedules the screeningSchedules to set
     */
    public void setScreeningSchedules(List<ScreeningSchedule> screeningSchedules) {
        this.screeningSchedules = screeningSchedules;
    }
    
    
    
}
