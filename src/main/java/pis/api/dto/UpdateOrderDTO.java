/**
 * Class for containing info about new created order.
 * @author Lukas Petr
 */

package pis.api.dto;

import java.util.List;

import pis.data.Order;
import pis.data.OrderStatus;

public class UpdateOrderDTO {
    private OrderStatus status;
    private long id;

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

}