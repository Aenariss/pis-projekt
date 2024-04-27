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

    private RegisteredUser registeredUser;

    private String changeDescription;

    private LocalDateTime modificationDate;

    public ProductDescriptionEvidence() {
        this.modificationDate = LocalDateTime.now();
    }

    public ProductDescriptionEvidence(RegisteredUser registeredUser, String changeDescription) {
        this();
        this.registeredUser = registeredUser;
        this.changeDescription = changeDescription;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public RegisteredUser getRegisteredUser() {
        return registeredUser;
    }

    public void setRegisteredUser(RegisteredUser registeredUser) {
        this.registeredUser = registeredUser;
    }
}