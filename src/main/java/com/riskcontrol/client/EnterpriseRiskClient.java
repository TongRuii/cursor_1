package com.riskcontrol.client;

import com.riskcontrol.model.EnterpriseInfo;

/**
 * 企业风控第三方服务客户端接口
 */
public interface EnterpriseRiskClient {
    
    /**
     * 根据企业名称查询企业信息
     *
     * @param enterpriseName 企业名称
     * @return 企业信息
     */
    EnterpriseInfo getEnterpriseInfoByName(String enterpriseName);
    
    /**
     * 根据统一社会信用代码查询企业信息
     *
     * @param creditCode 统一社会信用代码
     * @return 企业信息
     */
    EnterpriseInfo getEnterpriseInfoByCreditCode(String creditCode);
    
    /**
     * 获取服务名称
     *
     * @return 服务名称
     */
    String getServiceName();
} 