/**
 * PIS Projekt 2024
 * UsersControl.java
 * @author Vojtech Fiala <xfiala61>
 */

package pis.api;

import pis.data.RegisteredUser;
import pis.service.RegisteredUserManager;

import java.util.List;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.SecurityContext;

@Path("/users")
public class UsersControl {
    @Inject
	private RegisteredUserManager userManager;

    @Context
    private SecurityContext securityContext;

    /**
     * Returns a list of all current employees
     */
    @GET
    @Path("/getEmployees")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("admin")
    public List<RegisteredUser> getAllEmployees() {
        return userManager.findEmployees();
    }

    /**
     * Returns a list of all current users - not suited for production
     */
    @GET
    @Path("/getUsers")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("admin")
    public List<RegisteredUser> getAllUsers() {
        return userManager.findAll();
    }

    /**
     * Returns a list of all employees whose surname contains text
     */
    @GET
    @Path("/getEmployeesByName/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("admin")
    public List<RegisteredUser> getEmployeeQuery(@PathParam("name") String name) {
        return userManager.getEmployeeQuery(name);
    }

    /**
     * Returns a list of all users whose surname contains text
     */
    @GET
    @Path("/getUsersByName/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("admin")
    public List<RegisteredUser> getUserQuery(@PathParam("name") String name) {
        return userManager.getUserQuery(name);
    }

    /**
     * Returns a list of all employees whose email matches given query
     */
    @GET
    @Path("/getEmployeesByEmail/{email}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("admin")
    public List<RegisteredUser> getEmployeeByEmailQuery(@PathParam("email") String email) {
        return userManager.getEmployeeByEmailQuery(email);
    }

    /**
     * Returns a list of all users whose email matches given query
     */
    @GET
    @Path("/getUsersByEmail/{email}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("admin")
    public List<RegisteredUser> getUserByEmailQuery(@PathParam("email") String email) {
        return userManager.getUserByEmailQuery(email);
    }
}
