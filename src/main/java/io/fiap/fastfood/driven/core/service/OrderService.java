package io.fiap.fastfood.driven.core.service;

import io.fiap.fastfood.driven.core.domain.Page;
import io.fiap.fastfood.driven.core.domain.model.Order;
import io.fiap.fastfood.driven.core.domain.order.port.inbound.OrderUseCase;
import io.fiap.fastfood.driven.core.domain.order.port.outbound.OrderPort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class OrderService implements OrderUseCase {

    private final OrderPort orderPort;

    public OrderService(OrderPort orderPort) {
        this.orderPort = orderPort;
    }

    @Override
    public Mono<Order> create(Order order) {
        return orderPort.createOrder(order);
    }

    @Override
    public Flux<Order> findAll(Page pageable) {
        return orderPort.findAll(pageable);
    }

    @Override
    public Mono<Order> findById(String id) {
        return orderPort.findById(id);
    }

}
