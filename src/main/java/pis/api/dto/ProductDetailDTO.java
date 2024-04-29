/*
 * PIS Projekt 2024
 * ProductDetailDTO.java
 * @author Tomas Ondrusek <xondru18>
 */

package pis.api.dto;

import java.util.List;

import pis.data.BookAuthor;
import pis.data.Discount;
import pis.data.Language;
import pis.data.Category;
import pis.data.ProductDescription;

public class ProductDetailDTO {

    private long id;
    private double price;
    private double currentPrice;
    private String name;
    private String description;
    private String ISBN;
    private int pages;
    private String image;
    private int availableQuantity;

    private BookAuthor author;

    private List<Category> categories;

    private Language language;

    private Discount discount;

    public ProductDetailDTO(ProductDescription productDescription) {
        this.id = productDescription.getId();
        this.price = productDescription.getPrice();
        this.name = productDescription.getName();
        this.description = productDescription.getDescription();
        this.ISBN = productDescription.getISBN();
        this.pages = productDescription.getPages();
        this.image = productDescription.getImage();
        this.availableQuantity = productDescription.getAvailableQuantity();
        this.author = productDescription.getAuthor();
        this.categories = productDescription.getCategories();
        this.language = productDescription.getLanguage();
        this.discount = productDescription.getDiscount();
        this.currentPrice = productDescription.getCurrentPrice();
    }

    public long getId() {
        return id;
    }

    public double getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getISBN() {
        return ISBN;
    }

    public int getPages() {
        return pages;
    }

    public String getImage() {
        return image;
    }

    public int getAvailableQuantity() {
        return availableQuantity;
    }

    public BookAuthor getAuthor() {
        return author;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public Language getLanguage() {
        return language;
    }

    public Discount getDiscount() {
        return discount;
    }

    public double getCurrentPrice() {
        return currentPrice;
    }
}