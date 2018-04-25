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
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
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

    private CinemaEntity cinemaEntityToView;
    private Long cinemaEntityToViewId;
    private HallEntity newHallEntity;
    private HallEntity selectedHallEntityToView;
    private HallEntity selectedHallEntityToUpDate;

    private List<HallEntity> hallEntities;

    public CinemaStaffMainPageManagedBean() {
        hallEntities = new ArrayList<>();
        cinemaEntityToView = new CinemaEntity();
        newHallEntity = new HallEntity();
    }

    @PostConstruct
    public void postConstruct() {
        cinemaEntityToViewId = (Long) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("selectedCinemaEntityId");
        hallEntities = hallEntityControllerLocal.retrieveAllHalls(cinemaEntityToViewId);
        cinemaEntityToView = cinemaEntityControllerLocal.retrieveCinemaByCinemaId(cinemaEntityToViewId);
    }

    public void createNewHall() {

        HallEntity he = hallEntityControllerLocal.createHallEntity(newHallEntity, cinemaEntityToViewId);
        hallEntities.add(he);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("New hall created successfully (Cinema ID: " + newHallEntity.getId() + ")"));
        newHallEntity = new HallEntity();
    }

    public void viewHallDetails(ActionEvent event) throws IOException {

        Long hallEntityToViewId = (Long) event.getComponent().getAttributes().get("hallId");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("hallIdToView", hallEntityToViewId);
        FacesContext.getCurrentInstance().getExternalContext().redirect("viewHallDetails.xhtml");
    }

    public void deleteHall(ActionEvent event) {
        try {
            HallEntity hallEntityToDelete = (HallEntity) event.getComponent().getAttributes().get("hallEntityToDelete");
            hallEntityControllerLocal.deleteHall(hallEntityToDelete.getId());

            hallEntities.remove(hallEntityToDelete);

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Hall deleted successfully", null));
        } catch (HallNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while deleting hall: " + ex.getMessage(), null));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
    }

    public void updateHall(ActionEvent event) {
        try {
            hallEntityControllerLocal.updateHallEntity(selectedHallEntityToUpDate);

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Hall updated successfully", null));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
    }

    public CinemaEntity getCinemaEntityToView() {
        return cinemaEntityToView;
    }

    public void setCinemaEntityToView(CinemaEntity cinemaEntityToView) {
        this.cinemaEntityToView = cinemaEntityToView;
    }

    public Long getCinemaEntityToViewId() {
        return cinemaEntityToViewId;
    }

    public void setCinemaEntityToViewId(Long cinemaEntityToViewId) {
        this.cinemaEntityToViewId = cinemaEntityToViewId;
    }

    public HallEntity getNewHallEntity() {
        return newHallEntity;
    }

    public void setNewHallEntity(HallEntity newHallEntity) {
        this.newHallEntity = newHallEntity;
    }

    public List<HallEntity> getHallEntities() {
        return hallEntities;
    }

    public void setHallEntities(List<HallEntity> hallEntities) {
        this.hallEntities = hallEntities;
    }

    public HallEntity getSelectedHallEntityToView() {
        return selectedHallEntityToView;
    }

    public void setSelectedHallEntityToView(HallEntity selectedHallEntityToView) {
        this.selectedHallEntityToView = selectedHallEntityToView;
    }

    public HallEntity getSelectedHallEntityToUpDate() {
        return selectedHallEntityToUpDate;
    }

    public void setSelectedHallEntityToUpDate(HallEntity selectedHallEntityToUpDate) {
        this.selectedHallEntityToUpDate = selectedHallEntityToUpDate;
    }

}
