package br.com.fourdf.restaurant.orderdomain.domain.order;

import br.com.fourdf.restaurant.orderdomain.port.spi.repository.OrderRepositoryPort;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class MockOrderRepositoryPort implements OrderRepositoryPort {

    List<Order> orders;

    public MockOrderRepositoryPort() {
        this.orders = new ArrayList<>();
    }

    @Override
    public Optional<Order> find(UUID orderId) {
        return orders.stream().filter(order -> order.getOrderId().equals(orderId)).findAny();
    }

    @Override
    public List<Order> findAll() {
        return orders;
    }

    @Override
    public Order insert(Order order) {
        if (!order.hasId()) {
            order.setOrderId(UUID.randomUUID());
        }
        orders.add(order);
        return order;
    }

    @Override
    public Order update(Order order) {
        remove(order.getOrderId());
        orders.add(order);
        return order;
    }

    @Override
    public void remove(UUID orderId) {
        Optional<Order> orderToRemove = find(orderId);
        orderToRemove.ifPresent(order -> orders.remove(order));
    }
}
