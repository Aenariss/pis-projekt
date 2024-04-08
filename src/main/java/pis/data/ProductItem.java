/**
 * PIS Projekt 2024
 * ProductItem.java
 * @author Filip Brna <xbrnaf00>
 */

package pis.data;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

/**
 * Data model for product item.
 */
@Entity
@Table(name = "ProductItem")
@NamedQuery(name = "ProductItem.findAll", query = "SELECT pi FROM ProductItem pi")
public class ProductItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull
    private int availableQuantity;

    @ManyToOne
    @JoinColumn(name = "productDescription_id")
    private ProductDescription productDescription;

    public ProductItem() {
    }

    public ProductItem(int availableQuantity, ProductDescription productDescription) {
        this.availableQuantity = availableQuantity;
        this.productDescription = productDescription;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(int availableQuantity) {
        this.availableQuantity = availableQuantity;
    }

    public ProductDescription getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(ProductDescription productDescription) {
        this.productDescription = productDescription;
    }
}