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


    public RegisterRequest(String firstname, String surname, String phone, String email, String password) {
        this.firstname = firstname;
        this.surname = surname;
        this.phone = phone;
        this.email = email;
        this.password = password; 
    }

    public RegisterRequest() {}

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

