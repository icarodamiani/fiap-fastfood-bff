package io.fiap.fastfood.driven.core.domain.tracking.port.inbound;

import io.fiap.fastfood.driven.core.domain.model.OrderTracking;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface OrderTrackingUseCase {
    Mono<OrderTracking> create(OrderTracking orderTracking);

    Mono<OrderTracking> findByOrderNumber(String orderId);

    Flux<OrderTracking> find(String role);
}
