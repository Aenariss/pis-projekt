/*
 * PIS Projekt 2024
 * ModificationDTO.java
 * @author Filip Brna <xbrnaf00>
 */

package pis.api.dto;

import java.time.LocalDateTime;

import pis.data.Modification;
import pis.data.OrderStatus;

public class ModificationDTO {

    private long userId;
    private LocalDateTime modificationDate;
    private OrderStatus toStatus;
    private String email;
    private String firstname;
    private String surname;

    public ModificationDTO(Modification modification) {
        this.userId = modification.getRegisteredUser().getId();
        this.modificationDate = modification.getModificationDate();
        this.toStatus = modification.getToStatus();
        this.email = modification.getRegisteredUser().getEmail();
        this.firstname = modification.getRegisteredUser().getFirstname();
        this.surname = modification.getRegisteredUser().getSurname();
    }

    public long getUserId() {
        return userId;
    }

    public LocalDateTime getModificationDate() {
        return modificationDate;
    }

    public OrderStatus getToStatus() {
        return toStatus;
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

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public void setModificationDate(LocalDateTime modificationDate) {
        this.modificationDate = modificationDate;
    }

    public void setToStatus(OrderStatus toStatus) {
        this.toStatus = toStatus;
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

}
