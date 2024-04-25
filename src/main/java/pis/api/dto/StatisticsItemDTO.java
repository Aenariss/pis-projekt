/**
 * PIS Projekt 2024
 * StatisticsItemDTO.java
 * @author Vojtech Fiala <xfaial61>
 */

package pis.api.dto;

import java.util.List;

public class StatisticsItemDTO {
    private long total;
    private List<StatisticsItemsInTime> perDay;
    
    public long getTotal() {
        return total;
    }
    public void setTotal(long total) {
        this.total = total;
    }
    public List<StatisticsItemsInTime> getPerDay() {
        return perDay;
    }
    public void setPerDay(List<StatisticsItemsInTime> perDay) {
        this.perDay = perDay;
    }
}
