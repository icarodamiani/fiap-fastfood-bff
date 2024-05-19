package io.fiap.fastfood.driver.controller.order.dto;

import java.math.BigDecimal;

public record PaymentDTO(
    String method,
    BigDecimal amount) {

    public static final class PaymentBuilder {
        private String method;
        private BigDecimal amount;

        private PaymentBuilder() {
        }

        public static PaymentBuilder builder() {
            return new PaymentBuilder();
        }

        public static PaymentBuilder from(PaymentDTO payment) {
            return PaymentBuilder.builder()
                .withMethod(payment.method)
                .withAmount(payment.amount);
        }

        public PaymentBuilder withMethod(String method) {
            this.method = method;
            return this;
        }

        public PaymentBuilder withAmount(BigDecimal amount) {
            this.amount = amount;
            return this;
        }

        public PaymentDTO build() {
            return new PaymentDTO(method, amount);
        }
    }
}
