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
    private List<SelectItem> selectItems;

    private List<MovieEntity> movieEntities;

    private List<String> selectedHandicap;
    private List<String> selectedHandicapToRemove;
    private List<String> selectedDisabled;
    private List<String> selectedDisabledToRemove;

    private Date currentDate;

    public HallDetailsManagedBean() {
        hallEntityToView = new HallEntity();
        hallEntityToUpdate = new HallEntity();
        screeningSchedules = new ArrayList<>();
        filteredScreeningSchedules = new ArrayList<>();
        movieEntities = new ArrayList<>();
        newScreeningSchedule = new ScreeningSchedule();
        selectItems = new ArrayList<>();
        selectedMovieEntity = new MovieEntity();
        selectedMovieEntityToUpdate = new MovieEntity();

        selectedHandicap = new ArrayList<>();
        selectedDisabled = new ArrayList<>();
        selectedHandicapToRemove = new ArrayList<>();
        selectedDisabledToRemove = new ArrayList<>();
        currentDate = new Date();

    }

    @PostConstruct
    public void postConstruct() {
        hallEntityToViewId = (Long) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("hallIdToView");
        hallEntityToView = hallEntityControllerLocal.retrieveHallByHallId(hallEntityToViewId);
        screeningSchedules = screeningScheduleControllerLocal.retrieveAllScreeningSchedules(hallEntityToViewId);
        filteredScreeningSchedules = screeningSchedules;
        movieEntities = movieEntityControllerLocal.retrieveAllMovieEntities();
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
        ScreeningSchedule se = screeningScheduleControllerLocal.createScreeningSchedule(newScreeningSchedule, selectedMovieEntity, hallEntityToViewId);
//        MovieEntity me = (MovieEntity)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("movieName");
//        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("selectedMovieEntity", me);
        screeningSchedules.add(se);
        filteredScreeningSchedules = screeningSchedules;
        newScreeningSchedule = new ScreeningSchedule();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New cinema created successfully (Screening Schedule ID: " + se.getId() + ")", null));
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
            screeningScheduleControllerLocal.updateScreeningSchedule(selectedScheduleToUpdate, selectedMovieEntityToUpdate, hallEntityToViewId);

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
            hallEntityToView.currentDisabledSeats();
            hallEntityToView.currentHandicapSeats();
            hallEntityToView.seatsInArray();

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Hall Seating updated successfully", null));
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
            hallEntityToView.currentDisabledSeats();
            hallEntityToView.currentHandicapSeats();
            hallEntityToView.seatsInArray();

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Hall Seating updated successfully", null));
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
            hallEntityToView.currentDisabledSeats();
            hallEntityToView.currentHandicapSeats();
            hallEntityToView.seatsInArray();

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Hall Seating updated successfully", null));
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
            hallEntityToView.currentDisabledSeats();
            hallEntityToView.currentHandicapSeats();
            hallEntityToView.seatsInArray();

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Hall Seating updated successfully", null));
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

}
