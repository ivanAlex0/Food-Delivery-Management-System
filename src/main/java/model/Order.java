package model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    @JsonProperty("orderId")
    public Integer orderId;
    @JsonProperty("clientId")
    public Integer clientId;
    @JsonProperty("orderDate")
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    public Date orderDate;
    @JsonProperty("quantity")
    public Integer quantity;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return orderId.equals(order.orderId) &&
                clientId.equals(order.clientId) &&
                orderDate.equals(order.orderDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, clientId, orderDate);
    }
}
