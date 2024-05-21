package io.fiap.fastfood.driven.core.domain.billing.port.outbound;

import reactor.core.publisher.Mono;

public interface BillingPort {
    Mono<Void> openBillingDay();

    Mono<Void> closeBillingDay();

}
