/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.elevation.el_result_evaluation.entity;

import javax.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;

/**
 * 高程——结果评定Entity
 * @author su
 * @version 2022-06-07
 */
@Table(name="el_result_evaluation", alias="a", columns={
		@Column(name="id", attrName="id", label="id", isPK=true),
		@Column(name="line_id", attrName="lineId", label="导线id"),
		@Column(name="traverse_class", attrName="traverseClass", label="导线等级", comment="导线等级(二等、三等、四等、五等)", queryType=QueryType.LIKE),
		@Column(name="is_qqc", attrName="isQQC", label="是否考虑球气差", comment="是否考虑球气差(1是 0否)", isQuery=false),
		@Column(name="coefficient_k", attrName="coefficientK", label="大气折光系数", isQuery=false),
		@Column(name="coefficient_r", attrName="coefficientR", label="地球半径", comment="地球半径(km)", isQuery=false),
		@Column(name="full_mid_err_eleva_diff_per_km", attrName="fullMidErrElevaDiffPerKm", label="每千米高差全中误差", comment="每千米高差全中误差(mm)", isQuery=false),
		@Column(name="full_mid_err_eleva_diff_per_km_norm", attrName="fullMidErrElevaDiffPerKmNorm", label="每千米高差全中误差标准", comment="每千米高差全中误差标准(mm)", isQuery=false),
		@Column(name="clos_err_line", attrName="closErrLine", label="导线闭合差", comment="导线闭合差(mm)", isQuery=false),
		@Column(name="clos_err_line_norm", attrName="closErrLineNorm", label="导线闭合差标准", comment="导线闭合差标准(mm)", isQuery=false),
		@Column(name="result_evaluation", attrName="resultEvaluation", label="结果评价", comment="结果评价(√ or 不通过原因)", queryType=QueryType.LIKE),
		@Column(name="solution_date", attrName="solutionDate", label="解算时间", queryType=QueryType.LIKE),
		@Column(includeEntity=DataEntity.class),
	}, orderBy="a.update_date DESC"
)
public class ElResultEvaluation extends DataEntity<ElResultEvaluation> {
	
	private static final long serialVersionUID = 1L;
	private String lineId;		// 导线id
	private String traverseClass;		// 导线等级(二等、三等、四等、五等)
	private Integer isQQC;			// 是否考虑球气差（1是 0否）
	private Double coefficientK;		// 大气折光系数
	private Double coefficientR;		// 地球半径(km)
	private Double fullMidErrElevaDiffPerKm;		// 每千米高差全中误差(mm)
	private Double fullMidErrElevaDiffPerKmNorm;		// 每千米高差全中误差标准(mm)
	private Double closErrLine;		// 导线闭合差(mm)
	private Double closErrLineNorm;		// 导线闭合差标准(mm)
	private String resultEvaluation;		// 结果评价(√ or 不通过原因)
	private Date solutionDate;		// 解算时间

	// 球气差的系数
	private String coefficient_QQC;
	
	public ElResultEvaluation() {
		this(null);
	}

	public ElResultEvaluation(String id){
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
	
	@NotBlank(message="导线等级不能为空")
	@Length(min=0, max=20, message="导线等级长度不能超过 20 个字符")
	public String getTraverseClass() {
		return traverseClass;
	}

	public void setTraverseClass(String traverseClass) {
		this.traverseClass = traverseClass;
	}

	public Integer getIsQQC() {
		return isQQC;
	}

	public void setIsQQC(Integer isQQC) {
		this.isQQC = isQQC;
	}

	public Double getCoefficientK() {
		return coefficientK;
	}

	public void setCoefficientK(Double coefficientK) {
		this.coefficientK = coefficientK;
	}

	public Double getCoefficientR() {
		return coefficientR;
	}

	public void setCoefficientR(Double coefficientR) {
		this.coefficientR = coefficientR;
	}

	public Double getFullMidErrElevaDiffPerKm() {
		return fullMidErrElevaDiffPerKm;
	}

	public void setFullMidErrElevaDiffPerKm(Double fullMidErrElevaDiffPerKm) {
		this.fullMidErrElevaDiffPerKm = fullMidErrElevaDiffPerKm;
	}
	
	public Double getFullMidErrElevaDiffPerKmNorm() {
		return fullMidErrElevaDiffPerKmNorm;
	}

	public void setFullMidErrElevaDiffPerKmNorm(Double fullMidErrElevaDiffPerKmNorm) {
		this.fullMidErrElevaDiffPerKmNorm = fullMidErrElevaDiffPerKmNorm;
	}
	
	public Double getClosErrLine() {
		return closErrLine;
	}

	public void setClosErrLine(Double closErrLine) {
		this.closErrLine = closErrLine;
	}
	
	public Double getClosErrLineNorm() {
		return closErrLineNorm;
	}

	public void setClosErrLineNorm(Double closErrLineNorm) {
		this.closErrLineNorm = closErrLineNorm;
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

	public String getCoefficient_QQC() {
		return "K:" + coefficientK + " R:" + coefficientR + "km";
	}
	
}