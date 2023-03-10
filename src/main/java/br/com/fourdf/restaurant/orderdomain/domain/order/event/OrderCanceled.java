package br.com.fourdf.restaurant.orderdomain.domain.order.event;

import br.com.fourdf.restaurant.observer.event.Observable;
import br.com.fourdf.restaurant.orderdomain.domain.order.Order;

import java.util.UUID;

public class OrderCanceled implements Observable {

    private UUID orderId;

    private OrderCanceled(Order order) {
        this.orderId = order.getOrderId();
    }

    public static OrderCanceled createEvent(Order order) {
        return new OrderCanceled(order);
    }

    public UUID getOrderId() {
        return orderId;
    }
}
