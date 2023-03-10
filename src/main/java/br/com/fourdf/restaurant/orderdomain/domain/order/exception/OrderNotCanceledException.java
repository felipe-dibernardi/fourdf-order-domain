package br.com.fourdf.restaurant.orderdomain.domain.order.exception;

public class OrderNotCanceledException extends RuntimeException {

    public OrderNotCanceledException(String message) {
        super(message);
    }
}
