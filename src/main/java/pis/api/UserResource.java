/**
 * PIS Projekt 2024
 * UserResource.java
 * @author Vojtech Fiala <xfiala61>
 */

package pis.api;


import pis.data.Address;
import pis.data.RegisteredUser;
import pis.service.RegisteredUserManager;

import java.util.regex.Pattern;
import org.apache.commons.codec.digest.DigestUtils;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.json.JsonObject;
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
    @RolesAllowed({"admin", "user", "employee"})
    public Response getProfile() {

        RegisteredUser u = userManager.findByEmail(securityContext.getUserPrincipal().getName());

        if (u == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("You are not logged in! How could this happen!").build();
        }

        u.setPasswordHash(""); // Only local overwrite cuz I don't persist the change
        return Response.status(Response.Status.OK).entity(u).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{email}")
    @RolesAllowed({"admin"})
    public Response getProfile(@PathParam("email") String email) {

        RegisteredUser u = userManager.findByEmail(email);

        if (u == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("User not found!").build();
        }

        u.setPasswordHash(""); // Only local overwrite cuz I don't persist the change
        return Response.status(Response.Status.OK).entity(u).build();
    }

    private Response setProfileBody(JsonObject json,  RegisteredUser u) {
        String fn; String sn; String phone; String email; Address addr = new Address();

        try {
            fn = json.getString("firstname");
            sn = json.getString("surname");
            phone = json.getString("phone");
            email = json.getString("email");

            // Email validation
            boolean isEmailLegit = Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$", Pattern.CASE_INSENSITIVE).matcher(email).find();

            if (!isEmailLegit) {
                return Response.status(Response.Status.BAD_REQUEST).entity("Wrong email format!!").build();
            }

            String state; String town; String street; String streetNumber; String postCode;
            JsonObject tmp = json.getJsonObject("address");
            state = tmp.getString("state");
            street = tmp.getString("street");
            town = tmp.getString("town");
            postCode = tmp.getString("postCode");
            streetNumber = tmp.getString("streetNumber");

            addr.setPostCode(postCode);
            addr.setState(state);
            addr.setStreet(street);
            addr.setStreetNumber(streetNumber);
            addr.setTown(town);
        }
        catch (Exception e) {
            System.out.println(e);
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid values given in JSON!").build();
        }

        u.setEmail(email); // If this was a real app, there'd have to be some sort of validity check - wait for confirmation from origin email etc..
        u.setFirstname(fn);
        u.setPhone(phone);
        u.setSurname(sn);
        u.setAddress(addr);
        
        userManager.save(u);
        return Response.status(Response.Status.OK).entity(u).build();
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed({"admin", "user", "employee"})
    public Response setProfile(JsonObject json) {


        RegisteredUser u = userManager.findByEmail(securityContext.getUserPrincipal().getName());

        if (u == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("You are not logged in! How could this happen!").build();
        }

        return this.setProfileBody(json, u);
    }

    @PUT
    @Path("/{email}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed({"admin"})
    public Response setProfile(@PathParam("email") String email, JsonObject json) {


        RegisteredUser u = userManager.findByEmail(email);

        if (u == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("User not found!").build();
        }

        return this.setProfileBody(json, u);
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/password")
    @RolesAllowed({"admin", "user", "employee"})
    public Response setPassword(JsonObject json) {

        String pass;
        RegisteredUser u = userManager.findByEmail(securityContext.getUserPrincipal().getName());
        try {
            pass = json.getString("password");
        }
        catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid request!").build();
        }

        if (u == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("You are not logged in! How could this happen!").build();
        }

        if (pass.length() < 3) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Password is too short!!").build();
        }
        
        u.setPasswordHash(DigestUtils.sha512Hex(pass));
        userManager.save(u);

        return Response.status(Response.Status.OK).entity(u).build();
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/password/{email}")
    @RolesAllowed({"admin"})
    public Response setPassword(@PathParam("email") String email, JsonObject json) {
        
        String pass;
        RegisteredUser u = userManager.findByEmail(email);
        try {
            pass = json.getString("password");
        }
        catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid request!").build();
        }

        if (u == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("User not found!").build();
        }

        if (pass.length() < 3) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Password is too short!!").build();
        }
        
        u.setPasswordHash(DigestUtils.sha512Hex(pass));
        userManager.save(u);

        return Response.status(Response.Status.OK).entity(u).build();
    }
}
