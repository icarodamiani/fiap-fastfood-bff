package io.fiap.fastfood.driven.core.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(title = "Fastfood BFF", version = "1.0"))
@SecurityScheme(
    name = "Bearer Authentication OIDC",
    type = SecuritySchemeType.OPENIDCONNECT,
    bearerFormat = "JWT",
    scheme = "bearer"
)
public class OpenApiConfig {
}
