package io.fiap.fastfood.driven.core.domain.model;

import java.math.BigDecimal;
import java.util.Optional;

public record Product(
    String id,
    String typeId,
    String description,
    BigDecimal price,
    Integer amount,
    ProductType type) {

    Optional<String> getId() {
        return Optional.ofNullable(id());
    }

    Optional<ProductType> getType() {
        return Optional.ofNullable(type());
    }


    public static final class ProductBuilder {
        private String id;
        private String typeId;
        private String description;
        private BigDecimal price;
        private Integer amount;
        private ProductType type;

        private ProductBuilder() {
        }

        public static ProductBuilder builder() {
            return new ProductBuilder();
        }

        public static Product.ProductBuilder from(Product product) {
            return Product.ProductBuilder.builder()
                .withId(product.id())
                .withTypeId(product.typeId())
                .withDescription(product.description())
                .withType(ProductType.ProductTypeBuilder.builder()
                    .withId(product.type().id())
                    .withDescription(product.type().description())
                    .build());
        }

        public ProductBuilder withId(String id) {
            this.id = id;
            return this;
        }

        public ProductBuilder withTypeId(String typeId) {
            this.typeId = typeId;
            return this;
        }

        public ProductBuilder withDescription(String description) {
            this.description = description;
            return this;
        }

        public ProductBuilder withPrice(BigDecimal price) {
            this.price = price;
            return this;
        }

        public ProductBuilder withAmount(Integer amount) {
            this.amount = amount;
            return this;
        }

        public ProductBuilder withType(ProductType type) {
            this.type = type;
            return this;
        }

        public Product build() {
            return new Product(id, typeId, description, price, amount, type);
        }
    }
}
