/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import ejb.session.stateless.CinemaEntityControllerLocal;
import ejb.session.stateless.CustomerEntityControllerLocal;
import ejb.session.stateless.HallEntityControllerLocal;
import ejb.session.stateless.MovieEntityControllerLocal;
import ejb.session.stateless.ScreeningScheduleControllerLocal;
import ejb.session.stateless.StaffEntityControllerLocal;
import entity.CinemaEntity;
import entity.CustomerEntity;
import entity.HallEntity;
import entity.MovieEntity;
import entity.ScreeningSchedule;
import entity.StaffEntity;
import java.util.Date;
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
    private ScreeningScheduleControllerLocal screeningScheduleControllerLocal;

    @EJB
    private HallEntityControllerLocal hallEntityControllerLocal;

    @EJB
    private CinemaEntityControllerLocal cinemaEntityControllerLocal;

    @EJB
    private MovieEntityControllerLocal movieEntityControllerLocal;

    @EJB
    private StaffEntityControllerLocal staffEntityControllerLocal;

    @EJB
    private CustomerEntityControllerLocal customerEntityControllerLocal;

    public DataInitializationSessionBean() {
    }

    @PostConstruct
    public void postConstruct() {
        try {
            staffEntityControllerLocal.retrieveStaffByUsername("manager");
        } catch (StaffNotFoundException ex) {
            initializeData();
        }

    }

    private void initializeData() {
        try {

            StaffEntity admin = staffEntityControllerLocal.createStaffEntity(new StaffEntity("Admin", "Manager", AccessRightEnum.ADMIN, "manager", "password"));
            StaffEntity cinemaStaff = staffEntityControllerLocal.createStaffEntity(new StaffEntity("Cinema", "Staff", AccessRightEnum.CINEMASTAFF, "cinemastaff", "password"));

            MovieEntity movieEntity = movieEntityControllerLocal.createMovieEntity(new MovieEntity("Avengers", "Action", "Robert Downey Jr, Chris Evans, Black Panther, Scarlett Johansson", "Russo Brother", 180, "English", "Avengers assemble to save Earth!", "PG13", "../assets/img/avengers.jpg"));
            movieEntity.setEnabled(Boolean.TRUE);
            MovieEntity movieEntity2 = movieEntityControllerLocal.createMovieEntity(new MovieEntity("Frozen", "Cartoon", "Elsa, Anna", "Kanye West", 120, "English", "Let it go, let it go!", "PG", "../assets/img/frozen.jpg"));
            movieEntity2.setEnabled(Boolean.TRUE);

            CinemaEntity cinemaEntity = cinemaEntityControllerLocal.createCinemaEntity(new CinemaEntity("Star Movie", "Kent Ridge Drive", "123456"));
            cinemaEntity.setEnabled(Boolean.TRUE);

            HallEntity hallEntity = hallEntityControllerLocal.createHallEntity(new HallEntity("Premium", 5, 6), cinemaEntity.getId());
            hallEntity.setEnabled(Boolean.TRUE);
            char[][] seating = new char[hallEntity.getRow()][hallEntity.getCol()];
            for (int i = 0; i < hallEntity.getRow(); i++) {
                for (int j = 0; j < hallEntity.getCol(); j++) {
                    seating[i][j] = 'o';
                }
            }
            hallEntity.setSeating(seating);

            Date calendarStart = new Date(2018, 5, 12, 14, 0);

            ScreeningSchedule screeningSchedule = screeningScheduleControllerLocal.createScreeningSchedule(new ScreeningSchedule(calendarStart), movieEntity, hallEntity.getId());
            screeningSchedule.setEnabled(Boolean.TRUE);

            customerEntityControllerLocal.createCustomerEntity(new CustomerEntity("Johnnie", "Walker", "25", "default", "password"));

        } catch (Exception ex) {
            System.err.println("********** DataInitializationSessionBean.initializeData(): An error has occurred while loading initial test data: " + ex.getMessage());
        }
    }
}
