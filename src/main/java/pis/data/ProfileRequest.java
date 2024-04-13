/**
 * PIS Projekt 2024
 * LoginRequest.java
 * @author Vojtech Fiala <xfiala61>
 */


package pis.data;

public class ProfileRequest extends Request {

    private Address address;
    private String email;
    private String firstname;
    private String phone;
    private String surname;

    public boolean valid() {
        if (!this.address.valid() || !super.valid()) {
            return false;
        }
        return true;
    }

    public ProfileRequest(Address address, String email, String firstname, String phone, String surname) {
        this.address = address;
        this.email = email;
        this.firstname = firstname;
        this.phone = phone;
        this.surname = surname;
    }

    public ProfileRequest() {}

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    
}
