package io.fiap.fastfood.driven.core.domain.order.mapper;

import io.fiap.fastfood.driven.core.domain.model.Order;
import io.fiap.fastfood.driver.controller.order.dto.OrderDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {OrderItemMapper.class, PaymentMapper.class, CustomerMapper.class})
public interface OrderMapper {
    Order domainFromDto(OrderDTO orderDTO);

    OrderDTO dtoFromDomain(Order order);
}
