/**
 * PIS Projekt 2024
 * DiscountResource.java
 * @discount Tomas Ondrusek <xondru18>
 */

package pis.api;

import java.util.List;

import pis.data.Discount;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import pis.service.DiscountManager;
import jakarta.ws.rs.core.Response;

/**
 * REST API resource for working with Discounts.
 */
@Path("/discount")
@PermitAll
public class DiscountResource {
	@Inject
	private DiscountManager discountManager;

    /**
     * Returns list of all Discounts.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Discount> getDiscounts() {
    	return discountManager.findAll();
    }

    /**
     * Adds new discount.
     * @param discount Discount to be added.
     * @return Response status.
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addDiscount(Discount discount) {
        if (discountManager.findDiscount(discount.getDiscount()) == null) {
            // Discount with given name does not exist, create new one
            Discount savedDiscount = discountManager.save(discount);
            return Response.ok().entity(savedDiscount).build();
        }
        return Response.status(Response.Status.CONFLICT).entity("Error: Discount already exists").build();
    }

    /**
     * Deletes a Discount.
     * @param discount Discount to be deleted.
     * @return Response status
     */
    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteDiscount(Discount discount) {
        Discount toDelete = discountManager.findDiscount(discount.getDiscount());
        if (toDelete == null) {
            // Discount with given name does not exist
            return Response.status(Response.Status.BAD_REQUEST).entity("Error: Discount does not exist").build();
        }
        discountManager.delete(toDelete);
        return Response.ok().entity("Succesfully removed the Discount").build();
    }
}
