package io.fiap.fastfood.driver.controller.order.dto;

import com.mongodb.lang.Nullable;
import jakarta.validation.constraints.NotNull;
import java.util.Optional;

public record OrderItemDTO(@NotNull String productId,
                           @NotNull Long amount,
                           @Nullable String quote) {

    Optional<String> getQuote() {
        return Optional.ofNullable(quote());
    }

    public static final class OrderItemDTOBuilder {
        private @NotNull String productId;
        private @NotNull Long amount;
        private @Nullable String quote;

        private OrderItemDTOBuilder() {
        }

        public static OrderItemDTOBuilder builder() {
            return new OrderItemDTOBuilder();
        }

        public OrderItemDTOBuilder withProductId(String productId) {
            this.productId = productId;
            return this;
        }

        public OrderItemDTOBuilder withAmount(Long amount) {
            this.amount = amount;
            return this;
        }

        public OrderItemDTOBuilder withQuote(String quote) {
            this.quote = quote;
            return this;
        }

        public OrderItemDTO build() {
            return new OrderItemDTO(productId, amount, quote);
        }
    }
}