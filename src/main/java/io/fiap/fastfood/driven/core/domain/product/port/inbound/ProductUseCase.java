package io.fiap.fastfood.driven.core.domain.product.port.inbound;

import io.fiap.fastfood.driven.core.domain.model.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductUseCase {
    Mono<Product> create(Product value);

    Flux<Product> findAll();

    Flux<Product> findByType(Integer typeId);

}
