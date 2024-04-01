/**
 * PIS Projekt 2024
 * Discount.java
 * @author Tomas Ondrusek <xondru18>
 */

package pis.data;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

/**
 * Data model for discount.
 */
@Entity
@Table(name = "Discount")
@NamedQuery(name="Discount.findAll", query="SELECT d FROM Discount d")
public class Discount {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull
    private int discount;

    public Discount() {}

    public Discount(int discount) {
        this.discount = discount;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}