package br.com.fourdf.restaurant.orderdomain.domain.order.event;

import br.com.fourdf.restaurant.observer.event.Observable;
import br.com.fourdf.restaurant.orderdomain.domain.order.Order;
import br.com.fourdf.restaurant.orderdomain.domain.order.OrderedProduct;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public class OrderPlaced implements Observable {

    private UUID orderId;

    private Set<OrderedProduct> orderedProducts;

    private LocalDateTime creationTimestamp;

    private UUID establishmentId;

    private UUID customerId;

    private OrderPlaced(Order order) {
        this.orderId = order.getOrderId();
        this.orderedProducts = order.getOrderedProducts();
        this.creationTimestamp = order.getCreationTimestamp();
        this.establishmentId = order.getEstablishmentId();
        this.customerId = order.getCustomerId();
    }

    public static OrderPlaced createEvent(Order order) {
        return new OrderPlaced(order);
    }

    public UUID getOrderId() {
        return orderId;
    }

    public Set<OrderedProduct> getOrderedProducts() {
        return orderedProducts;
    }

    public UUID getEstablishmentId() {
        return establishmentId;
    }

    public UUID getCustomerId() {
        return customerId;
    }
}
