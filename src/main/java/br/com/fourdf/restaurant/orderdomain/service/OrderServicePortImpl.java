package br.com.fourdf.restaurant.orderdomain.service;

import br.com.fourdf.restaurant.observer.manager.EventManager;
import br.com.fourdf.restaurant.orderdomain.domain.order.Order;
import br.com.fourdf.restaurant.orderdomain.domain.order.event.OrderCanceled;
import br.com.fourdf.restaurant.orderdomain.domain.order.event.OrderCompleted;
import br.com.fourdf.restaurant.orderdomain.domain.order.event.OrderConfirmed;
import br.com.fourdf.restaurant.orderdomain.domain.order.event.OrderPlaced;
import br.com.fourdf.restaurant.orderdomain.domain.order.exception.OrderNotCanceledException;
import br.com.fourdf.restaurant.orderdomain.domain.order.exception.OrderNotCompletedException;
import br.com.fourdf.restaurant.orderdomain.domain.order.exception.OrderNotConfirmedException;
import br.com.fourdf.restaurant.orderdomain.domain.order.exception.OrderNotPlacedException;
import br.com.fourdf.restaurant.orderdomain.port.api.OrderServicePort;
import br.com.fourdf.restaurant.orderdomain.port.spi.repository.OrderRepositoryPort;

import java.util.Optional;
import java.util.UUID;

public class OrderServicePortImpl implements OrderServicePort {

    private final OrderRepositoryPort repositoryPort;

    public OrderServicePortImpl(OrderRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public UUID placeOrder(Order order) {
        try {
            order.place();
            save(order);
            EventManager.notify(OrderPlaced.createEvent(order));
            return order.getOrderId();
        } catch (Exception e) {
            throw new OrderNotPlacedException("Unable to place order: " + e.getMessage());
        }

    }

    @Override
    public void confirmOrder(UUID orderId) {
        Optional<Order> orderOpt = repositoryPort.find(orderId);
        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            try {
                order.confirm();
                save(order);
                EventManager.notify(OrderConfirmed.createEvent(order));
            } catch (Exception e) {
                throw new OrderNotConfirmedException("Unable to confirm Order: " + e.getMessage());
            }
        } else {
            throw new OrderNotConfirmedException("Unable to confirm Order: Order " + orderId + " not found.");
        }
    }

    @Override
    public void cancelOrder(UUID orderId) {
        Optional<Order> orderOpt = repositoryPort.find(orderId);
        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            try {
                order.cancel();
                save(order);
                EventManager.notify(OrderCanceled.createEvent(order));
            } catch (Exception e) {
                throw new OrderNotCanceledException("Unable to cancel Order: " + e.getMessage());
            }
        } else {
            throw new OrderNotCanceledException("Unable to cancel Order: Order " + orderId + " not found.");
        }
    }

    @Override
    public void completeOrder(UUID orderId) {
        Optional<Order> orderOpt = repositoryPort.find(orderId);
        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            try {
                order.complete();
                save(order);
                EventManager.notify(OrderCompleted.createEvent(order));
            } catch (Exception e) {
                throw new OrderNotCompletedException("Unable to complete Order: " + e.getMessage());
            }
        } else {
            throw new OrderNotCompletedException("Unable to complete Order: Order " + orderId + " not found.");
        }
    }

    private Order save(Order order) {
        if (order.hasId()) {
            return repositoryPort.update(order);
        }
        return repositoryPort.insert(order);
    }

}
