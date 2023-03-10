package br.com.fourdf.restaurant.orderdomain.service;

import br.com.fourdf.restaurant.observer.manager.EventManager;
import br.com.fourdf.restaurant.orderdomain.domain.order.MockOrderRepositoryPort;
import br.com.fourdf.restaurant.orderdomain.domain.order.Order;
import br.com.fourdf.restaurant.orderdomain.domain.order.enums.Status;
import br.com.fourdf.restaurant.orderdomain.domain.order.event.OrderCanceled;
import br.com.fourdf.restaurant.orderdomain.domain.order.event.OrderCompleted;
import br.com.fourdf.restaurant.orderdomain.domain.order.event.OrderConfirmed;
import br.com.fourdf.restaurant.orderdomain.domain.order.event.OrderPlaced;
import br.com.fourdf.restaurant.orderdomain.domain.order.exception.OrderNotCanceledException;
import br.com.fourdf.restaurant.orderdomain.domain.order.exception.OrderNotCompletedException;
import br.com.fourdf.restaurant.orderdomain.domain.order.exception.OrderNotConfirmedException;
import br.com.fourdf.restaurant.orderdomain.domain.order.exception.OrderNotPlacedException;
import br.com.fourdf.restaurant.orderdomain.helper.MockOrderFactory;
import br.com.fourdf.restaurant.orderdomain.port.api.OrderServicePort;
import br.com.fourdf.restaurant.orderdomain.port.spi.producer.OrderCanceledProducerPort;
import br.com.fourdf.restaurant.orderdomain.port.spi.producer.OrderCompletedProducerPort;
import br.com.fourdf.restaurant.orderdomain.port.spi.producer.OrderConfirmedProducerPort;
import br.com.fourdf.restaurant.orderdomain.port.spi.producer.OrderPlacedProducerPort;
import br.com.fourdf.restaurant.orderdomain.port.spi.repository.OrderRepositoryPort;
import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class OrderServicePortImplTest {

    private OrderServicePort orderServicePort;

    private OrderRepositoryPort mockOrderRepositoryPort;

    @BeforeEach
    public void setup() {
        mockOrderRepositoryPort = new MockOrderRepositoryPort();
        orderServicePort = new OrderServicePortImpl(mockOrderRepositoryPort);
    }

    @Nested
    class PlaceOrderTest {
        @Test
        public void shouldPlaceOrderAndFireOrderAppliedEvent() {
            Order order = MockOrderFactory.createOrderWithDefaultValues();

            OrderPlacedProducerPort orderPlacedProducerPort = new OrderPlacedProducerPort() {

                @Override
                public void handle(OrderPlaced orderPlaced) {
                    assertThat(orderPlaced.getOrderId()).isEqualByComparingTo(order.getOrderId());
                }

                @Override
                public void subscribe() {
                    EventManager.subscribe(OrderPlaced.class, this);
                }

                @Override
                public void unsubscribe() {

                }
            };

            orderPlacedProducerPort.subscribe();

            orderServicePort.placeOrder(order);
            assertThat(order.getStatus()).isEqualByComparingTo(Status.PLACED);
        }

        @Test
        public void shouldNotPlaceOrderWhenOrderStatusIsPlaced() {
            Order order = MockOrderFactory.createFullOrder(Status.PLACED);

            assertThatThrownBy(() -> orderServicePort.placeOrder(order))
                    .hasMessageContaining("PLACED")
                    .isInstanceOf(OrderNotPlacedException.class);
        }

        @Test
        public void shouldNotPlaceOrderWhenOrderStatusIsConfirmed() {
            Order order = MockOrderFactory.createFullOrder(Status.CONFIRMED);

            assertThatThrownBy(() -> orderServicePort.placeOrder(order))
                    .hasMessageContaining("CONFIRMED")
                    .isInstanceOf(OrderNotPlacedException.class);
        }

        @Test
        public void shouldNotPlaceOrderWhenOrderStatusIsCanceled() {
            Order order = MockOrderFactory.createFullOrder(Status.CANCELED);

            assertThatThrownBy(() -> orderServicePort.placeOrder(order))
                    .hasMessageContaining("CANCELED")
                    .isInstanceOf(OrderNotPlacedException.class);
        }

        @Test
        public void shouldNotPlaceOrderWhenOrderStatusIsCompleted() {
            Order order = MockOrderFactory.createFullOrder(Status.COMPLETED);

            assertThatThrownBy(() -> orderServicePort.placeOrder(order))
                    .hasMessageContaining("COMPLETED")
                    .isInstanceOf(OrderNotPlacedException.class);
        }
    }

    @Nested
    class ConfirmOrderTest {

        @Test
        public void shouldConfirmOrderAndFireOrderConfirmedEvent() {
            Order order = MockOrderFactory.createFullOrder(Status.PLACED);
            mockOrderRepositoryPort.insert(order);

            OrderConfirmedProducerPort orderConfirmedProducerPort = new OrderConfirmedProducerPort() {

                @Override
                public void handle(OrderConfirmed orderConfirmed) {
                    assertThat(orderConfirmed.getOrderId()).isEqualByComparingTo(order.getOrderId());
                }

                @Override
                public void subscribe() {
                    EventManager.subscribe(OrderConfirmed.class, this);
                }

                @Override
                public void unsubscribe() {

                }
            };

            orderConfirmedProducerPort.subscribe();

            orderServicePort.confirmOrder(order.getOrderId());
            assertThat(order.getStatus()).isEqualByComparingTo(Status.CONFIRMED);
        }

        @Test
        public void shouldNotConfirmOrderWhenOrderStatusIsRequested() {
            Order order = MockOrderFactory.createFullOrder(Status.REQUESTED);
            mockOrderRepositoryPort.insert(order);

            assertThatThrownBy(() -> orderServicePort.confirmOrder(order.getOrderId()))
                    .hasMessageContaining("REQUESTED")
                    .isInstanceOf(OrderNotConfirmedException.class);
        }

        @Test
        public void shouldNotConfirmOrderWhenOrderStatusIsConfirmed() {
            Order order = MockOrderFactory.createFullOrder(Status.CONFIRMED);
            mockOrderRepositoryPort.insert(order);

            assertThatThrownBy(() -> orderServicePort.confirmOrder(order.getOrderId()))
                    .hasMessageContaining("CONFIRMED")
                    .isInstanceOf(OrderNotConfirmedException.class);
        }

        @Test
        public void shouldNotConfirmOrderWhenOrderStatusIsCanceled() {
            Order order = MockOrderFactory.createFullOrder(Status.CANCELED);
            mockOrderRepositoryPort.insert(order);

            assertThatThrownBy(() -> orderServicePort.confirmOrder(order.getOrderId()))
                    .hasMessageContaining("CANCELED")
                    .isInstanceOf(OrderNotConfirmedException.class);
        }

        @Test
        public void shouldNotConfirmOrderWhenOrderStatusIsCompleted() {
            Order order = MockOrderFactory.createFullOrder(Status.COMPLETED);
            mockOrderRepositoryPort.insert(order);

            assertThatThrownBy(() -> orderServicePort.confirmOrder(order.getOrderId()))
                    .hasMessageContaining("COMPLETED")
                    .isInstanceOf(OrderNotConfirmedException.class);
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class CancelOrderTest {
        private Order order;

        private OrderCanceledProducerPort orderCanceledProducerPort;

        @BeforeAll
        public void setupClass() {
            orderCanceledProducerPort = new OrderCanceledProducerPort() {

                @Override
                public void handle(OrderCanceled orderCanceled) {
                    assertThat(orderCanceled.getOrderId()).isEqualByComparingTo(order.getOrderId());
                }

                @Override
                public void subscribe() {
                    EventManager.subscribe(OrderCanceled.class, this);
                }

                @Override
                public void unsubscribe() {

                }
            };

            orderCanceledProducerPort.subscribe();
        }

        @Test
        public void shouldCancelOrderWhenOrderHasPlacedStateAndFireOrderConfirmedEvent() {
            order = MockOrderFactory.createFullOrder(Status.PLACED);
            mockOrderRepositoryPort.insert(order);

            orderServicePort.cancelOrder(order.getOrderId());
            assertThat(order.getStatus()).isEqualByComparingTo(Status.CANCELED);
        }

        @Test
        public void shouldCancelOrderWhenOrderHasConfirmedStateAndFireOrderConfirmedEvent() {
            order = MockOrderFactory.createFullOrder(Status.CONFIRMED);
            mockOrderRepositoryPort.insert(order);

            orderServicePort.cancelOrder(order.getOrderId());
            assertThat(order.getStatus()).isEqualByComparingTo(Status.CANCELED);
        }

        @Test
        public void shouldNotCancelOrderWhenOrderStatusIsRequested() {
            order = MockOrderFactory.createFullOrder(Status.REQUESTED);
            mockOrderRepositoryPort.insert(order);

            assertThatThrownBy(() -> orderServicePort.cancelOrder(order.getOrderId()))
                    .hasMessageContaining("REQUESTED")
                    .isInstanceOf(OrderNotCanceledException.class);
        }

        @Test
        public void shouldNotCancelOrderWhenOrderStatusIsCanceled() {
            order = MockOrderFactory.createFullOrder(Status.CANCELED);
            mockOrderRepositoryPort.insert(order);

            assertThatThrownBy(() -> orderServicePort.cancelOrder(order.getOrderId()))
                    .hasMessageContaining("CANCELED")
                    .isInstanceOf(OrderNotCanceledException.class);
        }

        @Test
        public void shouldNotPlaceOrderWhenOrderStatusIsCompleted() {
            order = MockOrderFactory.createFullOrder(Status.COMPLETED);
            mockOrderRepositoryPort.insert(order);

            assertThatThrownBy(() -> orderServicePort.cancelOrder(order.getOrderId()))
                    .hasMessageContaining("COMPLETED")
                    .isInstanceOf(OrderNotCanceledException.class);
        }
    }

    @Nested
    class CompleteOrderTest {

        @Test
        public void shouldCompleteOrderAndFireOrderCompletedEvent() {
            Order order = MockOrderFactory.createFullOrder(Status.CONFIRMED);
            mockOrderRepositoryPort.insert(order);

            OrderCompletedProducerPort orderCompletedProducerPort = new OrderCompletedProducerPort() {

                @Override
                public void handle(OrderCompleted orderCompleted) {
                    assertThat(orderCompleted.getOrderId()).isEqualByComparingTo(order.getOrderId());
                }

                @Override
                public void subscribe() {
                    EventManager.subscribe(OrderCompleted.class, this);
                }

                @Override
                public void unsubscribe() {

                }
            };

            orderCompletedProducerPort.subscribe();

            orderServicePort.completeOrder(order.getOrderId());
            assertThat(order.getStatus()).isEqualByComparingTo(Status.COMPLETED);
        }

        @Test
        public void shouldNotCompleteOrderWhenOrderStatusIsRequested() {
            Order order = MockOrderFactory.createFullOrder(Status.REQUESTED);
            mockOrderRepositoryPort.insert(order);

            assertThatThrownBy(() -> orderServicePort.completeOrder(order.getOrderId()))
                    .hasMessageContaining("REQUESTED")
                    .isInstanceOf(OrderNotCompletedException.class);
        }

        @Test
        public void shouldNotCompleteOrderWhenOrderStatusIsPlaced() {
            Order order = MockOrderFactory.createFullOrder(Status.PLACED);
            mockOrderRepositoryPort.insert(order);

            assertThatThrownBy(() -> orderServicePort.completeOrder(order.getOrderId()))
                    .hasMessageContaining("PLACED")
                    .isInstanceOf(OrderNotCompletedException.class);
        }

        @Test
        public void shouldNotCompleteOrderWhenOrderStatusIsCanceled() {
            Order order = MockOrderFactory.createFullOrder(Status.CANCELED);
            mockOrderRepositoryPort.insert(order);

            assertThatThrownBy(() -> orderServicePort.completeOrder(order.getOrderId()))
                    .hasMessageContaining("CANCELED")
                    .isInstanceOf(OrderNotCompletedException.class);
        }

        @Test
        public void shouldNotCompleteOrderWhenOrderStatusIsCompleted() {
            Order order = MockOrderFactory.createFullOrder(Status.COMPLETED);
            mockOrderRepositoryPort.insert(order);

            assertThatThrownBy(() -> orderServicePort.completeOrder(order.getOrderId()))
                    .hasMessageContaining("COMPLETED")
                    .isInstanceOf(OrderNotCompletedException.class);
        }
    }

}
