package br.com.fourdf.restaurant.orderdomain.domain.order;

import java.math.BigDecimal;
import java.util.UUID;

public class OrderedProduct {

    private String name;

    private BigDecimal valuePerUnit;

    private Integer quantity;

    private UUID productId;

    public OrderedProduct(String name, BigDecimal valuePerUnit, Integer quantity, UUID productId) {
        this.name = name;
        this.valuePerUnit = valuePerUnit;
        this.quantity = quantity;
        this.productId = productId;
    }

    protected BigDecimal calculateTotalValue() {
        return valuePerUnit.multiply(new BigDecimal(quantity));
    }

    public String getName() {
        return name;
    }

    public BigDecimal getValuePerUnit() {
        return valuePerUnit;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public UUID getProductId() {
        return productId;
    }
}
