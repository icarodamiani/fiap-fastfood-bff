package io.fiap.fastfood.driven.core.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.WebFluxConfigurer;

@Configuration
@OpenAPIDefinition(info = @Info(title = "Fiap Fastfood - 3SOAT - 16", version = "1.0"))
public class WebConfig implements WebFluxConfigurer {

}
