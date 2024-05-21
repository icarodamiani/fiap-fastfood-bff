package io.fiap.fastfood.driver.controller.order.dto;

import java.math.BigDecimal;

public record PaymentDTO(
    String method,
    BigDecimal total) {

    public static final class PaymentBuilder {
        private String method;
        private BigDecimal total;

        private PaymentBuilder() {
        }

        public static PaymentBuilder builder() {
            return new PaymentBuilder();
        }

        public static PaymentBuilder from(PaymentDTO payment) {
            return PaymentBuilder.builder()
                .withMethod(payment.method)
                .withTotal(payment.total);
        }

        public PaymentBuilder withMethod(String method) {
            this.method = method;
            return this;
        }

        public PaymentBuilder withTotal(BigDecimal total) {
            this.total = total;
            return this;
        }

        public PaymentDTO build() {
            return new PaymentDTO(method, total);
        }
    }
}
