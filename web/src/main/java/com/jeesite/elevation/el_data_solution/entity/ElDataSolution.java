/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.elevation.el_data_solution.entity;

import com.jeesite.common.utils.excel.annotation.ExcelField;
import com.jeesite.common.utils.excel.annotation.ExcelFields;
import com.jeesite.elevation.el_line.entity.ELLine;
import com.jeesite.elevation.el_result_evaluation.entity.ElResultEvaluation;
import com.jeesite.elevation.el_station.entity.ELStation;
import com.jeesite.modules.group.entity.Group;
import com.jeesite.modules.sys.entity.Employee;
import org.hibernate.validator.constraints.Length;
import java.util.Date;
import com.jeesite.common.mybatis.annotation.JoinTable;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;

import javax.validation.Valid;

/**
 * 高程——数据解算Entity
 * @author su
 * @version 2022-05-24
 */
@Table(name="el_data_solution", alias="a", columns={
		@Column(name="id", attrName="id", label="id", isPK=true),
		@Column(name="station_id", attrName="stationId", label="测站id"),
		@Column(name="fore_stn_id", attrName="foreStnId", label="前视站点id"),
		@Column(name="back_stn_id", attrName="backStnId", label="后视站点id"),
		@Column(name="height_instrument_fore_avg", attrName="heightInstrumentForeAvg", label="各测回平均仪器高", comment="各测回平均仪器高(m)(测前站时)", isQuery=false),
		@Column(name="height_lens_fore_avg", attrName="heightLensForeAvg", label="各测回平均镜高", comment="各测回平均镜高(m)(测前站时)", isQuery=false),
		@Column(name="vertical_ang_deg_fore_avg", attrName="verticalAngDegForeAvg", label="各测回平均垂直角度", comment="各测回平均垂直角度(与前站)", isQuery=false),
		@Column(name="vertical_ang_min_fore_avg", attrName="verticalAngMinForeAvg", label="各测回平均垂直角分", comment="各测回平均垂直角分(与前站)", isQuery=false),
		@Column(name="vertical_ang_sec_fore_avg", attrName="verticalAngSecForeAvg", label="各测回平均垂直角秒", comment="各测回平均垂直角秒(与前站)", isQuery=false),
		@Column(name="vertical_ang_sign_fore", attrName="verticalAngSignFore", label="垂直角的符号", comment="垂直角的符号(与前站)(1正 -1负)", isQuery=false),
		@Column(name="vertical_ang_radian_fore", attrName="verticalAngRadianFore", label="垂直角弧度", comment="垂直角弧度(与前站)", isQuery=false),
		@Column(name="vertical_ang_radian_fore_cor", attrName="verticalAngRadianForeCor", label="改正后的垂直角弧度", comment="改正后的垂直角弧度(与前站)", isQuery=false),
		@Column(name="elevation_diff_fore", attrName="elevationDiffFore", label="高差", comment="高差(m)(与前站)", isQuery=false),
		@Column(name="elevation_diff_fore_cor", attrName="elevationDiffForeCor", label="改正后的高差", comment="改正后的高差(m)(与前站)", isQuery=false),
		@Column(name="elevation_fore", attrName="elevationFore", label="高程", comment="高程(m)(前视)", isQuery=false),
		@Column(name="height_instrument_back_avg", attrName="heightInstrumentBackAvg", label="各测回平均仪器高", comment="各测回平均仪器高(m)(测后站时)", isQuery=false),
		@Column(name="height_lens_back_avg", attrName="heightLensBackAvg", label="各测回平均镜高", comment="各测回平均镜高(m)(测后站时)", isQuery=false),
		@Column(name="vertical_ang_deg_back_avg", attrName="verticalAngDegBackAvg", label="各测回平均垂直角度", comment="各测回平均垂直角度(与后站)", isQuery=false),
		@Column(name="vertical_ang_min_back_avg", attrName="verticalAngMinBackAvg", label="各测回平均垂直角分", comment="各测回平均垂直角分(与后站)", isQuery=false),
		@Column(name="vertical_ang_sec_back_avg", attrName="verticalAngSecBackAvg", label="各测回平均垂直角秒", comment="各测回平均垂直角秒(与后站)", isQuery=false),
		@Column(name="vertical_ang_sign_back", attrName="verticalAngSignBack", label="垂直角的符号", comment="垂直角的符号(与后站)(1正 -1负)", isQuery=false),
		@Column(name="vertical_ang_radian_back", attrName="verticalAngRadianBack", label="垂直角弧度", comment="垂直角弧度(与后站)", isQuery=false),
		@Column(name="vertical_ang_radian_back_cor", attrName="verticalAngRadianBackCor", label="改正后的垂直角弧度", comment="改正后的垂直角弧度(与后站)", isQuery=false),
		@Column(name="elevation_diff_back", attrName="elevationDiffBack", label="高差", comment="高差(m)(与后站)", isQuery=false),
		@Column(name="elevation_diff_back_cor", attrName="elevationDiffBackCor", label="改正后的高差", comment="改正后的高差(m)(与后站)", isQuery=false),
		@Column(name="distance_fore_avg", attrName="distanceForeAvg", label="平均平距", comment="平均平距(m)(与前站)", isQuery=false),
		@Column(name="elevation_back", attrName="elevationBack", label="高程", comment="高程(m)(后视)", isQuery=false),
		@Column(name="elevation", attrName="elevation", label="高程", comment="高程(m)", isQuery=false),
		@Column(name="err_eleva_diff_round_trip_fore", attrName="errElevaDiffRoundTripFore", label="往返测高差较差", comment="往返测高差较差(mm)(与前站)", isQuery=false),
		@Column(name="err_eleva_diff_round_trip_fore_norm", attrName="errElevaDiffRoundTripForeNorm", label="往返测高差较差标准", comment="往返测高差较差标准(mm)(与前站)", isQuery=false),
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
				// 2022年6月7日 su添加
				@JoinTable(type = JoinTable.Type.LEFT_JOIN, entity = ElResultEvaluation.class, attrName = "resEval", alias = "u7",
						on = "u7.line_id = u5.id and u7.solution_date = a.solution_date", columns = {
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

		orderBy="u6.id ASC, u5.id ASC, a.solution_date DESC, a.station_id ASC", extWhereKeys = "dsfOffice"
)
public class ElDataSolution extends DataEntity<ElDataSolution> {
	
	private static final long serialVersionUID = 1L;
	private String stationId;		// 测站id
	private String foreStnId;		// 前视站点id
	private String backStnId;		// 后视站点id
	private Double heightInstrumentForeAvg;		// 各测回平均仪器高(m)(测前站时)
	private Double heightLensForeAvg;		// 各测回平均镜高(m)(测前站时)
	private Integer verticalAngSignFore;		// 垂直角的符号(与前站)(1正 -1负)
	private Integer verticalAngDegForeAvg;		// 各测回平均垂直角度(与前站)
	private Integer verticalAngMinForeAvg;		// 各测回平均垂直角分(与前站)
	private Double verticalAngSecForeAvg;		// 各测回平均垂直角秒(与前站)
	private Double verticalAngRadianFore;		// 垂直角弧度(与前站)
	private Double verticalAngRadianForeCor;		// 改正后的垂直角弧度(与前站)
	private Double elevationDiffFore;		// 高差(m)(与前站)
	private Double elevationDiffForeCor;		// 改正后的高差(m)(与前站)
	private Double elevationFore;			// 高程(m)(前视)
	private Double heightInstrumentBackAvg;		// 各测回平均仪器高(m)(测后站时)
	private Double heightLensBackAvg;		// 各测回平均镜高(m)(测后站时)
	private Integer verticalAngSignBack;		// 垂直角的符号(与后站)(1正 -1负)
	private Integer verticalAngDegBackAvg;		// 各测回平均垂直角度(与后站)
	private Integer verticalAngMinBackAvg;		// 各测回平均垂直角分(与后站)
	private Double verticalAngSecBackAvg;		// 各测回平均垂直角秒(与后站)
	private Double verticalAngRadianBack;		// 垂直角弧度(与后站)
	private Double verticalAngRadianBackCor;		// 改正后的垂直角弧度(与后站)
	private Double elevationDiffBack;		// 高差(m)(与后站)
	private Double elevationDiffBackCor;		// 改正后的高差(m)(与后站)
	private Double distanceForeAvg;		// 平均平距(m)(与前站)
	private Double elevationBack;			// 高程(m)(后视)
	private Double elevation;		// 高程(m)
	private Double errElevaDiffRoundTripFore;		// 往返测高差较差(mm)(与前站)
	private Double errElevaDiffRoundTripForeNorm;		// 往返测高差较差标准(mm)(与前站)
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
	// 结果评定
	private ElResultEvaluation resEval;

	// 是否考虑球气差（1是 0否）
	private Integer isQQC;
	// 大气折光系数
	private Double coefficient_K;
	// 地球半径(km)
	private Double coefficient_R;

	//用于在Excel显示的字段 2022年7月8日 su添加
	// 带符号的度
	private String verticalAngDegForeAvgStr;
	private String verticalAngDegBackAvgStr;

	public ElDataSolution() {
		this(null);
	}

	public ElDataSolution(String id){
		super(id);
	}

	// 2022年7月7日 su添加
	// 配置所需要输出到Excel的字段
	@Valid
	@ExcelFields({
			@ExcelField(title = "后视站点", attrName = "backStn.stationName", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 10),
			@ExcelField(title = "测站", attrName = "station.stationName", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 20),
			@ExcelField(title = "前视站点", attrName = "foreStn.stationName", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 30),
			@ExcelField(title = "仪器高(m)", attrName = "heightInstrumentForeAvg", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 40, dataFormat = "0.0000"),
			@ExcelField(title = "镜高(m)", attrName = "heightLensForeAvg", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 50, dataFormat = "0.0000"),
			@ExcelField(title = "度", attrName = "verticalAngDegForeAvgStr", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 60),
			@ExcelField(title = "分", attrName = "verticalAngMinForeAvg", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 70),
			@ExcelField(title = "秒", attrName = "verticalAngSecForeAvg", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 80, dataFormat = "0.0"),
			@ExcelField(title = "弧度", attrName = "verticalAngRadianFore", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 81, dataFormat = "0.000000000"),
			@ExcelField(title = "高差(m)", attrName = "elevationDiffFore", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 82, dataFormat = "0.0000"),
			@ExcelField(title = "改正后的高差(m)", attrName = "elevationDiffForeCor", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 83, dataFormat = "0.0000"),
			@ExcelField(title = "平均平距(前视)(m)", attrName = "distanceForeAvg", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 90, dataFormat = "0.0000"),
			@ExcelField(title = "仪器高(m)", attrName = "heightInstrumentBackAvg", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 100, dataFormat = "0.0000"),
			@ExcelField(title = "镜高(m)", attrName = "heightLensBackAvg", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 110, dataFormat = "0.0000"),
			@ExcelField(title = "度", attrName = "verticalAngDegBackAvgStr", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 120),
			@ExcelField(title = "分", attrName = "verticalAngMinBackAvg", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 130),
			@ExcelField(title = "秒", attrName = "verticalAngSecBackAvg", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 140, dataFormat = "0.0"),
			@ExcelField(title = "弧度", attrName = "verticalAngRadianBack", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 140, dataFormat = "0.000000000"),
			@ExcelField(title = "高差(m)", attrName = "elevationDiffBack", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 141, dataFormat = "0.0000"),
			@ExcelField(title = "改正后的高差(m)", attrName = "elevationDiffBackCor", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 142, dataFormat = "0.0000"),
			@ExcelField(title = "前视", attrName = "elevationFore", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 150, dataFormat = "0.0000"),
			@ExcelField(title = "后视", attrName = "elevationBack", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 151, dataFormat = "0.0000"),
			@ExcelField(title = "平均", attrName = "elevation", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 152, dataFormat = "0.0000"),
			@ExcelField(title = "导线等级", attrName = "resEval.traverseClass", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 160),
			@ExcelField(title = "球气差", attrName = "resEval.coefficient_QQC", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 170),
			@ExcelField(title = "计算", attrName = "errElevaDiffRoundTripFore", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 180, dataFormat = "0.00"),
			@ExcelField(title = "标准", attrName = "errElevaDiffRoundTripForeNorm", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 181, dataFormat = "0.00"),
			@ExcelField(title = "计算", attrName = "resEval.fullMidErrElevaDiffPerKm", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 190, dataFormat = "0.00"),
			@ExcelField(title = "标准", attrName = "resEval.fullMidErrElevaDiffPerKmNorm", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 191, dataFormat = "0.00"),
			@ExcelField(title = "计算", attrName = "resEval.closErrLine", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 200, dataFormat = "0.00"),
			@ExcelField(title = "标准", attrName = "resEval.closErrLineNorm", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 201, dataFormat = "0.00"),
			@ExcelField(title = "评价", attrName = "resEval.resultEvaluation", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 210),
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
	
	public Double getHeightInstrumentForeAvg() {
		return heightInstrumentForeAvg;
	}

	public void setHeightInstrumentForeAvg(Double heightInstrumentForeAvg) {
		this.heightInstrumentForeAvg = heightInstrumentForeAvg;
	}
	
	public Double getHeightLensForeAvg() {
		return heightLensForeAvg;
	}

	public void setHeightLensForeAvg(Double heightLensForeAvg) {
		this.heightLensForeAvg = heightLensForeAvg;
	}

	public Integer getVerticalAngSignFore() {
		return verticalAngSignFore;
	}

	public void setVerticalAngSignFore(Integer verticalAngSignFore) {
		this.verticalAngSignFore = verticalAngSignFore;
	}

	public Integer getVerticalAngDegForeAvg() {
		return verticalAngDegForeAvg;
	}

	public void setVerticalAngDegForeAvg(Integer verticalAngDegForeAvg) {
		this.verticalAngDegForeAvg = verticalAngDegForeAvg;
	}
	
	public Integer getVerticalAngMinForeAvg() {
		return verticalAngMinForeAvg;
	}

	public void setVerticalAngMinForeAvg(Integer verticalAngMinForeAvg) {
		this.verticalAngMinForeAvg = verticalAngMinForeAvg;
	}
	
	public Double getVerticalAngSecForeAvg() {
		return verticalAngSecForeAvg;
	}

	public void setVerticalAngSecForeAvg(Double verticalAngSecForeAvg) {
		this.verticalAngSecForeAvg = verticalAngSecForeAvg;
	}
	
	public Double getVerticalAngRadianFore() {
		return verticalAngRadianFore;
	}

	public void setVerticalAngRadianFore(Double verticalAngRadianFore) {
		this.verticalAngRadianFore = verticalAngRadianFore;
	}
	
	public Double getVerticalAngRadianForeCor() {
		return verticalAngRadianForeCor;
	}

	public void setVerticalAngRadianForeCor(Double verticalAngRadianForeCor) {
		this.verticalAngRadianForeCor = verticalAngRadianForeCor;
	}
	
	public Double getElevationDiffFore() {
		return elevationDiffFore;
	}

	public void setElevationDiffFore(Double elevationDiffFore) {
		this.elevationDiffFore = elevationDiffFore;
	}
	
	public Double getElevationDiffForeCor() {
		return elevationDiffForeCor;
	}

	public void setElevationDiffForeCor(Double elevationDiffForeCor) {
		this.elevationDiffForeCor = elevationDiffForeCor;
	}

	public Double getElevationFore() {
		return elevationFore;
	}

	public void setElevationFore(Double elevationFore) {
		this.elevationFore = elevationFore;
	}

	public Double getHeightInstrumentBackAvg() {
		return heightInstrumentBackAvg;
	}

	public void setHeightInstrumentBackAvg(Double heightInstrumentBackAvg) {
		this.heightInstrumentBackAvg = heightInstrumentBackAvg;
	}
	
	public Double getHeightLensBackAvg() {
		return heightLensBackAvg;
	}

	public void setHeightLensBackAvg(Double heightLensBackAvg) {
		this.heightLensBackAvg = heightLensBackAvg;
	}

	public Integer getVerticalAngSignBack() {
		return verticalAngSignBack;
	}

	public void setVerticalAngSignBack(Integer verticalAngSignBack) {
		this.verticalAngSignBack = verticalAngSignBack;
	}

	public Integer getVerticalAngDegBackAvg() {
		return verticalAngDegBackAvg;
	}

	public void setVerticalAngDegBackAvg(Integer verticalAngDegBackAvg) {
		this.verticalAngDegBackAvg = verticalAngDegBackAvg;
	}
	
	public Integer getVerticalAngMinBackAvg() {
		return verticalAngMinBackAvg;
	}

	public void setVerticalAngMinBackAvg(Integer verticalAngMinBackAvg) {
		this.verticalAngMinBackAvg = verticalAngMinBackAvg;
	}
	
	public Double getVerticalAngSecBackAvg() {
		return verticalAngSecBackAvg;
	}

	public void setVerticalAngSecBackAvg(Double verticalAngSecBackAvg) {
		this.verticalAngSecBackAvg = verticalAngSecBackAvg;
	}
	
	public Double getVerticalAngRadianBack() {
		return verticalAngRadianBack;
	}

	public void setVerticalAngRadianBack(Double verticalAngRadianBack) {
		this.verticalAngRadianBack = verticalAngRadianBack;
	}
	
	public Double getVerticalAngRadianBackCor() {
		return verticalAngRadianBackCor;
	}

	public void setVerticalAngRadianBackCor(Double verticalAngRadianBackCor) {
		this.verticalAngRadianBackCor = verticalAngRadianBackCor;
	}
	
	public Double getElevationDiffBack() {
		return elevationDiffBack;
	}

	public void setElevationDiffBack(Double elevationDiffBack) {
		this.elevationDiffBack = elevationDiffBack;
	}
	
	public Double getElevationDiffBackCor() {
		return elevationDiffBackCor;
	}

	public void setElevationDiffBackCor(Double elevationDiffBackCor) {
		this.elevationDiffBackCor = elevationDiffBackCor;
	}

	public Double getDistanceForeAvg() {
		return distanceForeAvg;
	}

	public void setDistanceForeAvg(Double distanceForeAvg) {
		this.distanceForeAvg = distanceForeAvg;
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

	public Double getErrElevaDiffRoundTripFore() {
		return errElevaDiffRoundTripFore;
	}

	public void setErrElevaDiffRoundTripFore(Double errElevaDiffRoundTripFore) {
		this.errElevaDiffRoundTripFore = errElevaDiffRoundTripFore;
	}

	public Double getErrElevaDiffRoundTripForeNorm() {
		return errElevaDiffRoundTripForeNorm;
	}

	public void setErrElevaDiffRoundTripForeNorm(Double errElevaDiffRoundTripForeNorm) {
		this.errElevaDiffRoundTripForeNorm = errElevaDiffRoundTripForeNorm;
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

	public Integer getIsQQC() {
		return isQQC;
	}

	public void setIsQQC(Integer isQQC) {
		this.isQQC = isQQC;
	}

	public Double getCoefficient_K() {
		return coefficient_K;
	}

	public void setCoefficient_K(Double coefficient_K) {
		this.coefficient_K = coefficient_K;
	}

	public Double getCoefficient_R() {
		return coefficient_R;
	}

	public void setCoefficient_R(Double coefficient_R) {
		this.coefficient_R = coefficient_R;
	}

	public ElResultEvaluation getResEval() {
		return resEval;
	}

	public void setResEval(ElResultEvaluation resEval) {
		this.resEval = resEval;
	}

	public String getVerticalAngDegForeAvgStr() {
		if (verticalAngSignFore == -1) {
			return "-" + verticalAngDegForeAvg;
		}
		return String.valueOf(verticalAngDegForeAvg);
	}

	public String getVerticalAngDegBackAvgStr() {
		if (verticalAngSignBack == -1) {
			return "-" + verticalAngDegBackAvg;
		}
		return String.valueOf(verticalAngDegBackAvg);
	}
}