/**
 * PIS Projekt 2024
 * RoleRequest.java
 * @author Vojtech Fiala <xfiala61>
 */

package pis.api.dto;

public class RoleRequest extends Request {
    private String email;

    public RoleRequest(String email) {
        this.email = email;
    }

    public RoleRequest() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
