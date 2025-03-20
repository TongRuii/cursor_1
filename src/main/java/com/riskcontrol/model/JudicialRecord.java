package com.riskcontrol.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 司法记录模型
 */
@Data
public class JudicialRecord implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 案件类型（如：民事判决、刑事判决等）
     */
    private String caseType;
    
    /**
     * 案号
     */
    private String caseNumber;
    
    /**
     * 案件名称
     */
    private String caseName;
    
    /**
     * 裁判日期
     */
    private Date judgmentDate;
    
    /**
     * 法院名称
     */
    private String court;
    
    /**
     * 案件状态
     */
    private String caseStatus;
    
    /**
     * 案件角色（原告、被告等）
     */
    private String caseRole;
    
    /**
     * 数据来源
     */
    private String dataSource;
} 