/**
 * PIS Projekt 2024
 * LoginRequest.java
 * @author Vojtech Fiala <xfiala61>
 */

package pis.api.dto;

public class LoginRequest extends Request {

    private String email;
    private String password;

    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public LoginRequest() {
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
}