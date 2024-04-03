/**
 * PIS Projekt 2024
 * Language.java
 * @author Tomas Ondrusek <xondru18>
 */

package pis.data;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

/**
 * Data model for language.
 */
@Entity
@Table(name = "Language")
@NamedQuery(name = "Language.findAll", query = "SELECT l FROM Language l")
public class Language {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull
    private String language;

    public Language() {
    }

    public Language(String language) {
        this.language = language;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
