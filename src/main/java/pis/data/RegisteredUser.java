/**
 * PIS Projekt 2024
 * RegisteredUser.java
 * @author Vojtech Fiala <xfiala61>
 */

package pis.data;

import jakarta.annotation.security.RolesAllowed;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * Data model for user.
 */
@Entity
@Table(name = "RegisteredUser")
@NamedQuery(name="RegisteredUser.findAll", query="SELECT c FROM RegisteredUser c")
public class RegisteredUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull
	private String firstname;
    @NotNull
	private String surname;
    @NotNull
	private String phone;
    @NotNull
	private String email;
    @NotNull
	private String passwordHash;
    @NotNull
    private String role;

    public RegisteredUser() {}

    public RegisteredUser(String firstname, String surname, String phone, String email, String password) {
        this.firstname = firstname;
        this.surname = surname;
        this.phone = phone;
        this.email = email;
        this.passwordHash = DigestUtils.sha512Hex(password); // create hash from plaintext
        this.role = "user"; // default role
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

    public String getEmail() {
        return email;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getRole() {
        return role;
    }

    @RolesAllowed("admin")
    public void setRole(String role) {
        this.role = role;
    }

    @RolesAllowed({"admin", "employee"})
    public void makeEmployee() {
        this.role = "employee";
    }

    @RolesAllowed("admin")
    public void makeAdmin()  {
        this.role = "admin";
    }

    /**
     * Password validation
     * @param password The user's password
     * @return success or fail
     */
    public boolean validatePassword(String password) {
        String savedHash = this.getPasswordHash();
        String givenHash = DigestUtils.sha512Hex(password);

        return savedHash.equals(givenHash); 
    }

    /*
     * TODO:
     * add Orders (another class reference)
     * add Address
     */
}
