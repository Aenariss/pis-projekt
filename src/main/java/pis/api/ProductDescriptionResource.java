/**
 * PIS Projekt 2024
 * ProductDescriptionResource.java
 * @author Tomas Ondrusek <xondru18>
 * @author Martin Balaz
 */

package pis.api;

import java.util.ArrayList;
import java.util.List;

import pis.data.ProductDescription;
import pis.data.ProductDescriptionEvidence;
import pis.data.Category;
import pis.api.dto.FilterQuery;
import pis.api.dto.SearchQuery;
import pis.api.dto.ProductDetailDTO;
import pis.api.dto.ProductDescriptionEvidenceDTO;
import pis.data.BookAuthor;
import pis.data.Language;
import pis.data.Discount;
import pis.data.RegisteredUser;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import pis.service.ProductDescriptionManager;
import pis.service.CategoryManager;
import pis.service.BookAuthorManager;
import pis.service.LanguageManager;
import pis.service.DiscountManager;
import pis.service.RegisteredUserManager;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

/**
 * REST API resource for working with ProductDescriptions.
 */
@Path("/productdescription")
@PermitAll
public class ProductDescriptionResource {
    // Injecting managers for ProductDescription, Category, BookAuthor, Language and
    // Discount
    @Inject
    private ProductDescriptionManager productDescriptionManager;
    @Inject
    private CategoryManager categoryManager;
    @Inject
    private BookAuthorManager bookAuthorManager;
    @Inject
    private LanguageManager languageManager;
    @Inject
    private DiscountManager discountManager;
    @Inject
    private SecurityContext securityContext;
    @Inject
    private RegisteredUserManager registeredUserManager;

    /**
     * Returns list of all ProductDescriptions.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<ProductDetailDTO> getProductDescriptions() {
        List<ProductDescription> productDescriptions = productDescriptionManager.findAll();
        List<ProductDetailDTO> productDetailDTOs = new ArrayList<>();
        for (ProductDescription productDescription : productDescriptions) {
            ProductDetailDTO productDetailDTO = new ProductDetailDTO(productDescription);
            productDetailDTOs.add(productDetailDTO);
        }
        return productDetailDTOs;
    }

    /**
     * Returns ProductDescription from id.
     * 
     * @param id ID of the ProductDescription.
     * @return ProductDescription with given id.
     */
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public ProductDetailDTO getProductDescription(@PathParam("id") long id) {
        ProductDescription productDescription = productDescriptionManager.find(id);
        if (productDescription != null) {
            ProductDetailDTO productDetailDTO = new ProductDetailDTO(productDescription);
            return productDetailDTO;
        }
        return null;
    }

    /**
     * Returns ProductDescriptionEvidences from ProductDescription id.
     * 
     * @param id ID of the ProductDescription.
     * @return List of ProductDescriptionEvidences with given id.
     */
    @GET
    @Path("/{id}/evidences")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ "admin", "employee" })
    public List<ProductDescriptionEvidenceDTO> getProductDescriptionEvidences(@PathParam("id") long id) {
        ProductDescription productDescription = productDescriptionManager.find(id);
        if (productDescription != null) {
            List<ProductDescriptionEvidence> productDescriptionEvidences = productDescription
                    .getProductDescriptionEvidences();
            List<ProductDescriptionEvidenceDTO> productDescriptionEvidenceDTOs = new ArrayList<>();
            for (ProductDescriptionEvidence productDescriptionEvidence : productDescriptionEvidences) {
                ProductDescriptionEvidenceDTO productDescriptionEvidenceDTO = new ProductDescriptionEvidenceDTO(
                        productDescriptionEvidence);
                productDescriptionEvidenceDTOs.add(productDescriptionEvidenceDTO);
            }
            return productDescriptionEvidenceDTOs;
        }
        return null;
    }

    /**
     * Returns search bar results from search query that can be name, author,
     * category, language ISBN or discount.
     * The search query can be partial.
     * 
     * @param searchQuery Search query.
     * @return List of ProductDescriptions with given search query.
     * @apiNote uses POST instead of GET because GET can not have body
     */
    @POST
    @Path("/search")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public List<ProductDescription> searchProductDescriptions(SearchQuery searchQuery) {

        // Empty array to be returned in case the request is not valid
        List<ProductDescription> arr = new ArrayList<>();
        // Request validation
        if (!searchQuery.valid()) {
            return arr;
        }

        return productDescriptionManager.searchProductDescriptions(searchQuery.getQuery())
                // filtering so it does not have to be done on frontend
                .stream().distinct().toList();
    }

    /**
     * Returns filtered results from filter query.
     * 
     * @param filterQuery Filter query.
     * @return List of ProductDescriptions with given filter query.
     * @apiNote uses POST instead of GET because GET can not have body
     */
    @POST
    @Path("/filter")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public List<ProductDescription> filterProductDescriptions(FilterQuery filterQuery) {
        return productDescriptionManager.filterProductDescriptions(filterQuery)
                // filtering so it does not have to be done on frontend
                .stream().distinct().toList();
    }

    /**
     * Adds new product description.
     * 
     * @param productDescription ProductDescription to be added.
     * @return Response status.
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ "admin" })
    public Response addProductDescription(ProductDescription productDescription) {

        if (productDescription.getName().length() < 2) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Product Description needs a valid name!")
                    .build();
        }
        if (productDescriptionManager.findProductDescription(productDescription.getName()) == null) {
            if (productDescription.getImage() == null) {
                productDescription.setDefaulImage();
            }
            // ProductDescription with given name does not exist, create new one
            ProductDescription savedProductDescription = productDescriptionManager.save(productDescription);
            return Response.ok().entity(savedProductDescription).build();
        }
        return Response.status(Response.Status.CONFLICT).entity("Error: Product Description already exists").build();
    }

    /**
     * Updates an entire ProductDescription.
     * 
     * @param id                 ID of the ProductDescription to be updated.
     * @param productDescription ProductDescription with updated values.
     * @return Response status.
     */
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ "admin" })
    public Response updateProductDescription(@PathParam("id") long id, ProductDescription productDescription) {

        ProductDescription toUpdate = productDescriptionManager.find(id);
        String userEmail = securityContext.getUserPrincipal().getName();
        RegisteredUser user = registeredUserManager.findByEmail(userEmail);
        if (toUpdate == null) {
            // ProductDescription with given id does not exist
            return Response.status(Response.Status.BAD_REQUEST).entity("Error: Product Description doesnt exist")
                    .build();
        }

        // Update author
        if (productDescription.getAuthor() != null) {
            BookAuthor author = bookAuthorManager.find(productDescription.getAuthor().getId());
            if (author != null) {
                // Update the author with new values
                author.setFirstName(productDescription.getAuthor().getFirstName());
                author.setLastName(productDescription.getAuthor().getLastName());

                // Save the updated author
                bookAuthorManager.save(author);

                if (productDescription.getAuthor().getId() != toUpdate.getAuthor().getId()) {
                    String message = "Author updated to " + author.getFirstName() + " " + author.getLastName() + ".";
                    ProductDescriptionEvidence quantityEvidence = new ProductDescriptionEvidence(user, message);
                    toUpdate.addProductDescriptionEvidence(quantityEvidence);
                }

                // Set the updated author to toUpdate
                toUpdate.setAuthor(author);
            }
        }

        // Update language
        if (productDescription.getLanguage() != null) {
            Language language = languageManager.find(productDescription.getLanguage().getId());
            if (language != null) {
                System.out.println(productDescription.getLanguage().getId());
                System.out.println(toUpdate.getLanguage().getId());
                if (productDescription.getLanguage().getId() != toUpdate.getLanguage().getId()) {
                    String message = "Language updated to " + language.getLanguage() + ".";
                    ProductDescriptionEvidence quantityEvidence = new ProductDescriptionEvidence(user, message);
                    toUpdate.addProductDescriptionEvidence(quantityEvidence);
                }
                toUpdate.setLanguage(language);
            }
        }

        // Update categories
        if (productDescription.getCategories() != null) {
            List<Category> pcategories = new ArrayList<>();
            pcategories = productDescription.getCategories();
            List<Category> tcategories = new ArrayList<>();
            tcategories = toUpdate.getCategories();

            // compare if all categories are the same
            int same = 0;
            for (Category category : pcategories) {
                for (Category category2 : tcategories) {
                    if (category.getId() == category2.getId()) {
                        same++;
                    }
                }
            }
            if (same == pcategories.size() && same == tcategories.size()) {
            } else {
                String message = "Categories updated to ";
                for (Category category : pcategories) {
                    message += category.getName() + ", ";
                }
                message = message.substring(0, message.length() - 2) + ".";
                ProductDescriptionEvidence quantityEvidence = new ProductDescriptionEvidence(user, message);
                toUpdate.addProductDescriptionEvidence(quantityEvidence);

            }

            toUpdate.clearCategories(); // Clear the existing categories
            for (Category category : productDescription.getCategories()) {
                Category categoryToUpdate = categoryManager.find(category.getId());
                if (categoryToUpdate != null) {
                    if (!toUpdate.getCategories().contains(categoryToUpdate)) {
                        toUpdate.addCategory(categoryToUpdate);
                    }
                }
            }
        }
        // log the changes
        if (!productDescription.getName().equals(toUpdate.getName())) {
            String message = "Name updated to " + productDescription.getName() + ".";
            ProductDescriptionEvidence quantityEvidence = new ProductDescriptionEvidence(user, message);
            toUpdate.addProductDescriptionEvidence(quantityEvidence);
        }
        if (!productDescription.getDescription().equals(toUpdate.getDescription())) {
            String message = "Description updated to " + productDescription.getDescription() + ".";
            ProductDescriptionEvidence quantityEvidence = new ProductDescriptionEvidence(user, message);
            toUpdate.addProductDescriptionEvidence(quantityEvidence);
        }
        if (productDescription.getPrice() != toUpdate.getPrice()) {
            String message = "Price updated to " + productDescription.getPrice() + ".";
            ProductDescriptionEvidence quantityEvidence = new ProductDescriptionEvidence(user, message);
            toUpdate.addProductDescriptionEvidence(quantityEvidence);
        }
        if (!productDescription.getISBN().equals(toUpdate.getISBN())) {
            String message = "ISBN updated to " + productDescription.getISBN() + ".";
            ProductDescriptionEvidence quantityEvidence = new ProductDescriptionEvidence(user, message);
            toUpdate.addProductDescriptionEvidence(quantityEvidence);
        }
        if (productDescription.getPages() != toUpdate.getPages()) {
            String message = "Pages updated to " + productDescription.getPages() + ".";
            ProductDescriptionEvidence quantityEvidence = new ProductDescriptionEvidence(user, message);
            toUpdate.addProductDescriptionEvidence(quantityEvidence);
        }
        if (!productDescription.getImage().equals(toUpdate.getImage())) {
            String message = "Image updated.";
            ProductDescriptionEvidence quantityEvidence = new ProductDescriptionEvidence(user, message);
            toUpdate.addProductDescriptionEvidence(quantityEvidence);
        }

        toUpdate.setName(productDescription.getName());
        toUpdate.setDescription(productDescription.getDescription());
        toUpdate.setPrice(productDescription.getPrice());
        toUpdate.setISBN(productDescription.getISBN());
        toUpdate.setPages(productDescription.getPages());
        toUpdate.setImage(productDescription.getImage());

        productDescriptionManager.save(toUpdate);
        return Response.ok().entity(toUpdate).build();
    }

    /**
     * Add discount to product description.
     * 
     * @param id          ID of the ProductDescription to be updated.
     * @param discount_id ID of the discount to be added.
     * @return Response status.
     */
    /*
     * @PUT
     * 
     * @Path("/{id}/discount/{discount_id}")
     * 
     * @Produces(MediaType.APPLICATION_JSON)
     * 
     * @RolesAllowed({ "admin" })
     * public Response addDiscountToProductDescription(@PathParam("id") long id,
     * 
     * @PathParam("discount_id") long discount_id) {
     * ProductDescription productDescription = productDescriptionManager.find(id);
     * if (productDescription == null) {
     * // ProductDescription with given id does not exist
     * return Response.status(Response.Status.BAD_REQUEST).
     * entity("Error: Product Description doesnt exist")
     * .build();
     * }
     * Discount discount = discountManager.find(discount_id);
     * if (discount == null) {
     * // Discount with given id does not exist
     * return Response.status(Response.Status.BAD_REQUEST).
     * entity("Error: Discount doesnt exist").build();
     * }
     * productDescription.setDiscount(discount);
     * productDescriptionManager.save(productDescription);
     * return Response.ok().entity(productDescription).build();
     * }
     */
    // TODO DELETE? - I think this is not needed, instead we can add discount by
    // value (function below)

    /**
     * Add discount to product description by discount name.
     *
     * @param id       ID of the ProductDescription to be updated.
     * @param discount Discount to be added.
     * @return Response status.
     */
    @PUT
    @Path("/{id}/discount/{discount_value}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ "admin" })
    public Response addDiscountToProductDescription(@PathParam("id") long id,
            @PathParam("discount_value") int discount) {
        ProductDescription productDescription = productDescriptionManager.find(id);
        if (productDescription == null) {
            // ProductDescription with given id does not exist
            return Response.status(Response.Status.BAD_REQUEST).entity("Error: Product Description doesnt exist")
                    .build();
        }
        Discount discountToAdd = discountManager.findDiscount(discount);
        if (discountToAdd == null) {
            // Discount with given discount does not exist, create a new one
            discountToAdd = new Discount();
            discountToAdd.setDiscount(discount);
            discountManager.save(discountToAdd);
        }
        String userEmail = securityContext.getUserPrincipal().getName();
        RegisteredUser user = registeredUserManager.findByEmail(userEmail);
        String message = "Discount updated to " + discount + "%.";
        ProductDescriptionEvidence discountEvidence = new ProductDescriptionEvidence(user, message);
        productDescription.addProductDescriptionEvidence(discountEvidence);
        productDescription.setDiscount(discountToAdd);
        productDescriptionManager.save(productDescription);
        return Response.ok().entity(productDescription).build();
    }

    /**
     * Deletes a ProductDescription from id.
     * 
     * @param id ID of the ProductDescription to be deleted.
     * @return Response status.
     */
    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ "admin" })
    public Response deleteProductDescription(@PathParam("id") long id) {
        ProductDescription toDelete = productDescriptionManager.find(id);
        if (toDelete == null) {
            // ProductDescription with given id does not exist
            return Response.status(Response.Status.BAD_REQUEST).entity("Error: Product Description doesnt exist")
                    .build();
        }
        productDescriptionManager.delete(toDelete);
        return Response.ok().entity("Successfully removed the Product Description").build();
    }

    /**
     * Update quantity of ProductItem
     */
    @PUT
    @Path("/{id}/{amount}")
    @RolesAllowed({ "admin", "employee" })
    public Response updateProductItemQuantity(@PathParam("id") long id, @PathParam("amount") int amount) {
        ProductDescription productDescription = productDescriptionManager.find(id);
        if (productDescription == null) {
            // ProductDescription with given id does not exist
            return Response.status(Response.Status.BAD_REQUEST).entity("Error: Product Description doesnt exist")
                    .build();
        }
        productDescription.setAvailableQuantity(amount);
        String userEmail = securityContext.getUserPrincipal().getName();
        RegisteredUser user = registeredUserManager.findByEmail(userEmail);
        String message = "Quantity updated to " + amount + ".";
        ProductDescriptionEvidence quantityEvidence = new ProductDescriptionEvidence(user, message);
        productDescription.addProductDescriptionEvidence(quantityEvidence);
        productDescriptionManager.save(productDescription);
        return Response.ok().entity("Succesfully updated quantity of given Product Description ID").build();
    }

}
