package com.student.northwind.filter;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.util.Random;

@Component
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
public class MDCLoggingFilter extends GenericFilterBean {
    private static final Random RANDOM = new Random();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        try {
            //With Integer: RANDOM.nextInt() faster than Math.random. But long is slower.
            //Do not use uuid, it is too slow
            String requestId = String.valueOf(RANDOM.nextInt(Integer.MAX_VALUE));
            MDC.put("requestId", requestId);
            chain.doFilter(request, response);
        } finally{
            MDC.clear();
        }
    }
}
