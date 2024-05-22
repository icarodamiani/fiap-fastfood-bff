package io.fiap.fastfood.driven.core.configuration.converter;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import reactor.core.publisher.Mono;

public class CustomGrantedAuthoritiesConverter implements Converter<Jwt, Mono<AbstractAuthenticationToken>> {
    private final JwtGrantedAuthoritiesConverter defaultGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

    private static Collection<? extends GrantedAuthority> extractCognitoGroups(final Jwt jwt) {
        Collection<String> groups = jwt.getClaim("cognito:groups");

        if (groups != null)
            return groups.stream()
                .map(x -> new SimpleGrantedAuthority("ROLE_" + x))
                .collect(Collectors.toSet());
        return Collections.emptySet();
    }

    @Override
    public Mono<AbstractAuthenticationToken> convert(final Jwt source) {
        Collection<GrantedAuthority> authorities = Stream.concat(
                defaultGrantedAuthoritiesConverter.convert(source).stream(),
                extractCognitoGroups(source).stream()
            )
            .collect(Collectors.toSet());
        return Mono.just(new JwtAuthenticationToken(source, authorities));
    }
}
