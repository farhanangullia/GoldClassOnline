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
import util.exception.CinemaNotFoundException;
import util.exception.CreateNewCinemaException;

/**
 *
 * @author KERK
 */
@Named(value = "cinemaManagementManagedBean")
@ViewScoped
public class CinemaManagementManagedBean implements Serializable {

    @EJB
    private HallEntityControllerLocal hallEntityControllerLocal;

    @EJB
    private CinemaEntityControllerLocal cinemaEntityControllerLocal;
    
    private List<CinemaEntity> cinemaEntities;
    private List<CinemaEntity> filteredCinemaEntities;
    private CinemaEntity newCinemaEntity;
    private CinemaEntity selectedCinemaEntityToView;
    private CinemaEntity selectedCinemaEntityToUpdate;
    

    /**
     * Creates a new instance of CinemaManagementManagedBean
     */
    public CinemaManagementManagedBean() {
        cinemaEntities = new ArrayList<>();
        filteredCinemaEntities = new ArrayList<>();
        newCinemaEntity = new CinemaEntity(); 

    }
    
    @PostConstruct
    public void postConstruct()
    {
        cinemaEntities = cinemaEntityControllerLocal.retrieveAllCinemaEntities();
        filteredCinemaEntities = cinemaEntities;
    }
    
    public void createNewCinema(ActionEvent event) {
        CinemaEntity ce = cinemaEntityControllerLocal.createCinemaEntity(newCinemaEntity);
        cinemaEntities.add(ce);
        filteredCinemaEntities.add(ce);
        newCinemaEntity = new CinemaEntity();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New cinema created successfully (Product ID: " + ce.getId() + ")", null));     
    }
    
    public void updateCinema(ActionEvent event) {
        try
        {
            cinemaEntityControllerLocal.updateCinemaEntity(selectedCinemaEntityToUpdate);

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Cinema updated successfully", null));
        }
        catch(Exception ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
    }
    
    public void deleteCinema(ActionEvent event)
    {
        try
        {
            CinemaEntity cinemaEntityToDelete = (CinemaEntity)event.getComponent().getAttributes().get("cinemaEntityToDelete");
            cinemaEntityControllerLocal.deleteCinema(cinemaEntityToDelete.getId());
            
            cinemaEntities.remove(cinemaEntityToDelete);
            filteredCinemaEntities.remove(cinemaEntityToDelete);

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Cinema deleted successfully", null));
        }
        catch(CinemaNotFoundException ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while deleting product: " + ex.getMessage(), null));
        }
        catch(Exception ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
    }

    public List<CinemaEntity> getCinemaEntities() {
        return cinemaEntities;
    }

    public void setCinemaEntities(List<CinemaEntity> cinemaEntities) {
        this.cinemaEntities = cinemaEntities;
    }

    public List<CinemaEntity> getFilteredCinemaEntities() {
        return filteredCinemaEntities;
    }

    public void setFilteredCinemaEntities(List<CinemaEntity> filteredCinemaEntities) {
        this.filteredCinemaEntities = filteredCinemaEntities;
    }

    public CinemaEntity getNewCinemaEntity() {
        return newCinemaEntity;
    }

    public void setNewCinemaEntity(CinemaEntity newCinemaEntity) {
        this.newCinemaEntity = newCinemaEntity;
    }

    public CinemaEntity getSelectedCinemaEntityToView() {
        return selectedCinemaEntityToView;
    }

    public void setSelectedCinemaEntityToView(CinemaEntity selectedCinemaEntityToView) {
        this.selectedCinemaEntityToView = selectedCinemaEntityToView;
    }

    public CinemaEntity getSelectedCinemaEntityToUpdate() {
        return selectedCinemaEntityToUpdate;
    }

    public void setSelectedCinemaEntityToUpdate(CinemaEntity selectedCinemaEntityToUpdate) {
        this.selectedCinemaEntityToUpdate = selectedCinemaEntityToUpdate;
    }
    
}
