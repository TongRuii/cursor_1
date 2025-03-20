package com.riskcontrol.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 失信记录模型
 */
@Data
public class DishonestyRecord implements Serializable {
    
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
     * 发布日期
     */
    private Date publishDate;
    
    /**
     * 执行法院
     */
    private String court;
    
    /**
     * 失信被执行人行为具体情形
     */
    private String dishonestyBehavior;
    
    /**
     * 履行情况
     */
    private String performanceStatus;
    
    /**
     * 数据来源
     */
    private String dataSource;
} 