/**
 * PIS Projekt 2024
 * BookAuthorResource.java
 * @author Tomas Ondrusek <xondru18>
 */

package pis.api;

import java.util.List;

import pis.data.BookAuthor;
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import pis.service.BookAuthorManager;

/**
 * REST API resource for working with BookAuthors.
 */
@Path("/bookauthor")
@PermitAll
public class BookAuthorResource {
    @Inject
    private BookAuthorManager bookAuthorManager;


    /**
     * Returns list of all BookAuthors.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<BookAuthor> getAuthors() {
        return bookAuthorManager.findAll();
    }
}
