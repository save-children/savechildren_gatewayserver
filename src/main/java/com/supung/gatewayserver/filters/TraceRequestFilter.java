package com.supung.gatewayserver.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Component
@Order(1)
public class TraceRequestFilter implements GlobalFilter {

    private final FilterUtility filterUtility;

    public TraceRequestFilter(final FilterUtility filterUtility) {
        this.filterUtility = filterUtility;
    }

    private static final Logger logger = LoggerFactory.getLogger(TraceRequestFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        HttpHeaders requestHeaders = exchange.getRequest().getHeaders();
        String correlationID = filterUtility.getCorrelationId(requestHeaders);

        if (Objects.nonNull(correlationID)) {
            logger.debug("correlation-id found in TraceRequestFilter: {}. ",
                    filterUtility.getCorrelationId(requestHeaders));
        } else {
            correlationID = generateCorrelationId();
            exchange = filterUtility.setCorrelationId(exchange, correlationID);
            logger.debug("correlation-id generated in TraceRequestFilter: {}.", correlationID);
        }
        return chain.filter(exchange);
    }

    private String generateCorrelationId() {
        return java.util.UUID.randomUUID().toString();
    }
}
