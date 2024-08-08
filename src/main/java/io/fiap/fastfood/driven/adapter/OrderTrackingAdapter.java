package io.fiap.fastfood.driven.adapter;

import io.fiap.fastfood.driven.client.tracking.OrderTrackingGrpcClient;
import io.fiap.fastfood.driven.core.domain.model.OrderTracking;
import io.fiap.fastfood.driven.core.domain.tracking.port.outbound.OrderTrackingPort;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class OrderTrackingAdapter implements OrderTrackingPort {
    private final OrderTrackingGrpcClient orderTrackingClient;


    public OrderTrackingAdapter(OrderTrackingGrpcClient orderTrackingClient) {
        this.orderTrackingClient = orderTrackingClient;
    }

    @Override
    public Mono<OrderTracking> createOrderTracking(OrderTracking orderTracking) {
        return orderTrackingClient.createTracking(orderTracking);
    }

    @Override
    public Mono<OrderTracking> findByOrderNumber(String orderNumber) {
        return orderTrackingClient.findTrackingByOrderNumber(orderNumber);
    }

    @Override
    public Flux<OrderTracking> find(String role) {
        return orderTrackingClient.findTracking(role);
    }
}
