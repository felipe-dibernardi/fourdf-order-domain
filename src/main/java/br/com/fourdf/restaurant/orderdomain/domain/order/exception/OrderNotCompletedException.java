package br.com.fourdf.restaurant.orderdomain.domain.order.exception;

public class OrderNotCompletedException extends RuntimeException {
    public OrderNotCompletedException(String message) {
        super(message);
    }
}
