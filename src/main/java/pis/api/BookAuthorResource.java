/**
 * PIS Projekt 2024
 * BookAuthorResource.java
 * @author Tomas Ondrusek <xondru18>
 */

package pis.api;

import java.util.List;

import pis.data.BookAuthor;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import pis.service.BookAuthorManager;
import jakarta.ws.rs.core.Response;

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

    /**
     * Returns BookAuthor from id.
     * @param id Id of the BookAuthor.
     * @return BookAuthor with given id.
     */
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public BookAuthor getAuthor(@PathParam("id") long id) {
        return bookAuthorManager.find(id);
    }

    /**
     * Adds new book author.
     * @param author BookAuthor to be added.
     * @return Response status.
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addBookAuthor(BookAuthor author) {
        if (author.getLastName().length() < 2) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Book Author needs a valid name!").build();
        }
        if (bookAuthorManager.findByName(author.getFirstName(), author.getLastName()) == null) {
            BookAuthor savedAuthor = bookAuthorManager.save(author);
            return Response.ok().entity(savedAuthor).build();
        }
        return Response.status(Response.Status.CONFLICT).entity("Error: Book Author already exists").build();
    }

    /**
     * Updates a BookAuthor by given id.
     * @param id Id of the BookAuthor to be updated.
     * @param author BookAuthor with updated values.
     * @return Response status.
     */
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateBookAuthor(@PathParam("id") long id, BookAuthor author) {
        BookAuthor toUpdate = bookAuthorManager.find(id);
        if (toUpdate == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Error: Book Author doesnt exist").build();
        }
        toUpdate.setFirstName(author.getFirstName());
        toUpdate.setLastName(author.getLastName());
        bookAuthorManager.save(toUpdate);
        return Response.ok().entity(toUpdate).build();
    }

    /**
     * Deletes a BookAuthor by given id.
     * @param id Id of the BookAuthor to be deleted.
     * @return Response status.
     */
    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteBookAuthor(@PathParam("id") long id) {
        BookAuthor toDelete = bookAuthorManager.find(id);
        if (toDelete == null) {
            // BookAuthor with given id does not exist
            return Response.status(Response.Status.BAD_REQUEST).entity("Error: Book Author doesnt exist").build();
        }
        bookAuthorManager.delete(toDelete);
        return Response.ok().entity("Succesfully removed the Book Author").build();
    }

    /**
     * Deletes a BookAuthor.
     * @param author BookAuthor to be deleted.
     * @return Response status
     */
    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteBookAuthor(BookAuthor author) {
        BookAuthor toDelete = bookAuthorManager.findByName(author.getFirstName(), author.getLastName());
        if (toDelete == null) {
            // BookAuthor with given name does not exist
            return Response.status(Response.Status.BAD_REQUEST).entity("Error: Book Author doesnt exist").build();
        }
        bookAuthorManager.delete(toDelete);
        return Response.ok().entity("Succesfully removed the Book Author").build();
    }
}
