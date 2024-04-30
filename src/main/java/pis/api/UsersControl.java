/**
 * PIS Projekt 2024
 * UsersControl.java
 * @author Vojtech Fiala <xfiala61>
 */

package pis.api;

import pis.api.dto.UserOverviewDTO;
import pis.data.RegisteredUser;
import pis.service.RegisteredUserManager;

import java.util.List;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.SecurityContext;

/**
 * REST API for managing the users
 */
@Path("/users")
public class UsersControl {
    @Inject
	private RegisteredUserManager userManager;

    @Context
    private SecurityContext securityContext;

    /**
     * Returns a list of all current employees
     * @return A list with all users with the 'employee' role
     */
    @GET
    @Path("/getEmployees")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("admin")
    public List<UserOverviewDTO> getAllEmployees() {
        List<RegisteredUser> users = userManager.findEmployees();
        return users.stream().map(user -> new UserOverviewDTO(user)).toList();
    }

    /**
     * Returns a list of all employees whose surname matches given query
     * @param name The surname of the employee you want to find
     * @return List of users whose surname matches the query
     */
    @GET
    @Path("/getEmployeesByName/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("admin")
    public List<UserOverviewDTO> getEmployeeQuery(@PathParam("name") String name) {
        List<RegisteredUser> users = userManager.getEmployeeQuery(name);
        return users.stream().map(user -> new UserOverviewDTO(user)).toList();
    }

    /**
     * Returns a list of all users whose surname matches text
     * @param name Surname of the user
     * @return List of users matching the query
     */
    @GET
    @Path("/getUsersByName/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("admin")
    public List<UserOverviewDTO> getUserQuery(@PathParam("name") String name) {
        List<RegisteredUser> users = userManager.getUserQuery(name);
        return users.stream().map(user -> new UserOverviewDTO(user)).toList();
    }

    /**
     * Returns a list of all employees whose email matches given query
     * @param email Employee's email
     * @return List containing employees with email matching the query
     */
    @GET
    @Path("/getEmployeesByEmail/{email}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("admin")
    public List<UserOverviewDTO> getEmployeeByEmailQuery(@PathParam("email") String email) {
        List<RegisteredUser> users = userManager.getEmployeeByEmailQuery(email);
        return users.stream().map(user -> new UserOverviewDTO(user)).toList();
    }

    /**
     * Returns a list of all users whose email matches given query
     * @param email User's email
     * @return List of all user's whose email matches the given query
     */
    @GET
    @Path("/getUsersByEmail/{email}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("admin")
    public List<UserOverviewDTO> getUserByEmailQuery(@PathParam("email") String email) {
        List<RegisteredUser> users = userManager.getUserByEmailQuery(email);
        return users.stream().map(user -> new UserOverviewDTO(user)).toList();
    }
}
