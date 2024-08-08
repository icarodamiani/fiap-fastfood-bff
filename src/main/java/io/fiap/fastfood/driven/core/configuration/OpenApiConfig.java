package io.fiap.fastfood.driven.core.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.Scopes;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    private static final String OAUTH_SCHEME = "auth";

    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String authURL;

    @Bean
    public OpenAPI customizeOpenAPI() {
        return new OpenAPI()
            .addSecurityItem(new SecurityRequirement()
                .addList(OAUTH_SCHEME))
            .components(new Components()
                .addSecuritySchemes(OAUTH_SCHEME, createOAuthScheme()))
            .addSecurityItem(new SecurityRequirement().addList(OAUTH_SCHEME));
    }

    private SecurityScheme createOAuthScheme() {
        return new SecurityScheme().name("Bearer Authentication OIDC").type(SecurityScheme.Type.OAUTH2).flows(createOAuthFlows());
    }

    private OAuthFlows createOAuthFlows() {
        final var oauthFlow = new OAuthFlow()
            .authorizationUrl(authURL + "/protocol/openid-connect" + "/auth")
            .refreshUrl(authURL + "/protocol/openid-connect" + "/token")
            .tokenUrl(authURL + "/protocol/openid-connect" + "/token")
            .scopes(new Scopes());
        return new OAuthFlows().authorizationCode(oauthFlow);
    }
}
