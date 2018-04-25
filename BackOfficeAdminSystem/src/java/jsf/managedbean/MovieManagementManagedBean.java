/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.MovieEntityControllerLocal;
import entity.MovieEntity;
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
import util.exception.MovieNotFoundException;

/**
 *
 * @author 
 */
@Named(value = "movieManagementManagedBean")
@ViewScoped
public class MovieManagementManagedBean implements Serializable {

    @EJB
    private MovieEntityControllerLocal movieEntityControllerLocal;
    
    private List<MovieEntity> movieEntities;
    private List<MovieEntity> filteredMovieEntities;
    private MovieEntity newMovieEntity;
    private MovieEntity selectedMovieEntityToView;
    private MovieEntity selectedMovieEntityToUpdate;
    
    public MovieManagementManagedBean() {
        movieEntities = new ArrayList<>();
        filteredMovieEntities = new ArrayList<>();
        
        newMovieEntity = new MovieEntity();
    }
    
    @PostConstruct
    public void postConstruct()
    {
        movieEntities = movieEntityControllerLocal.retrieveAllMovieEntities();
        filteredMovieEntities = movieEntities;
    }
    
    public void createNewMovie(ActionEvent event)
    {
        
            MovieEntity me = movieEntityControllerLocal.createMovieEntity(newMovieEntity);
            movieEntities.add(me);
            filteredMovieEntities = movieEntities;
            newMovieEntity = new MovieEntity();

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New movie created successfully (Movie ID: " + me.getId() + ")", null));
    }
    
    public void updateMovie(ActionEvent event)
    {
        try
        {
            movieEntityControllerLocal.updateMovieEntity(selectedMovieEntityToUpdate);

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Movie updated successfully", null));
        } 
        catch(Exception ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
    }
    
    public void deleteMovie(ActionEvent event)
    {
        try
        {
            MovieEntity movieEntityToDelete = (MovieEntity)event.getComponent().getAttributes().get("movieEntityToDelete");
            movieEntityControllerLocal.deleteMovie(movieEntityToDelete.getId());
            
            movieEntities.remove(movieEntityToDelete);
            filteredMovieEntities.remove(movieEntityToDelete);

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Movie deleted successfully", null));
        }
        catch(MovieNotFoundException ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while deleting movie: " + ex.getMessage(), null));
        }
        catch(Exception ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
    }

    public List<MovieEntity> getMovieEntities() {
        return movieEntities;
    }

    public void setMovieEntities(List<MovieEntity> movieEntities) {
        this.movieEntities = movieEntities;
    }

    public List<MovieEntity> getFilteredMovieEntities() {
        return filteredMovieEntities;
    }

    public void setFilteredMovieEntities(List<MovieEntity> filteredMovieEntities) {
        this.filteredMovieEntities = filteredMovieEntities;
    }

    public MovieEntity getNewMovieEntity() {
        return newMovieEntity;
    }

    public void setNewMovieEntity(MovieEntity newMovieEntity) {
        this.newMovieEntity = newMovieEntity;
    }

    public MovieEntity getSelectedMovieEntityToView() {
        return selectedMovieEntityToView;
    }

    public void setSelectedMovieEntityToView(MovieEntity selectedMovieEntityToView) {
        this.selectedMovieEntityToView = selectedMovieEntityToView;
    }

    public MovieEntity getSelectedMovieEntityToUpdate() {
        return selectedMovieEntityToUpdate;
    }

    public void setSelectedMovieEntityToUpdate(MovieEntity selectedMovieEntityToUpdate) {
        this.selectedMovieEntityToUpdate = selectedMovieEntityToUpdate;
    }
    
    
}
