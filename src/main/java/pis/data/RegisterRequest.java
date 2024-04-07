/**
 * PIS Projekt 2024
 * RegisterRequest.java
 * @author Vojtech Fiala <xfiala61>
 */


package pis.data;

public class RegisterRequest {
    
    private String firstname;
	private String surname;
	private String phone;
	private String email;
	private String password;

    private String state;
    private String town;
    private String street;
    private Integer streetNumber;
    private String postCode;


    public RegisterRequest(String firstname, String surname, String phone, String email, String password, String state, String town, String street, Integer streetNumber, String postCode) {
        this.firstname = firstname;
        this.surname = surname;
        this.phone = phone;
        this.email = email;
        this.password = password; 
        this.state = state;
        this.town = town;
        this.street = street;
        this.streetNumber = streetNumber;
        this.postCode = postCode;
    }

    public RegisterRequest() {}

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

    public Integer getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(Integer streetNumber) {
        this.streetNumber = streetNumber;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    
}

