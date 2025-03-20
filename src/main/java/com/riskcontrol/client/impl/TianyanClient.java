package com.riskcontrol.client.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.riskcontrol.client.EnterpriseRiskClient;
import com.riskcontrol.config.RiskControlProperties;
import com.riskcontrol.model.DishonestyRecord;
import com.riskcontrol.model.EnterpriseInfo;
import com.riskcontrol.model.ExecutedRecord;
import com.riskcontrol.model.JudicialRecord;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 天眼查客户端实现
 */
@Slf4j
@Component
@RefreshScope
public class TianyanClient implements EnterpriseRiskClient {
    
    private static final String SERVICE_NAME = "tianyan";
    
    @Autowired
    private RiskControlProperties properties;
    
    @Autowired
    private RestTemplate restTemplate;
    
    @Override
    public EnterpriseInfo getEnterpriseInfoByName(String enterpriseName) {
        log.info("通过天眼查查询企业信息，企业名称：{}", enterpriseName);
        
        if (StringUtils.isBlank(enterpriseName)) {
            return null;
        }
        
        try {
            // 获取天眼查配置
            RiskControlProperties.ThirdPartyConfig config = getConfig();
            if (config == null || !config.isEnabled()) {
                log.warn("天眼查服务未启用");
                return null;
            }
            
            // 构建请求参数
            Map<String, String> params = new HashMap<>();
            params.put("name", enterpriseName);
            
            // 发送请求
            String url = config.getUrl() + "/v1/company/base";
            HttpHeaders headers = buildHeaders(config);
            HttpEntity<Map<String, String>> entity = new HttpEntity<>(params, headers);
            
            ResponseEntity<String> response = restTemplate.exchange(
                    url, HttpMethod.POST, entity, String.class);
            
            // 解析响应
            return parseResponse(response.getBody());
        } catch (Exception e) {
            log.error("天眼查查询企业信息异常，企业名称：{}", enterpriseName, e);
            return null;
        }
    }
    
    @Override
    public EnterpriseInfo getEnterpriseInfoByCreditCode(String creditCode) {
        log.info("通过天眼查查询企业信息，统一社会信用代码：{}", creditCode);
        
        if (StringUtils.isBlank(creditCode)) {
            return null;
        }
        
        try {
            // 获取天眼查配置
            RiskControlProperties.ThirdPartyConfig config = getConfig();
            if (config == null || !config.isEnabled()) {
                log.warn("天眼查服务未启用");
                return null;
            }
            
            // 构建请求参数
            Map<String, String> params = new HashMap<>();
            params.put("creditCode", creditCode);
            
            // 发送请求
            String url = config.getUrl() + "/v1/company/base";
            HttpHeaders headers = buildHeaders(config);
            HttpEntity<Map<String, String>> entity = new HttpEntity<>(params, headers);
            
            ResponseEntity<String> response = restTemplate.exchange(
                    url, HttpMethod.POST, entity, String.class);
            
            // 解析响应
            return parseResponse(response.getBody());
        } catch (Exception e) {
            log.error("天眼查查询企业信息异常，统一社会信用代码：{}", creditCode, e);
            return null;
        }
    }
    
    @Override
    public String getServiceName() {
        return SERVICE_NAME;
    }
    
    /**
     * 获取天眼查配置
     */
    private RiskControlProperties.ThirdPartyConfig getConfig() {
        if (properties.getThirdParty() == null) {
            return null;
        }
        return properties.getThirdParty().get(SERVICE_NAME);
    }
    
    /**
     * 构建请求头
     */
    private HttpHeaders buildHeaders(RiskControlProperties.ThirdPartyConfig config) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json;charset=UTF-8");
        
        // 添加认证信息
        if (config.getAuth() != null) {
            headers.set("Authorization", config.getAuth().getToken());
        }
        
        return headers;
    }
    
    /**
     * 解析响应数据
     */
    private EnterpriseInfo parseResponse(String responseBody) {
        if (StringUtils.isBlank(responseBody)) {
            return null;
        }
        
        try {
            JSONObject jsonObject = JSON.parseObject(responseBody);
            JSONObject result = jsonObject.getJSONObject("result");
            if (result == null) {
                return null;
            }
            
            EnterpriseInfo enterpriseInfo = new EnterpriseInfo();
            enterpriseInfo.setEnterpriseName(result.getString("name"));
            enterpriseInfo.setCreditCode(result.getString("creditCode"));
            enterpriseInfo.setLegalRepresentative(result.getString("legalPersonName"));
            enterpriseInfo.setRegisteredCapital(result.getString("regCapital"));
            enterpriseInfo.setStatus(result.getString("regStatus"));
            enterpriseInfo.setBusinessScope(result.getString("businessScope"));
            enterpriseInfo.setRegisteredAddress(result.getString("regLocation"));
            
            // 设置失信记录
            enterpriseInfo.setHasDishonestyRecord(false);
            enterpriseInfo.setDishonestyRecords(new ArrayList<>());
            
            // 设置被执行人记录
            enterpriseInfo.setHasExecutedRecord(false);
            enterpriseInfo.setExecutedRecords(new ArrayList<>());
            
            // 设置司法风险
            enterpriseInfo.setHasJudicialRisk(false);
            enterpriseInfo.setJudicialRecords(new ArrayList<>());
            
            // 查询失信记录
            queryDishonestyRecords(enterpriseInfo, result.getString("id"));
            
            // 查询被执行人记录
            queryExecutedRecords(enterpriseInfo, result.getString("id"));
            
            // 查询司法风险
            queryJudicialRecords(enterpriseInfo, result.getString("id"));
            
            return enterpriseInfo;
        } catch (Exception e) {
            log.error("解析天眼查响应数据异常", e);
            return null;
        }
    }
    
    /**
     * 查询失信记录
     */
    private void queryDishonestyRecords(EnterpriseInfo enterpriseInfo, String companyId) {
        if (StringUtils.isBlank(companyId)) {
            return;
        }
        
        try {
            // 获取天眼查配置
            RiskControlProperties.ThirdPartyConfig config = getConfig();
            if (config == null || !config.isEnabled()) {
                return;
            }
            
            // 构建请求参数
            Map<String, String> params = new HashMap<>();
            params.put("id", companyId);
            
            // 发送请求
            String url = config.getUrl() + "/v1/company/dishonesty";
            HttpHeaders headers = buildHeaders(config);
            HttpEntity<Map<String, String>> entity = new HttpEntity<>(params, headers);
            
            ResponseEntity<String> response = restTemplate.exchange(
                    url, HttpMethod.POST, entity, String.class);
            
            // 解析响应
            List<DishonestyRecord> records = parseDishonestyRecords(response.getBody());
            
            // 设置失信记录
            enterpriseInfo.setHasDishonestyRecord(!records.isEmpty());
            enterpriseInfo.setDishonestyRecords(records);
        } catch (Exception e) {
            log.error("天眼查查询失信记录异常，企业ID：{}", companyId, e);
        }
    }
    
    /**
     * 解析失信记录
     */
    private List<DishonestyRecord> parseDishonestyRecords(String responseBody) {
        List<DishonestyRecord> records = new ArrayList<>();
        
        // 模拟解析失信记录
        
        return records;
    }
    
    /**
     * 查询被执行人记录
     */
    private void queryExecutedRecords(EnterpriseInfo enterpriseInfo, String companyId) {
        if (StringUtils.isBlank(companyId)) {
            return;
        }
        
        try {
            // 获取天眼查配置
            RiskControlProperties.ThirdPartyConfig config = getConfig();
            if (config == null || !config.isEnabled()) {
                return;
            }
            
            // 构建请求参数
            Map<String, String> params = new HashMap<>();
            params.put("id", companyId);
            
            // 发送请求
            String url = config.getUrl() + "/v1/company/zhixing";
            HttpHeaders headers = buildHeaders(config);
            HttpEntity<Map<String, String>> entity = new HttpEntity<>(params, headers);
            
            ResponseEntity<String> response = restTemplate.exchange(
                    url, HttpMethod.POST, entity, String.class);
            
            // 解析响应
            List<ExecutedRecord> records = parseExecutedRecords(response.getBody());
            
            // 设置被执行人记录
            enterpriseInfo.setHasExecutedRecord(!records.isEmpty());
            enterpriseInfo.setExecutedRecords(records);
        } catch (Exception e) {
            log.error("天眼查查询被执行人记录异常，企业ID：{}", companyId, e);
        }
    }
    
    /**
     * 解析被执行人记录
     */
    private List<ExecutedRecord> parseExecutedRecords(String responseBody) {
        List<ExecutedRecord> records = new ArrayList<>();
        
        // 模拟解析被执行人记录
        
        return records;
    }
    
    /**
     * 查询司法风险
     */
    private void queryJudicialRecords(EnterpriseInfo enterpriseInfo, String companyId) {
        if (StringUtils.isBlank(companyId)) {
            return;
        }
        
        try {
            // 获取天眼查配置
            RiskControlProperties.ThirdPartyConfig config = getConfig();
            if (config == null || !config.isEnabled()) {
                return;
            }
            
            // 构建请求参数
            Map<String, String> params = new HashMap<>();
            params.put("id", companyId);
            
            // 发送请求
            String url = config.getUrl() + "/v1/company/lawsuit";
            HttpHeaders headers = buildHeaders(config);
            HttpEntity<Map<String, String>> entity = new HttpEntity<>(params, headers);
            
            ResponseEntity<String> response = restTemplate.exchange(
                    url, HttpMethod.POST, entity, String.class);
            
            // 解析响应
            List<JudicialRecord> records = parseJudicialRecords(response.getBody());
            
            // 设置司法风险
            enterpriseInfo.setHasJudicialRisk(!records.isEmpty());
            enterpriseInfo.setJudicialRecords(records);
        } catch (Exception e) {
            log.error("天眼查查询司法风险异常，企业ID：{}", companyId, e);
        }
    }
    
    /**
     * 解析司法风险
     */
    private List<JudicialRecord> parseJudicialRecords(String responseBody) {
        List<JudicialRecord> records = new ArrayList<>();
        
        // 模拟解析司法风险
        
        return records;
    }
} 