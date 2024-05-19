package io.fiap.fastfood.driven.core.domain.order.mapper;

import io.fiap.fastfood.driven.core.domain.model.OrderItem;
import io.fiap.fastfood.driver.controller.order.dto.OrderItemDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {
    OrderItem domainFromDto(OrderItemDTO orderItemDTO);

    OrderItemDTO dtoFromDomain(OrderItem orderItem);
}
