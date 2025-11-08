package com.erp.erp.config.security;

import com.erp.erp.dto.ApiResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException) throws IOException {

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);

        String message = (String) request.getAttribute("errorMessage");
        if (message == null) {
            message = "Access denied: missing, invalid, or expired token";
        }

        ApiResponseDto<String> apiResponse = ApiResponseDto.<String>builder()
                .status("error")
                .message(message)
                .data(null)
                .build();

        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
    }
}
