package io.fiap.fastfood.driven.client.tracking;


import com.google.protobuf.Timestamp;
import io.fiap.fastfood.FindAllOrderTrackingRequest;
import io.fiap.fastfood.FindOrderTrackingByOrderIdRequest;
import io.fiap.fastfood.OrderTrackingRole;
import io.fiap.fastfood.OrderTrackingStatus;
import io.fiap.fastfood.ReactorOrderTrackingServiceGrpc;
import io.fiap.fastfood.SaveOrderTrackingRequest;
import io.fiap.fastfood.driven.core.domain.model.OrderTracking;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class OrderTrackingGrpcClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderTrackingGrpcClient.class);


    @GrpcClient("tracking-api")
    private ReactorOrderTrackingServiceGrpc.ReactorOrderTrackingServiceStub reactiveStub;

    public OrderTrackingGrpcClient() {
    }

    public OrderTrackingGrpcClient(ReactorOrderTrackingServiceGrpc.ReactorOrderTrackingServiceStub reactiveStub) {
        this.reactiveStub = reactiveStub;
    }

    public Mono<OrderTracking> createTracking(OrderTracking tracking) {
        return reactiveStub.withWaitForReady()
            .saveOrderTracking(SaveOrderTrackingRequest.newBuilder()
                .setOrderId(tracking.orderId())
                .setOrderNumber(tracking.orderNumber())
                .setOrderStatus(OrderTrackingStatus.valueOf(tracking.orderStatus()))
                .setOrderDateTime(toTimestamp(LocalDateTime.now()))
                .build())
            .doOnError(throwable -> LOGGER.error("Failed to open billing day.", throwable))
            .map(response -> tracking);
    }

    public Flux <OrderTracking> findTracking(String role) {
        return reactiveStub.withWaitForReady()
            .findAllOrderTracking(FindAllOrderTrackingRequest.newBuilder()
                .setRole(OrderTrackingRole.valueOf(role))
                .build())
            .map(response -> OrderTracking.OrderTrackingBuilder.builder()
                .withId(response.getId())
                .withOrderNumber(response.getOrderNumber())
                .withOrderId(response.getOrderId())
                .withOrderDateTime(toLocalDate(response.getOrderDateTime()))
                .withOrderTimeSpent(response.getTotalTimeSpent())
                .withRole(response.getRole().name())
                .build()
            )
            .doOnError(throwable -> LOGGER.error("Failed to open billing day.", throwable));
    }

    public Mono<OrderTracking> findTrackingByOrderId(String orderId) {
        return reactiveStub.withWaitForReady()
            .findOrderTrackingByOrderId(FindOrderTrackingByOrderIdRequest.newBuilder()
                .setOrderId(orderId)
                .build())
            .map(response -> OrderTracking.OrderTrackingBuilder.builder()
                .withId(response.getId())
                .withOrderNumber(response.getOrderNumber())
                .withOrderId(response.getOrderId())
                .withOrderDateTime(toLocalDate(response.getOrderDateTime()))
                .withOrderTimeSpent(response.getTotalTimeSpent())
                .withRole(response.getRole().name())
                .build()
            )
            .doOnError(throwable -> LOGGER.error("Failed to open billing day.", throwable));
    }

    private static LocalDateTime toLocalDate(Timestamp ts) {
        return Instant
            .ofEpochSecond(ts.getSeconds(), ts.getNanos())
            .atZone(ZoneId.ofOffset("UTC", ZoneOffset.UTC))
            .toLocalDateTime();
    }

    protected Timestamp toTimestamp(LocalDateTime localDateTime) {
        if (localDateTime != null) {
            Instant instant = localDateTime.toInstant(ZoneOffset.UTC);

            return Timestamp.newBuilder()
                .setSeconds(instant.getEpochSecond())
                .setNanos(instant.getNano())
                .build();
        }
        return null;
    }
}
