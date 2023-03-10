package br.com.fourdf.restaurant.orderdomain.domain.order.exception;

public class OrderNotPlacedException extends RuntimeException {

    public OrderNotPlacedException(String message) {
        super(message);
    }
}
