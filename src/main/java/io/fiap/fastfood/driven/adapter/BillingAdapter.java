package io.fiap.fastfood.driven.adapter;

import io.fiap.fastfood.driven.client.billing.BillingGrpcClient;
import io.fiap.fastfood.driven.core.domain.billing.port.outbound.BillingPort;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class BillingAdapter implements BillingPort {

    private final BillingGrpcClient billingGrpcClient;

    public BillingAdapter(BillingGrpcClient billingGrpcClient) {
        this.billingGrpcClient = billingGrpcClient;
    }

    @Override
    public Mono<Void> openBillingDay() {
        return billingGrpcClient.open();
    }

    @Override
    public Mono<Void> closeBillingDay() {
        return billingGrpcClient.close();
    }
}
