package io.fiap.fastfood.driven.adapter;

import io.fiap.fastfood.driven.client.customer.CustomerGrpcClient;
import io.fiap.fastfood.driven.core.domain.customer.port.outbound.CustomerPort;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class CustomerAdapter implements CustomerPort {
    private final CustomerGrpcClient customerGrpcClient;

    public CustomerAdapter(CustomerGrpcClient customerGrpcClient) {
        this.customerGrpcClient = customerGrpcClient;
    }

    @Override
    public Mono<Void> deleteCustomer(String vat) {
        return customerGrpcClient.deleteCustomer(vat);
    }
}
