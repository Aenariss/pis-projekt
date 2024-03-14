package pis.api;

import java.util.List;

import pis.data.Category;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import pis.service.CategoryManager;
import jakarta.ws.rs.core.Response;

/**
 * REST API resource for categories.
 */
@Path("/category")
public class CategoryResource {
	@Inject
	private CategoryManager categoryManager;

    /**
     * Returns list of all categories.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Category> getCategories() {
    	return categoryManager.findAll();
    }

    /**
     * Adds new category.
     * @param cat Category which will be added.
     * @return Response status.
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addCategory(Category cat) {
        if (cat.getName().length() < 2) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Categry needs a valid name!").build();
        }
        if (categoryManager.findByName(cat.getName()) == null) {
            // Category with given name does not exist, therefore add it.
            Category savedCategory = categoryManager.save(cat);
            return Response.ok().entity(savedCategory).build();
        }
        return Response.status(Response.Status.CONFLICT).entity("Error: category already exists").build();
    }

    /**
     * Deletes a category by name
     * @param cat Category name to be removed
     * @return Response status
     */
    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteCategory(Category cat) {
        Category toDelete = categoryManager.findByName(cat.getName());
        if (toDelete == null) {
            // Category with given name does not exist, nothing to remove
            return Response.status(Response.Status.BAD_REQUEST).entity("Error: category doesnt exist").build();
        }
        categoryManager.delete(toDelete);
        return Response.ok().entity("Succesfully removed the category").build();
    }
}
