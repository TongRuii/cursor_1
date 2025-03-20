package com.riskcontrol.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 企业风控请求模型
 */
@Data
public class EnterpriseRiskRequest implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 企业名称（与统一社会信用代码二选一必填）
     */
    private String enterpriseName;
    
    /**
     * 统一社会信用代码（与企业名称二选一必填）
     */
    private String creditCode;
} 