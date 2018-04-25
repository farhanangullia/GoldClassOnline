/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.converter;


import entity.CinemaEntity;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author User
 */
@FacesConverter(value = "cinemaEntityConverter", forClass = CinemaEntity.class)
public class CinemaEntityConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.trim().isEmpty() || value.equals("null")) {
            return null;
        }
        try {
            List<CinemaEntity> cinemaEntities = (List<CinemaEntity>) context.getExternalContext().getSessionMap().get("CinemaEntityConverter.cinemaEntities");
            for (CinemaEntity cinemaEntity : cinemaEntities) {
                if (cinemaEntity.getId().toString().equals(value)) {
                    return cinemaEntity;
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

        if (value instanceof CinemaEntity) {
            try {
                CinemaEntity cinemaEntity = (CinemaEntity) value;
                if (cinemaEntity.getId() == null) {
                    return "";
                }
                return cinemaEntity.getId().toString();
            } catch (Exception e) {
                throw new IllegalArgumentException("Invalid value");
            }
        } else {
            throw new IllegalArgumentException("Invalid value");
        }
    }
}
