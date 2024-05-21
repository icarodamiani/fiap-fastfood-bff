package io.fiap.fastfood.driven.core.service;

import io.fiap.fastfood.driven.core.domain.model.Product;
import io.fiap.fastfood.driven.core.domain.product.port.inbound.ProductUseCase;
import io.fiap.fastfood.driven.core.domain.product.port.outbound.ProductPort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProductService implements ProductUseCase {

    private final ProductPort productPort;

    public ProductService(ProductPort productPort) {
        this.productPort = productPort;
    }

    @Override
    public Mono<Product> create(Product product) {
        return productPort.createProduct(product);
    }

    @Override
    public Flux<Product> findAll() {
        return productPort.findAllProducts();
    }

    @Override
    public Flux<Product> findByType(Integer typeId) {
        return productPort.findProductsByTypeId(typeId);
    }
}
