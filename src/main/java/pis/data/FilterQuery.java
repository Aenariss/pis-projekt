/**
 * PIS Projekt 2024
 * FilterQuery.java
 * @author Tomas Ondrusek <xondru18>
 */

package pis.data;

import java.util.List;

/**
 * Data model for search query.
 */
public class FilterQuery {
    private List<Long> authorIds;
    private List<Long> categoryIds;
    private List<Long> languageIds;
    private Double priceFrom;
    private Double priceTo;
    private int pagesFrom;
    private int pagesTo;
    private int discountFrom;
    private int discountTo;

    // Getters and Setters
    public List<Long> getAuthorIds() {
        return authorIds;
    }

    public void setAuthorIds(List<Long> authorIds) {
        this.authorIds = authorIds;
    }

    public List<Long> getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(List<Long> categoryIds) {
        this.categoryIds = categoryIds;
    }

    public List<Long> getLanguageIds() {
        return languageIds;
    }

    public void setLanguageIds(List<Long> languageIds) {
        this.languageIds = languageIds;
    }

    public Double getPriceFrom() {
        return priceFrom;
    }

    public void setPriceFrom(Double priceFrom) {
        this.priceFrom = priceFrom;
    }

    public Double getPriceTo() {
        return priceTo;
    }

    public void setPriceTo(Double priceTo) {
        this.priceTo = priceTo;
    }

    public int getPagesFrom() {
        return pagesFrom;
    }

    public void setPagesFrom(int pagesFrom) {
        this.pagesFrom = pagesFrom;
    }

    public int getPagesTo() {
        return pagesTo;
    }

    public void setPagesTo(int pagesTo) {
        this.pagesTo = pagesTo;
    }

    public int getDiscountFrom() {
        return discountFrom;
    }

    public void setDiscountFrom(int discountFrom) {
        this.discountFrom = discountFrom;
    }

    public int getDiscountTo() {
        return discountTo;
    }

    public void setDiscountTo(int discountTo) {
        this.discountTo = discountTo;
    }
}
