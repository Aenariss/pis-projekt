/**
 * PIS Projekt 2024
 * LoginRequest.java
 * @author Vojtech Fiala <xfiala61>
 */

package pis.data;

public class PasswordChangeRequest extends Request {
    
    private String password;

    public PasswordChangeRequest(String password) {
        this.password = password;
    }

    public PasswordChangeRequest() {}

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
}
