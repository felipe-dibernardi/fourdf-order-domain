package br.com.fourdf.restaurant.orderdomain.domain.order.exception;

public class OrderNotConfirmedException extends RuntimeException {

    public OrderNotConfirmedException(String message) {
        super(message);
    }
}
