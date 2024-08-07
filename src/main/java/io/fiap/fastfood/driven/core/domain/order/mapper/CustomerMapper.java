package io.fiap.fastfood.driven.core.domain.order.mapper;

import io.fiap.fastfood.driven.core.domain.model.Customer;
import io.fiap.fastfood.driver.controller.order.dto.CustomerDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    Customer domainFromDto(CustomerDTO customerDTO);
}