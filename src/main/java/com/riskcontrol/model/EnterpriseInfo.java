package com.riskcontrol.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 企业信息模型
 */
@Data
public class EnterpriseInfo implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 企业名称
     */
    private String enterpriseName;
    
    /**
     * 统一社会信用代码
     */
    private String creditCode;
    
    /**
     * 法定代表人
     */
    private String legalRepresentative;
    
    /**
     * 注册资本
     */
    private String registeredCapital;
    
    /**
     * 成立日期
     */
    private Date establishDate;
    
    /**
     * 企业状态
     */
    private String status;
    
    /**
     * 经营范围
     */
    private String businessScope;
    
    /**
     * 注册地址
     */
    private String registeredAddress;
    
    /**
     * 是否存在失信记录
     */
    private Boolean hasDishonestyRecord;
    
    /**
     * 失信记录列表
     */
    private List<DishonestyRecord> dishonestyRecords;
    
    /**
     * 是否存在被执行人记录
     */
    private Boolean hasExecutedRecord;
    
    /**
     * 被执行人记录列表
     */
    private List<ExecutedRecord> executedRecords;
    
    /**
     * 是否存在司法风险
     */
    private Boolean hasJudicialRisk;
    
    /**
     * 司法风险记录列表
     */
    private List<JudicialRecord> judicialRecords;
} 