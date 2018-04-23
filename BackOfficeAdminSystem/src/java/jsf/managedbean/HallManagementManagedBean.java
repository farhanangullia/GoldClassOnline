/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.HallEntityControllerLocal;
import entity.CinemaEntity;
import entity.HallEntity;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 *
 * @author KERK
 */
@Named(value = "hallManagementManagedBean")
@ViewScoped
public class HallManagementManagedBean implements Serializable {

    @EJB
    private HallEntityControllerLocal hallEntityControllerLocal;

 
    public HallManagementManagedBean() {
 
    }
    
}
