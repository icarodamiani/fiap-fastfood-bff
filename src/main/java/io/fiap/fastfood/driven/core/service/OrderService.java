package io.fiap.fastfood.driven.core.service;

import io.fiap.fastfood.OrderTrackingStatus;
import io.fiap.fastfood.driven.core.domain.model.Order;
import io.fiap.fastfood.driven.core.domain.model.OrderTracking;
import io.fiap.fastfood.driven.core.domain.order.port.inbound.OrderUseCase;
import io.fiap.fastfood.driven.core.domain.order.port.outbound.OrderPort;
import io.fiap.fastfood.driven.core.domain.tracking.port.outbound.OrderTrackingPort;
import java.time.LocalDateTime;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class OrderService implements OrderUseCase {

    private final OrderPort orderPort;
    private final OrderTrackingPort orderTrackingPort;

    public OrderService(OrderPort orderPort, OrderTrackingPort orderTrackingPort) {
        this.orderPort = orderPort;
        this.orderTrackingPort = orderTrackingPort;
    }

    @Override
    public Mono<Order> create(Order order) {
        return orderPort.createOrder(order)
            .doOnSuccess(this::createWaitingPaymentTracking);
    }

    private Mono<Order> createWaitingPaymentTracking(Order order) {
        return orderTrackingPort.createOrderTracking(
                OrderTracking.OrderTrackingBuilder.builder()
                    .withOrderId(order.id())
                    .withOrderNumber(order.number())
                    .withOrderDateTime(LocalDateTime.now())
                    .withOrderStatus(OrderTrackingStatus.WAITING_PAYMENT.name())
                    .build())
            .map(__ -> order);
    }

    @Override
    public Flux<Order> findAll(Pageable pageable) {
        return orderPort.findAll(pageable);
    }

    @Override
    public Mono<Order> findById(String id) {
        return orderPort.findById(id);
    }

}
