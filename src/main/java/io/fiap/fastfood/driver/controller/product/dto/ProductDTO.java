package io.fiap.fastfood.driver.controller.product.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Optional;
import reactor.util.annotation.Nullable;

public record ProductDTO(String id,
                         @NotNull String typeId,
                         @NotNull @Size(min = 2, max = 50) String description,
                         @NotNull @Min(0) BigDecimal price,
                         @NotNull @Min(0) Integer amount,
                         @Nullable ProductTypeDTO type) {

    Optional<String> getId() {
        return Optional.ofNullable(id());
    }

    Optional<ProductTypeDTO> getType() {
        return Optional.ofNullable(type());
    }


    public static final class ProductDTOBuilder {
        private String id;
        private @NotNull String typeId;
        private @NotNull @Size(min = 2, max = 50) String description;
        private @NotNull @Min(0) BigDecimal price;
        private @NotNull @Min(0) Integer availability;
        private @Nullable ProductTypeDTO type;

        private ProductDTOBuilder() {
        }

        public static ProductDTOBuilder builder() {
            return new ProductDTOBuilder();
        }

        public ProductDTOBuilder withId(String id) {
            this.id = id;
            return this;
        }

        public ProductDTOBuilder withTypeId(String typeId) {
            this.typeId = typeId;
            return this;
        }

        public ProductDTOBuilder withDescription(String description) {
            this.description = description;
            return this;
        }

        public ProductDTOBuilder withPrice(BigDecimal price) {
            this.price = price;
            return this;
        }

        public ProductDTOBuilder withAvailability(Integer availability) {
            this.availability = availability;
            return this;
        }

        public ProductDTOBuilder withType(ProductTypeDTO type) {
            this.type = type;
            return this;
        }

        public ProductDTO build() {
            return new ProductDTO(id, typeId, description, price, availability, type);
        }
    }
}
