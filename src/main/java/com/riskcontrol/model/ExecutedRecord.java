package com.riskcontrol.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 被执行人记录模型
 */
@Data
public class ExecutedRecord implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 案号
     */
    private String caseNumber;
    
    /**
     * 立案日期
     */
    private Date filingDate;
    
    /**
     * 执行标的
     */
    private String executionSubject;
    
    /**
     * 执行法院
     */
    private String court;
    
    /**
     * 案件状态
     */
    private String caseStatus;
    
    /**
     * 数据来源
     */
    private String dataSource;
} 