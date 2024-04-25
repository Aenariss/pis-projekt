/**
 * PIS Projekt 2024
 * StatisticsRequest.java
 * @author Vojtech Fiala <xfiala61>
*/

package pis.api.dto;

public class StatisticsRequest extends Request {
    private String fromDate;
    private String toDate;

    public StatisticsRequest(String fromDate, String toDate) {
        this.fromDate = fromDate;
        this.toDate = toDate;
    }
 
    public StatisticsRequest() {}

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }
}
 