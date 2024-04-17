/**
 * Class used for representing item in new order.
 * @author Lukas Petr
 */
package pis.api.dto;

public class CreateOrderItemDTO {
    private long id;
    private int amount;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}