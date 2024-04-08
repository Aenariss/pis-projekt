/**
 * PIS Projekt 2024
 * UserResource.java
 * @author Vojtech Fiala <xfiala61>
 */

package pis.api;


import pis.data.Address;
import pis.data.RegisteredUser;
import pis.service.RegisteredUserManager;

import org.apache.commons.codec.digest.DigestUtils;

//import java.util.regex.Pattern;

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

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/address")
    @RolesAllowed({"admin", "user", "employee"})
    public Response setAddress(JsonObject json) {

        String state;
        String town;
        String street;
        Integer streetNumber;
        String postCode; 

        try {
            state = json.getString("state");
            town = json.getString("town");
            street = json.getString("street");
            streetNumber = Integer.valueOf(json.getString("streetNumber"));
            postCode = json.getString("postCode");
        }
        catch (Exception e) {
            System.out.println(e);
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid request body!").build(); 
        }

        RegisteredUser u = userManager.findByEmail(securityContext.getUserPrincipal().getName());

        if (u == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("You are not logged in! How could this happen!").build();
        }

        Address address = u.getAddress();
        address.setPostCode(postCode);
        address.setState(state);
        address.setStreet(street);
        address.setStreetNumber(streetNumber);
        address.setTown(town);

        u.setAddress(address);
        userManager.save(u);

        return Response.status(Response.Status.OK).entity(u).build();
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/address/{email}")
    @RolesAllowed({"admin"})
    public Response setAddress(@PathParam("email") String email, JsonObject json) {

        String state;
        String town;
        String street;
        Integer streetNumber;
        String postCode; 

        try {
            state = json.getString("state");
            town = json.getString("town");
            street = json.getString("street");
            streetNumber = Integer.valueOf(json.getString("streetNumber"));
            postCode = json.getString("postCode");
        }
        catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid request body!").build(); 
        }

        RegisteredUser u = userManager.findByEmail(email);

        if (u == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("User not found!").build();
        }

        Address address = u.getAddress();
        address.setPostCode(postCode);
        address.setState(state);
        address.setStreet(street);
        address.setStreetNumber(streetNumber);
        address.setTown(town);

        u.setAddress(address);
        userManager.save(u);

        return Response.status(Response.Status.OK).entity(u).build();
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/address/state")
    @RolesAllowed({"admin", "user", "employee"})
    public Response setState(JsonObject json) {
        String state;

        RegisteredUser u = userManager.findByEmail(securityContext.getUserPrincipal().getName());
        try {
            state = json.getString("state");
        }
        catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid request!").build();
        }

        if (u == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("You are not logged in! How could this happen!").build();
        }

        Address addr = u.getAddress();
        addr.setState(state);
        u.setAddress(addr);
        userManager.save(u);

        return Response.status(Response.Status.OK).entity(u).build();
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/address/{email}/state")
    @RolesAllowed({"admin"})
    public Response setState(@PathParam("email") String email, JsonObject json) {
        String state;

        RegisteredUser u = userManager.findByEmail(email);
        try {
            state = json.getString("state");
        }
        catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid request!").build();
        }

        if (u == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("User not found!").build();
        }

        Address addr = u.getAddress();
        addr.setState(state);
        u.setAddress(addr);
        userManager.save(u);

        return Response.status(Response.Status.OK).entity(u).build();
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/address/town")
    @RolesAllowed({"admin", "user", "employee"})
    public Response setTown(JsonObject json) {

        String town;
        RegisteredUser u = userManager.findByEmail(securityContext.getUserPrincipal().getName());
        try {
            town = json.getString("town");
        }
        catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid request!").build();
        }

        if (u == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("You are not logged in! How could this happen!").build();
        }

        Address addr = u.getAddress();
        addr.setTown(town);
        u.setAddress(addr);
        userManager.save(u);

        return Response.status(Response.Status.OK).entity(u).build();
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/address/{email}/town")
    @RolesAllowed({"admin"})
    public Response setTown(@PathParam("email") String email, JsonObject json) {
        String town;
        RegisteredUser u = userManager.findByEmail(email);
        try {
            town = json.getString("town");
        }
        catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid request!").build();
        }

        if (u == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("User not found!").build();
        }

        Address addr = u.getAddress();
        addr.setTown(town);
        u.setAddress(addr);
        userManager.save(u);

        return Response.status(Response.Status.OK).entity(u).build();
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/address/street")
    @RolesAllowed({"admin", "user", "employee"})
    public Response setStreet(JsonObject json) {

        RegisteredUser u = userManager.findByEmail(securityContext.getUserPrincipal().getName());
        String street;
        try {
            street = json.getString("street");
        }
        catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid request!").build();
        }

        if (u == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("You are not logged in! How could this happen!").build();
        }

        Address addr = u.getAddress();
        addr.setStreet(street);
        u.setAddress(addr);
        userManager.save(u);

        return Response.status(Response.Status.OK).entity(u).build();
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/address/{email}/street")
    @RolesAllowed({"admin"})
    public Response setStreet(@PathParam("email") String email, JsonObject json) {
        RegisteredUser u = userManager.findByEmail(email);
        String street;
        try {
            street = json.getString("street");
        }
        catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid request!").build();
        }

        if (u == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("User not found!").build();
        }

        Address addr = u.getAddress();
        addr.setStreet(street);
        u.setAddress(addr);
        userManager.save(u);

        return Response.status(Response.Status.OK).entity(u).build();
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/address/streetNumber")
    @RolesAllowed({"admin", "user", "employee"})
    public Response setStreetNumber(JsonObject json) {

        RegisteredUser u = userManager.findByEmail(securityContext.getUserPrincipal().getName());
        Integer streetN = 0;
        try {
            streetN = Integer.valueOf(json.getString("streetNumber"));
        }
        catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid street number!").build();
        }

        if (u == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("You are not logged in! How could this happen!").build();
        }

        Address addr = u.getAddress();
        addr.setStreetNumber(streetN);
        u.setAddress(addr);
        userManager.save(u);

        return Response.status(Response.Status.OK).entity(u).build();
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/address/{email}/streetNumber")
    @RolesAllowed({"admin"})
    public Response setStreetNumber(@PathParam("email") String email, JsonObject json) {
        RegisteredUser u = userManager.findByEmail(email);
        Integer streetN = 0;
        try {
            streetN = Integer.valueOf(json.getString("streetNumber"));
        }
        catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid street number!").build();
        }

        if (u == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("User not found!").build();
        }

        Address addr = u.getAddress();
        addr.setStreetNumber(streetN);
        u.setAddress(addr);
        userManager.save(u);

        return Response.status(Response.Status.OK).entity(u).build();
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/address/postCode")
    @RolesAllowed({"admin", "user", "employee"})
    public Response setPostCode(JsonObject json) {

        String psc;
        RegisteredUser u = userManager.findByEmail(securityContext.getUserPrincipal().getName());
        try {
            psc = json.getString("postCode");
        }
        catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid request!").build();
        }

        if (u == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("You are not logged in! How could this happen!").build();
        }

        Address addr = u.getAddress();
        addr.setPostCode(psc);
        u.setAddress(addr);
        userManager.save(u);

        return Response.status(Response.Status.OK).entity(u).build();
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/address/{email}/postCode")
    @RolesAllowed({"admin"})
    public Response setPostCode(@PathParam("email") String email, JsonObject json) {
        String psc;
        RegisteredUser u = userManager.findByEmail(email);
        try {
            psc = json.getString("postCode");
        }
        catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid request!").build();
        }

        if (u == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("User not found!").build();
        }

        Address addr = u.getAddress();
        addr.setPostCode(psc);
        u.setAddress(addr);
        userManager.save(u);

        return Response.status(Response.Status.OK).entity(u).build();
    }
    

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/firstname")
    @RolesAllowed({"admin", "user", "employee"})
    public Response getFirstname() {

        RegisteredUser u = userManager.findByEmail(securityContext.getUserPrincipal().getName());

        if (u == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("You are not logged in! How could this happen!").build();
        }

        String firstname = u.getFirstname();

        return Response.status(Response.Status.OK).entity(firstname).build();
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/firstname")
    @RolesAllowed({"admin", "user", "employee"})
    public Response firstname(JsonObject json) {

        RegisteredUser u = userManager.findByEmail(securityContext.getUserPrincipal().getName());
        String fn;
        try {
            fn = json.getString("firstname");
        }
        catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid request!").build();
        }

        if (u == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("You are not logged in! How could this happen!").build();
        }

        u.setFirstname(fn);
        userManager.save(u);

        return Response.status(Response.Status.OK).entity(u).build();
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/firstname/{email}")
    @RolesAllowed({"admin"})
    public Response firstname(@PathParam("email") String email, JsonObject json) {
        
        RegisteredUser u = userManager.findByEmail(email);
        String fn;
        try {
            fn = json.getString("firstname");
        }
        catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid request!").build();
        }

        if (u == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("User not found!").build();
        }

        u.setFirstname(fn);
        userManager.save(u);

        return Response.status(Response.Status.OK).entity(u).build();
    }
    

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/surname")
    @RolesAllowed({"admin", "user", "employee"})
    public Response surname(JsonObject json) {

        RegisteredUser u = userManager.findByEmail(securityContext.getUserPrincipal().getName());
        String sn;
        try { 
            sn = json.getString("surname");
        }
        catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid request!").build();
        }

        if (u == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("You are not logged in! How could this happen!").build();
        }

        u.setSurname(sn);
        userManager.save(u);

        return Response.status(Response.Status.OK).entity(u).build();
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/surname/{email}")
    @RolesAllowed({"admin"})
    public Response surname(@PathParam("email") String email, JsonObject json) {
        
        RegisteredUser u = userManager.findByEmail(email);
        String sn;
        try {
            sn = json.getString("surname");
        }
        catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid request!").build();
        }

        if (u == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("User not found!").build();
        }

        u.setFirstname(sn);
        userManager.save(u);

        return Response.status(Response.Status.OK).entity(u).build();
    }
    

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/surname")
    @RolesAllowed({"admin", "user", "employee"})
    public Response getSurname() {

        RegisteredUser u = userManager.findByEmail(securityContext.getUserPrincipal().getName());

        if (u == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("You are not logged in! How could this happen!").build();
        }

        String surname = u.getSurname();

        return Response.status(Response.Status.OK).entity(surname).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/phone")
    @RolesAllowed({"admin", "user", "employee"})
    public Response getPhone() {

        RegisteredUser u = userManager.findByEmail(securityContext.getUserPrincipal().getName());

        if (u == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("You are not logged in! How could this happen!").build();
        }

        String phone = u.getPhone();

        return Response.status(Response.Status.OK).entity(phone).build();
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/phone")
    @RolesAllowed({"admin", "user", "employee"})
    public Response setPhone(JsonObject json) {

        String phone;
        RegisteredUser u = userManager.findByEmail(securityContext.getUserPrincipal().getName());
        try { 
            phone = json.getString("phone");
        }
        catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid request!").build();
        }

        if (u == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("You are not logged in! How could this happen!").build();
        }

        u.setPhone(phone);
        userManager.save(u);

        return Response.status(Response.Status.OK).entity(u).build();
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/phone/{email}")
    @RolesAllowed({"admin"})
    public Response phone(@PathParam("email") String email, JsonObject json) {
        
        RegisteredUser u = userManager.findByEmail(email);
        String phone;
        try {
            phone = json.getString("phone");
        }
        catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid request!").build();
        }

        if (u == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("User not found!").build();
        }

        u.setFirstname(phone);
        userManager.save(u);

        return Response.status(Response.Status.OK).entity(u).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/email")
    @RolesAllowed({"admin", "user", "employee"})
    public Response getEmail() {

        RegisteredUser u = userManager.findByEmail(securityContext.getUserPrincipal().getName());

        if (u == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("You are not logged in! How could this happen!").build();
        }

        String email = u.getEmail();

        return Response.status(Response.Status.OK).entity(email).build();
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

    /* Special admin endpoints to get information about other users */
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

    /* Special admin endpoints to get information about other users */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/firstname/{email}")
    @RolesAllowed({"admin"})
    public Response getFirstname(@PathParam("email") String email) {

        RegisteredUser u = userManager.findByEmail(email);

        if (u == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("User doesn't exist!").build();
        }

        String firstname = u.getFirstname();

        return Response.status(Response.Status.OK).entity(firstname).build();
    }

    /* Special admin endpoints to get information about other users */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/surname/{email}")
    @RolesAllowed({"admin"})
    public Response getSurname(@PathParam("email") String email) {

        RegisteredUser u = userManager.findByEmail(email);

        if (u == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("User doesn't exist!").build();
        }

        String surname = u.getSurname();

        return Response.status(Response.Status.OK).entity(surname).build();
    }

    /* Special admin endpoints to get information about other users */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/phone/{email}")
    @RolesAllowed({"admin"})
    public Response getPhone(@PathParam("email") String email) {

        RegisteredUser u = userManager.findByEmail(email);

        if (u == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("User doesn't exist!").build();
        }

        String phone = u.getPhone();

        return Response.status(Response.Status.OK).entity(phone).build();
    }

    /* Special admin endpoints to get information about other users */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/email/{email}")
    @RolesAllowed({"admin"})
    public Response getEmail(@PathParam("email") String email) {

        RegisteredUser u = userManager.findByEmail(email);

        if (u == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("User doesn't exist!").build();
        }

        String em = u.getEmail();

        return Response.status(Response.Status.OK).entity(em).build();
    }
}
