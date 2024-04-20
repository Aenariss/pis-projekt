/**
 * PIS Projekt 2024
 * OrderItem.java
 * @author Filip Brna <xbrnaf00>
 */

package pis.data;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "OrderItem")
@NamedQuery(name = "OrderItem.findAll", query = "SELECT oi FROM OrderItem oi")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull
    private Integer quantity;

    /**
     * Price of one piece of the product at the time the product was bought.
     */
    @NotNull
    private double pricePerPiece;

    @OneToOne
    @JoinColumn(name = "productDescription_id")
    private ProductDescription productDescription;

    public OrderItem() {
    }

    public OrderItem(Integer quantity, ProductDescription productDescription) {
        this.quantity = quantity;
        this.productDescription = productDescription;
        this.pricePerPiece = productDescription.getCurrentPrice();
    }

    public double getPricePerPiece() {
        return this.pricePerPiece;
    }

    /**
     * Get total price for ordered items.
     */
    public double getPrice() {
        return this.pricePerPiece * quantity;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ProductDescription getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(ProductDescription productDescription) {
        this.productDescription = productDescription;
    }
}
