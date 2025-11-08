package com.erp.erp.security;

import java.util.Arrays;
import java.util.List;

public class PublicEndpoint {

    // Semua endpoint yang tidak memerlukan autentikasi JWT
    public static final List<String> ALL_WHITELIST = Arrays.asList(
            // Auth endpoints
            "/api/auth/login",
            "/api/auth/register",

            // Swagger UI
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/v3/api-docs/**",
            "/v3/api-docs.yaml",

            // Static and root
            "/",
            "/favicon.ico",
            "/error");
}
