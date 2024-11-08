/**
 * Class for containing info about new created order.
 * @author Lukas Petr
 */

package pis.api.dto;

import java.util.List;

import pis.data.OrderUserInfo;

public class CreateOrderDTO {
    private List<CreateOrderItemDTO> items;
    private AddressDTO userAddress;
    private AddressDTO deliveryAddress;
    private OrderUserInfo orderUserInfo;

    public List<CreateOrderItemDTO> getItems() {
        return items;
    }

    public void setItems(List<CreateOrderItemDTO> items) {
        this.items = items;
    }

    public AddressDTO getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(AddressDTO userAddress) {
        this.userAddress = userAddress;
    }

    public AddressDTO getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(AddressDTO deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public OrderUserInfo getOrderUserInfo() {
        return orderUserInfo;
    }

    public void setOrderUserInfo(OrderUserInfo orderUserInfo) {
        this.orderUserInfo = orderUserInfo;
    }
}