package com.hotel.reservation.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.util.StringUtils;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
@Profile("!test")
public class SecurityConfig {

/*
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest().permitAll() // 👈 allow all requests
                )
                .csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }
*/

    @Bean
    public SecurityFilterChain securityFilterChain(
            final HttpSecurity http,
            final Environment environment) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers(HttpMethod.POST, "/hotel/**").hasAnyAuthority(
                                "ROLE_create:hotel_reservation", "ROLE_super_user:hotel_reservation",
                                "SCOPE_create:hotel_reservation", "SCOPE_super_user:hotel_reservation")
                        .requestMatchers(HttpMethod.GET, "/hotel/**").hasAnyAuthority(
                                "ROLE_read:hotel_reservation", "ROLE_super_user:hotel_reservation",
                                "SCOPE_read:hotel_reservation", "SCOPE_super_user:hotel_reservation")
                        .requestMatchers(HttpMethod.PUT, "/hotel/**").hasAnyAuthority(
                                "ROLE_update:hotel_reservation", "ROLE_super_user:hotel_reservation",
                                "SCOPE_update:hotel_reservation", "SCOPE_super_user:hotel_reservation")
                        .requestMatchers(HttpMethod.DELETE, "/hotel/**").hasAnyAuthority(
                                "ROLE_delete:hotel_reservation", "ROLE_super_user:hotel_reservation",
                                "SCOPE_delete:hotel_reservation", "SCOPE_super_user:hotel_reservation")
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/v3/api-docs/**"
                        ).permitAll()
                        .anyRequest().permitAll() // allow all other requests
                );

        if (hasJwtConfiguration(environment)) {
            http.oauth2ResourceServer(oauth2 -> oauth2
                    .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthConverter()))
            );
        }

        return http.build();
    }

@Bean
public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration config = new CorsConfiguration();

    config.setAllowedOrigins(List.of("http://localhost:3000"));
    config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    config.setAllowedHeaders(List.of("*"));
    config.setAllowCredentials(true); // if you're sending cookies or Authorization headers

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config);

    return source;
}

    private JwtAuthenticationConverter jwtAuthConverter() {
        JwtGrantedAuthoritiesConverter scopeConverter = new JwtGrantedAuthoritiesConverter();
        scopeConverter.setAuthorityPrefix("SCOPE_");
        scopeConverter.setAuthoritiesClaimName("scope");

        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            Collection<GrantedAuthority> authorities = new ArrayList<>(scopeConverter.convert(jwt));
            authorities.addAll(extractRoleAuthorities(jwt));
            return authorities;
        });
        return converter;
    }

    private Collection<GrantedAuthority> extractRoleAuthorities(Jwt jwt) {
        Set<String> roleNames = new LinkedHashSet<>();

        // Spring Authorization Server custom claims often place roles directly on the token.
        roleNames.addAll(extractRolesFromCollectionClaim(jwt, "roles"));
        roleNames.addAll(extractRolesFromCollectionClaim(jwt, "authorities"));

        // Keycloak realm roles.
        roleNames.addAll(extractRoles(extractClaimAsMap(jwt, "realm_access")));

        // Keycloak client roles can be present under the token audience/client.
        Map<String, Object> resourceAccess = extractClaimAsMap(jwt, "resource_access");
        roleNames.addAll(extractRoles(resourceAccess, "account"));
        roleNames.addAll(extractRoles(resourceAccess, jwt.getClaimAsString("azp")));
        roleNames.addAll(extractRoles(resourceAccess, jwt.getClaimAsString("client_id")));

        return roleNames.stream()
                .filter(StringUtils::hasText)
                .map(this::normalizeRoleAuthority)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
    }

    private List<String> extractRolesFromCollectionClaim(Jwt jwt, String claimName) {
        Object claimValue = jwt.getClaims().get(claimName);
        if (!(claimValue instanceof Collection<?> roleCollection)) {
            return Collections.emptyList();
        }

        return roleCollection.stream()
                .filter(Objects::nonNull)
                .map(String::valueOf)
                .toList();
    }

    private Map<String, Object> extractClaimAsMap(Jwt jwt, String claimName) {
        Object claim = jwt.getClaims().get(claimName);
        if (!(claim instanceof Map<?, ?> claimMap)) {
            return Collections.emptyMap();
        }

        return claimMap.entrySet().stream()
                .filter(entry -> entry.getKey() != null)
                .collect(Collectors.toMap(entry -> String.valueOf(entry.getKey()), Map.Entry::getValue));
    }

    private String normalizeRoleAuthority(String roleOrAuthority) {
        if (roleOrAuthority.startsWith("ROLE_") || roleOrAuthority.startsWith("SCOPE_")) {
            return roleOrAuthority;
        }
        return "ROLE_" + roleOrAuthority;
    }

    private List<String> extractRoles(Map<String, Object> claimSection) {
        if (claimSection == null) {
            return Collections.emptyList();
        }

        Object roles = claimSection.get("roles");
        if (!(roles instanceof Collection<?> roleCollection)) {
            return Collections.emptyList();
        }

        return roleCollection.stream()
                .filter(Objects::nonNull)
                .map(String::valueOf)
                .toList();
    }

    private List<String> extractRoles(Map<String, Object> resourceAccess, String clientId) {
        if (resourceAccess == null || !StringUtils.hasText(clientId)) {
            return Collections.emptyList();
        }

        Object clientSection = resourceAccess.get(clientId);
        if (!(clientSection instanceof Map<?, ?> clientSectionMap)) {
            return Collections.emptyList();
        }

        Object roles = clientSectionMap.get("roles");
        if (!(roles instanceof Collection<?> roleCollection)) {
            return Collections.emptyList();
        }

        return roleCollection.stream()
                .filter(Objects::nonNull)
                .map(String::valueOf)
                .toList();
    }

    private boolean hasJwtConfiguration(Environment environment) {
        return StringUtils.hasText(environment.getProperty("spring.security.oauth2.resourceserver.jwt.issuer-uri"))
                || StringUtils.hasText(environment.getProperty("spring.security.oauth2.resourceserver.jwt.jwk-set-uri"));
    }
}
