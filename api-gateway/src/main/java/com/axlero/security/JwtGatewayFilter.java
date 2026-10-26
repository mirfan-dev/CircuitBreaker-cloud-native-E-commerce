package com.axlero.security;

import com.axlero.util.AppConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtGatewayFilter implements GatewayFilter, Ordered {

    private final JwtToken jwtToken;


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        String path= exchange.getRequest().getURI().getPath();
        String method = exchange.getRequest().getMethod().name();
        if((Arrays.stream(AppConstant.PUBLIC_URLS).anyMatch(path::contains))){
            return chain.filter(exchange);
        }

        String authHeader= exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader == null && authHeader.startsWith("Bearer ")){
            return unauthorized(exchange);
        }
        String token = authHeader.substring(7);
        if (!jwtToken.validateToken(token)){
            return unauthorized(exchange);
        }

        String role = jwtToken.getRole(token);

        if (role != null && role.startsWith("ROLE_")) {
            role = role.substring(5);
        }
        if(!isAuthorized(role, path, method)) {
            return forbidden(exchange);
        }

        String userId = jwtToken.getUserId(token);
        ServerHttpRequest modifiedRequest= exchange.getRequest().mutate().header("X-user-Id", userId).header("X-User-Role",role).build();
        return chain.filter(exchange.mutate().request(modifiedRequest).build());
    }

    @Override
    public int getOrder() {
        return -1;
    }

    private Mono<Void> unauthorized(ServerWebExchange exchange){
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }

    private Mono<Void> forbidden(ServerWebExchange exchange){
        exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
        return exchange.getResponse().setComplete();
    }

    private static final Map<String, Map<String, List<String>>> ROLE_API_PERMISSION = Map.of(
            "ADMIN", Map.of(
                    "GET", List.of("/api/products", "/api/categories", "/api/orders", "/api/payments"),
                    "POST", List.of("/api/products", "/api/categories", "/api/inventory"),
                    "PUT", List.of("/api/products", "/api/categories", "/api/payments"),
                    "DELETE", List.of("/api/products", "/api/categories")
            ),
            "USER", Map.of(
                    "GET", List.of("/api/products", "/api/categories", "/api/inventory", "/api/payments"),
                    "POST", List.of("/api/categories", "/api/products", "/api/order", "/api/payment")
            )
    );

    private boolean isAuthorized(String role, String path, String method) {
        Map<String, List<String>> permission  = ROLE_API_PERMISSION.get(role);
        if(permission == null) return false;
        List<String> allowedPaths = permission.get(method);
        if(allowedPaths==null) return false;
        return allowedPaths.stream().anyMatch(path:: startsWith);
    }
}
