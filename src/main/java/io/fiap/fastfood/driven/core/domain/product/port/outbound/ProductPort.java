package io.fiap.fastfood.driven.core.domain.product.port.outbound;

import io.fiap.fastfood.driven.core.domain.model.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductPort {

    Mono<Product> createProduct(Product product);

    Flux<Product> findAllProducts();

    Flux<Product> findProductsByTypeId(Integer typeId);
}
