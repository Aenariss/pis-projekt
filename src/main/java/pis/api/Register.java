/**
 * PIS Projekt 2024
 * Register.java
 * @author Vojtech Fiala <xfiala61>
 */

 package pis.api;

 import pis.data.RegisteredUser;
 import pis.data.RegisterRequest;
 
 import pis.service.RegisteredUserManager;
 
 import java.util.regex.Pattern;
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
    public Response register(RegisterRequest r) {

        // Email validation
        boolean isEmailLegit = Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$", Pattern.CASE_INSENSITIVE).matcher(r.getEmail()).find();

        if (!isEmailLegit) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Wrong email format!!").build();
        }

        // Password validation
        if (r.getPassword().length() < 3) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Password is too short!!").build();
        }

        RegisteredUser u = new RegisteredUser(r.getFirstname(), r.getSurname(), r.getPhone(), r.getEmail(), r.getPassword());

        // User doesnt exist yet validation
        RegisteredUser taken = userManager.findByEmail(r.getEmail());
        if (taken != null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("User with this email already exists!!").build();
        }

        // Create user
        u = userManager.save(u);

        if (u == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Something went wrong!!").build();
        }

        return Response.status(Response.Status.OK).entity("Registered!").build();
     }
 }
 