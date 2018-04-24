/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 *
 * @author KERK
 */
@Entity
public class MovieEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String title;
    private String genre;
    private String cast;
    private String director;
    @Column(nullable = false)
    private Integer runningTime;
    private String language;
    private String sypnosis;
    @Column(nullable = false)
    private String ratings;
    private Boolean enabled;
    @Column
    private String imagePath;
    @OneToMany (mappedBy="movieEntity", fetch = FetchType.EAGER)
    private List<ScreeningSchedule> screeningSchedules;
    
    public MovieEntity() {
        this.enabled = true;
    }

    
    public MovieEntity(String title, String genre, String cast, String director, Integer runningTime, String language, String sypnosis, String ratings, String imagePath) {
        this.title = title;
        this.genre = genre;
        this.cast = cast;
        this.director = director;
        this.runningTime = runningTime;
        this.language = language;
        this.sypnosis = sypnosis;
        this.ratings = ratings;
        this.imagePath = imagePath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getCast() {
        return cast;
    }

    public void setCast(String cast) {
        this.cast = cast;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public Integer getRunningTime() {
        return runningTime;
    }

    public void setRunningTime(Integer runningTime) {
        this.runningTime = runningTime;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getSypnosis() {
        return sypnosis;
    }

    public void setSypnosis(String sypnosis) {
        this.sypnosis = sypnosis;
    }

    public String getRatings() {
        return ratings;
    }

    public void setRatings(String ratings) {
        this.ratings = ratings;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MovieEntity)) {
            return false;
        }
        MovieEntity other = (MovieEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.MovieEntity[ id=" + id + " ]";
    }

    public List<ScreeningSchedule> getScreeningSchedules() {
        return screeningSchedules;
    }

    public void setScreeningSchedules(List<ScreeningSchedule> screeningSchedules) {
        this.screeningSchedules = screeningSchedules;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * @return the imagePath
     */
    public String getImagePath() {
        return imagePath;
    }

    /**
     * @param imagePath the imagePath to set
     */
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
    
}
