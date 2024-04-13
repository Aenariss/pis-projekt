/**
 * PIS Projekt 2024
 * Register.java
 * @author Vojtech Fiala <xfiala61>
 */

 package pis.api;

import pis.data.RegisteredUser;

import pis.service.RegisteredUserManager;

import java.util.regex.Pattern;
import jakarta.inject.Inject;
import jakarta.json.JsonObject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/register")
public class Register {
 
    @Inject
    private RegisteredUserManager userManager;
    /**
    * Register
    */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response register(JsonObject json) {

        String firstname; String surname; String phone; String email; 
        String password; String state; String town; String street; String streetNumber; 
        String postCode;

        try {
            firstname = json.getString("firstname");
            surname = json.getString("surname");
            phone = json.getString("phone");
            email = json.getString("email");
            password = json.getString("password");
            state = json.getString("state");
            street = json.getString("street");
            town = json.getString("town");
            streetNumber = json.getString("streetNumber");
            postCode = json.getString("postCode");
            
        }
        catch (Exception e) {
            System.out.println(e);
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid request!").build(); 
        }

        // Email validation
        boolean isEmailLegit = Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$", Pattern.CASE_INSENSITIVE).matcher(email).find();

        if (!isEmailLegit) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Wrong email format!!").build();
        }

        // Password validation
        if (password.length() < 3) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Password is too short!!").build();
        }

        RegisteredUser u = new RegisteredUser(firstname, surname, phone, email,
        password, state, town, street, streetNumber, postCode);

        // TODO: Validations for address?

        // User doesnt exist yet validation
        RegisteredUser taken = userManager.findByEmail(email);
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
 