/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class ScreeningSchedule implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Temporal(TemporalType.TIMESTAMP)
    private Date screeningTime;
    @Temporal(TemporalType.TIMESTAMP)
    private Date screeningEndTime;
    private Boolean enabled;
    @Column(precision = 11, scale = 2)
    private BigDecimal price;
    @ManyToOne(fetch = FetchType.EAGER)
    private HallEntity hallEntity;
    @ManyToOne(fetch = FetchType.EAGER)
    private MovieEntity movieEntity;
    @OneToMany(mappedBy = "screeningSchedule", fetch = FetchType.EAGER)
    private List<TicketEntity> ticketEntities;

    public ScreeningSchedule() {
        this.enabled = true;
        ticketEntities = new ArrayList<>();
    }

    public ScreeningSchedule(Date screeningTime) {
        this.screeningTime = screeningTime;
    }

    public Date getScreeningTime() {
        return screeningTime;
    }

    public void setScreeningTime(Date screeningTime) {
        this.screeningTime = screeningTime;
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
        if (!(object instanceof ScreeningSchedule)) {
            return false;
        }
        ScreeningSchedule other = (ScreeningSchedule) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.ScreeningSchedule[ id=" + id + " ]";
    }

    public HallEntity getHallEntity() {
        return hallEntity;
    }

    public void setHallEntity(HallEntity hallEntity) {
        this.hallEntity = hallEntity;
    }

    public MovieEntity getMovieEntity() {
        return movieEntity;
    }

    public void setMovieEntity(MovieEntity movieEntity) {
        this.movieEntity = movieEntity;
    }

    public List<TicketEntity> getTicketEntities() {
        return ticketEntities;
    }

    public void setTicketEntities(List<TicketEntity> ticketEntities) {
        this.ticketEntities = ticketEntities;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Date getScreeningEndTime() {
        return screeningEndTime;
    }

    public void setScreeningEndTime(Date screeningEndTime) {
        this.screeningEndTime = screeningEndTime;
    }

    /**
     * @return the price
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

}
