package io.fiap.fastfood.driven.core.domain.order.port.inbound;

import io.fiap.fastfood.driven.core.domain.Page;
import io.fiap.fastfood.driven.core.domain.model.Order;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface OrderUseCase {
    Mono<Order> create(Order value);

    Flux<Order> findAll(Page pageable);

    Mono<Order> findById(String id);

}
