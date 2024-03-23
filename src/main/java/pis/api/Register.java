/**
 * PIS Projekt 2024
 * Register.java
 * @author Vojtech Fiala <xfiala61>
 */

 package pis.api;

 import pis.data.RegisteredUser;
 import pis.data.RegisterRequest;
 
 import pis.service.RegisteredUserManager;
 
 import jakarta.inject.Inject;
 import jakarta.ws.rs.*;
 import jakarta.ws.rs.core.MediaType;
 import jakarta.ws.rs.core.Response;

 @Path("/register")
 public class Register {
 
    @Inject
    private RegisteredUserManager userManager;
    /**
    * Login
    */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response register(RegisterRequest r) {

        // TODO: Add checks if given values are valid

        RegisteredUser u = new RegisteredUser(r.getFirstname(), r.getSurname(), r.getPhone(), r.getEmail(), r.getPassword());

        u = userManager.save(u);

        if (u == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Something went wrong!!").build();
        }

        return Response.status(Response.Status.OK).entity("Registered!").build();
     }
 }
 