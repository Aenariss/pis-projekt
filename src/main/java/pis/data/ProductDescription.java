/**
 * PIS Projekt 2024
 * ProductDescription.java
 * @author Tomas Ondrusek <xondru18>
 */

package pis.data;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * Data model for product description.
 */
@Entity
@Table(name = "ProductDescription")
@NamedQuery(name = "ProductDescription.findAll", query = "SELECT pd FROM ProductDescription pd")
public class ProductDescription {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull
    private double price;
    private String name;
    private String description;
    private String ISBN;
    private int pages;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private BookAuthor author;

    @ManyToMany
    @JoinTable(name = "ProductCategory", joinColumns = @JoinColumn(name = "product_id"), inverseJoinColumns = @JoinColumn(name = "category_id"))
    private List<Category> categories;

    @ManyToOne
    @JoinColumn(name = "language_id")
    private Language language;

    @ManyToOne
    @JoinColumn(name = "discount_id")
    private Discount discount;

    public ProductDescription() {
    }

    public ProductDescription(double price, String name, String description, String ISBN, int pages, BookAuthor author,
            List<Category> categories, Language language, Discount discount) {
        this.price = price;
        this.name = name;
        this.description = description;
        this.ISBN = ISBN;
        this.pages = pages;
        this.author = author;
        this.categories = categories;
        this.language = language;
        this.discount = discount;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public BookAuthor getAuthor() {
        return author;
    }

    public void setAuthor(BookAuthor author) {
        this.author = author;
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

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public Discount getDiscount() {
        return discount;
    }

    public void setDiscount(Discount discount) {
        this.discount = discount;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

}