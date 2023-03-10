package br.com.fourdf.restaurant.orderdomain.helper;

import br.com.fourdf.restaurant.orderdomain.domain.order.Order;
import br.com.fourdf.restaurant.orderdomain.domain.order.OrderedProduct;
import br.com.fourdf.restaurant.orderdomain.domain.order.enums.Status;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class MockOrderFactory {

    public static Order createOrderWithDefaultValues() {
        OrderedProduct orderedProductOne =
                new OrderedProduct("productOne", new BigDecimal("2.50"), 4, UUID.randomUUID());
        OrderedProduct orderedProductTwo =
                new OrderedProduct("productTwo", new BigDecimal("5.25"), 2, UUID.randomUUID());

        Set<OrderedProduct> orderedProducts = new HashSet<>();
        orderedProducts.add(orderedProductOne);
        orderedProducts.add(orderedProductTwo);

        return new Order(orderedProducts, UUID.randomUUID(), UUID.randomUUID());
    }

    public static Order createFullOrder(Status status) {
        OrderedProduct orderedProductOne =
                new OrderedProduct("productOne", new BigDecimal("2.50"), 4, UUID.randomUUID());
        OrderedProduct orderedProductTwo =
                new OrderedProduct("productTwo", new BigDecimal("5.25"), 2, UUID.randomUUID());

        Set<OrderedProduct> orderedProducts = new HashSet<>();
        orderedProducts.add(orderedProductOne);
        orderedProducts.add(orderedProductTwo);

        LocalDateTime now = LocalDateTime.now();

        return new Order(UUID.randomUUID(), orderedProducts, status, now, now, UUID.randomUUID(),
                UUID.randomUUID());
    }

}
