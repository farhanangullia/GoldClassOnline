/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class HallEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Integer row;
    private Integer col;
    private String[][] seating;
    private Boolean enabled;
    @ManyToOne(fetch = FetchType.EAGER)
    private CinemaEntity cinemaEntity;
    @OneToMany(mappedBy = "hallEntity", fetch = FetchType.EAGER)
    private List<ScreeningSchedule> screeningSchedules;

    public HallEntity() {
        this.enabled = true;
        screeningSchedules = new ArrayList<>();
    }

    public HallEntity(String name, Integer row, Integer col) {
        this();
        this.name = name;
        this.row = row;
        this.col = col;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getRow() {
        return row;
    }

    public void setRow(Integer row) {
        this.row = row;
    }

    public Integer getCol() {
        return col;
    }

    public void setCol(Integer col) {
        this.col = col;
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
        if (!(object instanceof HallEntity)) {
            return false;
        }
        HallEntity other = (HallEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.HallEntity[ id=" + id + " ]";
    }

    public CinemaEntity getCinemaEntity() {
        return cinemaEntity;
    }

    public void setCinemaEntity(CinemaEntity cinemaEntity) {
        this.cinemaEntity = cinemaEntity;
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
     * @return the seating
     */
    public String[][] getSeating() {
        return seating;
    }

    /**
     * @param seating the seating to set
     */
    public void setSeating(String[][] seating) {
        this.seating = seating;
    }

    public List<String> seatsInArray() {
        List<String> seatsToArray = new ArrayList<>();
        List<String> seatsToArray2 = new ArrayList<>();
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (seating[i][j].equals("X") || seating[i][j].contains("(H)")) {
                    seatsToArray2.add(seating[i][j]);
                } else {
                    seatsToArray.add(seating[i][j]);
                }
            }
        }
        return seatsToArray;
    }

    public List<String> currentDisabledSeats() {
        List<String> seatsToArray = new ArrayList<>();
        String[][] seating2 = new String[row][col];
        char r, c;
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (seating[i][j].equals("X")) {
                    r = (char) (i + 65);
                    c = (char) (j + 49);
                    seating2[i][j] = "" + r + c;
                    seatsToArray.add(seating2[i][j]);
                }
            }
        }
        return seatsToArray;
    }

    public List<String> currentHandicapSeats() {
        List<String> seatsToArray = new ArrayList<>();
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (seating[i][j].contains("(H)")) {
                    seatsToArray.add(seating[i][j]);
                }
            }
        }
        return seatsToArray;
    }

}
