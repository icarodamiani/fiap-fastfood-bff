package io.fiap.fastfood.driven.client.customer;


import io.fiap.fastfood.DeleteCustomerRequest;
import io.fiap.fastfood.ReactorCustomerServiceGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class CustomerGrpcClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerGrpcClient.class);


    @GrpcClient("customer-api")
    private ReactorCustomerServiceGrpc.ReactorCustomerServiceStub reactiveStub;

    public CustomerGrpcClient() {
    }

    public CustomerGrpcClient(ReactorCustomerServiceGrpc.ReactorCustomerServiceStub reactiveStub) {
        this.reactiveStub = reactiveStub;
    }

    public Mono<Void> deleteCustomer(String vat) {
        return reactiveStub.withWaitForReady()
            .deleteCustomer(DeleteCustomerRequest.newBuilder().setVat(vat).build())
            .doOnError(throwable -> LOGGER.error("Failed to delete customer.", throwable))
            .then();
    }
}
