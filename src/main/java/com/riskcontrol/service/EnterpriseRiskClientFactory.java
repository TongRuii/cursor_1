package com.riskcontrol.service;

import com.riskcontrol.client.EnterpriseRiskClient;
import com.riskcontrol.config.RiskControlProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 企业风控客户端工厂
 */
@Slf4j
@Component
@RefreshScope
public class EnterpriseRiskClientFactory {
    
    @Autowired
    private RiskControlProperties properties;
    
    @Autowired
    private List<EnterpriseRiskClient> clients;
    
    /**
     * 客户端映射
     */
    private final Map<String, EnterpriseRiskClient> clientMap = new HashMap<>();
    
    /**
     * 初始化
     */
    @PostConstruct
    public void init() {
        // 初始化客户端映射
        for (EnterpriseRiskClient client : clients) {
            clientMap.put(client.getServiceName(), client);
            log.info("注册企业风控客户端：{}", client.getServiceName());
        }
    }
    
    /**
     * 获取客户端
     *
     * @return 客户端
     */
    public EnterpriseRiskClient getClient() {
        // 检查是否启用风控服务
        if (!properties.isEnabled()) {
            log.warn("风控服务未启用");
            return null;
        }
        
        // 获取启用的服务列表
        List<String> enabledServices = properties.getEnabledServices();
        if (enabledServices == null || enabledServices.isEmpty()) {
            log.warn("未配置启用的风控服务");
            return null;
        }
        
        // 构建可用客户端列表
        List<EnterpriseRiskClient> availableClients = new ArrayList<>();
        for (String serviceName : enabledServices) {
            EnterpriseRiskClient client = clientMap.get(serviceName);
            if (client != null) {
                // 检查服务是否启用
                RiskControlProperties.ThirdPartyConfig config = properties.getThirdParty().get(serviceName);
                if (config != null && config.isEnabled()) {
                    // 根据权重添加客户端
                    for (int i = 0; i < config.getWeight(); i++) {
                        availableClients.add(client);
                    }
                }
            }
        }
        
        // 随机选择一个客户端
        if (!availableClients.isEmpty()) {
            int index = ThreadLocalRandom.current().nextInt(availableClients.size());
            EnterpriseRiskClient client = availableClients.get(index);
            log.info("选择企业风控客户端：{}", client.getServiceName());
            return client;
        }
        
        log.warn("没有可用的风控服务");
        return null;
    }
    
    /**
     * 获取指定名称的客户端
     *
     * @param serviceName 服务名称
     * @return 客户端
     */
    public EnterpriseRiskClient getClient(String serviceName) {
        // 检查是否启用风控服务
        if (!properties.isEnabled()) {
            log.warn("风控服务未启用");
            return null;
        }
        
        // 获取指定客户端
        EnterpriseRiskClient client = clientMap.get(serviceName);
        if (client != null) {
            // 检查服务是否启用
            RiskControlProperties.ThirdPartyConfig config = properties.getThirdParty().get(serviceName);
            if (config != null && config.isEnabled()) {
                log.info("选择企业风控客户端：{}", serviceName);
                return client;
            }
        }
        
        log.warn("指定的风控服务不可用：{}", serviceName);
        return null;
    }
} 