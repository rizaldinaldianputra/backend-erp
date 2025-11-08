package com.erp.erp.security;

import java.util.List;

public class PublicEndpoint {

    public static final List<String> ALL_WHITELIST = List.of(
            "/api/auth/login",
            "/api/auth/register",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/v3/api-docs/**",
            "/v3/api-docs.yaml",
            "/swagger-ui/index.html");
}
