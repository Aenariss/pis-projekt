/**
 * DTO for sending basic information (not details) about orders.
 * @author Lukas Petr <xpetrl06>
 */
package pis.api.dto;

import java.time.LocalDateTime;

import pis.data.Order;
import pis.data.OrderStatus;

public class OrderPreviewDTO {
    private long id;
    private OrderStatus status;
    private LocalDateTime creationDate;
    private double totalPrice;
    /** Email from userinfo from the order. */
    private String email;
    public OrderPreviewDTO(long id, OrderStatus status, LocalDateTime creationDate, double totalPrice,
                           String email) {
        this.id = id;
        this.status = status;
        this.creationDate = creationDate;
        this.totalPrice = totalPrice;
        this.email = email;
    }
    static public OrderPreviewDTO createFromOrder(Order o) {
        OrderPreviewDTO dto = new OrderPreviewDTO(
            o.getId(), o.getStatus(), o.getCreationDate(), o.getTotalPrice(), o.getOrderUserInfo().getEmail());
        return dto;
    }
    public String getEmail() {
        return email;
    }
    public long getId() {
        return id;
    }
    public OrderStatus getStatus() {
        return status;
    }
    public LocalDateTime getCreationDate() {
        return creationDate;
    }
    public double getTotalPrice() {
        return totalPrice;
    }
}
