package com.riskcontrol.config;

import feign.Logger;
import feign.Request;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Feign客户端配置
 */
@Configuration
public class FeignConfig {
    
    /**
     * 配置Feign日志级别
     */
    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }
    
    /**
     * 配置Feign请求超时时间
     */
    @Bean
    public Request.Options options() {
        return new Request.Options(5000, 10000);
    }
    
    /**
     * 配置Feign请求拦截器，添加通用请求头
     */
    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            requestTemplate.header("Content-Type", "application/json;charset=UTF-8");
            requestTemplate.header("Accept", "application/json");
        };
    }
} 