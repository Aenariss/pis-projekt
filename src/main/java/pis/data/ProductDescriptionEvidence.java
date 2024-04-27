/*
 * PIS Projekt 2024
 * ProductDescriptionEvidence.java
 * @author Tomas Ondrusek <xondru18>
 */
package pis.data;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ProductDescriptionEvidence")
@NamedQuery(name = "ProductDescriptionEvidence.findAll", query = "SELECT pde FROM ProductDescriptionEvidence pde")
public class ProductDescriptionEvidence {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    // User who made the change
    private String firstName;
    private String lastName;
    private String email;
    private String role;

    private String changeDescription;

    private LocalDateTime modificationDate;

    public ProductDescriptionEvidence() {
        this.modificationDate = LocalDateTime.now();
    }

    public ProductDescriptionEvidence(RegisteredUser registeredUser, String changeDescription) {
        this();
        this.firstName = registeredUser.getFirstname();
        this.lastName = registeredUser.getSurname();
        this.email = registeredUser.getEmail();
        this.role = registeredUser.getRole();
        this.changeDescription = changeDescription;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getChangeDescription() {
        return changeDescription;
    }

    public void setChangeDescription(String changeDescription) {
        this.changeDescription = changeDescription;
    }

    public LocalDateTime getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(LocalDateTime modificationDate) {
        this.modificationDate = modificationDate;
    }
}