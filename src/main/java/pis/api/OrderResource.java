/**
 * PIS Projekt 2024
 * OrderResource.java
 * @author Filip Brna <xbrnaf00>
 * @author Lukas Petr <xpetrl06>
 */

package pis.api;

import java.util.LinkedList;
import java.util.List;

import pis.data.Order;
import pis.data.OrderItem;
import pis.data.OrderStatus;
import pis.data.ProductDescription;
import pis.data.RegisteredUser;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import pis.api.dto.CreateOrderDTO;
import pis.api.dto.CreateOrderItemDTO;
import pis.api.dto.OrderPreviewDTO;
import pis.api.dto.UpdateOrderDTO;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import pis.service.OrderManager;
import pis.service.ProductDescriptionManager;
import pis.service.RegisteredUserManager;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

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

    @Context
    private SecurityContext securityContext;

    @Inject
    private RegisteredUserManager registeredUserManager;

    /**
     * Returns list of all Orders.
     */
    @GET
    @RolesAllowed({ "admin", "employee" })
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public List<OrderPreviewDTO> getOrders() {
        List<Order> orders = orderManager.findAll();
        return orders.stream().map(o -> OrderPreviewDTO.createFromOrder(o)).toList();
    }

    /**
     * Returns list of all Orders of user found by email
     */
    @GET
    @Path("/byEmail/{email}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<OrderPreviewDTO> getOrdersByEmail(@PathParam("email") String email) {
        // Find orders where the email is set in user info
        List<Order> orders = new LinkedList<Order>();
        orders.addAll(orderManager.findByEmail(email));
        // Also add orders which was done by registered user with given email
        RegisteredUser user = registeredUserManager.findByEmail(email);
        if (user != null) {
            orders.addAll(user.getOrders());
        }
        // If the user did not chagend the email for the order there will be
        // duplicates, therefore we are removing them.
        orders = orders.stream().distinct().toList();
        return orders.stream().map(o -> OrderPreviewDTO.createFromOrder(o)).toList();
    }

    /**
     * Returns list of all Orders for logged in user.
     */
    @GET
    @RolesAllowed({ "admin", "user", "employee" })
    @Produces(MediaType.APPLICATION_JSON)
    public List<OrderPreviewDTO> getOrdersForUser() {
        String userEmail = securityContext.getUserPrincipal().getName();
        RegisteredUser user = registeredUserManager.findByEmail(userEmail);
        return user.getOrders().stream().map(o -> OrderPreviewDTO.createFromOrder(o)).toList();
    }

    /**
     * Returns Order from id.
     * 
     * @param id Id of the Order.
     * @return Order with given id.
     */
    @GET
    @Path("/{id}")
    @Operation(summary = "Returns order", description = "Returns order specified by id")
    @APIResponses(
        value = {
            @APIResponse(responseCode = "200", description = "Order was found.",
                content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = Order.class)
                )
            ),
            @APIResponse(responseCode = "404", description = "Order with given id was not found."
            ),
        }
    )
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOrder(@PathParam("id") long id) {
        Order o = orderManager.find(id);
        if (o != null) {
            return Response.ok().entity(o).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
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

        order = orderManager.save(order);
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
        // returning order id in the body of response
        return Response.ok(order.getId()).build();
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
        return Response.ok().entity("Succesfully updated status for given order ID").build();
    }

}
