/**
 * PIS Projekt 2024
 * UserAddress.java
 * @author Filip Brna <xbrnaf00>
 */

package pis.data;

import jakarta.persistence.*;

@Embeddable
public class UserAddress {

    private String userState;
    private String userTown;
    private String userStreet;
    private Integer userStreetNumber;
    private String userPostCode;

    public UserAddress() {
    }

    public UserAddress(String userState, String userTown, String userStreet, Integer userStreetNumber,
            String userPostCode) {
        this.userState = userState;
        this.userTown = userTown;
        this.userStreet = userStreet;
        this.userStreetNumber = userStreetNumber;
        this.userPostCode = userPostCode;
    }

    public String getState() {
        return userState;
    }

    public void setState(String userState) {
        this.userState = userState;
    }

    public String getTown() {
        return userTown;
    }

    public void setTown(String userTown) {
        this.userTown = userTown;
    }

    public String getStreet() {
        return userStreet;
    }

    public void setStreet(String userStreet) {
        this.userStreet = userStreet;
    }

    public Integer getStreetNumber() {
        return userStreetNumber;
    }

    public void setStreetNumber(Integer userStreetNumber) {
        this.userStreetNumber = userStreetNumber;
    }

    public String getPostCode() {
        return userPostCode;
    }

    public void setPostCode(String userPostCode) {
        this.userPostCode = userPostCode;
    }

}
