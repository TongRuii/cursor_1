package com.riskcontrol.controller;

import com.riskcontrol.model.ApiResponse;
import com.riskcontrol.model.EnterpriseInfo;
import com.riskcontrol.model.EnterpriseRiskRequest;
import com.riskcontrol.service.EnterpriseRiskService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 企业风控控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/enterprise/risk")
public class EnterpriseRiskController {
    
    @Autowired
    private EnterpriseRiskService enterpriseRiskService;
    
    /**
     * 查询企业风控信息
     */
    @PostMapping("/query")
    public ApiResponse<EnterpriseInfo> queryEnterpriseRisk(@RequestBody EnterpriseRiskRequest request) {
        log.info("查询企业风控信息，请求参数：{}", request);
        
        // 参数校验
        if (StringUtils.isBlank(request.getEnterpriseName()) && StringUtils.isBlank(request.getCreditCode())) {
            return ApiResponse.error(400, "企业名称和统一社会信用代码不能同时为空");
        }
        
        // 查询企业风控信息
        EnterpriseInfo enterpriseInfo;
        if (StringUtils.isNotBlank(request.getCreditCode())) {
            enterpriseInfo = enterpriseRiskService.getEnterpriseRiskByCreditCode(request.getCreditCode());
        } else {
            enterpriseInfo = enterpriseRiskService.getEnterpriseRiskByName(request.getEnterpriseName());
        }
        
        if (enterpriseInfo == null) {
            return ApiResponse.error(404, "未查询到企业信息");
        }
        
        return ApiResponse.success(enterpriseInfo);
    }
    
    /**
     * 查询企业风控信息（指定服务）
     */
    @PostMapping("/query/{serviceName}")
    public ApiResponse<EnterpriseInfo> queryEnterpriseRiskByService(
            @PathVariable String serviceName,
            @RequestBody EnterpriseRiskRequest request) {
        log.info("查询企业风控信息，服务名称：{}，请求参数：{}", serviceName, request);
        
        // 参数校验
        if (StringUtils.isBlank(request.getEnterpriseName()) && StringUtils.isBlank(request.getCreditCode())) {
            return ApiResponse.error(400, "企业名称和统一社会信用代码不能同时为空");
        }
        
        // 查询企业风控信息
        EnterpriseInfo enterpriseInfo;
        if (StringUtils.isNotBlank(request.getCreditCode())) {
            enterpriseInfo = enterpriseRiskService.getEnterpriseRiskByCreditCode(request.getCreditCode(), serviceName);
        } else {
            enterpriseInfo = enterpriseRiskService.getEnterpriseRiskByName(request.getEnterpriseName(), serviceName);
        }
        
        if (enterpriseInfo == null) {
            return ApiResponse.error(404, "未查询到企业信息");
        }
        
        return ApiResponse.success(enterpriseInfo);
    }
} 