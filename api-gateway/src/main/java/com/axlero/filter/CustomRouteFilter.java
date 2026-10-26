package com.axlero.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class CustomRouteFilter extends AbstractGatewayFilterFactory<Object> {

    public CustomRouteFilter() {
        super(Object.class);
    }

    @Override
    public GatewayFilter apply(Object config) {
        return ((exchange, chain) -> {
            String path = exchange.getRequest().getURI().getPath();
            log.info("Route Filter : Before Routing ,{}", path);
            exchange.getRequest().mutate().header("X-Route-Header", "Added-By-Gateway")
                    .build();
            return chain.filter(exchange)
                    .then(Mono.fromRunnable(()-> {
                        log.info("Route Filter : After Routing ,{}", path);
                    }));
        });
    }
}
