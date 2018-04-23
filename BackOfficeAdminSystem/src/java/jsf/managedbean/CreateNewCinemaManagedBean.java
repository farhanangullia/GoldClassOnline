/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.CinemaEntityControllerLocal;
import entity.CinemaEntity;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 *
 * @author KERK
 */
@Named(value = "createNewCinemaManagedBean")
@RequestScoped
public class CreateNewCinemaManagedBean {

    @EJB
    private CinemaEntityControllerLocal cinemaEntityControllerLocal;
    
    private CinemaEntity newCinemaEntity;

    public CreateNewCinemaManagedBean() {
        newCinemaEntity = new CinemaEntity();       
    }
    
    public void createNewCinema() {
        cinemaEntityControllerLocal.createCinemaEntity(newCinemaEntity);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("New cinema created successfully (Cinema ID: " + newCinemaEntity.getId()+ ")"));
        newCinemaEntity = new CinemaEntity();      
    }

    public CinemaEntity getNewCinemaEntity() {
        return newCinemaEntity;
    }

    public void setNewCinemaEntity(CinemaEntity newCinemaEntity) {
        this.newCinemaEntity = newCinemaEntity;
    }
    
    
    
}
