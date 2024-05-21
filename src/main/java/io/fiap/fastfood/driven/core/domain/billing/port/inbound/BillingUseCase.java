package io.fiap.fastfood.driven.core.domain.billing.port.inbound;

import reactor.core.publisher.Mono;

public interface BillingUseCase {
    Mono<Void> open();
    Mono<Void> close();
}
