/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import ejb.session.stateless.CinemaEntityControllerLocal;
import ejb.session.stateless.HallEntityControllerLocal;
import ejb.session.stateless.MovieEntityControllerLocal;
import ejb.session.stateless.StaffEntityControllerLocal;
import entity.CinemaEntity;
import entity.HallEntity;
import entity.MovieEntity;
import entity.StaffEntity;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import util.enumeration.AccessRightEnum;
import util.exception.StaffNotFoundException;

/**
 *
 * @author KERK
 */
@Singleton
@LocalBean
@Startup
public class DataInitializationSessionBean {

    @EJB
    private HallEntityControllerLocal hallEntityControllerLocal;

    @EJB
    private CinemaEntityControllerLocal cinemaEntityControllerLocal;

    @EJB
    private MovieEntityControllerLocal movieEntityControllerLocal;

    @EJB
    private StaffEntityControllerLocal staffEntityControllerLocal;

    public DataInitializationSessionBean() {
    }

   @PostConstruct
   public void postConstruct() {
       try
        {
            staffEntityControllerLocal.retrieveStaffByUsername("admin");
        }
        catch(StaffNotFoundException ex)
        {
            initializeData();
        }
       
   }
   
   private void initializeData() {
       try {
           
           StaffEntity admin = staffEntityControllerLocal.createStaffEntity(new StaffEntity("Admin", "Manager", AccessRightEnum.ADMIN, "admin","password"));
           StaffEntity cinemaStaff = staffEntityControllerLocal.createStaffEntity(new StaffEntity("Cinema", "Staff", AccessRightEnum.CINEMASTAFF, "cinemastaff", "password"));
       
           MovieEntity movieEntity = movieEntityControllerLocal.createMovieEntity(new MovieEntity("Avengers", "Action", "Robert Downey Jr, Chris Evans, Black Panther, Scarlett Johansson", "Russo Brother", "3 Hours", "English", "Avengers assemble to save Earth!", "PG13"));
           movieEntity.setEnabled(Boolean.TRUE);
           
           CinemaEntity cinemaEntity = cinemaEntityControllerLocal.createCinemaEntity(new CinemaEntity("Star Movie", "Kent Ridge Drive", "123456"));
           cinemaEntity.setEnabled(Boolean.TRUE);
           
           HallEntity hallEntity = hallEntityControllerLocal.createHallEntity(new HallEntity("Premium Suite", 4, 5), cinemaEntity);
           
       } catch(Exception ex)
        {
            System.err.println("********** DataInitializationSessionBean.initializeData(): An error has occurred while loading initial test data: " + ex.getMessage());
        }
   }
}
