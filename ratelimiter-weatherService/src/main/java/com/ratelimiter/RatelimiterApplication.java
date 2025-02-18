package com.ratelimiter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import reactor.core.publisher.Mono;

@SpringBootApplication
public class RatelimiterApplication {

	private static final Logger logger = LoggerFactory.getLogger(RatelimiterApplication.class);
public static void main(String[] args) {
    // Start the Spring Boot application
    SpringApplication.run(RatelimiterApplication.class, args);
}

@Bean
public KeyResolver ipKeyResolver() {
    // Resolve the client's IP address for rate limiting
    return exchange -> {
        String ipAddress = exchange.getRequest().getRemoteAddress().getAddress().getHostAddress();
        // Log the resolved IP address
        logger.info("Resolved IP address: " + ipAddress);
        return Mono.just(ipAddress);
    };
}
}
