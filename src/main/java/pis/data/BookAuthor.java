/**
 * PIS Projekt 2024
 * BookAuthor.java
 * @author Tomas Ondrusek <xondru18>
 */

package pis.data;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

/**
 * Data model for BookAuthor.
 */
@Entity
@Table(name = "BookAuthor")
@NamedQuery(name = "BookAuthor.findAll", query = "SELECT a FROM BookAuthor a")
public class BookAuthor {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull
    private String firstName;
    @NotNull
    private String lastName;

    public BookAuthor() {
    }

    public BookAuthor(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}