package io.fiap.fastfood.driven.core.domain.customer.port.inbound;

import reactor.core.publisher.Mono;

public interface CustomerUseCase {
    Mono<Void> deleteCustomer(String vat);
}
