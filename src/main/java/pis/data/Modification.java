/*
 * PIS Projekt 2024
 * Modification.java
 * @author Filip Brna <xbrnaf00>
 */
package pis.data;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "Modification")
@NamedQuery(name = "Modification.findAll", query = "SELECT m FROM Modification m")
public class Modification {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @OneToOne
    @JoinColumn(name = "registeredUser_id")
    private RegisteredUser registeredUser;

    @NotNull
    private OrderStatus toStatus;

    @NotNull
    private LocalDateTime modificationDate;

    public Modification() {
        this.modificationDate = LocalDateTime.now();
    }

    public Modification(RegisteredUser registeredUser, OrderStatus toStatus) {
        this();
        this.registeredUser = registeredUser;
        this.toStatus = toStatus;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public RegisteredUser getRegisteredUser() {
        return registeredUser;
    }

    public void setRegisteredUser(RegisteredUser registeredUser) {
        this.registeredUser = registeredUser;
    }

    public OrderStatus getToStatus() {
        return toStatus;
    }

    public void setToStatus(OrderStatus toStatus) {
        this.toStatus = toStatus;
    }

    public LocalDateTime getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(LocalDateTime modificationDate) {
        this.modificationDate = modificationDate;
    }
}