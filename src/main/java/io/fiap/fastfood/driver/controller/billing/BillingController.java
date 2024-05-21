package io.fiap.fastfood.driver.controller.billing;

import static org.slf4j.LoggerFactory.getLogger;

import io.fiap.fastfood.driven.core.domain.billing.port.inbound.BillingUseCase;
import io.fiap.fastfood.driven.core.exception.HttpStatusExceptionConverter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/v1/billing", produces = MediaType.APPLICATION_JSON_VALUE)
public class BillingController {
    private static final Logger LOGGER = getLogger(BillingController.class);
    private final BillingUseCase billingUseCase;
    private final HttpStatusExceptionConverter httpStatusExceptionConverter;

    public BillingController(BillingUseCase billingUseCase,
                             HttpStatusExceptionConverter httpStatusExceptionConverter) {
        this.billingUseCase = billingUseCase;
        this.httpStatusExceptionConverter = httpStatusExceptionConverter;
    }

    @PostMapping(value = "/open")
    @Operation(description = "Open billing day")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Opened"),
        @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
        @ApiResponse(responseCode = "404", description = "Not found", content = @Content)
    })
    public Mono<ResponseEntity<Void>> open() {
        return billingUseCase.open()
            .map(ResponseEntity::ok)
            .defaultIfEmpty(ResponseEntity.noContent().build())
            .onErrorMap(e ->
                new ResponseStatusException(httpStatusExceptionConverter.convert(e), e.getMessage(), e))
            .doOnError(throwable -> LOGGER.error(throwable.getMessage(), throwable));
    }

    @PostMapping(value = "/close")
    @Operation(description = "Close billing day")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Opened"),
        @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
        @ApiResponse(responseCode = "404", description = "Not found", content = @Content)
    })
    public Mono<ResponseEntity<Void>> close() {
        return billingUseCase.close()
            .map(ResponseEntity::ok)
            .defaultIfEmpty(ResponseEntity.noContent().build())
            .onErrorMap(e ->
                new ResponseStatusException(httpStatusExceptionConverter.convert(e), e.getMessage(), e))
            .doOnError(throwable -> LOGGER.error(throwable.getMessage(), throwable));
    }
}
