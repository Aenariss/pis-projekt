/**
 * PIS Projekt 2024
 * UserResource.java
 * @author Vojtech Fiala <xfiala61>
 */

package pis.api;

import pis.api.dto.ProfileRequest;
import pis.api.dto.Address;
import pis.api.dto.*;
import pis.data.RegisteredUser;
import pis.service.RegisteredUserManager;

import java.util.regex.Pattern;
import org.apache.commons.codec.digest.DigestUtils;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

@Path("/user")
public class UserResource {
    @Inject
    private RegisteredUserManager userManager;

    @Context
    private SecurityContext securityContext;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ "admin", "user", "employee" })
    public Response getProfile() {

        RegisteredUser u = userManager.findByEmail(securityContext.getUserPrincipal().getName());

        if (u == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("You are not logged in! How could this happen!")
                    .build();
        }

        return Response.status(Response.Status.OK).entity(u).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    @RolesAllowed({"admin"})
    public Response getProfile(@PathParam("id") long id) {

        RegisteredUser u = userManager.find(id);

        if (u == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("User not found!").build();
        }

        return Response.status(Response.Status.OK).entity(u).build();
    }

    private Response setProfileBody(ProfileRequest r, RegisteredUser u) {

        Address addr = new Address();

        if (!r.valid()) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid request!!").build();
        }

        // Email validation
        boolean isEmailLegit = Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$", Pattern.CASE_INSENSITIVE)
                .matcher(r.getEmail()).find();

        if (!isEmailLegit) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Wrong email format!!").build();
        }

        addr.setPostCode(r.getAddress().getPostCode());
        addr.setState(r.getAddress().getState());
        addr.setStreet(r.getAddress().getStreet());
        addr.setStreetNumber(r.getAddress().getStreetNumber());
        addr.setTown(r.getAddress().getTown());

        u.setEmail(r.getEmail()); // If this was a real app, there'd have to be some sort of validity check - wait
                                  // for confirmation from origin email etc..
        u.setFirstname(r.getFirstname());
        u.setPhone(r.getPhone());
        u.setSurname(r.getSurname());
        u.setAddress(r.getAddress());

        userManager.save(u);

        return Response.status(Response.Status.OK).build();
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed({ "admin", "user", "employee" })
    public Response setProfile(ProfileRequest r) {

        RegisteredUser u = userManager.findByEmail(securityContext.getUserPrincipal().getName());

        if (u == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("You are not logged in! How could this happen!")
                    .build();
        }

        return this.setProfileBody(r, u);
    }

    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed({"admin"})
    public Response setProfile(@PathParam("id") long userId, ProfileRequest r) {


        RegisteredUser u = userManager.find(userId);

        if (u == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("User not found!").build();
        }

        return this.setProfileBody(r, u);
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/password")
    @RolesAllowed({ "admin", "user", "employee" })
    // Documentation for http://localhost:9089/openapi/ui/
    @APIResponses(
        value = {
            @APIResponse(
                responseCode = "200",
                description = "Password was successfully changed"
            ),
            @APIResponse(
                responseCode = "403",
                description = "Invalid current password!"
            ),
            @APIResponse(
                responseCode = "400",
                description = "Password was not possible to change"
            )
        }
    )
    public Response setPassword(PasswordChangeRequest r) {

        if (!r.valid()) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid request!!").build();
        }

        RegisteredUser u = userManager.findByEmail(securityContext.getUserPrincipal().getName());

        if (u == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("You are not logged in! How could this happen!")
                    .build();
        }
        
        if (!DigestUtils.sha512Hex(r.getOld_password()).equals(u.getPasswordHash())) {
            return Response.status(Response.Status.FORBIDDEN).entity("Invalid current password!").build();
        }

        if (r.getPassword().length() < 3) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Password is too short!!").build();
        }

        u.setPasswordHash(DigestUtils.sha512Hex(r.getPassword()));
        userManager.save(u);

        return Response.status(Response.Status.OK).build();
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/password/{id}")
    @RolesAllowed({"admin"})
    public Response setPassword(@PathParam("id") long userId, PasswordChangeRequest r) {
        
        if (!r.valid()) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid request!!").build();
        }

        RegisteredUser u = userManager.find(userId);

        if (u == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("User not found!").build();
        }

        if (r.getPassword().length() < 3) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Password is too short!!").build();
        }

        u.setPasswordHash(DigestUtils.sha512Hex(r.getPassword()));
        userManager.save(u);

        return Response.status(Response.Status.OK).build();
    }
}
