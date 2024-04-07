/**
 * PIS Projekt 2024
 * SetRole.java
 * @author Vojtech Fiala <xfiala61>
 */

 package pis.api;

import pis.data.RegisteredUser;
import pis.data.RoleRequest;
 
import pis.service.RegisteredUserManager;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/setRole")
public class SetRole {

    @Inject
	private RegisteredUserManager userManager;

    /**
     * Set the client's role as regular user
     */
    @POST
    @Path("/user")
    @RolesAllowed({"admin"})
    @Consumes(MediaType.APPLICATION_JSON)
    public Response setUser(RoleRequest r) {
        
        RegisteredUser u = userManager.findByEmail(r.getEmail());

        if (u == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Unknown user!").build();
        }

        u.setRole("user");
        userManager.save(u);
        String resp_text = u.getEmail() + " is now an user";
        return Response.status(Response.Status.OK).entity(resp_text).build();
    }

    /**
     * Set the client's role as employee
     */
    @POST
    @Path("/employee")
    @RolesAllowed({"admin"})
    @Consumes(MediaType.APPLICATION_JSON)
    public Response setEmployee(RoleRequest r) {
        
        RegisteredUser u = userManager.findByEmail(r.getEmail());

        if (u == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Unknown user!").build();
        }

        u.setRole("employee");
        userManager.save(u);
        String resp_text = u.getEmail() + " is now an employee";
        return Response.status(Response.Status.OK).entity(resp_text).build();
    }

    /**
     * Set the client's role as adm,in
     */
    @POST
    @Path("/admin")
    @RolesAllowed({"admin"})
    @Consumes(MediaType.APPLICATION_JSON)
    public Response setAdmin(RoleRequest r) {
        
        RegisteredUser u = userManager.findByEmail(r.getEmail());

        if (u == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Unknown user!").build();
        }

        u.setRole("admin");
        userManager.save(u);
        String resp_text = u.getEmail() + " is now an admin";
        return Response.status(Response.Status.OK).entity(resp_text).build();
    }

    /**
     * Set the test@test.cz user's role to admin
     */
    @POST
    @Path("/testUser")
    public Response testAdmin() {
        
        RegisteredUser u = userManager.findByEmail("test@test.cz");

        if (u == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("test user not registered!").build();
        }

        u.setRole("admin");
        userManager.save(u);
        String resp_text = u.getEmail() + " is now an admin";
        return Response.status(Response.Status.OK).entity(resp_text).build();
    }


}
