/**
 * PIS Projekt 2024
 * Address.java
 * @author Vojtech Fiala <xfiala61>
 */

package pis.data;

import jakarta.persistence.*;

@Embeddable
public class Address extends Request { // Not exactly a request body, but may be used as such

    private String state;
    private String town;
    private String street;
    private String streetNumber;
    private String postCode;


    public Address() {}

    public Address(String state, String town, String street, String streetNumber, String postCode) {
        this.state = state;
        this.town = town;
        this.street = street;
        this.streetNumber = streetNumber;
        this.postCode = postCode;
    }


    public String getState() {
        return state;
    }


    public void setState(String state) {
        this.state = state;
    }


    public String getTown() {
        return town;
    }


    public void setTown(String town) {
        this.town = town;
    }


    public String getStreet() {
        return street;
    }


    public void setStreet(String street) {
        this.street = street;
    }


    public String getStreetNumber() {
        return streetNumber;
    }


    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }


    public String getPostCode() {
        return postCode;
    }


    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

}
