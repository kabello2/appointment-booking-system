package com.javaguru.appointmentbooking.config;



import com.javaguru.appointmentbooking.utils.errorhandling.RateLimitExceededException;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class AuthenticationRateLimitFilter
        extends OncePerRequestFilter {

    private final RateLimitProperties rateLimitProperties;

    private final Map<String, Bucket> loginBuckets =
            new ConcurrentHashMap<>();

    private final Map<String, Bucket> registrationBuckets =
            new ConcurrentHashMap<>();

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String path =
                request.getRequestURI();

        String clientKey =
                request.getRemoteAddr();

        if (
                "/api/v1/auth/login".equals(path)
                        && "POST".equalsIgnoreCase(request.getMethod())
        ) {

            consumeToken(
                    loginBuckets,
                    clientKey,
                    rateLimitProperties
                            .login()
                            .capacity(),
                    Duration.ofMinutes(
                            rateLimitProperties
                                    .login()
                                    .durationMinutes()
                    )
            );
        }

        if (
                "/api/v1/auth/register".equals(path)
                        && "POST".equalsIgnoreCase(request.getMethod())
        ) {

            consumeToken(
                    registrationBuckets,
                    clientKey,
                    rateLimitProperties
                            .registration()
                            .capacity(),
                    Duration.ofHours(
                            rateLimitProperties
                                    .registration()
                                    .durationHours()
                    )
            );
        }

        filterChain.doFilter(
                request,
                response
        );
    }

    private void consumeToken(
            Map<String, Bucket> bucketCache,
            String key,
            long capacity,
            Duration refillPeriod
    ) {

        Bucket bucket =
                bucketCache.computeIfAbsent(
                        key,
                        ignored -> createBucket(
                                capacity,
                                refillPeriod
                        )
                );

        if (!bucket.tryConsume(1)) {

            throw new RateLimitExceededException(
                    "Too many requests. Please try again later."
            );
        }
    }

    private Bucket createBucket(
            long capacity,
            Duration refillPeriod
    ) {

        Bandwidth bandwidth =
                Bandwidth.builder()
                        .capacity(capacity)
                        .refillGreedy(
                                capacity,
                                refillPeriod
                        )
                        .build();

        return Bucket.builder()
                .addLimit(bandwidth)
                .build();
    }
}