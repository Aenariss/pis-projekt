/**
 * PIS Projekt 2024
 * SetRole.java
 * @author Vojtech Fiala <xfiala61>
 */

package pis.api;

import pis.data.RegisteredUser;
import pis.api.dto.RoleRequest;

import pis.service.RegisteredUserManager;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * REST API for working with user roles
 */
@Path("/setRole")
public class SetRole {

    @Inject
	private RegisteredUserManager userManager;

    /**
     * Private method for setting the user's role
     * @param r Request, must be valid
     * @param role can be either "user", "employee" or "admin"
     * @return Response
     */
    private Response setRoleBody(RoleRequest r, String role) {
        if (!r.valid()) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid user!").build();
        }
        
        RegisteredUser u = userManager.findByEmail(r.getEmail());

        if (u == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Unknown user!").build();
        }

        u.setRole(role);
        userManager.save(u); // save for persistence
        String resp_text = u.getEmail() + " is now a " + role;
        return Response.status(Response.Status.OK).entity(resp_text).build();
    }

    /**
     * Set the client's role as regular user
     * @param r Request for role change, has to be valid
     * @return Response
     */
    @POST
    @Path("/user")
    @RolesAllowed({"admin"})
    @Consumes(MediaType.APPLICATION_JSON)
    public Response setUser(RoleRequest r) {

        return this.setRoleBody(r, "user");
    }

    /**
     * Set the client's role as employee
     * @param r Request for role change, has to be valid
     * @return Response
     */
    @POST
    @Path("/employee")
    @RolesAllowed({"admin"})
    @Consumes(MediaType.APPLICATION_JSON)
    public Response setEmployee(RoleRequest r) {

        return this.setRoleBody(r, "employee");
    }

    /**
     * Set the client's role as admin
     * @param r Request for role change, has to be valid
     * @return Response
     */
    @POST
    @Path("/admin")
    @RolesAllowed({"admin"})
    @Consumes(MediaType.APPLICATION_JSON)
    public Response setAdmin(RoleRequest r) {

        return this.setRoleBody(r, "admin");
    }
}