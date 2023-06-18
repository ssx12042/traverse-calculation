/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.result_evaluation_strict.entity;

import javax.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;
import java.util.Date;
import com.jeesite.common.mybatis.annotation.JoinTable;
import com.jeesite.common.mybatis.annotation.JoinTable.Type;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;

/**
 * 间接平差的结果评定Entity
 * @author su
 * @version 2022-04-11
 */
@Table(name="result_evaluation_strict", alias="a", columns={
		@Column(name="id", attrName="id", label="id", isPK=true),
		@Column(name="line_id", attrName="lineId", label="导线id"),
		@Column(name="mediumerror_unitweight", attrName="mediumerrorUnitweight", label="单位权中误差", comment="单位权中误差（毫米）", isQuery=false),
		@Column(name="traverse_class", attrName="traverseClass", label="导线等级", comment="导线等级（三等、四等、一级、二级、三级）", isQuery=false),
		@Column(name="result_evaluation", attrName="resultEvaluation", label="结果评价", comment="结果评价（√ or 不通过原因）", isQuery=false),
		@Column(name="solution_date", attrName="solutionDate", label="解算时间", isQuery=false),
		@Column(includeEntity=DataEntity.class),
	}, orderBy="a.update_date DESC"
)
public class ResultEvaluationStrict extends DataEntity<ResultEvaluationStrict> {
	
	private static final long serialVersionUID = 1L;
	private String lineId;		// 导线id
	private Double mediumerrorUnitweight;		// 单位权中误差（毫米）
	private String traverseClass;		// 导线等级（三等、四等、一级、二级、三级）
	private String resultEvaluation;		// 结果评价（√ or 不通过原因）
	private Date solutionDate;		// 解算时间
	
	public ResultEvaluationStrict() {
		this(null);
	}

	public ResultEvaluationStrict(String id){
		super(id);
	}
	
	@NotBlank(message="导线id不能为空")
	@Length(min=0, max=64, message="导线id长度不能超过 64 个字符")
	public String getLineId() {
		return lineId;
	}

	public void setLineId(String lineId) {
		this.lineId = lineId;
	}
	
	public Double getMediumerrorUnitweight() {
		return mediumerrorUnitweight;
	}

	public void setMediumerrorUnitweight(Double mediumerrorUnitweight) {
		this.mediumerrorUnitweight = mediumerrorUnitweight;
	}
	
	@Length(min=0, max=20, message="导线等级长度不能超过 20 个字符")
	public String getTraverseClass() {
		return traverseClass;
	}

	public void setTraverseClass(String traverseClass) {
		this.traverseClass = traverseClass;
	}
	
	@Length(min=0, max=50, message="结果评价长度不能超过 50 个字符")
	public String getResultEvaluation() {
		return resultEvaluation;
	}

	public void setResultEvaluation(String resultEvaluation) {
		this.resultEvaluation = resultEvaluation;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getSolutionDate() {
		return solutionDate;
	}

	public void setSolutionDate(Date solutionDate) {
		this.solutionDate = solutionDate;
	}
	
}