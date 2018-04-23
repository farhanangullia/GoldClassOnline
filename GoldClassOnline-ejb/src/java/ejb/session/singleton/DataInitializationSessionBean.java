/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import ejb.session.stateless.CinemaEntityControllerLocal;
import ejb.session.stateless.HallEntityControllerLocal;
import ejb.session.stateless.MovieEntityControllerLocal;
import ejb.session.stateless.ScreeningScheduleControllerLocal;
import ejb.session.stateless.StaffEntityControllerLocal;
import entity.CinemaEntity;
import entity.HallEntity;
import entity.MovieEntity;
import entity.ScreeningSchedule;
import entity.StaffEntity;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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

    @EJB
    private ScreeningScheduleControllerLocal screeningScheduleControllerLocal;

    @PersistenceContext(unitName = "GoldClassOnline-ejbPU")
    private EntityManager em;

    public DataInitializationSessionBean() {
    }

    @PostConstruct
    public void postConstruct() {
        try {
            staffEntityControllerLocal.retrieveStaffByUsername("admin");
        } catch (StaffNotFoundException ex) {
            initializeData();
        }

    }

    private void initializeData() {
        try {

            StaffEntity admin = staffEntityControllerLocal.createStaffEntity(new StaffEntity("Admin", "Manager", AccessRightEnum.ADMIN, "admin", "password"));
            StaffEntity cinemaStaff = staffEntityControllerLocal.createStaffEntity(new StaffEntity("Cinema", "Staff", AccessRightEnum.CINEMASTAFF, "cinemastaff", "password"));

            MovieEntity movieEntity = movieEntityControllerLocal.createMovieEntity(new MovieEntity("Avengers", "Action", "Robert Downey Jr, Chris Evans, Black Panther, Scarlett Johansson", "Russo Brother", 180, "English", "Avengers assemble to save Earth!", "PG13", "../assets/img/avengers.jpg"));
            movieEntity.setEnabled(Boolean.TRUE);

            CinemaEntity cinemaEntity = cinemaEntityControllerLocal.createCinemaEntity(new CinemaEntity("Star Movie", "Kent Ridge Drive", "123456"));
            cinemaEntity.setEnabled(Boolean.TRUE);

            HallEntity hallEntity = hallEntityControllerLocal.createHallEntity(new HallEntity("Premium Suite", 4, 5), cinemaEntity.getId());

            List<Date> calendarScreeningDays = new ArrayList<>();
            //  Date screenDay = new Date();
            Date screenDay = new Date(118, 3, 25);

            calendarScreeningDays.add(screenDay);
            hallEntity.setCalendarScreeningDays(calendarScreeningDays);

            screenDay.setHours(11);
            screenDay.setMinutes(30);

            ScreeningSchedule screeningSchedule = screeningScheduleControllerLocal.createScreeningSchedule(new ScreeningSchedule(screenDay, true, hallEntity, movieEntity));
            hallEntity.getScreeningSchedules().add(screeningSchedule);
            movieEntity.getScreeningSchedules().add(screeningSchedule);
            em.merge(hallEntity);
            em.merge(movieEntity);

            System.out.println(screenDay.toString());
            DateFormat dateFormatter = DateFormat.getDateInstance();
            System.out.println(dateFormatter.format(screenDay));

        } catch (Exception ex) {
            System.err.println("********** DataInitializationSessionBean.initializeData(): An error has occurred while loading initial test data: " + ex.getMessage());
        }
    }
}
