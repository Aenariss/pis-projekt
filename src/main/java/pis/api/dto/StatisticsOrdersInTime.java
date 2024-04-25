/**
 * PIS Projekt 2024
 * StatisticsOrdersInTime.java
 * @author Vojtech Fiala <xfiala61>
*/

package pis.api.dto;

import java.sql.Date;

/* Class for response which is returned in statistics */
public class StatisticsOrdersInTime {
    private Date date;
    private Long occurence;

    
    public StatisticsOrdersInTime(Date date, Long occurence) {
        this.date = date;
        this.occurence = occurence;
    }

    public StatisticsOrdersInTime() {}

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
 