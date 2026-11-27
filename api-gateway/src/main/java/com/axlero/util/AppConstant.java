package com.axlero.util;

public class AppConstant {

    private AppConstant() {
        // Prevent instantiation
    }

    public static final String[] PUBLIC_URLS = {
            // Public User APIs
            "/api/users/**",

            // Swagger UI
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/swagger-resources/**",
            "/webjars/**",

            // OpenAPI documentation (Gateway & aggregated microservice docs)
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/api/*/v3/api-docs/**"
    };
}
