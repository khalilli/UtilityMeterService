package com.example.utilitymeterservice.config;

import com.example.utilitymeterservice.security.JwtAuthFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {
    @Bean
    public FilterRegistrationBean<JwtAuthFilter> jwtFilter(JwtAuthFilter filter) {
        FilterRegistrationBean<JwtAuthFilter> bean = new FilterRegistrationBean<>();

        bean.setFilter(filter);
        bean.addUrlPatterns("/api/*");
        bean.setOrder(1);

        return bean;
    }
}
