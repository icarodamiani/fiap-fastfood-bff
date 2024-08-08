package io.fiap.fastfood.driven.core.domain.tracking.port.outbound;

import io.fiap.fastfood.driven.core.domain.model.OrderTracking;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface OrderTrackingPort {
    Mono<OrderTracking> createOrderTracking(OrderTracking orderTracking);

    Mono<OrderTracking> findByOrderNumber(String orderId);

    Flux<OrderTracking> find(String role);
}
