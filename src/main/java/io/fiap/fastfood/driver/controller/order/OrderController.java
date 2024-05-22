package io.fiap.fastfood.driver.controller.order;

import static org.slf4j.LoggerFactory.getLogger;

import io.fiap.fastfood.driven.core.domain.order.mapper.OrderMapper;
import io.fiap.fastfood.driven.core.domain.order.port.inbound.OrderUseCase;
import io.fiap.fastfood.driven.core.exception.HttpStatusExceptionConverter;
import io.fiap.fastfood.driver.controller.order.dto.OrderDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.slf4j.Logger;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
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
@RequestMapping(value = "/v1/order", produces = MediaType.APPLICATION_JSON_VALUE)
public class OrderController {
    private static final Logger LOGGER = getLogger(OrderController.class);
    private final OrderUseCase orderUseCase;
    private final OrderMapper mapper;
    private final HttpStatusExceptionConverter httpStatusExceptionConverter;

    public OrderController(OrderUseCase orderUseCase,
                           OrderMapper mapper, HttpStatusExceptionConverter httpStatusExceptionConverter) {
        this.orderUseCase = orderUseCase;
        this.mapper = mapper;
        this.httpStatusExceptionConverter = httpStatusExceptionConverter;
    }

    @PostMapping(value = "/")
    @Operation(description = "Create a new order.")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Created"),
        @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
        @ApiResponse(responseCode = "404", description = "Not found", content = @Content)
    })
    public Mono<ResponseEntity<OrderDTO>> createOrder(@Validated @RequestBody OrderDTO orderDTO) {
        return orderUseCase.create(mapper.domainFromDto(orderDTO))
            .map(mapper::dtoFromDomain)
            .map(ResponseEntity::ok)
            .onErrorMap(e ->
                new ResponseStatusException(httpStatusExceptionConverter.convert(e), e.getMessage(), e))
            .doOnError(throwable -> LOGGER.error(throwable.getMessage(), throwable));
    }

    @GetMapping(value = "/")
    @Operation(description = "Find orders paginated.")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Created"),
        @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
        @ApiResponse(responseCode = "404", description = "Not found", content = @Content)
    })
    public Flux<OrderDTO> findOrders(Pageable pageable) {
        return orderUseCase.findAll(pageable)
            .map(mapper::dtoFromDomain)
            .onErrorMap(e ->
                new ResponseStatusException(httpStatusExceptionConverter.convert(e), e.getMessage(), e))
            .doOnError(throwable -> LOGGER.error(throwable.getMessage(), throwable));
    }

    @GetMapping(value = "/{id}")
    @Operation(description = "Find order by id.")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Created"),
        @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
        @ApiResponse(responseCode = "404", description = "Not found", content = @Content)
    })
    public Mono<ResponseEntity<OrderDTO>> findOrders(String id) {
        return orderUseCase.findById(id)
            .map(mapper::dtoFromDomain)
            .map(ResponseEntity::ok)
            .onErrorMap(e ->
                new ResponseStatusException(httpStatusExceptionConverter.convert(e), e.getMessage(), e))
            .doOnError(throwable -> LOGGER.error(throwable.getMessage(), throwable));
    }
}
