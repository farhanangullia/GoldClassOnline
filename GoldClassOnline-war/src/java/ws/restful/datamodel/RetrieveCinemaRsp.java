/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful.datamodel;

import entity.CinemaEntity;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author Farhan Angullia
 */
@XmlRootElement
@XmlType(name = "retrieveCinemaRsp", propOrder = {
    "cinemaEntity"
})
public class RetrieveCinemaRsp {
    private CinemaEntity cinemaEntity;

    public RetrieveCinemaRsp() {
    }

    public RetrieveCinemaRsp(CinemaEntity cinemaEntity) {
        this.cinemaEntity = cinemaEntity;
    }

    /**
     * @return the cinemaEntity
     */
    public CinemaEntity getCinemaEntity() {
        return cinemaEntity;
    }

    /**
     * @param cinemaEntity the cinemaEntity to set
     */
    public void setCinemaEntity(CinemaEntity cinemaEntity) {
        this.cinemaEntity = cinemaEntity;
    }
    
    
}
