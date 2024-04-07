/**
 * PIS Projekt 2024
 * UserResource.java
 * @author Vojtech Fiala <xfiala61>
 */

package pis.api;


import pis.data.Address;
import pis.data.RegisteredUser;
import pis.service.RegisteredUserManager;

//import java.util.regex.Pattern;

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
    @Path("/address")
    @RolesAllowed({"admin", "user", "employee"})
    public Response getAddress() {

        RegisteredUser u = userManager.findByEmail(securityContext.getUserPrincipal().getName());

        if (u == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("You are not logged in! How could this happen!").build();
        }

        Address address = u.getAddress();

        return Response.status(Response.Status.OK).entity(address).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/address/{email}")
    @RolesAllowed({"admin"})
    public Response getAddress(@PathParam("email") String email) {

        RegisteredUser u = userManager.findByEmail(email);

        if (u == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("User doesn't exist!").build();
        }

        Address address = u.getAddress();

        return Response.status(Response.Status.OK).entity(address).build();
    }
}
