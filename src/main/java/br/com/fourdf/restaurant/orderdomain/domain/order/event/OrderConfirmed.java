package br.com.fourdf.restaurant.orderdomain.domain.order.event;

import br.com.fourdf.restaurant.observer.event.Observable;
import br.com.fourdf.restaurant.orderdomain.domain.order.Order;

import java.util.UUID;

public class OrderConfirmed implements Observable {

    private UUID orderId;

    private OrderConfirmed(Order order) {
        this.orderId = order.getOrderId();
    }

    public static OrderConfirmed createEvent(Order order) {
        return new OrderConfirmed(order);
    }

    public UUID getOrderId() {
        return orderId;
    }
}
