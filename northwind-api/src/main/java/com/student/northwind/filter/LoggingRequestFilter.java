package com.student.northwind.filter;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

@Component
public class LoggingRequestFilter extends GenericFilterBean {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        if (request.getMethod().equals("OPTIONS")) {
            try {
                response.getWriter().print("OK");
                response.getWriter().flush();
            } catch (IOException e) {
                logger.error(e.toString(), e);
            }
            return;
        }

        String url = getUrl(request);
        String realIp = getRealIp(request);

        logger.info("Request: Real: " + realIp + " - Remote: " + request.getRemoteAddr() + " - Url: " + url);
        long start = System.currentTimeMillis();
        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            logger.info("Response: Real: " + realIp + " - Remote: " + request.getRemoteAddr() + " - Url: " + url + " - Status:" + response.getStatus() + " - Duration: " + (System.currentTimeMillis() - start));
        }
    }

    private String getUrl(HttpServletRequest request) {
        var url = request.getRequestURL().toString();
        var query = request.getQueryString();
        if (!StringUtils.isEmpty(query)) {
            url = url + "?" + query;
        }

        var idxAuth = (url = url.toLowerCase()).indexOf("Authorization");
        return idxAuth > 0 ? url.substring(0, idxAuth) + "[SENSITIVE]" : url;
    }

    private String getRealIp(HttpServletRequest request) {
        Enumeration<String> _enum = request.getHeaderNames();
        String forwardFor = null;

        while (_enum.hasMoreElements()) {
            String header = _enum.nextElement();
            if ("x-real-ip".equalsIgnoreCase(header)) {
                return request.getHeader(header);
            } else if ("x-forwarded-for".equalsIgnoreCase(header)) {
                forwardFor = request.getHeader(header);
            }
        }
        return StringUtils.isEmpty(forwardFor) ? request.getRemoteAddr() : forwardFor;
    }
}
