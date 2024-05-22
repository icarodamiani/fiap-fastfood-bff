package io.fiap.fastfood.driven.core.domain.order.port.outbound;

import io.fiap.fastfood.driven.core.domain.Page;
import io.fiap.fastfood.driven.core.domain.model.Order;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface OrderPort {

    Mono<Order> createOrder(Order order);

    Flux<Order> findAll(Page pageable);

    Mono<Order> findById(String id);
}
