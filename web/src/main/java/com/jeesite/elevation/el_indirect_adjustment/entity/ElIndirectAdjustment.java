/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.elevation.el_indirect_adjustment.entity;

import com.jeesite.common.utils.excel.annotation.ExcelField;
import com.jeesite.common.utils.excel.annotation.ExcelFields;
import com.jeesite.elevation.el_data_solution.entity.ElDataSolution;
import com.jeesite.elevation.el_line.entity.ELLine;
import com.jeesite.elevation.el_result_evaluation.entity.ElResultEvaluation;
import com.jeesite.elevation.el_station.entity.ELStation;
import com.jeesite.modules.group.entity.Group;
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

import javax.validation.Valid;

/**
 * 高程——间接平差Entity
 * @author su
 * @version 2022-07-04
 */
@Table(name="el_indirect_adjustment", alias="a", columns={
		@Column(name="id", attrName="id", label="id", isPK=true),
		@Column(name="station_id", attrName="stationId", label="测站id"),
		@Column(name="fore_stn_id", attrName="foreStnId", label="前视站点id"),
		@Column(name="back_stn_id", attrName="backStnId", label="后视站点id"),
		@Column(name="elevation_diff_fore_cor", attrName="elevationDiffForeCor", label="改正后的高差", comment="改正后的高差(m)(前视)", isQuery=false),
		@Column(name="elevation_diff_back_cor", attrName="elevationDiffBackCor", label="改正后的高差", comment="改正后的高差(m)(后视)", isQuery=false),
		@Column(name="elevation_fore", attrName="elevationFore", label="高程", comment="高程(m)(前视)", isQuery=false),
		@Column(name="elevation_back", attrName="elevationBack", label="高程", comment="高程(m)(后视)", isQuery=false),
		@Column(name="elevation", attrName="elevation", label="高程", comment="高程(m)", isQuery=false),
		@Column(name="solution_date", attrName="solutionDate", label="解算时间", queryType=QueryType.LIKE),
		@Column(includeEntity=DataEntity.class),
	},

		//2022年5月30日 su添加
		joinTable = {
				@JoinTable(type = JoinTable.Type.LEFT_JOIN, entity = Employee.class, attrName = "employee", alias = "u1",
						on = "u1.emp_code = a.create_by", columns = {
						@Column(name = "emp_code", label = "用户编码", isPK = true),
						@Column(name = "office_code", label = "机构编码", isQuery = false),
						@Column(name = "office_name", label = "机构名称"),
				}),
				@JoinTable(type = JoinTable.Type.LEFT_JOIN, entity = ELStation.class, attrName = "station", alias = "u2",
						on = "u2.id = a.station_id", columns = {
						@Column(name = "line_id", label = "导线名称", isQuery = false),
						@Column(name = "station_name", label = "测站名称", isQuery = false),
				}),
				@JoinTable(type = JoinTable.Type.LEFT_JOIN, entity = ELStation.class, attrName = "foreStn", alias = "u3",
						on = "u3.id = a.fore_stn_id", columns = {
						@Column(name = "line_id", label = "导线名称", isQuery = false),
						@Column(name = "station_name", label = "前视站点名称", isQuery = false),
				}),
				@JoinTable(type = JoinTable.Type.LEFT_JOIN, entity = ELStation.class, attrName = "backStn", alias = "u4",
						on = "u4.id = a.back_stn_id", columns = {
						@Column(name = "station_name", label = "后视站点名称", isQuery = false),
				}),
				@JoinTable(type = JoinTable.Type.LEFT_JOIN, entity = ELLine.class, attrName = "line", alias = "u5",
						on = "u5.id = u2.line_id", columns = {
						@Column(name = "id", label = "导线id", isPK = true),
						@Column(name = "group_id", label = "小组id", isQuery = false),
						@Column(name = "line_name", label = "导线名称", isQuery = false),
				}),
				@JoinTable(type = JoinTable.Type.LEFT_JOIN, entity = Group.class, attrName = "group", alias = "u6",
						on = "u6.id = u5.group_id", columns = {
						@Column(name = "id", label = "小组id", isPK = true),
						@Column(name = "group_name", label = "小组名称", isQuery = false),
				}),
				@JoinTable(type = Type.LEFT_JOIN, entity = ElDataSolution.class, attrName = "elds", alias = "u7",
						on = "u7.station_id = a.station_id and u7.solution_date = a.solution_date"
				),
				// 2022年7月25日 su添加
				@JoinTable(type = JoinTable.Type.LEFT_JOIN, entity = ElResultEvaluation.class, attrName = "resEval", alias = "u8",
						on = "u8.line_id = u5.id and u8.solution_date = a.solution_date", columns = {
						@Column(name = "traverse_class", label = "导线等级", isQuery = false),
						@Column(name = "is_qqc", label = "是否考虑球气差", isQuery = false),
						@Column(name = "coefficient_k", label = "大气折光系数", isQuery = false),
						@Column(name = "coefficient_r", label = "地球半径", isQuery = false),
						@Column(name = "full_mid_err_eleva_diff_per_km", label = "每千米高差全中误差", isQuery = false),
						@Column(name = "full_mid_err_eleva_diff_per_km_norm", label = "每千米高差全中误差标准", isQuery = false),
						@Column(name = "clos_err_line", label = "导线闭合差", isQuery = false),
						@Column(name = "clos_err_line_norm", label = "导线闭合差标准", isQuery = false),
						@Column(name = "result_evaluation", label = "结果评价", isQuery = false),
				})
		},

		orderBy="u6.id ASC, u5.id ASC, a.create_date DESC, a.station_id ASC", extWhereKeys = "dsfOffice"
)
public class ElIndirectAdjustment extends DataEntity<ElIndirectAdjustment> {
	
	private static final long serialVersionUID = 1L;
	private String stationId;		// 测站id
	private String foreStnId;		// 前视站点id
	private String backStnId;		// 后视站点id
	private Double elevationDiffForeCor;		// 改正后的高差(m)(前视)
	private Double elevationDiffBackCor;		// 改正后的高差(m)(后视)
	private Double elevationFore;		// 高程(m)(前视)
	private Double elevationBack;		// 高程(m)(后视)
	private Double elevation;		// 高程(m)
	private Date solutionDate;		// 解算时间

	//2022年5月30日 su添加
	//测站
	private ELStation station;
	//前视站点
	private ELStation foreStn;
	//后视站点
	private ELStation backStn;
	//导线
	private ELLine line;
	//小组
	private Group group;
	// 导线测量等级
	private String traverseClass;

	// 数据解算
	private ElDataSolution elds;

	// 结果评定
	private ElResultEvaluation resEval;
	
	public ElIndirectAdjustment() {
		this(null);
	}

	public ElIndirectAdjustment(String id){
		super(id);
	}

	// 2022年7月7日 su添加
	// 配置所需要输出到Excel的字段
	@Valid
	@ExcelFields({
			@ExcelField(title = "后视站点", attrName = "backStn.stationName", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 10),
			@ExcelField(title = "测站", attrName = "station.stationName", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 20),
			@ExcelField(title = "前视站点", attrName = "foreStn.stationName", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 30),
			@ExcelField(title = "仪器高(m)", attrName = "elds.heightInstrumentForeAvg", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 40, dataFormat = "0.0000"),
			@ExcelField(title = "镜高(m)", attrName = "elds.heightLensForeAvg", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 50, dataFormat = "0.0000"),
			@ExcelField(title = "度", attrName = "elds.verticalAngDegForeAvgStr", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 60),
			@ExcelField(title = "分", attrName = "elds.verticalAngMinForeAvg", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 70),
			@ExcelField(title = "秒", attrName = "elds.verticalAngSecForeAvg", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 80, dataFormat = "0.0"),
			@ExcelField(title = "弧度", attrName = "elds.verticalAngRadianFore", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 81, dataFormat = "0.000000000"),
			@ExcelField(title = "高差(m)", attrName = "elds.elevationDiffFore", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 82, dataFormat = "0.0000"),
			@ExcelField(title = "改正后的高差(m)", attrName = "elevationDiffForeCor", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 83, dataFormat = "0.0000"),
			@ExcelField(title = "平均平距(前视)(m)", attrName = "elds.distanceForeAvg", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 90, dataFormat = "0.0000"),
			@ExcelField(title = "仪器高(m)", attrName = "elds.heightInstrumentBackAvg", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 100, dataFormat = "0.0000"),
			@ExcelField(title = "镜高(m)", attrName = "elds.heightLensBackAvg", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 110, dataFormat = "0.0000"),
			@ExcelField(title = "度", attrName = "elds.verticalAngDegBackAvgStr", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 120),
			@ExcelField(title = "分", attrName = "elds.verticalAngMinBackAvg", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 130),
			@ExcelField(title = "秒", attrName = "elds.verticalAngSecBackAvg", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 140, dataFormat = "0.0"),
			@ExcelField(title = "弧度", attrName = "elds.verticalAngRadianBack", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 140, dataFormat = "0.000000000"),
			@ExcelField(title = "高差(m)", attrName = "elds.elevationDiffBack", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 141, dataFormat = "0.0000"),
			@ExcelField(title = "改正后的高差(m)", attrName = "elevationDiffBackCor", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 142, dataFormat = "0.0000"),
			@ExcelField(title = "前视", attrName = "elevationFore", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 150, dataFormat = "0.0000"),
			@ExcelField(title = "后视", attrName = "elevationBack", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 151, dataFormat = "0.0000"),
			@ExcelField(title = "平均", attrName = "elevation", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 152, dataFormat = "0.0000"),
			// @ExcelField(title = "导线等级", attrName = "resEval.traverseClass", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 160),
			// @ExcelField(title = "球气差", attrName = "resEval.coefficient_QQC", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 170),
			// @ExcelField(title = "计算", attrName = "errElevaDiffRoundTripFore", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 180, dataFormat = "0.00"),
			// @ExcelField(title = "标准", attrName = "errElevaDiffRoundTripForeNorm", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 181, dataFormat = "0.00"),
			// @ExcelField(title = "计算", attrName = "resEval.fullMidErrElevaDiffPerKm", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 190, dataFormat = "0.00"),
			// @ExcelField(title = "标准", attrName = "resEval.fullMidErrElevaDiffPerKmNorm", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 191, dataFormat = "0.00"),
			// @ExcelField(title = "计算", attrName = "resEval.closErrLine", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 200, dataFormat = "0.00"),
			// @ExcelField(title = "标准", attrName = "resEval.closErrLineNorm", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 201, dataFormat = "0.00"),
			// @ExcelField(title = "评价", attrName = "resEval.resultEvaluation", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 210),
			@ExcelField(title = "解算时间", attrName = "solutionDate", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 220),
			@ExcelField(title = "备注信息", attrName = "remarks", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 230),
	})
	
	@Length(min=0, max=64, message="测站id长度不能超过 64 个字符")
	public String getStationId() {
		return stationId;
	}

	public void setStationId(String stationId) {
		this.stationId = stationId;
	}
	
	@Length(min=0, max=64, message="前视站点id长度不能超过 64 个字符")
	public String getForeStnId() {
		return foreStnId;
	}

	public void setForeStnId(String foreStnId) {
		this.foreStnId = foreStnId;
	}
	
	@Length(min=0, max=64, message="后视站点id长度不能超过 64 个字符")
	public String getBackStnId() {
		return backStnId;
	}

	public void setBackStnId(String backStnId) {
		this.backStnId = backStnId;
	}
	
	public Double getElevationDiffForeCor() {
		return elevationDiffForeCor;
	}

	public void setElevationDiffForeCor(Double elevationDiffForeCor) {
		this.elevationDiffForeCor = elevationDiffForeCor;
	}
	
	public Double getElevationDiffBackCor() {
		return elevationDiffBackCor;
	}

	public void setElevationDiffBackCor(Double elevationDiffBackCor) {
		this.elevationDiffBackCor = elevationDiffBackCor;
	}
	
	public Double getElevationFore() {
		return elevationFore;
	}

	public void setElevationFore(Double elevationFore) {
		this.elevationFore = elevationFore;
	}
	
	public Double getElevationBack() {
		return elevationBack;
	}

	public void setElevationBack(Double elevationBack) {
		this.elevationBack = elevationBack;
	}
	
	public Double getElevation() {
		return elevation;
	}

	public void setElevation(Double elevation) {
		this.elevation = elevation;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getSolutionDate() {
		return solutionDate;
	}

	public void setSolutionDate(Date solutionDate) {
		this.solutionDate = solutionDate;
	}

	public ELStation getStation() {
		return station;
	}

	public void setStation(ELStation station) {
		this.station = station;
	}

	public ELStation getForeStn() {
		return foreStn;
	}

	public void setForeStn(ELStation foreStn) {
		this.foreStn = foreStn;
	}

	public ELStation getBackStn() {
		return backStn;
	}

	public void setBackStn(ELStation backStn) {
		this.backStn = backStn;
	}

	public ELLine getLine() {
		return line;
	}

	public void setLine(ELLine line) {
		this.line = line;
	}

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	public String getTraverseClass() {
		return traverseClass;
	}

	public void setTraverseClass(String traverseClass) {
		this.traverseClass = traverseClass;
	}

	public ElDataSolution getElds() {
		return elds;
	}

	public void setElds(ElDataSolution elds) {
		this.elds = elds;
	}

	public ElResultEvaluation getResEval() {
		return resEval;
	}

	public void setResEval(ElResultEvaluation resEval) {
		this.resEval = resEval;
	}
}