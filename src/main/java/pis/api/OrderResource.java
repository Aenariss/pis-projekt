/**
 * PIS Projekt 2024
 * OrderResource.java
 * @author Filip Brna <xbrnaf00>
 */

package pis.api;

import java.util.List;

import pis.data.Order;
import pis.data.OrderItem;
import pis.data.OrderStatus;
import pis.data.OrderUserInfo;
import pis.data.ProductDescription;
import pis.data.RegisteredUser;
import pis.data.UserAddress;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import pis.api.dto.AddressDTO;
import pis.api.dto.CreateOrderDTO;
import pis.api.dto.CreateOrderItemDTO;
import pis.api.dto.OrderUserInfoDTO;
import pis.api.dto.UpdateOrderDTO;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import pis.service.OrderManager;
import pis.service.OrderUserInfoManager;
import pis.service.ProductDescriptionManager;
import pis.service.RegisteredUserManager;
import jakarta.ws.rs.core.Response;
import pis.api.dto.Address;
import jakarta.ws.rs.core.SecurityContext;

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
    private ProductDescriptionManager productDescriptionManager;

    @Inject
    private OrderUserInfoManager orderUserInfoManager;

    @Context
    private SecurityContext securityContext;

    @Inject
    private RegisteredUserManager registeredUserManager;

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

    /**
     * Creates new order.
     * 
     * @param dto DTO with order data.
     * @return Response status.
     */
    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createNewOrder(CreateOrderDTO dto) {
        List<CreateOrderItemDTO> items = dto.getItems();

        for (CreateOrderItemDTO item : items) {
            ProductDescription product = productDescriptionManager.find(item.getId());
            int amount = item.getAmount();
            if (amount <= 0) {
                return Response.status(Response.Status.BAD_REQUEST).entity("Error: Amount cannot be negative or zero")
                        .build();
            }

            if (product == null) {
                return Response.status(Response.Status.NOT_FOUND).entity("Error: Product not found").build();
            }

            if (product.getAvailableQuantity() < amount) {
                return Response.status(Response.Status.BAD_REQUEST).entity("Error: Not enough items in stock").build();
            }
        }

        // Create order
        Order order = new Order(OrderStatus.IN_PROGRESS, dto.getOrderUserInfo(), dto.getDeliveryAddress().getState(),
                dto.getDeliveryAddress().getTown(), dto.getDeliveryAddress().getStreet(),
                dto.getDeliveryAddress().getStreetNumber(), dto.getDeliveryAddress().getPostCode(),
                dto.getUserAddress().getState(), dto.getUserAddress().getTown(), dto.getUserAddress().getStreet(),
                dto.getUserAddress().getStreetNumber(), dto.getUserAddress().getPostCode());

        for (CreateOrderItemDTO item : items) {
            ProductDescription product = productDescriptionManager.find(item.getId());
            OrderItem orderItem = new OrderItem(item.getAmount(), product);
            int amount = item.getAmount();
            order.addOrderItem(orderItem);
            product.setAvailableQuantity(product.getAvailableQuantity() - amount);
            // Need to persist
            productDescriptionManager.save(product);
        }

        // Check if the order did user which is logged in.
        // If it was logged in user then the securityContext will contain his email.
        if (securityContext.getUserPrincipal() != null) {
            String userEmail = securityContext.getUserPrincipal().getName();
            // Getting user who ordered the items
            RegisteredUser user = registeredUserManager.findByEmail(userEmail);
            user.addOrder(order);
            // Saving user for the reason his updated orders are saved in DB.
            registeredUserManager.save(user);
        }

        // Save order
        orderManager.save(order);
        return Response.ok().build();
    }

    @PUT
    @Path("/update")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ "admin", "employee" })
    public Response updateOrderStatus(UpdateOrderDTO dto) {
        Order o = orderManager.find(dto.getId());
        System.out.println(dto.getId());
        if (o == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Error: Order not found").build();
        }
        o.setStatus(dto.getStatus());
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
