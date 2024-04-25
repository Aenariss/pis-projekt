/**
 * PIS Projekt 2024
 * StatisticsCategoriesInTime.java
 * @author Vojtech Fiala <xfiala61>
*/

package pis.api.dto;

/* Class for response which is returned in statistics */
public class StatisticsItemsInTime {
    private String name;
    private Long occurence;

    
    public StatisticsItemsInTime(String name, Long occurence) {
        this.name = name;
        this.occurence = occurence;
    }

    public StatisticsItemsInTime() {}

    public Long getOccurence() {
        return occurence;
    }

    public void setOccurence(Long occurence) {
        this.occurence = occurence;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
 