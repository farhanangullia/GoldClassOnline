/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful.datamodel;

import entity.MovieEntity;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author Farhan Angullia
 */
@XmlRootElement
@XmlType(name = "retrieveMovieRsp", propOrder = {
    "movieEntity"
})
public class RetrieveMovieRsp {
    private MovieEntity movieEntity;

    public RetrieveMovieRsp() {
    }

    public RetrieveMovieRsp(MovieEntity movieEntity) {
        this.movieEntity = movieEntity;
    }

    /**
     * @return the movieEntity
     */
    public MovieEntity getMovieEntity() {
        return movieEntity;
    }

    /**
     * @param movieEntity the movieEntity to set
     */
    public void setMovieEntity(MovieEntity movieEntity) {
        this.movieEntity = movieEntity;
    }
    
}
