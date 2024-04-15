/**
 * PIS Projekt 2024
 * OrderResource.java
 * @author Filip Brna <xbrnaf00>
 */

package pis.api;

import java.util.List;

import pis.data.Order;
import pis.data.OrderStatus;
import pis.data.OrderUserInfo;
import pis.data.UserAddress;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import pis.api.dto.AddressDTO;
import pis.api.dto.CreateOrderDTO;
import pis.api.dto.UserDTO;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import pis.service.OrderManager;
import pis.service.OrderUserInfoManager;
import jakarta.ws.rs.core.Response;
import pis.api.dto.Address;

//TODO: check permissions

/**
 * REST API resource for working with Orders.
 */
@Path("/order")
@PermitAll
public class OrderResource {
    @Inject
    private OrderManager orderManager;

    @Inject
    private OrderUserInfoManager orderUserInfoManager;

    /**
     * Returns list of all Orders.
     */
    @GET
    @RolesAllowed("admin")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Order> getOrders() {
        return orderManager.findAll();
    }

    /**
     * Returns Order from id.
     * 
     * @param id Id of the Order.
     * @return Order with given id.
     */
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Order getOrder(@PathParam("id") long id) {
        return orderManager.find(id);
    }

    /**
     * Adds new order.
     * 
     * @param order Order to be added.
     * @return Response status.
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addOrder(Order order) {
        // check if order already exists
        Order o = orderManager.find(order.getId());
        if (o == null) {
            o = orderManager.save(order);
            return Response.ok(o).entity(o).build();
        }
        return Response.status(Response.Status.CONFLICT).entity("Error: Order already exists").build();
    }

    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createNewOrder(CreateOrderDTO dto) {
        // TODO
        // Add items to order (find product description by id)
        // check available quantity
        // Create order item connected to ProductItem with quantity set to amount

        // Check if the order did user which is logged in
        // Probably something like this could be used
        // RegisteredUser u =
        // userManager.findByEmail(securityContext.getUserPrincipal().getName());

        // Could by used - depends how we want to do it
        // UserDTO userDTO = dto.getUserInfo();
        // OrderUserInfo userInfo = new OrderUserInfo(
        // userDTO.getFirstname(),
        // userDTO.getSurname(),
        // userDTO.getPhone(),
        // userDTO.getEmail()
        // );
        // AddressDTO deliveryAddressDTO = dto.getDeliveryAddress();
        // Address deliveryAddress = new Address(
        // deliveryAddressDTO.getState(),
        // deliveryAddressDTO.getTown(),
        // deliveryAddressDTO.getStreet(),
        // deliveryAddressDTO.getStreetNumber(),
        // deliveryAddressDTO.getPostCode()
        // );
        // AddressDTO userAddressDTO = dto.getUserAddress();
        // UserAddress userAddress = ...
        return Response.ok().build();
    }

    /**
     * Updates order.
     * 
     * @param id    Id of the Order.
     * @param order Order to be updated.
     * @return Response status.
     */
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ "admin", "employee" })
    public Response updateOrder(@PathParam("id") long id, Order order) {
        Order o = orderManager.find(id);
        if (o == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Error: Order not found").build();
        }
        o.setStatus(order.getStatus());
        orderManager.save(o);
        return Response.ok(o).entity(o).build();
    }

    /**
     * Deletes order.
     * 
     * @param id Id of the Order.
     * @return Response status.
     */
    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ "admin" })
    public Response deleteOrder(@PathParam("id") long id) {
        Order o = orderManager.find(id);
        if (o == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Error: Order not found").build();
        }
        orderManager.delete(o);
        return Response.ok().entity("Order deleted").build();
    }

    /**
     * Delete all orders.
     * 
     * @return Response status.
     */
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ "admin" })
    public Response deleteAllOrders() {
        orderManager.deleteAll();
        return Response.ok().entity("All orders deleted").build();
    }

    @GET
    @Path("/orderUserInfo/{author_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Order> getOrdersByOrderUserInfo(@PathParam("author_id") long author_id) {
        OrderUserInfo orderUserInfo = orderUserInfoManager.find(author_id);
        if (orderUserInfo == null) {
            return null;
        }
        return orderManager.findByOrderUserId(orderUserInfo.getId());
    }

    @PUT
    @Path("{id}/orderUserInfo/{author_id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ "admin" })
    public Response addOrderUserInfoToOrder(@PathParam("id") long orderId, @PathParam("author_id") long userInfoId) {
        // Find the order by ID
        Order order = orderManager.find(orderId);
        if (order == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Error: Order with the given ID doesn't exist")
                    .build();
        }

        // Find the order user info by ID
        OrderUserInfo orderUserInfo = orderUserInfoManager.find(userInfoId);
        if (orderUserInfo == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Error: Order user info with the given ID doesn't exist")
                    .build();
        }

        // Associate the order with the order user info
        order.setOrderUserInfo(orderUserInfo);
        orderManager.save(order);

        return Response.ok().entity(order).build();
    }

}