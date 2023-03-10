package br.com.fourdf.restaurant.orderdomain.domain.order;

import br.com.fourdf.restaurant.commons.model.BaseModel;
import br.com.fourdf.restaurant.orderdomain.domain.order.enums.Status;
import br.com.fourdf.restaurant.orderdomain.domain.order.exception.OrderNotCanceledException;
import br.com.fourdf.restaurant.orderdomain.domain.order.exception.OrderNotCompletedException;
import br.com.fourdf.restaurant.orderdomain.domain.order.exception.OrderNotConfirmedException;
import br.com.fourdf.restaurant.orderdomain.domain.order.exception.OrderNotPlacedException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public class Order implements BaseModel {

    private UUID orderId;

    private Set<OrderedProduct> orderedProducts;

    private Status status;

    private LocalDateTime updateTimestamp;

    private LocalDateTime creationTimestamp;

    private UUID establishmentId;

    private UUID customerId;

    public Order(Set<OrderedProduct> orderedProducts, UUID establishmentId, UUID customerId) {
        LocalDateTime now = LocalDateTime.now();

        this.orderId = UUID.randomUUID();
        this.orderedProducts = orderedProducts;
        this.establishmentId = establishmentId;
        this.customerId = customerId;
        this.status = Status.REQUESTED;
        this.creationTimestamp = now;
        this.updateTimestamp = now;
    }

    public Order(UUID orderId, Set<OrderedProduct> orderedProducts, Status status, LocalDateTime updateTimestamp,
                 LocalDateTime creationTimestamp, UUID establishmentId, UUID customerId) {
        this.orderId = orderId;
        this.orderedProducts = orderedProducts;
        this.status = status;
        this.updateTimestamp = updateTimestamp;
        this.creationTimestamp = creationTimestamp;
        this.establishmentId = establishmentId;
        this.customerId = customerId;
    }

    public void place() {
        if (status.equals(Status.REQUESTED)) {
            this.status = Status.PLACED;
            this.updateTimestamp = LocalDateTime.now();
        } else {
            throw new OrderNotPlacedException("Unable to place order. Order status: " + status);
        }
    }

    public void cancel() {
        if (isActive()) {
            this.status = Status.CANCELED;
            this.updateTimestamp = LocalDateTime.now();
        } else {
            throw new OrderNotCanceledException("Unable to cancel order. Order status: " + status);
        }
    }

    public void confirm() {
        if (status.equals(Status.PLACED)) {
            this.status = Status.CONFIRMED;
            this.updateTimestamp = LocalDateTime.now();
        } else {
            throw new OrderNotConfirmedException("Unable to confirm order. Order status: " + status);
        }
    }

    public void complete() {
        if (status.equals(Status.CONFIRMED)) {
            this.status = Status.COMPLETED;
            this.updateTimestamp = LocalDateTime.now();
        } else {
            throw new OrderNotCompletedException("Unable to complete order. Order status: " + status);
        }
    }

    public BigDecimal calculateTotalValue() {
        return orderedProducts.stream()
                .map(OrderedProduct::calculateTotalValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private boolean isActive() {
        return !status.equals(Status.COMPLETED) && !status.equals(Status.CANCELED) && !status.equals(Status.REQUESTED);
    }

    public UUID getOrderId() {
        return orderId;
    }

    public Set<OrderedProduct> getOrderedProducts() {
        return orderedProducts;
    }

    public Status getStatus() {
        return status;
    }

    public LocalDateTime getUpdateTimestamp() {
        return updateTimestamp;
    }

    public LocalDateTime getCreationTimestamp() {
        return creationTimestamp;
    }

    public UUID getEstablishmentId() {
        return establishmentId;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    protected void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }

    @Override
    public boolean hasId() {
        return orderId != null;
    }
}
