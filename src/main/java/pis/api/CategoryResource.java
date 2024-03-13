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
     * @return Returns inserted category.
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addCategory(Category cat) {
        if (categoryManager.find(cat.getId()) == null) {
            // Category with given id does not exist, therefore add it.
            Category savedCategory = categoryManager.save(cat);
            return Response.ok().entity(savedCategory).build();
        }
        return Response.status(Response.Status.CONFLICT).entity("Error: existing id").build();
    }

}
