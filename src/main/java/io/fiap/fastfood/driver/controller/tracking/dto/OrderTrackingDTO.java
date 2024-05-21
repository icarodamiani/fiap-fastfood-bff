package io.fiap.fastfood.driver.controller.tracking.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record OrderTrackingDTO(
        @Schema(accessMode = Schema.AccessMode.READ_ONLY)
        String id,
        @NotNull
        String orderId,
        @NotNull
        String orderNumber,
        @NotNull
        OrderTrackingStatusTypeDTO orderStatus,
        @NotNull
        OrderTrackingRoleTypeDTO role,
        @Schema(accessMode = Schema.AccessMode.READ_ONLY)
        @JsonFormat(pattern = "yyyy-M-d'T'HH:mm:ss.yyyy'Z'")
        LocalDateTime orderDateTime,

        Long orderTimeSpent
) {
}
