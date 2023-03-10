package br.com.fourdf.restaurant.orderdomain.domain.order;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class OrderedProductTest {

    @Test
    public void shouldCalculateOrderedProductTotalValue() {
        OrderedProduct orderedProduct = new OrderedProduct("product", new BigDecimal("2.50"), 4,
                UUID.randomUUID());
        BigDecimal total = orderedProduct.calculateTotalValue();

        assertThat(total).isEqualByComparingTo(new BigDecimal("10.00"));
    }

}
