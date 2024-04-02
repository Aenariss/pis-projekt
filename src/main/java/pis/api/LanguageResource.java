/**
 * PIS Projekt 2024
 * LanguageResource.java
 * @language Tomas Ondrusek <xondru18>
 */

package pis.api;

import java.util.List;

import pis.data.Language;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import pis.service.LanguageManager;
import jakarta.ws.rs.core.Response;

/**
 * REST API resource for working with Languages.
 */
@Path("/language")
@PermitAll
public class LanguageResource {
    @Inject
    private LanguageManager languageManager;

    /**
     * Returns list of all Languages.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Language> getLanguages() {
        return languageManager.findAll();
    }

    /**
     * Returns Language from id.
     * 
     * @param id Id of the Language.
     * @return Language with given id.
     */
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Language getLanguage(@PathParam("id") long id) {
        return languageManager.find(id);
    }

    /**
     * Adds new language.
     * 
     * @param language Language to be added.
     * @return Response status.
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addLanguage(Language language) {
        if (language.getLanguage().length() < 2) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Language needs a valid name!").build();
        }
        if (languageManager.findLanguage(language.getLanguage()) == null) {
            // Language with given name does not exist, create new one
            Language savedLanguage = languageManager.save(language);
            return Response.ok().entity(savedLanguage).build();
        }
        return Response.status(Response.Status.CONFLICT).entity("Error: Language already exists").build();
    }

    /**
     * Updates a Language.
     * 
     * @param language Language to be updated.
     * @return Response status
     */
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateLanguage(@PathParam("id") long id, Language language) {
        Language toUpdate = languageManager.find(id);
        if (toUpdate == null) {
            // Language with given id does not exist
            return Response.status(Response.Status.BAD_REQUEST).entity("Error: Language does not exist").build();
        }
        toUpdate.setLanguage(language.getLanguage());
        languageManager.save(toUpdate);
        return Response.ok().entity(toUpdate).build();
    }

    /**
     * Deletes a Language by given id.
     * 
     * @param id Id of the Language to be deleted.
     * @return Response status
     */
    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteLanguage(@PathParam("id") long id) {
        Language toDelete = languageManager.find(id);
        if (toDelete == null) {
            // Language with given id does not exist
            return Response.status(Response.Status.BAD_REQUEST).entity("Error: Language does not exist").build();
        }
        languageManager.delete(toDelete);
        return Response.ok().entity("Succesfully removed the Language").build();
    }

    /**
     * Deletes a Language.
     * 
     * @param language Language to be deleted.
     * @return Response status
     */
    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteLanguage(Language language) {
        Language toDelete = languageManager.findLanguage(language.getLanguage());
        if (toDelete == null) {
            // Language with given name does not exist
            return Response.status(Response.Status.BAD_REQUEST).entity("Error: Language does not exist").build();
        }
        languageManager.delete(toDelete);
        return Response.ok().entity("Succesfully removed the Language").build();
    }
}
