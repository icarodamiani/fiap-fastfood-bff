package io.fiap.fastfood.driven.core.domain.product.mapper;

import io.fiap.fastfood.driven.core.domain.model.ProductType;
import io.fiap.fastfood.driver.controller.product.dto.ProductTypeDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductTypeMapper {
    ProductType domainFromDto(ProductTypeDTO productTypeDTO);

    ProductTypeDTO dtoFromDomain(ProductType productType);
}
