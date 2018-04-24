/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.converter;


import entity.MovieEntity;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author User
 */
@FacesConverter(value = "movieEntityConverter", forClass = MovieEntity.class)
public class MovieEntityConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.trim().isEmpty() || value.equals("null")) {
            return null;
        }
        try {
            List<MovieEntity> movieEntities = (List<MovieEntity>) context.getExternalContext().getSessionMap().get("MovieEntityConverter.movieEntities");
            for (MovieEntity movieEntity : movieEntities) {
                if (movieEntity.getId().toString().equals(value)) {
                    return movieEntity;
                }
            }
        } catch (Exception ex) {
            throw new IllegalArgumentException("Please select a valid value");
        }

        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value == null) {
            return "";
        }

        if (value instanceof String) {
            return "";
        }

        if (value instanceof MovieEntity) {
            try {
                MovieEntity movieEntity = (MovieEntity) value;
                if (movieEntity.getId() == null) {
                    return "";
                }
                return movieEntity.getId().toString();
            } catch (Exception e) {
                throw new IllegalArgumentException("Invalid value");
            }
        } else {
            throw new IllegalArgumentException("Invalid value");
        }
    }
}
