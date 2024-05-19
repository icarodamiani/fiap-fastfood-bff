package io.fiap.fastfood.driven.client.product;


import com.google.type.Decimal;
import io.fiap.fastfood.FindAllProductByTypeRequest;
import io.fiap.fastfood.FindAllProductRequest;
import io.fiap.fastfood.ProductTypeRequest;
import io.fiap.fastfood.ReactorProductServiceGrpc;
import io.fiap.fastfood.SaveProductRequest;
import io.fiap.fastfood.driven.core.domain.model.Product;
import io.fiap.fastfood.driven.core.domain.model.ProductType;
import java.math.BigDecimal;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class ProductGrpcClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductGrpcClient.class);

    @GrpcClient("product-api")
    private ReactorProductServiceGrpc.ReactorProductServiceStub reactiveStub;

    public ProductGrpcClient() {
    }

    public ProductGrpcClient(ReactorProductServiceGrpc.ReactorProductServiceStub reactiveStub) {
        this.reactiveStub = reactiveStub;
    }

    public Mono<Product> createProduct(Product product) {
        return reactiveStub.withWaitForReady()
            .saveProduct(SaveProductRequest.newBuilder()
                .setTypeId(product.typeId())
                .setDescription(product.description())
                .setPrice(toDecimal(product.price()))
                .setAmount(product.amount())
                .setType(ProductTypeRequest.newBuilder()
                    .setDescription(product.type().description())
                    .setId(product.type().id())
                    .build())
                .build())
            .doOnError(throwable -> LOGGER.error("Failed to create product. {}", product, throwable))
            .map(response ->
                Product.ProductBuilder.from(product).withId(response.getId()).build()
            );
    }

    public Flux<Product> findAll() {
        return reactiveStub.withWaitForReady()
            .findAllProduct(FindAllProductRequest.getDefaultInstance())
            .map(product -> Product.ProductBuilder.builder()
                .withId(product.getId())
                .withAmount(product.getAmount())
                .withPrice(new BigDecimal(product.getPrice().getValue()))
                .withDescription(product.getDescription())
                .withTypeId(product.getTypeId())
                .withType(ProductType.ProductTypeBuilder.builder()
                    .withId(product.getType().getId())
                    .withDescription(product.getType().getDescription())
                    .build())
                .build())
            .doOnError(throwable -> LOGGER.error("Failed to open billing day.", throwable));
    }

    public Flux<Product> findByTypeId(Integer typeId) {
        return reactiveStub.withWaitForReady()
            .findAllProductByType(FindAllProductByTypeRequest.newBuilder()
                .setTypeId(String.valueOf(typeId))
                .build())
            .map(product -> Product.ProductBuilder.builder()
                .withId(product.getId())
                .withAmount(product.getAmount())
                .withPrice(new BigDecimal(product.getPrice().getValue()))
                .withDescription(product.getDescription())
                .withTypeId(product.getTypeId())
                .withType(ProductType.ProductTypeBuilder.builder()
                    .withId(product.getType().getId())
                    .withDescription(product.getType().getDescription())
                    .build())
                .build())
            .doOnError(throwable -> LOGGER.error("Failed to open billing day.", throwable));
    }

    private static Decimal toDecimal(BigDecimal value) {
        return Decimal.newBuilder().setValue(value.toString()).build();
    }
}
