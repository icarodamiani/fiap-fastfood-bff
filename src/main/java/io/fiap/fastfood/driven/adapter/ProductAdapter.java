package io.fiap.fastfood.driven.adapter;

import io.fiap.fastfood.driven.client.product.ProductGrpcClient;
import io.fiap.fastfood.driven.core.domain.model.Product;
import io.fiap.fastfood.driven.core.domain.product.port.outbound.ProductPort;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class ProductAdapter implements ProductPort {
    private final ProductGrpcClient productClient;

    public ProductAdapter(ProductGrpcClient productClient) {
        this.productClient = productClient;
    }

    @Override
    public Mono<Product> createProduct(Product product) {
        return productClient.createProduct(product);
    }

    @Override
    public Flux<Product> findAllProducts() {
        return productClient.findAll();
    }

    @Override
    public Flux<Product> findProductsByTypeId(Integer typeId) {
        return productClient.findByTypeId(typeId);
    }
}
