/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful.datamodel;

import entity.CinemaEntity;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author Farhan Angullia
 */

@XmlRootElement
@XmlType(name = "retrieveAllCinemasByMovieRsp", propOrder = {
    "cinemaEntities"
})
public class RetrieveAllCinemasByMovieRsp {
    
    private List<CinemaEntity> cinemaEntities;

    public RetrieveAllCinemasByMovieRsp() {
    }

    public RetrieveAllCinemasByMovieRsp(List<CinemaEntity> cinemaEntities) {
        this.cinemaEntities = cinemaEntities;
    }

    /**
     * @return the cinemaEntities
     */
    public List<CinemaEntity> getCinemaEntities() {
        return cinemaEntities;
    }

    /**
     * @param cinemaEntities the cinemaEntities to set
     */
    public void setCinemaEntities(List<CinemaEntity> cinemaEntities) {
        this.cinemaEntities = cinemaEntities;
    }
    
    
    
}
