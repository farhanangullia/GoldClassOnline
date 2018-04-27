/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.validator;

import ejb.session.stateless.ScreeningScheduleControllerLocal;
import entity.MovieEntity;
import entity.ScreeningSchedule;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 *
 * @author KERK
 */
@FacesValidator(value = "screeningScheduleValidator")
public class ScreeningScheduleValidator implements Validator {

     @EJB
    private ScreeningScheduleControllerLocal screeningScheduleControllerLocal;
     
    private Date date;
    private int count;
    private Long hallEntityToViewId;
    private MovieEntity movieEntity;
    private List<ScreeningSchedule> screeningSchedules;
    
    public ScreeningScheduleValidator() {
        date = new Date();
        screeningSchedules = new ArrayList<>();
    }
    
    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
          
        date = (Date) value;
        count = 0;
        
        hallEntityToViewId = (Long)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("hallIdToView");
        System.err.println("hallId: " + hallEntityToViewId);
        movieEntity = (MovieEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("selectedMovieEntity");
        System.err.println("MovieId: " + movieEntity.getId());
        screeningSchedules = screeningScheduleControllerLocal.retrieveAllScreeningSchedules(hallEntityToViewId);
        
        for (ScreeningSchedule ss : screeningSchedules) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.MINUTE, movieEntity.getRunningTime());
            calendar.add(Calendar.MINUTE, 20);
            Date endTime = calendar.getTime();
            if (endTime.compareTo(ss.getScreeningTime()) > 1 && endTime.compareTo(ss.getScreeningEndTime()) < 1) {
                count++;
            }
        }
        if(count > 0)
                {
                    throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "There is a clash in schedule!", null));
                }
    }
}
