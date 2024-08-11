package io.fiap.fastfood.driver.controller.customer;

import static org.slf4j.LoggerFactory.getLogger;

import io.fiap.fastfood.driven.core.domain.customer.port.inbound.CustomerUseCase;
import io.fiap.fastfood.driven.core.exception.HttpStatusExceptionConverter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@RestController
@SecurityRequirement(name = "Bearer Authentication OIDC")
@RequestMapping(value = "/v1/customers", produces = MediaType.APPLICATION_JSON_VALUE)
public class CustomerController {
    private static final Logger LOGGER = getLogger(CustomerController.class);
    private final CustomerUseCase customerUseCase;
    private final HttpStatusExceptionConverter httpStatusExceptionConverter;

    public CustomerController(CustomerUseCase customerUseCase, HttpStatusExceptionConverter httpStatusExceptionConverter) {
        this.customerUseCase = customerUseCase;
        this.httpStatusExceptionConverter = httpStatusExceptionConverter;
    }


    @DeleteMapping("/{vat}")
    @Operation(description = "Delete customer by VAT (CPF/CNPJ).")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Created"),
        @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
        @ApiResponse(responseCode = "404", description = "Not found", content = @Content)
    })
    public Mono<Void> createOrder(@PathVariable String vat) {
        return customerUseCase.deleteCustomer(vat)
            .onErrorMap(e ->
                new ResponseStatusException(httpStatusExceptionConverter.convert(e), e.getMessage(), e))
            .doOnError(throwable -> LOGGER.error(throwable.getMessage(), throwable));
    }
}
