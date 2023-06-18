/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.result_evaluation.entity;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import com.jeesite.common.utils.excel.annotation.ExcelField;
import com.jeesite.common.utils.excel.annotation.ExcelFields;
import com.jeesite.modules.group.entity.Group;
import com.jeesite.modules.line.entity.Line;
import com.jeesite.modules.sys.entity.Employee;
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
 * 结果评定Entity
 * @author su
 * @version 2022-03-15
 */
@Table(name="result_evaluation", alias="a", columns={
		@Column(name="id", attrName="id", label="id", isPK=true),
		@Column(name="line_id", attrName="lineId", label="导线id"),
		@Column(name="angle_closure_error", attrName="angleClosureError", label="角度闭合差", comment="角度闭合差（秒）", isQuery=false),
		@Column(name="clos_err_of_coordincrex", attrName="closErrOfCoordincrex", label="坐标增量闭合差X", comment="坐标增量闭合差X（毫米）", isQuery=false),
		@Column(name="clos_err_of_coordincrey", attrName="closErrOfCoordincrey", label="坐标增量闭合差Y", comment="坐标增量闭合差Y（毫米）", isQuery=false),
		@Column(name="total_length_clos_error", attrName="totalLengthClosError", label="导线全长闭合差", comment="导线全长闭合差（毫米）", isQuery=false),
		@Column(name="rela_clos_error", attrName="relaClosError", label="导线相对闭合差", isQuery=false),
		@Column(name="traverse_class", attrName="traverseClass", label="导线等级", comment="导线等级（三等、四等、一级、二级、三级）", isQuery=false),
		@Column(name="result_evaluation", attrName="resultEvaluation", label="结果评价", comment="结果评价（√ or 不通过原因）", isQuery=false),
		@Column(name="solution_date", attrName="solutionDate", label="解算时间", queryType=QueryType.LIKE),
		@Column(includeEntity=DataEntity.class),
	},

		//2022年3月15日 su添加
		joinTable = {
				@JoinTable(type = JoinTable.Type.LEFT_JOIN, entity = Employee.class, attrName = "employee", alias = "u1",
						on = "u1.emp_code = a.create_by", columns = {
						@Column(name = "emp_code", label = "用户编码", isPK = true),
						@Column(name = "office_code", label = "机构编码", isQuery = false),
						@Column(name = "office_name", label = "机构名称"),
				}),
				@JoinTable(type = JoinTable.Type.LEFT_JOIN, entity = Line.class, attrName = "line", alias = "u2",
						on = "u2.id = a.line_id", columns = {
						@Column(name = "id", label = "导线id", isPK = true),
						@Column(name = "group_id", label = "小组id", isQuery = false),
						@Column(name = "line_name", label = "导线名称", isQuery = false),
				}),
				@JoinTable(type = JoinTable.Type.LEFT_JOIN, entity = Group.class, attrName = "group", alias = "u3",
						on = "u3.id = u2.group_id", columns = {
						@Column(name = "id", label = "小组id", isPK = true),
						@Column(name = "group_name", label = "小组名称", isQuery = false)
				})
		},

		orderBy="u3.id ASC, a.line_id ASC, a.solution_date DESC", extWhereKeys = "dsfOffice"
)
public class ResultEvaluation extends DataEntity<ResultEvaluation> {
	
	private static final long serialVersionUID = 1L;
	private String lineId;		// 导线id
	private Double angleClosureError;		// 角度闭合差（秒）
	private Double closErrOfCoordincrex;		// 坐标增量闭合差X（毫米）
	private Double closErrOfCoordincrey;		// 坐标增量闭合差Y（毫米）
	private Double totalLengthClosError;		// 导线全长闭合差（毫米）
	private Double relaClosError;		// 导线相对闭合差
	private String traverseClass;		// 导线等级（三等、四等、一级、二级、三级）
	private String resultEvaluation;		// 结果评价（√ or 不通过原因）
	private Date solutionDate;		// 解算时间

	//2022年3月15日 su添加
	//小组
	private Group group;
	//导线
	private Line line;
	
	public ResultEvaluation() {
		this(null);
	}

	public ResultEvaluation(String id){
		super(id);
	}

	// 2022年5月3日 su添加
	// 配置所需要输出到Excel的字段
	@Valid
	@ExcelFields({
			@ExcelField(title = "角度闭合差(\")", attrName = "angleClosureError", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 3, dataFormat = "0.0000"),
			@ExcelField(title = "坐标增量闭合差(mm)X", attrName = "closErrOfCoordincrex", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 4, dataFormat = "0.00"),
			@ExcelField(title = "坐标增量闭合差(mm)Y", attrName = "closErrOfCoordincrey", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 5, dataFormat = "0.00"),
			@ExcelField(title = "导线全长闭合差(mm)", attrName = "totalLengthClosError", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 6, dataFormat = "0.00"),
			@ExcelField(title = "导线相对闭合差", attrName = "relaClosError", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 7),
			@ExcelField(title = "导线等级", attrName = "traverseClass", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 8),
			@ExcelField(title = "结果评价", attrName = "resultEvaluation", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 9),
			@ExcelField(title = "解算时间", attrName = "solutionDate", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 80),
			@ExcelField(title = "备注信息", attrName = "remarks", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 90),
	})
	
	@NotBlank(message="导线id不能为空")
	@Length(min=0, max=64, message="导线id长度不能超过 64 个字符")
	public String getLineId() {
		return lineId;
	}

	public void setLineId(String lineId) {
		this.lineId = lineId;
	}
	
	public Double getAngleClosureError() {
		return angleClosureError;
	}

	public void setAngleClosureError(Double angleClosureError) {
		this.angleClosureError = angleClosureError;
	}
	
	public Double getClosErrOfCoordincrex() {
		return closErrOfCoordincrex;
	}

	public void setClosErrOfCoordincrex(Double closErrOfCoordincrex) {
		this.closErrOfCoordincrex = closErrOfCoordincrex;
	}
	
	public Double getClosErrOfCoordincrey() {
		return closErrOfCoordincrey;
	}

	public void setClosErrOfCoordincrey(Double closErrOfCoordincrey) {
		this.closErrOfCoordincrey = closErrOfCoordincrey;
	}
	
	public Double getTotalLengthClosError() {
		return totalLengthClosError;
	}

	public void setTotalLengthClosError(Double totalLengthClosError) {
		this.totalLengthClosError = totalLengthClosError;
	}
	
	public Double getRelaClosError() {
		return relaClosError;
	}

	public void setRelaClosError(Double relaClosError) {
		this.relaClosError = relaClosError;
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

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	public Line getLine() {
		return line;
	}

	public void setLine(Line line) {
		this.line = line;
	}
}