/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.HallEntityControllerLocal;
import ejb.session.stateless.MovieEntityControllerLocal;
import ejb.session.stateless.ScreeningScheduleControllerLocal;
import entity.HallEntity;
import entity.MovieEntity;
import entity.ScreeningSchedule;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 *
 * @author
 */
@Named(value = "hallDetailsManagedBean")
@ViewScoped
public class HallDetailsManagedBean implements Serializable {

    @EJB
    private MovieEntityControllerLocal movieEntityControllerLocal;

    @EJB
    private ScreeningScheduleControllerLocal screeningScheduleControllerLocal;

    @EJB
    private HallEntityControllerLocal hallEntityControllerLocal;

    private HallEntity hallEntityToView;
    private Long hallEntityToViewId;
    private HallEntity hallEntityToUpdate;
    private ScreeningSchedule selectedScheduleToUpdate;

    private MovieEntity selectedMovieEntity;
    private MovieEntity selectedMovieEntityToUpdate;

    private ScreeningSchedule newScreeningSchedule;

    private List<ScreeningSchedule> screeningSchedules;
    private List<ScreeningSchedule> filteredScreeningSchedules;
    private List<ScreeningSchedule> retrieveAllSS;
    private List<SelectItem> selectItems;
    private SelectItem selectedItem;

    private List<MovieEntity> movieEntities;

    private List<String> selectedHandicap;
    private List<String> selectedHandicapToRemove;
    private List<String> selectedDisabled;
    private List<String> selectedDisabledToRemove;

    private List<String> seatsInArray;
    private List<String> currentDisabledSeats;
    private List<String> currentHandicapSeats;

    private Date currentDate;
    private Date selectedSSDate;

    public HallDetailsManagedBean() {
        hallEntityToView = new HallEntity();
        hallEntityToUpdate = new HallEntity();
        screeningSchedules = new ArrayList<>();
        filteredScreeningSchedules = new ArrayList<>();
        retrieveAllSS = new ArrayList<>();
        movieEntities = new ArrayList<>();
        newScreeningSchedule = new ScreeningSchedule();
        selectItems = new ArrayList<>();

        selectedItem = new SelectItem();

        selectedMovieEntity = new MovieEntity();
        selectedMovieEntityToUpdate = new MovieEntity();

        selectedHandicap = new ArrayList<>();
        selectedDisabled = new ArrayList<>();
        selectedHandicapToRemove = new ArrayList<>();
        selectedDisabledToRemove = new ArrayList<>();
        currentDate = new Date();
        selectedSSDate = new Date();

        seatsInArray = new ArrayList<>();
        currentDisabledSeats = new ArrayList<>();
        currentHandicapSeats = new ArrayList<>();

    }

    @PostConstruct
    public void postConstruct() {
        hallEntityToViewId = (Long) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("hallIdToView");
        hallEntityToView = hallEntityControllerLocal.retrieveHallByHallId(hallEntityToViewId);
        screeningSchedules = screeningScheduleControllerLocal.retrieveAllScreeningSchedules(hallEntityToViewId);
        filteredScreeningSchedules = screeningSchedules;
        movieEntities = movieEntityControllerLocal.retrieveAllMovieEntities();
        seatsInArray = hallEntityToView.seatsInArray();
        currentDisabledSeats = hallEntityToView.currentDisabledSeats();
        currentHandicapSeats = hallEntityToView.currentHandicapSeats();
        for (MovieEntity movieEntity : movieEntities) {
            selectItems.add(new SelectItem(movieEntity, movieEntity.getTitle(), movieEntity.getId().toString()));
        }
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("MovieEntityConverter.movieEntities", movieEntities);
    }

    @PreDestroy
    public void preDestroy() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("MovieEntityConverter.movieEntities", null);
    }

    public void createNewScreeningSchedule(ActionEvent event) {

        retrieveAllSS = screeningScheduleControllerLocal.retrieveAllScreeningSchedules(hallEntityToViewId);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(newScreeningSchedule.getScreeningTime());
        calendar.add(Calendar.MINUTE, selectedMovieEntity.getRunningTime());
        calendar.add(Calendar.MINUTE, 20); // cleaning up
        selectedSSDate = calendar.getTime();
        int count = 0;
        for (ScreeningSchedule ss : retrieveAllSS) {
            if (selectedSSDate.after(ss.getScreeningTime()) && selectedSSDate.before(ss.getScreeningEndTime())) {
                count++;
            }
        }
       // System.err.println("count: " + count);
        if (count > 0) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "There is a clash in screening schedule timing!", null));
        } else if (count == 0) {
            ScreeningSchedule se = screeningScheduleControllerLocal.createScreeningSchedule(newScreeningSchedule, selectedMovieEntity, hallEntityToViewId);
            screeningSchedules.add(se);
            filteredScreeningSchedules = screeningSchedules;
            newScreeningSchedule = new ScreeningSchedule();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New screening schedule created successfully (ID: " + se.getId() + ")", null));
        }
    }

    public void deleteScreeningSchedule(ActionEvent event) {
        try {
            ScreeningSchedule ssEntityToDelete = (ScreeningSchedule) event.getComponent().getAttributes().get("ssEntityToDelete");
            screeningScheduleControllerLocal.deleteScreeningSchedule(ssEntityToDelete.getId());

            screeningSchedules.remove(ssEntityToDelete);
            filteredScreeningSchedules.remove(ssEntityToDelete);

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Screening Schedule deleted successfully", null));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
    }

    public void updateScreeningSchedule(ActionEvent event) {
        try {
        retrieveAllSS = screeningScheduleControllerLocal.retrieveAllScreeningSchedules(hallEntityToViewId);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(selectedScheduleToUpdate.getScreeningTime());
        calendar.add(Calendar.MINUTE, selectedMovieEntityToUpdate.getRunningTime());
        calendar.add(Calendar.MINUTE, 20); // cleaning up
        selectedSSDate = calendar.getTime();
        int count = 0;
        for (ScreeningSchedule ss : retrieveAllSS) {
            if (selectedSSDate.after(ss.getScreeningTime()) && selectedSSDate.before(ss.getScreeningEndTime())) {
                count++;
            }
        }
        if (count > 0) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "There is a clash in screening schedule timing!", null));
        } else if (count == 0) {
            screeningScheduleControllerLocal.updateScreeningSchedule(selectedScheduleToUpdate, selectedMovieEntityToUpdate, hallEntityToViewId);
        }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Screening Schedule updated successfully", null));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
    }

    public void updateHandicapSeats(ActionEvent event) {
        try {
            hallEntityToViewId = (Long) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("hallIdToView");
            hallEntityToUpdate = hallEntityControllerLocal.retrieveHallByHallId(hallEntityToViewId);
            hallEntityControllerLocal.updateHallHandicapSeats(hallEntityToUpdate, selectedHandicap);
            hallEntityToView = hallEntityControllerLocal.retrieveHallByHallId(hallEntityToViewId);
            FacesContext.getCurrentInstance().getExternalContext().redirect("viewHallDetails.xhtml");
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
    }

    public void deleteHandicapSeats(ActionEvent event) {
        try {
            hallEntityToViewId = (Long) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("hallIdToView");
            hallEntityToUpdate = hallEntityControllerLocal.retrieveHallByHallId(hallEntityToViewId);
            hallEntityControllerLocal.removeHallHandicapSeats(hallEntityToUpdate, selectedHandicapToRemove);
            hallEntityToView = hallEntityControllerLocal.retrieveHallByHallId(hallEntityToViewId);
            FacesContext.getCurrentInstance().getExternalContext().redirect("viewHallDetails.xhtml");          
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
    }

    public void updateDisabledSeats(ActionEvent event) {
        try {
            hallEntityToViewId = (Long) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("hallIdToView");
            hallEntityToUpdate = hallEntityControllerLocal.retrieveHallByHallId(hallEntityToViewId);
            hallEntityControllerLocal.updateHallDisabledSeats(hallEntityToView, selectedDisabled);
            hallEntityToView = hallEntityControllerLocal.retrieveHallByHallId(hallEntityToViewId);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Hall Seating updated successfully", null));
            FacesContext.getCurrentInstance().getExternalContext().redirect("viewHallDetails.xhtml");
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
    }

    public void deleteDisabledSeats(ActionEvent event) {
        try {
            hallEntityToViewId = (Long) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("hallIdToView");
            hallEntityToUpdate = hallEntityControllerLocal.retrieveHallByHallId(hallEntityToViewId);
            hallEntityControllerLocal.removeHallDisabledSeats(hallEntityToUpdate, selectedDisabledToRemove);
            hallEntityToView = hallEntityControllerLocal.retrieveHallByHallId(hallEntityToViewId);
            FacesContext.getCurrentInstance().getExternalContext().redirect("viewHallDetails.xhtml");
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
    }

    public HallEntity getHallEntityToView() {
        return hallEntityToView;
    }

    public void setHallEntityToView(HallEntity hallEntityToView) {
        this.hallEntityToView = hallEntityToView;
    }

    public Long getHallEntityToViewId() {
        return hallEntityToViewId;
    }

    public void setHallEntityToViewId(Long hallEntityToViewId) {
        this.hallEntityToViewId = hallEntityToViewId;
    }

    public List<ScreeningSchedule> getScreeningSchedules() {
        return screeningSchedules;
    }

    public void setScreeningSchedules(List<ScreeningSchedule> screeningSchedules) {
        this.screeningSchedules = screeningSchedules;
    }

    public List<ScreeningSchedule> getFilteredScreeningSchedules() {
        return filteredScreeningSchedules;
    }

    public void setFilteredScreeningSchedules(List<ScreeningSchedule> filteredScreeningSchedules) {
        this.filteredScreeningSchedules = filteredScreeningSchedules;
    }

    public ScreeningSchedule getSelectedScheduleToUpdate() {
        return selectedScheduleToUpdate;
    }

    public void setSelectedScheduleToUpdate(ScreeningSchedule selectedScheduleToUpdate) {
        this.selectedScheduleToUpdate = selectedScheduleToUpdate;
    }

    public ScreeningSchedule getNewScreeningSchedule() {
        return newScreeningSchedule;
    }

    public void setNewScreeningSchedule(ScreeningSchedule newScreeningSchedule) {
        this.newScreeningSchedule = newScreeningSchedule;
    }

    public List<MovieEntity> getMovieEntities() {
        return movieEntities;
    }

    public void setMovieEntities(List<MovieEntity> movieEntities) {
        this.movieEntities = movieEntities;
    }

    public List<SelectItem> getSelectItems() {
        return selectItems;
    }

    public void setSelectItems(List<SelectItem> selectItems) {
        this.selectItems = selectItems;
    }

    public MovieEntity getSelectedMovieEntity() {
        return selectedMovieEntity;
    }

    public void setSelectedMovieEntity(MovieEntity selectedMovieEntity) {
        this.selectedMovieEntity = selectedMovieEntity;
    }

    public MovieEntity getSelectedMovieEntityToUpdate() {
        return selectedMovieEntityToUpdate;
    }

    public void setSelectedMovieEntityToUpdate(MovieEntity selectedMovieEntityToUpdate) {
        this.selectedMovieEntityToUpdate = selectedMovieEntityToUpdate;
    }

    public HallEntity getHallEntityToUpdate() {
        return hallEntityToUpdate;
    }

    public void setHallEntityToUpdate(HallEntity hallEntityToUpdate) {
        this.hallEntityToUpdate = hallEntityToUpdate;
    }

    public List<String> getSelectedHandicap() {
        return selectedHandicap;
    }

    public void setSelectedHandicap(List<String> selectedHandicap) {
        this.selectedHandicap = selectedHandicap;
    }

    public List<String> getSelectedDisabled() {
        return selectedDisabled;
    }

    public void setSelectedDisabled(List<String> selectedDisabled) {
        this.selectedDisabled = selectedDisabled;
    }

    public List<String> getSelectedHandicapToRemove() {
        return selectedHandicapToRemove;
    }

    public void setSelectedHandicapToRemove(List<String> selectedHandicapToRemove) {
        this.selectedHandicapToRemove = selectedHandicapToRemove;
    }

    public List<String> getSelectedDisabledToRemove() {
        return selectedDisabledToRemove;
    }

    public void setSelectedDisabledToRemove(List<String> selectedDisabledToRemove) {
        this.selectedDisabledToRemove = selectedDisabledToRemove;
    }

    public Date getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(Date currentDate) {
        this.currentDate = currentDate;
    }

    public List<String> getSeatsInArray() {
        return seatsInArray;
    }

    public void setSeatsInArray(List<String> seatsInArray) {
        this.seatsInArray = seatsInArray;
    }

    public List<String> getCurrentDisabledSeats() {
        return currentDisabledSeats;
    }

    public void setCurrentDisabledSeats(List<String> currentDisabledSeats) {
        this.currentDisabledSeats = currentDisabledSeats;
    }

    public List<String> getCurrentHandicapSeats() {
        return currentHandicapSeats;
    }

    public void setCurrentHandicapSeats(List<String> currentHandicapSeats) {
        this.currentHandicapSeats = currentHandicapSeats;
    }

    public SelectItem getSelectedItem() {
        return selectedItem;
    }

    public void setSelectedItem(SelectItem selectedItem) {
        this.selectedItem = selectedItem;
    }

    public List<ScreeningSchedule> getRetrieveAllSS() {
        return retrieveAllSS;
    }

    public void setRetrieveAllSS(List<ScreeningSchedule> retrieveAllSS) {
        this.retrieveAllSS = retrieveAllSS;
    }

    public Date getSelectedSSDate() {
        return selectedSSDate;
    }

    public void setSelectedSSDate(Date selectedSSDate) {
        this.selectedSSDate = selectedSSDate;
    }

}
