/**
 * PIS Projekt 2024
 * SearchQuery.java
 * @author Tomas Ondrusek <xondru18>
 */

package pis.api.dto;

/**
 * Data model for search query.
 */
public class SearchQuery extends Request {
    private String query;


    public String getQuery() {
        // parse querry from json to string and return its value
        return query;

    }

    public void setQuery(String query) {
        this.query = query;
    }
}
