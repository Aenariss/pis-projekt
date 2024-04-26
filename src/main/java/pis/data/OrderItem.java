/**
 * PIS Projekt 2024
 * OrderItem.java
 * @author Filip Brna <xbrnaf00>
 */

package pis.data;

import java.util.List;

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

    // Product description fields (for when the product is no longer available in the system)
    private String image;
    private String productName;
    private String authorFirstName;
    private String authorLastName;
    private List<Category> categories;


    public OrderItem() {
    }

    public OrderItem(Integer quantity, ProductDescription productDescription) {
        this.quantity = quantity;
        this.image = productDescription.getImage();
        this.productName = productDescription.getName();
        this.authorFirstName = productDescription.getAuthor() != null ? productDescription.getAuthor().getFirstName() : null;
        this.authorLastName = productDescription.getAuthor() != null ? productDescription.getAuthor().getLastName() : null;
        this.categories = productDescription.getCategories();
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return productName;
    }

    public void setName(String productName) {
        this.productName = productName;
    }

    public String getFirstName() {
        return authorFirstName;
    }

    public void setFirstName(String authorFirstName) {
        this.authorFirstName = authorFirstName;
    }

    public String getLastName() {
        return authorLastName;
    }

    public void setLastName(String authorLastName) {
        this.authorLastName = authorLastName;
    }
    
    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public void addCategory(Category category) {
        this.categories.add(category);
    }

    public void removeCategory(Category category) {
        this.categories.remove(category);
    }

    public void clearCategories() {
        this.categories.clear();
    }

}
