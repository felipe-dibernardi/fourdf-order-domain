package br.com.fourdf.restaurant.orderdomain.port.spi.producer;

import br.com.fourdf.restaurant.observer.listener.Observer;
import br.com.fourdf.restaurant.orderdomain.domain.order.event.OrderCanceled;

public interface OrderCanceledProducerPort extends Observer<OrderCanceled> {

}
