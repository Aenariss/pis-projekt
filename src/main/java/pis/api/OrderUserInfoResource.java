/*
 * PIS Projekt 2024
 * OredUserInfoResource.java
 * @autthor Filip Brna <xbrnaf00>
 */

package pis.api;

import java.util.List;

import pis.data.OrderUserInfo;
//import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import pis.service.OrderUserInfoManager;
import jakarta.ws.rs.core.Response;

// TODO: add permisssions

@Path("/orderUserInfo")
public class OrderUserInfoResource {

    @Inject
    private OrderUserInfoManager orderUserInfoManager;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<OrderUserInfo> getOrderUserInfo() {
        return orderUserInfoManager.findAll();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public OrderUserInfo getOrderUserInfo(@PathParam("id") long id) {
        return orderUserInfoManager.find(id);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addOrderUserInfo(OrderUserInfo orderUserInfo) {
        OrderUserInfo orderUserInfoFound = orderUserInfoManager.findByName(orderUserInfo.getFirstname(),
                orderUserInfo.getSurname());
        if (orderUserInfoFound == null) {
            // Save the orderUserInfo object itself, not the null orderUserInfoFound
            OrderUserInfo newOrderUserInfo = orderUserInfoManager.save(orderUserInfo);
            return Response.ok().entity(newOrderUserInfo).build();
        }
        return Response.ok().entity(orderUserInfoFound).build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateOrderUserInfo(@PathParam("id") long id, OrderUserInfo orderUserInfo) {
        OrderUserInfo orderUserInfoFound = orderUserInfoManager.find(id);
        if (orderUserInfoFound != null) {
            orderUserInfoFound.setFirstname(orderUserInfo.getFirstname());
            orderUserInfoFound.setSurname(orderUserInfo.getSurname());
            orderUserInfoFound.setPhone(orderUserInfo.getPhone());
            orderUserInfoFound.setEmail(orderUserInfo.getEmail());
            orderUserInfoManager.save(orderUserInfoFound);
            return Response.ok().entity(orderUserInfoFound).build();
        }
        return Response.status(Response.Status.BAD_REQUEST).entity("Error: Order user info with given ID doesnt exist")
                .build();
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteOrderUserInfo(@PathParam("id") long id) {
        OrderUserInfo orderUserInfoFound = orderUserInfoManager.find(id);
        if (orderUserInfoFound != null) {
            orderUserInfoManager.delete(orderUserInfoFound);
            return Response.ok().entity("Succesfully removed the Order User Info").build();
        }
        return Response.status(Response.Status.BAD_REQUEST).entity("Error: Order user info with given ID doesnt exist")
                .build();
    }

    /*
     * @DELETE
     * 
     * @Consumes(MediaType.APPLICATION_JSON)
     * 
     * @Produces(MediaType.APPLICATION_JSON)
     * public Response deleteOrderUserInfo(OrderUserInfo orderUserInfo) {
     * OrderUserInfo orderUserInfoFound =
     * orderUserInfoManager.findByName(orderUserInfo.getFirstname(),
     * orderUserInfo.getSurname());
     * if (orderUserInfoFound != null) {
     * orderUserInfoManager.delete(orderUserInfoFound);
     * return
     * Response.ok().entity("Succesfully removed the Order User Info").build();
     * }
     * return Response.status(Response.Status.BAD_REQUEST)
     * .entity("Error: Order user info with given data doesnt exist")
     * .build();
     * }
     */

}