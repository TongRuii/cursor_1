package com.riskcontrol.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

/**
 * 风控服务配置属性
 */
@Data
@Configuration
@RefreshScope
@ConfigurationProperties(prefix = "risk-control")
public class RiskControlProperties {
    
    /**
     * 是否启用风控服务
     */
    private boolean enabled = true;
    
    /**
     * 第三方服务配置
     */
    private Map<String, ThirdPartyConfig> thirdParty;
    
    /**
     * 启用的第三方服务列表
     */
    private List<String> enabledServices;
    
    /**
     * 第三方服务配置
     */
    @Data
    public static class ThirdPartyConfig {
        /**
         * 服务名称
         */
        private String name;
        
        /**
         * 是否启用
         */
        private boolean enabled = true;
        
        /**
         * 服务地址
         */
        private String url;
        
        /**
         * 认证信息
         */
        private Auth auth;
        
        /**
         * 超时时间（毫秒）
         */
        private int timeout = 5000;
        
        /**
         * 重试次数
         */
        private int retryTimes = 3;
        
        /**
         * 权重（用于多服务加权选择）
         */
        private int weight = 1;
    }
    
    /**
     * 认证信息
     */
    @Data
    public static class Auth {
        /**
         * 认证类型（如：apiKey, token等）
         */
        private String type;
        
        /**
         * API Key
         */
        private String apiKey;
        
        /**
         * API Secret
         */
        private String apiSecret;
        
        /**
         * Token
         */
        private String token;
    }
} 