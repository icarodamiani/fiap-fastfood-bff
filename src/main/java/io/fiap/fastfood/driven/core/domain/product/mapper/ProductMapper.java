package io.fiap.fastfood.driven.core.domain.product.mapper;

import io.fiap.fastfood.driven.core.domain.model.Product;
import io.fiap.fastfood.driver.controller.product.dto.ProductDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {ProductTypeMapper.class})
public interface ProductMapper {
    Product domainFromDto(ProductDTO productDTO);

    ProductDTO dtoFromDomain(Product product);
}
