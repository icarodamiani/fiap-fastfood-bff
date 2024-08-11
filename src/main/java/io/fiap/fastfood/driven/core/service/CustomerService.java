package io.fiap.fastfood.driven.core.service;

import io.fiap.fastfood.driven.core.domain.customer.port.inbound.CustomerUseCase;
import io.fiap.fastfood.driven.core.domain.customer.port.outbound.CustomerPort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class CustomerService implements CustomerUseCase {

    private final CustomerPort customerPort;

    public CustomerService(CustomerPort customerPort) {
        this.customerPort = customerPort;
    }

    @Override
    public Mono<Void> deleteCustomer(String vat) {
        return customerPort.deleteCustomer(vat);
    }

}
