/**
 * PIS Projekt 2024
 * StatisticsEarningsDTO.java
 * @author Vojtech Fiala <xfaial61>
 */

package pis.api.dto;

import java.util.List;

public class StatisticsEarningsDTO {
    private long total;
    private List<StatisticsOrdersInTime> perDay;
    
    public long getTotal() {
        return total;
    }
    public void setTotal(long total) {
        this.total = total;
    }
    public List<StatisticsOrdersInTime> getPerDay() {
        return perDay;
    }
    public void setPerDay(List<StatisticsOrdersInTime> perDay) {
        this.perDay = perDay;
    }
}
 