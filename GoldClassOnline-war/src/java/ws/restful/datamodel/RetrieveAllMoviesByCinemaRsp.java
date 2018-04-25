/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful.datamodel;

import entity.MovieEntity;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author Farhan Angullia
 */

@XmlRootElement
@XmlType(name = "retrieveAllMoviesByCinemaRsp", propOrder = {
    "movieEntities"
})
public class RetrieveAllMoviesByCinemaRsp {
    private List<MovieEntity> movieEntities;

    public RetrieveAllMoviesByCinemaRsp() {
    }

    public RetrieveAllMoviesByCinemaRsp(List<MovieEntity> movieEntities) {
        this.movieEntities = movieEntities;
    }

    /**
     * @return the movieEntities
     */
    public List<MovieEntity> getMovieEntities() {
        return movieEntities;
    }

    /**
     * @param movieEntities the movieEntities to set
     */
    public void setMovieEntities(List<MovieEntity> movieEntities) {
        this.movieEntities = movieEntities;
    }
    
}
