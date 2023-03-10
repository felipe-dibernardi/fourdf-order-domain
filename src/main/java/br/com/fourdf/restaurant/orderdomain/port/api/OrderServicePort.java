package br.com.fourdf.restaurant.orderdomain.port.api;


import br.com.fourdf.restaurant.orderdomain.domain.order.Order;

import java.util.UUID;

public interface OrderServicePort {

    UUID placeOrder(Order order);

    void confirmOrder(UUID orderId);

    void cancelOrder(UUID orderId);

    void completeOrder(UUID orderId);
}
