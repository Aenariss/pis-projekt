/**
 * DTO for sending detail info about user to frontend.
 * @author Lukas Petr
 */
package pis.api.dto;

import pis.data.RegisteredUser;

public class UserProfileDTO {
    private long id;
	private String firstname;
	private String surname;
	private String phone;
	private String email;
    private String role;
    private Address address;

    public UserProfileDTO(RegisteredUser user) {
        this.id = user.getId();
        this.firstname = user.getFirstname();
        this.surname = user.getSurname();
        this.phone = user.getPhone();
        this.email = user.getEmail();
        this.role = user.getRole();
        this.address = user.getAddress();
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

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public Address getAddress() {
        return address;
    }

}
