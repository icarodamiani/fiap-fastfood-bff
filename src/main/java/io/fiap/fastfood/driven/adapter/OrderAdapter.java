package io.fiap.fastfood.driven.adapter;

import io.fiap.fastfood.driven.client.order.OrderGrpcClient;
import io.fiap.fastfood.driven.core.domain.model.Order;
import io.fiap.fastfood.driven.core.domain.order.port.outbound.OrderPort;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class OrderAdapter implements OrderPort {
    private final OrderGrpcClient orderClient;

    public OrderAdapter(OrderGrpcClient orderClient) {
        this.orderClient = orderClient;
    }

    @Override
    public Mono<Order> createOrder(Order order) {
        return orderClient.createOrder(order);
    }

    @Override
    public Flux<Order> findAll(Pageable pageable) {
        return orderClient.findAll(pageable);
    }

    @Override
    public Mono<Order> findById(String id) {
        return orderClient.findById(id);
    }
}
