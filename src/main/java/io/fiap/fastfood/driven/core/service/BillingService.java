package io.fiap.fastfood.driven.core.service;

import io.fiap.fastfood.driven.core.domain.billing.port.inbound.BillingUseCase;
import io.fiap.fastfood.driven.core.domain.billing.port.outbound.BillingPort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class BillingService implements BillingUseCase {

    private final BillingPort billingPort;

    public BillingService(BillingPort billingPort) {
        this.billingPort = billingPort;
    }

    @Override
    public Mono<Void> open() {
        return billingPort.openBillingDay();
    }

    @Override
    public Mono<Void> close() {
        return billingPort.closeBillingDay();
    }
}
