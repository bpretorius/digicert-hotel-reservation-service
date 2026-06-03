package com.hotel.reservation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = ReservationApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("local")
class SecurityTokenCompatibilityTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private JwtDecoder jwtDecoder;

    @BeforeEach
    void setUp() {
        when(jwtDecoder.decode(anyString())).thenAnswer(invocation -> {
            String token = invocation.getArgument(0);
            return switch (token) {
                case "keycloak-token" -> keycloakJwt(token);
                case "sas-token" -> sasJwt(token);
                default -> insufficientJwt(token);
            };
        });
    }

    @Test
    void keycloakTokenAllowsReadReservationList() throws Exception {
        mvc.perform(get("/hotel/reservation/list")
                        .header("Authorization", "Bearer keycloak-token"))
                .andExpect(status().isOk());
    }

    @Test
    void sasTokenAllowsReadReservationList() throws Exception {
        mvc.perform(get("/hotel/reservation/list")
                        .header("Authorization", "Bearer sas-token"))
                .andExpect(status().isOk());
    }

    @Test
    void tokenWithoutReadAuthorityIsForbidden() throws Exception {
        mvc.perform(get("/hotel/reservation/list")
                        .header("Authorization", "Bearer no-read-token"))
                .andExpect(status().isForbidden());
    }

    private Jwt keycloakJwt(String tokenValue) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("sub", "keycloak-user");
        claims.put("realm_access", Map.of("roles", List.of("read:hotel_reservation")));
        claims.put("resource_access", Map.of("account", Map.of("roles", List.of("view-profile"))));

        return baseJwt(tokenValue, claims);
    }

    private Jwt sasJwt(String tokenValue) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("sub", "sas-user");
        claims.put("roles", List.of("read:hotel_reservation"));

        return baseJwt(tokenValue, claims);
    }

    private Jwt insufficientJwt(String tokenValue) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("sub", "no-read-user");
        claims.put("roles", List.of("create:hotel_reservation"));

        return baseJwt(tokenValue, claims);
    }

    private Jwt baseJwt(String tokenValue, Map<String, Object> claims) {
        Instant now = Instant.now();
        return Jwt.withTokenValue(tokenValue)
                .header("alg", "none")
                .claims(c -> c.putAll(claims))
                .issuedAt(now)
                .expiresAt(now.plusSeconds(3600))
                .build();
    }
}

