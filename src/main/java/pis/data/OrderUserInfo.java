/**
 * PIS Projekt 2024
 * OrderUserInfo.java
 * @author Filip Brna <xbrnaf00>
 */

package pis.data;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "OrderUserInfo")
@NamedQuery(name = "OrderUserInfo.findAll", query = "SELECT o FROM OrderUserInfo o")
public class OrderUserInfo {
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

    public OrderUserInfo() {
    }

    public OrderUserInfo(String firstname, String surname, String phone, String email) {
        this.firstname = firstname;
        this.surname = surname;
        this.phone = phone;
        this.email = email;
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

    public void setEmail(String email) {
        this.email = email;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}