/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.elevation.el_observe_data.entity;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import com.jeesite.common.mybatis.mapper.query.QueryType;
import com.jeesite.common.utils.excel.annotation.ExcelField;
import com.jeesite.common.utils.excel.annotation.ExcelFields;
import com.jeesite.elevation.el_line.entity.ELLine;
import com.jeesite.elevation.el_station.entity.ELStation;
import com.jeesite.modules.group.entity.Group;
import com.jeesite.modules.sys.entity.Employee;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotNull;
import java.util.Date;
import com.jeesite.common.mybatis.annotation.JoinTable;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;

/**
 * 高程——观测数据Entity
 * @author su
 * @version 2022-05-23
 */
@Table(name="el_observe_data", alias="a", columns={
		@Column(name="id", attrName="id", label="id", isPK=true),
		@Column(name="station_id", attrName="stationId", label="测站id"),
		@Column(name="fore_stn_id", attrName="foreStnId", label="前视站点id"),
		@Column(name="back_stn_id", attrName="backStnId", label="后视站点id"),
		@Column(name="observe_round", attrName="observeRound", label="测回", isQuery=false),
		@Column(name="height_instrument_fore", attrName="heightInstrumentFore", label="仪器高", comment="仪器高(m)(测前站时)", isQuery=false),
		@Column(name="height_lens_fore", attrName="heightLensFore", label="镜高", comment="镜高(m)(测前站时)", isQuery=false),
		@Column(name="vertical_ang_sign_fore", attrName="verticalAngSignFore", label="垂直角的符号", comment="垂直角的符号(与前站)(1正 -1负)", isQuery=false),
		@Column(name="vertical_ang_deg_fore_str", attrName="verticalAngDegForeStr", label="带符号的垂直角度", comment="带符号的垂直角度(与前站)", isQuery=false),
		@Column(name="vertical_ang_deg_fore", attrName="verticalAngDegFore", label="垂直角度", comment="垂直角度(与前站)", isQuery=false),
		@Column(name="vertical_ang_min_fore", attrName="verticalAngMinFore", label="垂直角分", comment="垂直角分(与前站)", isQuery=false),
		@Column(name="vertical_ang_sec_fore", attrName="verticalAngSecFore", label="垂直角秒", comment="垂直角秒(与前站)", isQuery=false),
		@Column(name="distance_fore", attrName="distanceFore", label="平距", comment="平距(m)(与前站)", isQuery=false),
		@Column(name="height_instrument_back", attrName="heightInstrumentBack", label="仪器高", comment="仪器高(m)(测后站时)", isQuery=false),
		@Column(name="height_lens_back", attrName="heightLensBack", label="镜高", comment="镜高(m)(测后站时)", isQuery=false),
		@Column(name="vertical_ang_sign_back", attrName="verticalAngSignBack", label="垂直角的符号", comment="垂直角的符号(与后站)(1正 -1负)", isQuery=false),
		@Column(name="vertical_ang_deg_back_str", attrName="verticalAngDegBackStr", label="带符号的垂直角度", comment="带符号的垂直角度(与后站)", isQuery=false),
		@Column(name="vertical_ang_deg_back", attrName="verticalAngDegBack", label="垂直角度", comment="垂直角度(与后站)", isQuery=false),
		@Column(name="vertical_ang_min_back", attrName="verticalAngMinBack", label="垂直角分", comment="垂直角分(与后站)", isQuery=false),
		@Column(name="vertical_ang_sec_back", attrName="verticalAngSecBack", label="垂直角秒", comment="垂直角秒(与后站)", isQuery=false),
		@Column(name="distance_back", attrName="distanceBack", label="平距", comment="平距(m)(与后站)", isQuery=false),
		@Column(name="solution_date", attrName="solutionDate", label="解算时间", queryType= QueryType.LIKE),
		@Column(includeEntity=DataEntity.class),
	},

		//2022年5月23日 su添加
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
						@Column(name = "group_name", label = "小组名称", isQuery = false)
				})
		},

		orderBy="u6.id ASC, u5.id ASC, a.solution_date DESC, a.station_id ASC, a.observe_round ASC", extWhereKeys = "dsfOffice"
)
public class ElObserveData extends DataEntity<ElObserveData> {
	
	private static final long serialVersionUID = 1L;
	private String stationId;		// 测站id
	private String foreStnId;		// 前视站点id
	private String backStnId;		// 后视站点id
	private Integer observeRound;		// 测回
	private Double heightInstrumentFore;		// 仪器高(m)(测前站时)
	private Double heightLensFore;		// 镜高(m)(测前站时)
	private Integer verticalAngSignFore;		// 垂直角的符号(与前站)(1正 -1负)
	private String verticalAngDegForeStr;		// 带符号的垂直角度(与前站)
	private Integer verticalAngDegFore;		// 垂直角度(与前站)
	private Integer verticalAngMinFore;		// 垂直角分(与前站)
	private Double verticalAngSecFore;		// 垂直角秒(与前站)
	private Double distanceFore;		// 平距(m)(与前站)
	private Double heightInstrumentBack;		// 仪器高(m)(测后站时)
	private Double heightLensBack;		// 镜高(m)(测后站时)
	private Integer verticalAngSignBack;	// 垂直角的符号(与后站)(1正 -1负)
	private String verticalAngDegBackStr;		// 带符号的垂直角度(与后站)
	private Integer verticalAngDegBack;		// 垂直角度(与后站)
	private Integer verticalAngMinBack;		// 垂直角分(与后站)
	private Double verticalAngSecBack;		// 垂直角秒(与后站)
	private Double distanceBack;		// 平距(m)(与后站)
	private Date solutionDate;		// 解算时间

	//2022年5月23日 su添加
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

	public ElObserveData() {
		this(null);
	}

	public ElObserveData(String id){
		super(id);
	}

	// 2022年7月7日 su添加
	// 配置所需要输出到Excel的字段
	@Valid
	@ExcelFields({
			@ExcelField(title = "后视站点", attrName = "backStn.stationName", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 10),
			@ExcelField(title = "测站", attrName = "station.stationName", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 20),
			@ExcelField(title = "前视站点", attrName = "foreStn.stationName", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 30),
			@ExcelField(title = "测回", attrName = "observeRound", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 31),
			@ExcelField(title = "仪器高(m)", attrName = "heightInstrumentFore", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 40, dataFormat = "0.0000"),
			@ExcelField(title = "镜高(m)", attrName = "heightLensFore", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 50, dataFormat = "0.0000"),
			@ExcelField(title = "度", attrName = "verticalAngDegForeStr", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 60),
			@ExcelField(title = "分", attrName = "verticalAngMinFore", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 70),
			@ExcelField(title = "秒", attrName = "verticalAngSecFore", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 80, dataFormat = "0.0"),
			@ExcelField(title = "平距(m)", attrName = "distanceFore", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 90, dataFormat = "0.0000"),
			@ExcelField(title = "仪器高(m)", attrName = "heightInstrumentBack", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 100, dataFormat = "0.0000"),
			@ExcelField(title = "镜高(m)", attrName = "heightLensBack", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 110, dataFormat = "0.0000"),
			@ExcelField(title = "度", attrName = "verticalAngDegBackStr", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 120),
			@ExcelField(title = "分", attrName = "verticalAngMinBack", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 130),
			@ExcelField(title = "秒", attrName = "verticalAngSecBack", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 140, dataFormat = "0.0"),
			@ExcelField(title = "平距(m)", attrName = "distanceBack", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 150, dataFormat = "0.0000"),
			@ExcelField(title = "解算时间", attrName = "solutionDate", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 160),
			@ExcelField(title = "备注信息", attrName = "remarks", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 170),
	})
	
	@NotBlank(message="测站id不能为空")
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

	public Integer getObserveRound() {
		return observeRound;
	}

	public void setObserveRound(Integer observeRound) {
		this.observeRound = observeRound;
	}
	
	@NotNull(message="仪器高不能为空")
	public Double getHeightInstrumentFore() {
		return heightInstrumentFore;
	}

	public void setHeightInstrumentFore(Double heightInstrumentFore) {
		this.heightInstrumentFore = heightInstrumentFore;
	}
	
	@NotNull(message="镜高不能为空")
	public Double getHeightLensFore() {
		return heightLensFore;
	}

	public void setHeightLensFore(Double heightLensFore) {
		this.heightLensFore = heightLensFore;
	}

	public Integer getVerticalAngSignFore() {
		return verticalAngSignFore;
	}

	public void setVerticalAngSignFore(Integer verticalAngSignFore) {
		this.verticalAngSignFore = verticalAngSignFore;
	}

	@NotNull(message="垂直角度不能为空")
	public String getVerticalAngDegForeStr() {
		return verticalAngDegForeStr;
	}

	public void setVerticalAngDegForeStr(String verticalAngDegForeStr) {
		this.verticalAngDegForeStr = verticalAngDegForeStr;
	}

	public Integer getVerticalAngDegFore() {
		return verticalAngDegFore;
	}

	public void setVerticalAngDegFore(Integer verticalAngDegFore) {
		this.verticalAngDegFore = verticalAngDegFore;
	}
	
	@NotNull(message="垂直角分不能为空")
	public Integer getVerticalAngMinFore() {
		return verticalAngMinFore;
	}

	public void setVerticalAngMinFore(Integer verticalAngMinFore) {
		this.verticalAngMinFore = verticalAngMinFore;
	}
	
	@NotNull(message="垂直角秒不能为空")
	public Double getVerticalAngSecFore() {
		return verticalAngSecFore;
	}

	public void setVerticalAngSecFore(Double verticalAngSecFore) {
		this.verticalAngSecFore = verticalAngSecFore;
	}
	
	@NotNull(message="平距不能为空")
	public Double getDistanceFore() {
		return distanceFore;
	}

	public void setDistanceFore(Double distanceFore) {
		this.distanceFore = distanceFore;
	}
	
	@NotNull(message="仪器高不能为空")
	public Double getHeightInstrumentBack() {
		return heightInstrumentBack;
	}

	public void setHeightInstrumentBack(Double heightInstrumentBack) {
		this.heightInstrumentBack = heightInstrumentBack;
	}
	
	@NotNull(message="镜高不能为空")
	public Double getHeightLensBack() {
		return heightLensBack;
	}

	public void setHeightLensBack(Double heightLensBack) {
		this.heightLensBack = heightLensBack;
	}

	public Integer getVerticalAngSignBack() {
		return verticalAngSignBack;
	}

	public void setVerticalAngSignBack(Integer verticalAngSignBack) {
		this.verticalAngSignBack = verticalAngSignBack;
	}

	@NotNull(message="垂直角度不能为空")
	public String getVerticalAngDegBackStr() {
		return verticalAngDegBackStr;
	}

	public void setVerticalAngDegBackStr(String verticalAngDegBackStr) {
		this.verticalAngDegBackStr = verticalAngDegBackStr;
	}

	public Integer getVerticalAngDegBack() {
		return verticalAngDegBack;
	}

	public void setVerticalAngDegBack(Integer verticalAngDegBack) {
		this.verticalAngDegBack = verticalAngDegBack;
	}
	
	@NotNull(message="垂直角分不能为空")
	public Integer getVerticalAngMinBack() {
		return verticalAngMinBack;
	}

	public void setVerticalAngMinBack(Integer verticalAngMinBack) {
		this.verticalAngMinBack = verticalAngMinBack;
	}
	
	@NotNull(message="垂直角秒不能为空")
	public Double getVerticalAngSecBack() {
		return verticalAngSecBack;
	}

	public void setVerticalAngSecBack(Double verticalAngSecBack) {
		this.verticalAngSecBack = verticalAngSecBack;
	}
	
	@NotNull(message="平距不能为空")
	public Double getDistanceBack() {
		return distanceBack;
	}

	public void setDistanceBack(Double distanceBack) {
		this.distanceBack = distanceBack;
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
}