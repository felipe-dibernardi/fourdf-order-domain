package br.com.fourdf.restaurant.orderdomain.domain.order;

import br.com.fourdf.restaurant.orderdomain.domain.order.enums.Status;
import br.com.fourdf.restaurant.orderdomain.domain.order.exception.OrderNotCanceledException;
import br.com.fourdf.restaurant.orderdomain.domain.order.exception.OrderNotConfirmedException;
import br.com.fourdf.restaurant.orderdomain.helper.MockOrderFactory;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class OrderTest {

    @Test
    public void shouldCalculateOrderTotalValue() {

        Order order = MockOrderFactory.createOrderWithDefaultValues();

        BigDecimal totalValue = order.calculateTotalValue();

        assertThat(totalValue).isEqualByComparingTo(new BigDecimal("20.50"));
    }

    @Test
    public void shouldConfirmOrder() {
        Order order = MockOrderFactory.createFullOrder(Status.PLACED);

        order.confirm();

        assertThat(order.getStatus()).isEqualByComparingTo(Status.CONFIRMED);
        assertThat(order.getUpdateTimestamp()).isNotEqualTo(order.getCreationTimestamp());
    }

    @Test
    public void shouldCancelOrder() {
        Order order = MockOrderFactory.createFullOrder(Status.PLACED);

        order.cancel();

        assertThat(order.getStatus()).isEqualByComparingTo(Status.CANCELED);
        assertThat(order.getUpdateTimestamp()).isNotEqualTo(order.getCreationTimestamp());
    }

    @Test
    public void shouldNotCompleteWhenTryingToCompleteOrderWithoutConfirmedState() {
        Order order = MockOrderFactory.createFullOrder(Status.COMPLETED);

        assertThatThrownBy(order::confirm)
                .hasMessage("Unable to confirm order. Order status: COMPLETED")
                .isInstanceOf(OrderNotConfirmedException.class);

        assertThatThrownBy(order::cancel)
                .hasMessage("Unable to cancel order. Order status: COMPLETED")
                .isInstanceOf(OrderNotCanceledException.class);
    }

    @Test
    public void shouldNotCancelWhenTryingToCancelOrderWithCanceledState() {
        Order order = MockOrderFactory.createFullOrder(Status.CANCELED);

        assertThatThrownBy(order::cancel)
                .hasMessage("Unable to cancel order. Order status: CANCELED")
                .isInstanceOf(OrderNotCanceledException.class);
    }

    @Test
    public void shouldNotCancelWhenTryingToCancelOrderWithCompletedState() {
        Order order = MockOrderFactory.createFullOrder(Status.COMPLETED);

        assertThatThrownBy(order::cancel)
                .hasMessage("Unable to cancel order. Order status: COMPLETED")
                .isInstanceOf(OrderNotCanceledException.class);
    }

    @Test
    public void shouldNotConfirmWhenTryingToConfirmOrderWithConfirmedState() {
        Order order = MockOrderFactory.createFullOrder(Status.CONFIRMED);

        assertThatThrownBy(order::confirm)
                .hasMessage("Unable to confirm order. Order status: CONFIRMED")
                .isInstanceOf(OrderNotConfirmedException.class);
    }

}
