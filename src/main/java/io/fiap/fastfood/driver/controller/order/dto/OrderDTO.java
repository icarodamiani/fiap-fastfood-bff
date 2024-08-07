package io.fiap.fastfood.driver.controller.order.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

public record OrderDTO(@Nullable String id,
                       @Nullable CustomerDTO customer,
                       @NotNull List<OrderItemDTO> items,
                       @NotNull PaymentDTO payment) {

    Optional<String> getId() {
        return Optional.ofNullable(id());
    }

    Optional<CustomerDTO> getCustomer() {
        return Optional.ofNullable(customer());
    }

    public static final class OrderDTOBuilder {
        private String id;
        private @Nullable CustomerDTO customer;
        private @NotNull List<OrderItemDTO> items;
        private @NotNull PaymentDTO payment;

        private OrderDTOBuilder() {
        }

        public static OrderDTOBuilder builder() {
            return new OrderDTOBuilder();
        }

        public OrderDTOBuilder withId(String id) {
            this.id = id;
            return this;
        }

        public OrderDTOBuilder withCustomer(CustomerDTO customer) {
            this.customer = customer;
            return this;
        }

        public OrderDTOBuilder withItems(List<OrderItemDTO> orderItemList) {
            this.items = orderItemList;
            return this;
        }

        public OrderDTOBuilder withPayment(PaymentDTO payment) {
            this.payment = payment;
            return this;
        }

        public OrderDTO build() {
            return new OrderDTO(id, customer, items, payment);
        }
    }
}