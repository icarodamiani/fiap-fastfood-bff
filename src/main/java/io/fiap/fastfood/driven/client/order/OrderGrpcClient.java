package io.fiap.fastfood.driven.client.order;


import com.google.protobuf.Timestamp;
import com.google.type.Decimal;
import io.fiap.fastfood.FindAllOrderRequest;
import io.fiap.fastfood.FindOrderByIdRequest;
import io.fiap.fastfood.OrderItemResponse;
import io.fiap.fastfood.ReactorOrderServiceGrpc;
import io.fiap.fastfood.SaveOrderItemRequest;
import io.fiap.fastfood.SaveOrderRequest;
import io.fiap.fastfood.SavePaymentRequest;
import io.fiap.fastfood.driven.core.domain.model.Order;
import io.fiap.fastfood.driven.core.domain.model.OrderItem;
import io.fiap.fastfood.driven.core.domain.model.Payment;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.stream.Collectors;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class OrderGrpcClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderGrpcClient.class);


    @GrpcClient("order-api")
    private ReactorOrderServiceGrpc.ReactorOrderServiceStub reactiveStub;

    public OrderGrpcClient() {
    }

    public OrderGrpcClient(ReactorOrderServiceGrpc.ReactorOrderServiceStub reactiveStub) {
        this.reactiveStub = reactiveStub;
    }

    public Mono<Order> createOrder(Order order) {
        return reactiveStub.withWaitForReady()
            .saveOrder(SaveOrderRequest.newBuilder()
                .setCustomerId(order.customerId())
                .addAllItems(order.items().stream()
                    .map(this::toOrderItemRequest)
                    .collect(Collectors.toList()))
                .setPayment(toPaymentRequest(order.payment()))
                .build())
            .doOnError(throwable -> LOGGER.error("Failed to open billing day.", throwable))
            .map(response ->
                Order.OrderBuilder.from(order).withId(response.getId()).withNumber(response.getNumber()).build()
            );
    }

    private SavePaymentRequest toPaymentRequest(Payment payment) {
        return SavePaymentRequest.newBuilder()
            .setMethod(payment.method())
            .setTotal(toDecimal(payment.total()))
            .build();
    }

    private SaveOrderItemRequest toOrderItemRequest(OrderItem item) {
        return SaveOrderItemRequest.newBuilder()
            .setQuote(item.quote())
            .setProductId(item.productId())
            .setAmount(item.amount())
            .build();
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

    private static Decimal toDecimal(BigDecimal value) {
        return Decimal.newBuilder().setValue(value.toString()).build();
    }

    public Flux<Order> findAll(Pageable pageable) {
        return reactiveStub.withWaitForReady()
            .findAllOrders(FindAllOrderRequest.newBuilder()
                .setPage(pageable.getPageNumber())
                .setPageSize(pageable.getPageSize())
                .build())
            .map(response -> Order.OrderBuilder.builder()
                .withCustomerId(response.getCustomerId())
                .withCreatedAt(toLocalDate(response.getCreatedAt()))
                .withNumber(response.getNumber())
                .withItems(response.getItemsList().stream()
                    .map(this::toOrderItem)
                    .collect(Collectors.toList()))
                .build())
            .doOnError(throwable -> LOGGER.error("Failed to fetch orders.", throwable));
    }

    public Mono<Order> findById(String id) {
        return reactiveStub.withWaitForReady()
            .findOrderById(FindOrderByIdRequest.newBuilder()
                .setId(id)
                .build())
            .map(response -> Order.OrderBuilder.builder()
                .withCustomerId(response.getCustomerId())
                .withCreatedAt(toLocalDate(response.getCreatedAt()))
                .withNumber(response.getNumber())
                .withItems(response.getItemsList().stream()
                    .map(this::toOrderItem)
                    .collect(Collectors.toList()))
                .build())
            .doOnError(throwable -> LOGGER.error("Failed to fetch orders.", throwable));
    }

    private OrderItem toOrderItem(OrderItemResponse response) {
        return OrderItem.OrderItemBuilder.builder()
            .withQuote(response.getQuote())
            .withAmount(response.getAmount())
            .withProductId(response.getProductId())
            .build();
    }


    private static LocalDateTime toLocalDate(Timestamp ts) {
        return Instant
            .ofEpochSecond(ts.getSeconds(), ts.getNanos())
            .atZone(ZoneId.ofOffset("UTC", ZoneOffset.UTC))
            .toLocalDateTime();
    }

}