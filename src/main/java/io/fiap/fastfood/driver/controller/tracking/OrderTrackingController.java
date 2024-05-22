package io.fiap.fastfood.driver.controller.tracking;

import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.MediaType.TEXT_EVENT_STREAM_VALUE;

import io.fiap.fastfood.driven.core.domain.tracking.mapper.OrderTrackingMapper;
import io.fiap.fastfood.driven.core.domain.tracking.port.inbound.OrderTrackingUseCase;
import io.fiap.fastfood.driven.core.exception.HttpStatusExceptionConverter;
import io.fiap.fastfood.driver.controller.tracking.dto.OrderTrackingDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@SecurityRequirement(name = "Bearer Authentication OIDC")
@RequestMapping(value = "/v1/tracking", produces = MediaType.APPLICATION_JSON_VALUE)
public class OrderTrackingController {

    private static final Logger LOGGER = getLogger(OrderTrackingController.class);

    private final OrderTrackingUseCase orderTrackingUseCase;
    private final OrderTrackingMapper orderTrackingMapper;
    private final HttpStatusExceptionConverter httpStatusExceptionConverter;

    public OrderTrackingController(OrderTrackingUseCase orderTrackingUseCase,
                                   OrderTrackingMapper orderTrackingMapper,
                                   HttpStatusExceptionConverter httpStatusExceptionConverter) {
        this.orderTrackingUseCase = orderTrackingUseCase;
        this.orderTrackingMapper = orderTrackingMapper;
        this.httpStatusExceptionConverter = httpStatusExceptionConverter;
    }

    @PostMapping
    @Operation(description = "Create a order tracking")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Added"),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "409", description = "Duplicated", content = @Content)
    })
    public Mono<ResponseEntity<OrderTrackingDTO>> create(@Validated @RequestBody OrderTrackingDTO body) {
        return orderTrackingUseCase.create(orderTrackingMapper.domainFromDto(body))
                .map(orderTrackingMapper::dtoFromDomain)
                .map(b -> ResponseEntity.status(HttpStatus.CREATED).body(b))
                .onErrorMap(e ->
                        new ResponseStatusException(httpStatusExceptionConverter.convert(e), e.getMessage(), e))
                .doOnError(throwable -> LOGGER.error(throwable.getMessage(), throwable));
    }

    @GetMapping("/{orderId}")
    @Operation(description = "Find order by id")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content)
    })
    public Mono<OrderTrackingDTO> findByOrderId(@PathVariable String orderId) {
        return orderTrackingUseCase.findByOrderId(orderId)
                .map(orderTrackingMapper::dtoFromDomain)
                .onErrorMap(e ->
                        new ResponseStatusException(httpStatusExceptionConverter.convert(e), e.getMessage(), e))
                .doOnError(throwable -> LOGGER.error(throwable.getMessage(), throwable));
    }

    @GetMapping(value = "/report/{role}", produces = TEXT_EVENT_STREAM_VALUE)
    @Operation(description = "Tracking report")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200"),
        @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
        @ApiResponse(responseCode = "404", description = "Not found", content = @Content)
    })
    public Flux<OrderTrackingDTO> find(String role) {
        return orderTrackingUseCase.find(role)
            .map(orderTrackingMapper::dtoFromDomain)
            .onErrorMap(e ->
                new ResponseStatusException(httpStatusExceptionConverter.convert(e), e.getMessage(), e))
            .doOnError(throwable -> LOGGER.error(throwable.getMessage(), throwable));
    }

}
