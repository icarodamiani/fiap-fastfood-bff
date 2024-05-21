package io.fiap.fastfood.driven.client.billing;


import io.fiap.fastfood.CloseBillingDayRequest;
import io.fiap.fastfood.OpenBillingDayRequest;
import io.fiap.fastfood.ReactorBillingServiceGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class BillingGrpcClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(BillingGrpcClient.class);


    @GrpcClient("order-api")
    private ReactorBillingServiceGrpc.ReactorBillingServiceStub reactiveStub;

    public BillingGrpcClient() {
    }

    public BillingGrpcClient(ReactorBillingServiceGrpc.ReactorBillingServiceStub reactiveStub) {
        this.reactiveStub = reactiveStub;
    }

    public Mono<Void> open() {
        return reactiveStub.withWaitForReady()
            .open(OpenBillingDayRequest.getDefaultInstance())
            .doOnError(throwable -> LOGGER.error("Failed to open billing day.", throwable))
            .then();
    }

    public Mono<Void> close() {
        return reactiveStub.withWaitForReady()
            .close(CloseBillingDayRequest.getDefaultInstance())
            .doOnError(throwable -> LOGGER.error("Failed to close billing day.", throwable))
            .then();
    }
}
