package io.fiap.fastfood.driven.core.domain.tracking.mapper;

import io.fiap.fastfood.driven.core.domain.model.OrderTracking;
import io.fiap.fastfood.driver.controller.tracking.dto.OrderTrackingDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface OrderTrackingMapper {

    @Mapping(source = "orderTrackingDTO", target = "orderStatusValue", qualifiedByName = "orderStatusValue")
    OrderTracking domainFromDto(OrderTrackingDTO orderTrackingDTO);

    OrderTrackingDTO dtoFromDomain(OrderTracking orderTracking);

    @Named("orderStatusValue")
    default Integer orderStatusValue(OrderTrackingDTO orderTracking) {
        return orderTracking.orderStatus().getValue();
    }
}
