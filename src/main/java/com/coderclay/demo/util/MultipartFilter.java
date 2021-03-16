//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.coderclay.demo.util;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

@Component
public class MultipartFilter extends OncePerRequestFilter {
    public static final String DEFAULT_MULTIPART_RESOLVER_BEAN_NAME = "filterMultipartResolver";
    private final MultipartResolver defaultMultipartResolver = new StandardServletMultipartResolver();
    private String multipartResolverBeanName = "filterMultipartResolver";

    public MultipartFilter() {
    }

    public void setMultipartResolverBeanName(String multipartResolverBeanName) {
        this.multipartResolverBeanName = multipartResolverBeanName;
    }

    protected String getMultipartResolverBeanName() {
        return this.multipartResolverBeanName;
    }

    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        MultipartResolver multipartResolver = this.lookupMultipartResolver(request);
        HttpServletRequest processedRequest = request;
        if (multipartResolver.isMultipart(request)) {
            if (this.logger.isTraceEnabled()) {
                this.logger.trace("Resolving multipart request");
            }

            processedRequest = multipartResolver.resolveMultipart(request);
        } else if (this.logger.isTraceEnabled()) {
            this.logger.trace("Not a multipart request");
        }

        try {
            filterChain.doFilter((ServletRequest)processedRequest, response);
        } finally {
            if (processedRequest instanceof MultipartHttpServletRequest) {
                multipartResolver.cleanupMultipart((MultipartHttpServletRequest)processedRequest);
            }

        }

    }

    protected MultipartResolver lookupMultipartResolver(HttpServletRequest request) {
        return this.lookupMultipartResolver();
    }

    protected MultipartResolver lookupMultipartResolver() {
        WebApplicationContext wac = WebApplicationContextUtils.getWebApplicationContext(this.getServletContext());
        String beanName = this.getMultipartResolverBeanName();
        if (wac != null && wac.containsBean(beanName)) {
            if (this.logger.isDebugEnabled()) {
                this.logger.debug("Using MultipartResolver '" + beanName + "' for MultipartFilter");
            }

            return (MultipartResolver)wac.getBean(beanName, MultipartResolver.class);
        } else {
            return this.defaultMultipartResolver;
        }
    }
}
