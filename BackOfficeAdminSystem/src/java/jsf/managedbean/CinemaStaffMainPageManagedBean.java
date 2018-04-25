/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.CinemaEntityControllerLocal;
import ejb.session.stateless.HallEntityControllerLocal;
import entity.CinemaEntity;
import entity.HallEntity;
import java.io.IOException;
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
import util.exception.HallNotFoundException;

/**
 *
 * @author 
 */
@Named(value = "cinemaStaffMainPageManagedBean")
@ViewScoped
public class CinemaStaffMainPageManagedBean implements Serializable {

    @EJB
    private HallEntityControllerLocal hallEntityControllerLocal;

    @EJB
    private CinemaEntityControllerLocal cinemaEntityControllerLocal;
    
    private HallEntity selectedHallEntityToUpdate;
    private HallEntity selectedHallEntityToView;
    private HallEntity newHallEntity;
    
    private CinemaEntity selectedCinemaEntity;
    private CinemaEntity selectedCinemaEntityToUpdate;
    
    private List<HallEntity> hallEntities;
    private List<HallEntity> filteredHallEntities;
    
    private List<CinemaEntity> cinemaEntities;
    
    private List<SelectItem> selectItems;

    
   

    public CinemaStaffMainPageManagedBean() {
        hallEntities = new ArrayList<>();
        filteredHallEntities = new ArrayList<>();
        selectedHallEntityToUpdate = new HallEntity();
        selectedHallEntityToView = new HallEntity();
        newHallEntity = new HallEntity();
        selectItems = new ArrayList<>();
        cinemaEntities = new ArrayList<>();
        selectedCinemaEntity = new CinemaEntity();
        selectedCinemaEntityToUpdate = new CinemaEntity();
                
  
    }

    @PostConstruct
    public void postConstruct() {
        hallEntities = hallEntityControllerLocal.retrieveAllHallsForCinemaStaff();
        filteredHallEntities = hallEntities;
        cinemaEntities = cinemaEntityControllerLocal.retrieveAllCinemaEntities();
         for(CinemaEntity cinemaEntity: cinemaEntities)
        {
            selectItems.add(new SelectItem(cinemaEntity, cinemaEntity.getName(), cinemaEntity.getId().toString()));
        }
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("CinemaEntityConverter.cinemaEntities", cinemaEntities);
     
    }
    
    @PreDestroy
    public void preDestroy()
    {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("CinemaEntityConverter.cinemaEntities", null);
    }
    
    public void createNewHall(ActionEvent event) {
        HallEntity he = hallEntityControllerLocal.createHallEntity(newHallEntity, selectedCinemaEntity.getId());
        hallEntities.add(he);
        filteredHallEntities.add(he);
        newHallEntity = new HallEntity();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New hall created successfully (Screening Schedule ID: " + he.getId() + ")", null));     
    }
    
    
    public void deleteHall(ActionEvent event)
    {
        try
        {
            HallEntity hallEntityToDelete = (HallEntity)event.getComponent().getAttributes().get("hallEntityToDelete");
            hallEntityControllerLocal.deleteHall(hallEntityToDelete.getId());
            
            hallEntities.remove(hallEntityToDelete);
            filteredHallEntities.remove(hallEntityToDelete);

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Hall deleted successfully", null));
        }
        catch (Exception ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
    }
    
    public void viewHallDetails(ActionEvent event) throws IOException {
        
        Long hallEntityToViewId = (Long) event.getComponent().getAttributes().get("hallId");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("hallIdToView", hallEntityToViewId);
        FacesContext.getCurrentInstance().getExternalContext().redirect("viewHallDetails.xhtml");
    }
    
     public void updateHall(ActionEvent event) {
        try
        {       
            hallEntityControllerLocal.updateHallEntityWithCinemaEntity(selectedHallEntityToUpdate, selectedCinemaEntityToUpdate);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Hall updated successfully", null));
        }
        catch(Exception ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
    }
  

    public List<HallEntity> getFilteredHallEntities() {
        return filteredHallEntities;
    }

    public void setFilteredHallEntities(List<HallEntity> filteredHallEntities) {
        this.filteredHallEntities = filteredHallEntities;
    }

    public List<HallEntity> getHallEntities() {
        return hallEntities;
    }

    public void setHallEntities(List<HallEntity> hallEntities) {
        this.hallEntities = hallEntities;
    }

     public HallEntity getSelectedHallEntityToUpdate() {
        return selectedHallEntityToUpdate;
    }

    public void setSelectedHallEntityToUpdate(HallEntity selectedHallEntityToUpdate) {
        this.selectedHallEntityToUpdate = selectedHallEntityToUpdate;
    }

    public HallEntity getSelectedHallEntityToView() {
        return selectedHallEntityToView;
    }

    public void setSelectedHallEntityToView(HallEntity selectedHallEntityToView) {
        this.selectedHallEntityToView = selectedHallEntityToView;
    }

    public HallEntity getNewHallEntity() {
        return newHallEntity;
    }

    public void setNewHallEntity(HallEntity newHallEntity) {
        this.newHallEntity = newHallEntity;
    }

    public List<CinemaEntity> getCinemaEntities() {
        return cinemaEntities;
    }

    public void setCinemaEntities(List<CinemaEntity> cinemaEntities) {
        this.cinemaEntities = cinemaEntities;
    }

    public List<SelectItem> getSelectItems() {
        return selectItems;
    }

    public void setSelectItems(List<SelectItem> selectItems) {
        this.selectItems = selectItems;
    }

    public CinemaEntity getSelectedCinemaEntity() {
        return selectedCinemaEntity;
    }

    public void setSelectedCinemaEntity(CinemaEntity selectedCinemaEntity) {
        this.selectedCinemaEntity = selectedCinemaEntity;
    }

    public CinemaEntity getSelectedCinemaEntityToUpdate() {
        return selectedCinemaEntityToUpdate;
    }

    public void setSelectedCinemaEntityToUpdate(CinemaEntity selectedCinemaEntityToUpdate) {
        this.selectedCinemaEntityToUpdate = selectedCinemaEntityToUpdate;
    }

}
