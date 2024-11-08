/**
 * PIS Projekt 2024
 * CategoryResource.java
 * @author Lukáš Petr <xpetrl06>
 * @author Vojtech Fiala <xfiala61>
 * @editor Tomas Ondrusek <xondru18>
 */

package pis.api;

import java.util.List;

import pis.data.Category;
import pis.data.Order;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import pis.service.CategoryManager;
import pis.service.OrderManager;
import jakarta.ws.rs.core.Response;
import pis.service.ProductDescriptionManager;
import pis.data.ProductDescription;

/**
 * REST API resource for categories.
 */
@Path("/category")
@PermitAll
public class CategoryResource {
    @Inject
    private CategoryManager categoryManager;

    @Inject
    private OrderManager orderManager;

    @Inject
    private ProductDescriptionManager productDescriptionManager;

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
     * 
     * @param cat Category which will be added.
     * @return Response status.
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"admin"})
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
     * Updates category.
     * 
     * @param cat Category to be updated.
     * @return Response status.
     */
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"admin"})
    public Response updateCategory(@PathParam("id") long id, Category cat) {
        Category toUpdate = categoryManager.find(id);
        if (toUpdate == null) {
            // Category with given id does not exist, nothing to update
            return Response.status(Response.Status.BAD_REQUEST).entity("Error: category doesnt exist").build();
        }
        toUpdate.setName(cat.getName());
        toUpdate.setDescription(cat.getDescription());
        categoryManager.save(toUpdate);
        return Response.ok().entity("Succesfully updated the category").build();
    }

    /**
     * Deletes a category by id
     * 
     * @param id Category id to be removed
     * @return Response status
     */
    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"admin"})
    public Response deleteCategory(@PathParam("id") long id) {
        Category toDelete = categoryManager.find(id);
        if (toDelete == null) {
            // Category with given id does not exist, nothing to remove
            return Response.status(Response.Status.BAD_REQUEST).entity("Error: category doesnt exist").build();
        }

        ProductDescription p = productDescriptionManager.findByCategoryOne(toDelete.getName());

        if (p != null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Error: Category is used in at least one product, for example " + p.getName())
                        .build();
        }

        Order o = orderManager.findByCategory(toDelete.getName());

        if (o != null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Error: Category is used in at least one order, can't delete anymore")
                        .build();
        }

        categoryManager.delete(toDelete);
        return Response.ok().entity("Succesfully removed the category").build();
    }
}
