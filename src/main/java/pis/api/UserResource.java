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

        u.setPasswordHash(""); // Only local overwrite cuz I don't persist the change
        return Response.status(Response.Status.OK).entity(u).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{email}")
    @RolesAllowed({ "admin" })
    public Response getProfile(@PathParam("email") String email) {

        RegisteredUser u = userManager.findByEmail(email);

        if (u == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("User not found!").build();
        }

        u.setPasswordHash(""); // Only local overwrite cuz I don't persist the change
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

        u.setPasswordHash(""); // Only local overwrite cuz I don't persist the change
        return Response.status(Response.Status.OK).entity(u).build();
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
    @Path("/{email}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed({ "admin" })
    public Response setProfile(@PathParam("email") String email, ProfileRequest r) {

        RegisteredUser u = userManager.findByEmail(email);

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
    public Response setPassword(PasswordChangeRequest r) {

        System.out.println("aaa");
        if (!r.valid()) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid request!!").build();
        }

        RegisteredUser u = userManager.findByEmail(securityContext.getUserPrincipal().getName());

        if (u == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("You are not logged in! How could this happen!")
                    .build();
        }

        if (r.getPassword().length() < 3) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Password is too short!!").build();
        }

        u.setPasswordHash(DigestUtils.sha512Hex(r.getPassword()));
        userManager.save(u);

        u.setPasswordHash("");
        return Response.status(Response.Status.OK).entity(u).build();
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/password/{email}")
    @RolesAllowed({ "admin" })
    public Response setPassword(@PathParam("email") String email, PasswordChangeRequest r) {

        if (!r.valid()) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid request!!").build();
        }

        RegisteredUser u = userManager.findByEmail(email);

        if (u == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("User not found!").build();
        }

        if (r.getPassword().length() < 3) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Password is too short!!").build();
        }

        u.setPasswordHash(DigestUtils.sha512Hex(r.getPassword()));
        userManager.save(u);

        u.setPasswordHash("");
        return Response.status(Response.Status.OK).entity(u).build();
    }
}
