/*
 * PIS Projekt 2024
 * ProductDescriptionEvidenceDTO.java
 * @author Tomas Ondrusek <xondru18>
 */

package pis.api.dto;

import java.time.LocalDateTime;

import pis.data.ProductDescriptionEvidence;

public class ProductDescriptionEvidenceDTO {

    private long userId;
    private LocalDateTime modificationDate;
    private String changeDescription;
    private String email;
    private String firstname;
    private String surname;
    private String role;

    public ProductDescriptionEvidenceDTO(ProductDescriptionEvidence productDescriptionEvidence) {
        this.userId = productDescriptionEvidence.getRegisteredUser().getId();
        this.modificationDate = productDescriptionEvidence.getModificationDate();
        this.changeDescription = productDescriptionEvidence.getChangeDescription();
        this.email = productDescriptionEvidence.getRegisteredUser().getEmail();
        this.firstname = productDescriptionEvidence.getRegisteredUser().getFirstname();
        this.surname = productDescriptionEvidence.getRegisteredUser().getSurname();
        this.role = productDescriptionEvidence.getRegisteredUser().getRole();
    }

    public long getUserId() {
        return userId;
    }

    public LocalDateTime getModificationDate() {
        return modificationDate;
    }

    public String getChangeDescription() {
        return changeDescription;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getSurname() {
        return surname;
    }

    public String getRole() {
        return role;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public void setModificationDate(LocalDateTime modificationDate) {
        this.modificationDate = modificationDate;
    }

    public void setChangeDescription(String changeDescription) {
        this.changeDescription = changeDescription;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setRole(String role) {
        this.role = role;
    }
}