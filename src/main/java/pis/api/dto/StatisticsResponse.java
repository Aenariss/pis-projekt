/**
 * PIS Projekt 2024
 * StatisticsResponse.java
 * @author Vojtech Fiala <xfiala61>
*/

package pis.api.dto;

import java.sql.Date;

/* Class for response which is returned in statistics */
public class StatisticsResponse {
    private Date date;
    private Long occurence;

    
    public StatisticsResponse(Date date, Long occurence) {
        this.date = date;
        this.occurence = occurence;
    }

    public StatisticsResponse() {}

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Long getOccurence() {
        return occurence;
    }

    public void setOccurence(Long occurence) {
        this.occurence = occurence;
    }
}
 