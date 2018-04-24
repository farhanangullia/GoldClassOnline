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
 * @author KERK
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
    private ScreeningSchedule selectedScheduleToUpdate;
    
    private MovieEntity selectedMovieEntity;
    private MovieEntity selectedMovieEntityToUpdate;
    
    private ScreeningSchedule newScreeningSchedule;
    
    private List<ScreeningSchedule> screeningSchedules;
    private List<ScreeningSchedule> filteredScreeningSchedules;
    private List<SelectItem> selectItems;
    
    private List<MovieEntity> movieEntities;

    
    public HallDetailsManagedBean() {
        hallEntityToView = new HallEntity();
        screeningSchedules = new ArrayList<>();
        filteredScreeningSchedules = new ArrayList<>();
        movieEntities =  new ArrayList<>();
        newScreeningSchedule = new ScreeningSchedule();
        selectItems = new ArrayList<>();
        selectedMovieEntity = new MovieEntity();
        selectedMovieEntityToUpdate = new MovieEntity();
    }
    
    @PostConstruct
    public void postConstruct()
    {
        hallEntityToViewId = (Long)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("hallIdToView");
        System.err.println("Hall ID: " + hallEntityToViewId);
        hallEntityToView = hallEntityControllerLocal.retrieveHallByHallId(hallEntityToViewId);
        screeningSchedules = screeningScheduleControllerLocal.retrieveAllScreeningSchedules(hallEntityToViewId);
        filteredScreeningSchedules = screeningSchedules;
        movieEntities = movieEntityControllerLocal.retrieveAllMovieEntities();
        for(MovieEntity movieEntity:movieEntities)
        {
            selectItems.add(new SelectItem(movieEntity, movieEntity.getTitle(), movieEntity.getId().toString()));
        }
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("MovieEntityConverter.movieEntities", movieEntities);
    }
    
    @PreDestroy
    public void preDestroy()
    {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("MovieEntityConverter.movieEntities", null);
    }
    
    
    public void createNewScreeningSchedule(ActionEvent event) {
        ScreeningSchedule se = screeningScheduleControllerLocal.createScreeningSchedule(newScreeningSchedule, selectedMovieEntity,hallEntityToViewId);
        screeningSchedules.add(se);
        filteredScreeningSchedules.add(se);
        newScreeningSchedule = new ScreeningSchedule();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New cinema created successfully (Screening Schedule ID: " + se.getId() + ")", null));     
    }
    
    public char[][] seatsArray() {
        char[][] seating = new char[hallEntityToView.getRow()][hallEntityToView.getCol()];
        for (int i = 0; i < hallEntityToView.getRow(); i++) {
            for (int j = 0; j < hallEntityToView.getCol(); j++) {
                seating[i][j] = 'x';
            }
        }
        return seating;
    }
    
    public void deleteScreeningSchedule (ActionEvent event)
    {
        try
        {
            ScreeningSchedule ssEntityToDelete = (ScreeningSchedule)event.getComponent().getAttributes().get("ssEntityToDelete");
            screeningScheduleControllerLocal.deleteScreeningSchedule(ssEntityToDelete.getId());
            
            screeningSchedules.remove(ssEntityToDelete);
            filteredScreeningSchedules.remove(ssEntityToDelete);

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Screening Schedule deleted successfully", null));
        }
        catch(Exception ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
    }
    
    public void updateScreeningSchedule(ActionEvent event) {
        try
        {
            screeningScheduleControllerLocal.updateScreeningSchedule(selectedScheduleToUpdate, selectedMovieEntityToUpdate, hallEntityToViewId);

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Screening Schedule updated successfully", null));
        }
        catch(Exception ex)
        {
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

}
