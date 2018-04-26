package ws.restful.datamodel;

import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement
@XmlType(name = "remoteCheckoutLineItem", propOrder = {
    "seats", 
    "screeningScheduleId"
})

public class RemoteCheckoutLineItem {

    private List<String> seats;
    private Long screeningScheduleId;

    public RemoteCheckoutLineItem() {
    }

    public RemoteCheckoutLineItem(List<String> seats,  Long screeningScheduleId) {
        this.seats = seats;
        this.screeningScheduleId = screeningScheduleId;
    }


    /**
     * @return the seats
     */
    public List<String> getSeats() {
        return seats;
    }

    /**
     * @param seats the seats to set
     */
    public void setSeats(List<String> seats) {
        this.seats = seats;
    }

    /**
     * @return the screeningScheduleId
     */
    public Long getScreeningScheduleId() {
        return screeningScheduleId;
    }

    /**
     * @param screeningScheduleId the screeningScheduleId to set
     */
    public void setScreeningScheduleId(Long screeningScheduleId) {
        this.screeningScheduleId = screeningScheduleId;
    }

}
