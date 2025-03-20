package com.riskcontrol.service.impl;

import com.riskcontrol.client.EnterpriseRiskClient;
import com.riskcontrol.model.EnterpriseInfo;
import com.riskcontrol.service.EnterpriseRiskClientFactory;
import com.riskcontrol.service.EnterpriseRiskService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 企业风控服务实现
 */
@Slf4j
@Service
public class EnterpriseRiskServiceImpl implements EnterpriseRiskService {
    
    @Autowired
    private EnterpriseRiskClientFactory clientFactory;
    
    @Override
    public EnterpriseInfo getEnterpriseRiskByName(String enterpriseName) {
        log.info("查询企业风控信息，企业名称：{}", enterpriseName);
        
        if (StringUtils.isBlank(enterpriseName)) {
            log.warn("企业名称为空");
            return null;
        }
        
        // 获取客户端
        EnterpriseRiskClient client = clientFactory.getClient();
        if (client == null) {
            log.warn("没有可用的风控服务");
            return null;
        }
        
        // 查询企业风控信息
        return client.getEnterpriseInfoByName(enterpriseName);
    }
    
    @Override
    public EnterpriseInfo getEnterpriseRiskByCreditCode(String creditCode) {
        log.info("查询企业风控信息，统一社会信用代码：{}", creditCode);
        
        if (StringUtils.isBlank(creditCode)) {
            log.warn("统一社会信用代码为空");
            return null;
        }
        
        // 获取客户端
        EnterpriseRiskClient client = clientFactory.getClient();
        if (client == null) {
            log.warn("没有可用的风控服务");
            return null;
        }
        
        // 查询企业风控信息
        return client.getEnterpriseInfoByCreditCode(creditCode);
    }
    
    @Override
    public EnterpriseInfo getEnterpriseRiskByName(String enterpriseName, String serviceName) {
        log.info("查询企业风控信息，企业名称：{}，服务名称：{}", enterpriseName, serviceName);
        
        if (StringUtils.isBlank(enterpriseName)) {
            log.warn("企业名称为空");
            return null;
        }
        
        if (StringUtils.isBlank(serviceName)) {
            return getEnterpriseRiskByName(enterpriseName);
        }
        
        // 获取指定客户端
        EnterpriseRiskClient client = clientFactory.getClient(serviceName);
        if (client == null) {
            log.warn("指定的风控服务不可用：{}", serviceName);
            return null;
        }
        
        // 查询企业风控信息
        return client.getEnterpriseInfoByName(enterpriseName);
    }
    
    @Override
    public EnterpriseInfo getEnterpriseRiskByCreditCode(String creditCode, String serviceName) {
        log.info("查询企业风控信息，统一社会信用代码：{}，服务名称：{}", creditCode, serviceName);
        
        if (StringUtils.isBlank(creditCode)) {
            log.warn("统一社会信用代码为空");
            return null;
        }
        
        if (StringUtils.isBlank(serviceName)) {
            return getEnterpriseRiskByCreditCode(creditCode);
        }
        
        // 获取指定客户端
        EnterpriseRiskClient client = clientFactory.getClient(serviceName);
        if (client == null) {
            log.warn("指定的风控服务不可用：{}", serviceName);
            return null;
        }
        
        // 查询企业风控信息
        return client.getEnterpriseInfoByCreditCode(creditCode);
    }
} 