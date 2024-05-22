package io.fiap.fastfood.driven.core.configuration;

import io.fiap.fastfood.driven.core.configuration.converter.CustomGrantedAuthoritiesConverter;
import io.fiap.fastfood.driven.core.exception.TechnicalException;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.util.matcher.NegatedServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import reactor.core.publisher.Mono;

@Configuration
public class SecurityConfiguration {

    @Bean
    public SecurityWebFilterChain securityWebFilterChainOidc(final ServerHttpSecurity http) {
        return http
            .csrf(ServerHttpSecurity.CsrfSpec::disable)
            .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
            .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
            .logout(ServerHttpSecurity.LogoutSpec::disable)
            .requestCache(ServerHttpSecurity.RequestCacheSpec::disable)
            .exceptionHandling(Customizer.withDefaults())
            .cors(corsSpec -> corsSpec.configurationSource(corsConfigurationSource()))
            .authorizeExchange(exchanges ->
                exchanges
                    .matchers(new NegatedServerWebExchangeMatcher(
                        ServerWebExchangeMatchers.pathMatchers("/actuator/**",
                            "/webjars/**", "/swagger*/**", "/api-docs/**", "/error")))
                    .permitAll()
                    .anyExchange()
                    .authenticated()
            )
            .oauth2ResourceServer(oAuth2ResourceServerSpec ->
                oAuth2ResourceServerSpec
                    .authenticationEntryPoint((swe, t) -> Mono.error(new TechnicalException("not authenticated")))
                    .accessDeniedHandler((swe, t) -> Mono.error(new TechnicalException("not enough permission")))
                    .jwt(jwtSpec -> jwtSpec.jwtAuthenticationConverter(new CustomGrantedAuthoritiesConverter()))
            )
            .build();
    }

    private CorsConfigurationSource corsConfigurationSource() {
        return exchange -> {
            CorsConfiguration config = new CorsConfiguration();
            config.setAllowCredentials(true);
            config.setAllowedOrigins(List.of("*"));
            config.setAllowedMethods(List.of("*"));
            config.setMaxAge(Long.valueOf(86400));
            return config;
        };
    }
}
