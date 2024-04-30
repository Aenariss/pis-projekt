/**
 * DTO for sending basic info about user - for listing multiple users in frontend.
 * @author Lukas Petr
 */
package pis.api.dto;

import pis.data.RegisteredUser;

public class UserOverviewDTO {
    private long id;
	private String firstname;
	private String surname;
	private String email;
    private String role;

    public UserOverviewDTO(RegisteredUser user) {
        this.id = user.getId();
        this.firstname = user.getFirstname();
        this.surname = user.getSurname();
        this.email = user.getEmail();
        this.role = user.getRole();
    }

    public long getId() {
        return id;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getSurname() {
        return surname;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

}
