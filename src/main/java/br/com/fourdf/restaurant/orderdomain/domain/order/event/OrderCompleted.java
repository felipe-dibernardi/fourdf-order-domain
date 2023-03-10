package br.com.fourdf.restaurant.orderdomain.domain.order.event;

import br.com.fourdf.restaurant.observer.event.Observable;
import br.com.fourdf.restaurant.orderdomain.domain.order.Order;

import java.util.UUID;

public class OrderCompleted implements Observable {

    private UUID orderId;

    private OrderCompleted(Order order) {
        this.orderId = order.getOrderId();
    }

    public static OrderCompleted createEvent(Order order) {
        return new OrderCompleted(order);
    }

    public UUID getOrderId() {
        return orderId;
    }
}
