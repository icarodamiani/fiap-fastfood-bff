package io.fiap.fastfood.driven.core.domain.customer.port.outbound;

import reactor.core.publisher.Mono;

public interface CustomerPort {
    Mono<Void> deleteCustomer(String vat);
}
