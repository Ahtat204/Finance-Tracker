package org.asue24.financetrackerbackend.security;

import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.redis.jedis.cas.JedisBasedProxyManager;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.function.Supplier;

@Component
@Order(3)
public class IdBasedRateLimitingFilter extends OncePerRequestFilter {
    private final JedisBasedProxyManager jedisBasedProxyManager;
    private final Supplier<BucketConfiguration> bucketConfigurationSupplier;

    @Autowired
    public IdBasedRateLimitingFilter(JedisBasedProxyManager jedisBasedProxyManager, Supplier<BucketConfiguration> bucketConfigurationSupplier) {
        this.jedisBasedProxyManager = jedisBasedProxyManager;
        this.bucketConfigurationSupplier = bucketConfigurationSupplier;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var clientId = request.getAttribute("id");//plan to do better here
        if (clientId != null) {
            var bucket = jedisBasedProxyManager.builder().build(clientId,bucketConfigurationSupplier);
            if(bucket.tryConsume(1)) {
                filterChain.doFilter(request, response);
            }
            else {
                response.setStatus(429);
                response.setContentType("application/json");
                response.getWriter().write("{\"error\":\"Too many requests from this ID\"}");
            }
        }
        doFilter(request, response, filterChain);

    }
}
