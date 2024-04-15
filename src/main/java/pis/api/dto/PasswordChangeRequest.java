/**
 * PIS Projekt 2024
 * PasswordChangeRequest.java
 * @author Vojtech Fiala <xfiala61>
 */

package pis.api.dto;

public class PasswordChangeRequest extends Request {
    
    private String password;
    private String old_password;

    public PasswordChangeRequest(String password, String old_password) {
        this.password = password;
        this.old_password = old_password;
    }

    public PasswordChangeRequest() {}

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getOld_password() {
        return old_password;
    }

    public void setOld_password(String old_password) {
        this.old_password = old_password;
    }
}
