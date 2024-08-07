package io.fiap.fastfood.driver.controller.order.dto;

import jakarta.annotation.Nullable;

public record CustomerDTO(
    @Nullable String id,
    String name,
    String vat,
    String email,
    String phone) {

    public static final class CustomerDTOBuilder {
        private String id;
        private String name;
        private String vat;
        private String email;
        private String phone;

        private CustomerDTOBuilder() {
        }

        public static CustomerDTOBuilder builder() {
            return new CustomerDTOBuilder();
        }

        public CustomerDTOBuilder withId(String id) {
            this.id = id;
            return this;
        }

        public CustomerDTOBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public CustomerDTOBuilder withVat(String vat) {
            this.vat = vat;
            return this;
        }

        public CustomerDTOBuilder withEmail(String email) {
            this.email = email;
            return this;
        }

        public CustomerDTOBuilder withPhone(String phone) {
            this.phone = phone;
            return this;
        }

        public CustomerDTO build() {
            return new CustomerDTO(id, name, vat, email, phone);
        }
    }
}

