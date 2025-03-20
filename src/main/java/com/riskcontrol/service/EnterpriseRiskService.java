package com.riskcontrol.service;

import com.riskcontrol.model.EnterpriseInfo;

/**
 * 企业风控服务接口
 */
public interface EnterpriseRiskService {
    
    /**
     * 根据企业名称查询企业风控信息
     *
     * @param enterpriseName 企业名称
     * @return 企业风控信息
     */
    EnterpriseInfo getEnterpriseRiskByName(String enterpriseName);
    
    /**
     * 根据统一社会信用代码查询企业风控信息
     *
     * @param creditCode 统一社会信用代码
     * @return 企业风控信息
     */
    EnterpriseInfo getEnterpriseRiskByCreditCode(String creditCode);
    
    /**
     * 根据企业名称查询企业风控信息（指定服务）
     *
     * @param enterpriseName 企业名称
     * @param serviceName    服务名称
     * @return 企业风控信息
     */
    EnterpriseInfo getEnterpriseRiskByName(String enterpriseName, String serviceName);
    
    /**
     * 根据统一社会信用代码查询企业风控信息（指定服务）
     *
     * @param creditCode  统一社会信用代码
     * @param serviceName 服务名称
     * @return 企业风控信息
     */
    EnterpriseInfo getEnterpriseRiskByCreditCode(String creditCode, String serviceName);
} 