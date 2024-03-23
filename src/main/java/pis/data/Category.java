/**
 * PIS Projekt 2024
 * Category.java
 * @author Lukáš Petr <xpetrl06>
 */

package pis.data;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

/**
 * Data model for category.
 */
@Entity
@Table(name = "Category")
@NamedQuery(name="Category.findAll", query="SELECT c FROM Category c")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull
	private String name;
    private String description;

    public Category() {}

    public Category(String name) {
        this.name = name;
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
