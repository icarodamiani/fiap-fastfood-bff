package io.fiap.fastfood.driver.controller.tracking.dto;

public enum OrderTrackingStatusTypeDTO {
    WAITING_PAYMENT(1),
    PAYMENT_CONFIRMED(2),
    PREPARING(4),
    READY(5),
    FINISHED(3),
    CANCELED(0);

    private Integer value;
    OrderTrackingStatusTypeDTO(int value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
}
