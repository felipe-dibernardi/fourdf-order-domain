package br.com.fourdf.restaurant.orderdomain.port.spi.producer;

import br.com.fourdf.restaurant.observer.listener.Observer;
import br.com.fourdf.restaurant.orderdomain.domain.order.event.OrderCompleted;

public interface OrderCompletedProducerPort extends Observer<OrderCompleted> {
}
