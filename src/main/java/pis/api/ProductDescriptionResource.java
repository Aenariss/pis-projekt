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
import pis.data.Category;
import pis.api.dto.FilterQuery;
import pis.api.dto.SearchQuery;
import pis.data.BookAuthor;
import pis.data.Language;
import pis.data.Discount;
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
import jakarta.ws.rs.core.Response;

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

    /**
     * Returns list of all ProductDescriptions.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<ProductDescription> getProductDescriptions() {
        return productDescriptionManager.findAll();
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
    public ProductDescription getProductDescription(@PathParam("id") long id) {
        return productDescriptionManager.find(id);
    }

    /**
     * Returns ProductDescriptions by author.
     * 
     * @param author_id ID of the author.
     * @return List of ProductDescriptions with given author.
     */
    @GET
    @Path("/author/{author_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<ProductDescription> getProductDescriptionsByAuthor(@PathParam("author_id") long author_id) {
        BookAuthor author = bookAuthorManager.find(author_id);
        if (author == null) {
            // Author with given id does not exist
            return null;
        }
        return productDescriptionManager.findByAuthor(author.getFirstName(), author.getLastName());
    }

    /**
     * Returns ProductDescriptions by category.
     * 
     * @param category_id ID of the category.
     * @return List of ProductDescriptions with given category.
     */
    @GET
    @Path("/category/{category_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<ProductDescription> getProductDescriptionsByCategory(@PathParam("category_id") long category_id) {
        Category category = categoryManager.find(category_id);
        if (category == null) {
            // Category with given id does not exist
            return null;
        }
        return productDescriptionManager.findByCategory(category.getName());
    }

    /**
     * Returns ProductDescriptions by language.
     * 
     * @param language_id ID of the language.
     * @return List of ProductDescriptions with given language.
     */
    @GET
    @Path("/language/{language_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<ProductDescription> getProductDescriptionsByLanguage(@PathParam("language_id") long language_id) {
        Language language = languageManager.find(language_id);
        if (language == null) {
            // Language with given id does not exist
            return null;
        }
        return productDescriptionManager.findByLanguage(language.getLanguage());
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

        // Empty array to be returned in case the request is not valid
        List<ProductDescription> arr = new ArrayList<>();
        // Request validation
        if (!filterQuery.valid()) {
            return arr;
        }
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
        
        /**
         * The request has to be valid -- contain all required fields
         */
        if (!productDescription.valid()) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid request!").build();
        }

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

        // The request must contain all necessary fields
        if (!productDescription.valid()) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid request!").build();
        }

        ProductDescription toUpdate = productDescriptionManager.find(id);
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

                // Set the updated author to toUpdate
                toUpdate.setAuthor(author);
            }
        }

        // Update language
        if (productDescription.getLanguage() != null) {
            Language language = languageManager.find(productDescription.getLanguage().getId());
            if (language != null) {
                toUpdate.setLanguage(language);
            }
        }

        // Update categories
        if (productDescription.getCategories() != null) {
            toUpdate.clearCategories(); // Clear the existing categories
            for (Category category : productDescription.getCategories()) {
                Category categoryToUpdate = categoryManager.find(category.getId());
                if (categoryToUpdate != null) {
                    toUpdate.addCategory(categoryToUpdate);
                }
            }
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
     * Add author to product description.
     * 
     * @param id        ID of the ProductDescription to be updated.
     * @param author_id ID of the author to be added.
     * @return Response status.
     */
    @PUT
    @Path("/{id}/author/{author_id}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ "admin" })
    public Response addAuthorToProductDescription(@PathParam("id") long id, @PathParam("author_id") long author_id) {
        ProductDescription productDescription = productDescriptionManager.find(id);
        if (productDescription == null) {
            // ProductDescription with given id does not exist
            return Response.status(Response.Status.BAD_REQUEST).entity("Error: Product Description doesnt exist")
                    .build();
        }
        BookAuthor author = bookAuthorManager.find(author_id);
        if (author == null) {
            // Author with given id does not exist
            return Response.status(Response.Status.BAD_REQUEST).entity("Error: Author doesnt exist").build();
        }
        productDescription.setAuthor(author);
        productDescriptionManager.save(productDescription);
        return Response.ok().entity(productDescription).build();
    }

    /**
     * Add language to product description.
     * 
     * @param id          ID of the ProductDescription to be updated.
     * @param language_id ID of the language to be added.
     * @return Response status.
     */
    @PUT
    @Path("/{id}/language/{language_id}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ "admin" })
    public Response addLanguageToProductDescription(@PathParam("id") long id,
            @PathParam("language_id") long language_id) {
        ProductDescription productDescription = productDescriptionManager.find(id);
        if (productDescription == null) {
            // ProductDescription with given id does not exist
            return Response.status(Response.Status.BAD_REQUEST).entity("Error: Product Description doesnt exist")
                    .build();
        }
        Language language = languageManager.find(language_id);
        if (language == null) {
            // Language with given id does not exist
            return Response.status(Response.Status.BAD_REQUEST).entity("Error: Language doesnt exist").build();
        }
        productDescription.setLanguage(language);
        productDescriptionManager.save(productDescription);
        return Response.ok().entity(productDescription).build();
    }

    /**
     * Add discount to product description.
     * 
     * @param id          ID of the ProductDescription to be updated.
     * @param discount_id ID of the discount to be added.
     * @return Response status.
     */
    /*@PUT
    @Path("/{id}/discount/{discount_id}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ "admin" })
    public Response addDiscountToProductDescription(@PathParam("id") long id,
            @PathParam("discount_id") long discount_id) {
        ProductDescription productDescription = productDescriptionManager.find(id);
        if (productDescription == null) {
            // ProductDescription with given id does not exist
            return Response.status(Response.Status.BAD_REQUEST).entity("Error: Product Description doesnt exist")
                    .build();
        }
        Discount discount = discountManager.find(discount_id);
        if (discount == null) {
            // Discount with given id does not exist
            return Response.status(Response.Status.BAD_REQUEST).entity("Error: Discount doesnt exist").build();
        }
        productDescription.setDiscount(discount);
        productDescriptionManager.save(productDescription);
        return Response.ok().entity(productDescription).build();
    }*/ // TODO DELETE? - I think this is not needed, instead we can add discount by value (function below)

    /**
     * Add discount to product description by discount name.
     *
     * @param id ID of the ProductDescription to be updated.
     * @param discount Discount to be added.
     * @return Response status.
     */
    @PUT
    @Path("/{id}/discount/{discount_value}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"admin"})
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
        productDescription.setDiscount(discountToAdd);
        productDescriptionManager.save(productDescription);
        return Response.ok().entity(productDescription).build();
    }

    /**
     * Add category to product description.
     * 
     * @param id          ID of the ProductDescription to be updated.
     * @param category_id ID of the category to be added.
     * @return Response status.
     */
    @PUT
    @Path("/{id}/category/{category_id}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ "admin" })
    public Response addCategoryToProductDescription(@PathParam("id") long id,
            @PathParam("category_id") long category_id) {
        ProductDescription productDescription = productDescriptionManager.find(id);
        if (productDescription == null) {
            // ProductDescription with given id does not exist
            return Response.status(Response.Status.BAD_REQUEST).entity("Error: Product Description doesnt exist")
                    .build();
        }
        Category category = categoryManager.find(category_id);
        if (category == null) {
            // Category with given id does not exist
            return Response.status(Response.Status.BAD_REQUEST).entity("Error: Category doesnt exist").build();
        }
        // if category is already in the list, return
        if (productDescription.getCategories().contains(category)) {
            return Response.ok().entity(productDescription).build();
        }
        productDescription.addCategory(category);
        productDescriptionManager.save(productDescription);
        return Response.ok().entity(productDescription).build();
    }

    /**
     * Add categories to product description.
     * 
     * @param id ID of the ProductDescription to be updated.
     * @return Response status.
     */
    @PUT
    @Path("/{id}/categories")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ "admin" })
    public Response addCategoriesToProductDescription(@PathParam("id") long id, List<String> categories) {
        ProductDescription productDescription = productDescriptionManager.find(id);
        if (productDescription == null) {
            // ProductDescription with given id does not exist
            return Response.status(Response.Status.BAD_REQUEST).entity("Error: Product Description doesnt exist")
                    .build();
        }
        for (String category : categories) {
            Category cat = categoryManager.findByName(category);
            System.out.println(cat);
            if (cat == null) {
                // Category with given id does not exist
                return Response.status(Response.Status.BAD_REQUEST).entity("Error: Category doesnt exist").build();
            }
            // if category is already in the list, return
            if (productDescription.getCategories().contains(cat)) {
                return Response.ok().entity(productDescription).build();
            }
            productDescription.addCategory(cat);
        }
        productDescriptionManager.save(productDescription);
        return Response.ok().entity(productDescription).build();
    }

    /**
     * Deletes a category from product description.
     * 
     * @param id          ID of the ProductDescription to be updated.
     * @param category_id ID of the category to be removed.
     * @return Response status.
     */
    @DELETE
    @Path("/{id}/category/{category_id}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ "admin" })
    public Response deleteCategoryFromProductDescription(@PathParam("id") long id,
            @PathParam("category_id") long category_id) {
        ProductDescription productDescription = productDescriptionManager.find(id);
        if (productDescription == null) {
            // ProductDescription with given id does not exist
            return Response.status(Response.Status.BAD_REQUEST).entity("Error: Product Description doesnt exist")
                    .build();
        }
        Category category = categoryManager.find(category_id);
        if (category == null) {
            // Category with given id does not exist
            return Response.status(Response.Status.BAD_REQUEST).entity("Error: Category doesnt exist").build();
        }
        // if category is not in the list, return
        if (!productDescription.getCategories().contains(category)) {
            return Response.ok().entity(productDescription).build();
        }
        productDescription.removeCategory(category);
        productDescriptionManager.save(productDescription);
        return Response.ok().entity(productDescription).build();
    }

    /**
     * Deletes categories from product description.
     * 
     * @param id ID of the ProductDescription to be updated.
     * @return Response status.
     */
    @DELETE
    @Path("/{id}/categories")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ "admin" })
    public Response deleteCategoriesFromProductDescription(@PathParam("id") long id) {
        ProductDescription productDescription = productDescriptionManager.find(id);
        if (productDescription == null) {
            // ProductDescription with given id does not exist
            return Response.status(Response.Status.BAD_REQUEST).entity("Error: Product Description doesnt exist")
                    .build();
        }
        productDescription.clearCategories();
        productDescriptionManager.save(productDescription);
        return Response.ok().entity(productDescription).build();
    }

    /**
     * Deletes author from product description.
     * 
     * @param id ID of the ProductDescription to be updated.
     * @return Response status.
     */
    @DELETE
    @Path("/{id}/author")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ "admin" })
    public Response deleteAuthorFromProductDescription(@PathParam("id") long id) {
        ProductDescription productDescription = productDescriptionManager.find(id);
        if (productDescription == null) {
            // ProductDescription with given id does not exist
            return Response.status(Response.Status.BAD_REQUEST).entity("Error: Product Description doesnt exist")
                    .build();
        }
        productDescription.setAuthor(null);
        productDescriptionManager.save(productDescription);
        return Response.ok().entity(productDescription).build();
    }

    /**
     * Deletes language from product description.
     * 
     * @param id ID of the ProductDescription to be updated.
     * @return Response status.
     */
    @DELETE
    @Path("/{id}/language")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ "admin" })
    public Response deleteLanguageFromProductDescription(@PathParam("id") long id) {
        ProductDescription productDescription = productDescriptionManager.find(id);
        if (productDescription == null) {
            // ProductDescription with given id does not exist
            return Response.status(Response.Status.BAD_REQUEST).entity("Error: Product Description doesnt exist")
                    .build();
        }
        productDescription.setLanguage(null);
        productDescriptionManager.save(productDescription);
        return Response.ok().entity(productDescription).build();
    }

    /**
     * Deletes discount from product description.
     * 
     * @param id ID of the ProductDescription to be updated.
     * @return Response status.
     */
    @DELETE
    @Path("/{id}/discount")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ "admin" })
    public Response deleteDiscountFromProductDescription(@PathParam("id") long id) {
        ProductDescription productDescription = productDescriptionManager.find(id);
        if (productDescription == null) {
            // ProductDescription with given id does not exist
            return Response.status(Response.Status.BAD_REQUEST).entity("Error: Product Description doesnt exist")
                    .build();
        }
        productDescription.setDiscount(null);
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
     * Deletes a ProductDescription
     * 
     * @param productDescription ProductDescription to be deleted.
     * @return Response status
     */
    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ "admin" })
    public Response deleteProductDescription(ProductDescription productDescription) {
        ProductDescription toDelete = productDescriptionManager.findProductDescription(productDescription.getName());
        if (toDelete == null) {
            // ProductDescription with given name does not exist
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
        productDescriptionManager.save(productDescription);
        return Response.ok().entity("Succesfully updated quantity of given Product Description ID").build();
    }

}
