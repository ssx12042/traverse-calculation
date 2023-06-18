/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.observation.entity;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import com.jeesite.common.utils.excel.annotation.ExcelField;
import com.jeesite.common.utils.excel.annotation.ExcelFields;
import com.jeesite.modules.group.entity.Group;
import com.jeesite.modules.line.entity.Line;
import com.jeesite.modules.station.entity.Station;
import com.jeesite.modules.sys.entity.Employee;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotNull;
import java.util.Date;
import com.jeesite.common.mybatis.annotation.JoinTable;
import com.jeesite.common.mybatis.annotation.JoinTable.Type;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;

/**
 * 观测数据Entity
 * @author su
 * @version 2022-03-14
 */
@Table(name="observation_data", alias="a", columns={
		@Column(name="id", attrName="id", label="id", isPK=true),
		@Column(name="station_id", attrName="stationId", label="测站id"),
		@Column(name="fore_stn_id", attrName="foreStnId", label="前视站点id"),
		@Column(name="back_stn_id", attrName="backStnId", label="后视站点id"),
		@Column(name="observation_round", attrName="observationRound", label="测回", isQuery=false),
		@Column(name="fore_left_deg", attrName="foreLeftDeg", label="前左度", isQuery=false),
		@Column(name="fore_left_min", attrName="foreLeftMin", label="前左分", isQuery=false),
		@Column(name="fore_left_sec", attrName="foreLeftSec", label="前左秒", isQuery=false),
		@Column(name="fore_right_deg", attrName="foreRightDeg", label="前右度", isQuery=false),
		@Column(name="fore_right_min", attrName="foreRightMin", label="前右分", isQuery=false),
		@Column(name="fore_right_sec", attrName="foreRightSec", label="前右秒", isQuery=false),
		@Column(name="back_left_deg", attrName="backLeftDeg", label="后左度", isQuery=false),
		@Column(name="back_left_min", attrName="backLeftMin", label="后左分", isQuery=false),
		@Column(name="back_left_sec", attrName="backLeftSec", label="后左秒", isQuery=false),
		@Column(name="back_right_deg", attrName="backRightDeg", label="后右度", isQuery=false),
		@Column(name="back_right_min", attrName="backRightMin", label="后右分", isQuery=false),
		@Column(name="back_right_sec", attrName="backRightSec", label="后右秒", isQuery=false),
		@Column(name="fore_distance", attrName="foreDistance", label="前视平距", isQuery=false),
		@Column(name="back_distance", attrName="backDistance", label="后视平距", isQuery=false),
		@Column(name="half_left_deg", attrName="halfLeftDeg", label="盘左半测回角值度", isQuery=false),
		@Column(name="half_left_min", attrName="halfLeftMin", label="盘左半测回角值分", isQuery=false),
		@Column(name="half_left_sec", attrName="halfLeftSec", label="盘左半测回角值秒", isQuery=false),
		@Column(name="half_right_deg", attrName="halfRightDeg", label="盘右半测回角值度", isQuery=false),
		@Column(name="half_right_min", attrName="halfRightMin", label="盘右半测回角值分", isQuery=false),
		@Column(name="half_right_sec", attrName="halfRightSec", label="盘右半测回角值秒", isQuery=false),
		@Column(name="full_value_deg", attrName="fullValueDeg", label="一测回角值度", isQuery=false),
		@Column(name="full_value_min", attrName="fullValueMin", label="一测回角值分", isQuery=false),
		@Column(name="full_value_sec", attrName="fullValueSec", label="一测回角值秒", isQuery=false),
		@Column(name="turning_ang_radian", attrName="turningAngRadian", label="转折角弧度", isQuery=false),
		@Column(name="double_c_fore", attrName="doubleCFore", label="前视2C互差", isQuery=false),
		@Column(name="double_c_back", attrName="doubleCBack", label="后视2C互差", isQuery=false),
		@Column(name="solution_date", attrName="solutionDate", label="解算时间", queryType=QueryType.LIKE),
		@Column(includeEntity=DataEntity.class),
	},

		//2022年3月14日 su添加
		joinTable = {
				@JoinTable(type = JoinTable.Type.LEFT_JOIN, entity = Employee.class, attrName = "employee", alias = "u1",
						on = "u1.emp_code = a.create_by", columns = {
						@Column(name = "emp_code", label = "用户编码", isPK = true),
						@Column(name = "office_code", label = "机构编码", isQuery = false),
						@Column(name = "office_name", label = "机构名称"),
				}),
				@JoinTable(type = JoinTable.Type.LEFT_JOIN, entity = Station.class, attrName = "station", alias = "u2",
						on = "u2.id = a.station_id", columns = {
						@Column(name = "line_id", label = "导线名称", isQuery = false),
						@Column(name = "station_name", label = "测站名称", isQuery = false),
				}),
				@JoinTable(type = JoinTable.Type.LEFT_JOIN, entity = Station.class, attrName = "foreStn", alias = "u3",
						on = "u3.id = a.fore_stn_id", columns = {
						@Column(name = "line_id", label = "导线名称", isQuery = false),
						@Column(name = "station_name", label = "前视站点名称", isQuery = false),
				}),
				@JoinTable(type = JoinTable.Type.LEFT_JOIN, entity = Station.class, attrName = "backStn", alias = "u4",
						on = "u4.id = a.back_stn_id", columns = {
						@Column(name = "station_name", label = "后视站点名称", isQuery = false),
				}),
				@JoinTable(type = JoinTable.Type.LEFT_JOIN, entity = Line.class, attrName = "line", alias = "u5",
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

		orderBy="u6.id ASC, u5.id ASC, a.solution_date DESC, a.station_id ASC, a.observation_round ASC", extWhereKeys = "dsfOffice"
)
public class ObservationData extends DataEntity<ObservationData> {
	
	private static final long serialVersionUID = 1L;
	private String stationId;		// 测站id
	private String foreStnId;		// 前视站点id
	private String backStnId;		// 后视站点id
	public Integer foreLeftDeg;		// 前左度
	public Integer foreLeftMin;		// 前左分
	public Double foreLeftSec;		// 前左秒
	public Integer foreRightDeg;		// 前右度
	public Integer foreRightMin;		// 前右分
	public Double foreRightSec;		// 前右秒
	public Integer backLeftDeg;		// 后左度
	public Integer backLeftMin;		// 后左分
	public Double backLeftSec;		// 后左秒
	public Integer backRightDeg;		// 后右度
	public Integer backRightMin;		// 后右分
	public Double backRightSec;		// 后右秒
	public Double foreDistance;		// 前视平距
	public Double backDistance;		// 后视平距
	public Integer halfLeftDeg;		// 盘左半测回角值度
	public Integer halfLeftMin;		// 盘左半测回角值分
	public Double halfLeftSec;		// 盘左半测回角值秒
	public Integer halfRightDeg;		// 盘右半测回角值度
	public Integer halfRightMin;		// 盘右半测回角值分
	public Double halfRightSec;		// 盘右半测回角值秒
	public Integer fullValueDeg;		// 一测回角值度
	public Integer fullValueMin;		// 一测回角值分
	public Double fullValueSec;		// 一测回角值秒
	public Double turningAngRadian;		// 转折角弧度
	public Double doubleCFore;		// 前视2C互差
	public Double doubleCBack;		// 后视2C互差
	private Date solutionDate;		// 解算时间
	private Integer observationRound;	// 测回

	//2022年3月14日 su添加
	//测站
	private Station station;
	//前视站点
	private Station foreStn;
	//后视站点
	private Station backStn;
	//导线
	private Line line;
	//小组
	private Group group;
	
	public ObservationData() {
		this(null);
	}

	public ObservationData(String id){
		super(id);
	}

	// 2022年4月25日 su添加
	// 配置所需要输出到Excel的字段
	@Valid
	@ExcelFields({
			@ExcelField(title = "后视站点", attrName = "backStn.stationName", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 3),
			@ExcelField(title = "测站", attrName = "station.stationName", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 4),
			@ExcelField(title = "前视站点", attrName = "foreStn.stationName", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 5),
			@ExcelField(title = "测回", attrName = "observationRound", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 6),
			@ExcelField(title = "前左度", attrName = "foreLeftDeg", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 7),
			@ExcelField(title = "前左分", attrName = "foreLeftMin", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 8),
			@ExcelField(title = "前左秒", attrName = "foreLeftSec", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 9, dataFormat = "0.0"),
			@ExcelField(title = "前右度", attrName = "foreRightDeg", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 10),
			@ExcelField(title = "前右分", attrName = "foreRightMin", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 11),
			@ExcelField(title = "前右秒", attrName = "foreRightSec", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 12, dataFormat = "0.0"),
			@ExcelField(title = "后左度", attrName = "backLeftDeg", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 13),
			@ExcelField(title = "后左分", attrName = "backLeftMin", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 14),
			@ExcelField(title = "后左秒", attrName = "backLeftSec", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 15, dataFormat = "0.0"),
			@ExcelField(title = "后右度", attrName = "backRightDeg", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 16),
			@ExcelField(title = "后右分", attrName = "backRightMin", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 17),
			@ExcelField(title = "后右秒", attrName = "backRightSec", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 18, dataFormat = "0.0"),
			@ExcelField(title = "平距(m)(前视)", attrName = "foreDistance", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 19, dataFormat = "0.0000"),
			@ExcelField(title = "平距(m)(后视)", attrName = "backDistance", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 20, dataFormat = "0.0000"),
			@ExcelField(title = "盘左半测回角-度", attrName = "halfLeftDeg", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 21),
			@ExcelField(title = "盘左半测回角-分", attrName = "halfLeftMin", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 22),
			@ExcelField(title = "盘左半测回角-秒", attrName = "halfLeftSec", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 23, dataFormat = "0.0"),
			@ExcelField(title = "盘右半测回角-度", attrName = "halfRightDeg", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 24),
			@ExcelField(title = "盘右半测回角-分", attrName = "halfRightMin", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 25),
			@ExcelField(title = "盘右半测回角-秒", attrName = "halfRightSec", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 26, dataFormat = "0.0"),
			@ExcelField(title = "一测回角-度", attrName = "fullValueDeg", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 27),
			@ExcelField(title = "一测回角-分", attrName = "fullValueMin", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 28),
			@ExcelField(title = "一测回角-秒", attrName = "fullValueSec", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 29, dataFormat = "0.0"),
			@ExcelField(title = "2C互差(\")(前视)", attrName = "doubleCFore", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 30, dataFormat = "0.0"),
			@ExcelField(title = "2C互差(\")(后视)", attrName = "doubleCBack", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 31, dataFormat = "0.0"),
			@ExcelField(title = "更新时间", attrName = "updateDate", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 80),
			@ExcelField(title = "备注信息", attrName = "remarks", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 90),
	})

	@NotBlank(message="测站id不能为空")
	@Length(min=0, max=64, message="测站id长度不能超过 64 个字符")
	public String getStationId() {
		return stationId;
	}

	public void setStationId(String stationId) {
		this.stationId = stationId;
	}
	
	@NotBlank(message="前视站点id不能为空")
	@Length(min=0, max=64, message="前视站点id长度不能超过 64 个字符")
	public String getForeStnId() {
		return foreStnId;
	}

	public void setForeStnId(String foreStnId) {
		this.foreStnId = foreStnId;
	}
	
	@NotBlank(message="后视站点id不能为空")
	@Length(min=0, max=64, message="后视站点id长度不能超过 64 个字符")
	public String getBackStnId() {
		return backStnId;
	}

	public void setBackStnId(String backStnId) {
		this.backStnId = backStnId;
	}
	
	@NotNull(message="前左度不能为空")
	public Integer getForeLeftDeg() {
		return foreLeftDeg;
	}

	public void setForeLeftDeg(Integer foreLeftDeg) {
		this.foreLeftDeg = foreLeftDeg;
	}
	
	@NotNull(message="前左分不能为空")
	public Integer getForeLeftMin() {
		return foreLeftMin;
	}

	public void setForeLeftMin(Integer foreLeftMin) {
		this.foreLeftMin = foreLeftMin;
	}
	
	@NotNull(message="前左秒不能为空")
	public Double getForeLeftSec() {
		return foreLeftSec;
	}

	public void setForeLeftSec(Double foreLeftSec) {
		this.foreLeftSec = foreLeftSec;
	}
	
	@NotNull(message="前右度不能为空")
	public Integer getForeRightDeg() {
		return foreRightDeg;
	}

	public void setForeRightDeg(Integer foreRightDeg) {
		this.foreRightDeg = foreRightDeg;
	}
	
	@NotNull(message="前右分不能为空")
	public Integer getForeRightMin() {
		return foreRightMin;
	}

	public void setForeRightMin(Integer foreRightMin) {
		this.foreRightMin = foreRightMin;
	}
	
	@NotNull(message="前右秒不能为空")
	public Double getForeRightSec() {
		return foreRightSec;
	}

	public void setForeRightSec(Double foreRightSec) {
		this.foreRightSec = foreRightSec;
	}
	
	@NotNull(message="后左度不能为空")
	public Integer getBackLeftDeg() {
		return backLeftDeg;
	}

	public void setBackLeftDeg(Integer backLeftDeg) {
		this.backLeftDeg = backLeftDeg;
	}
	
	@NotNull(message="后左分不能为空")
	public Integer getBackLeftMin() {
		return backLeftMin;
	}

	public void setBackLeftMin(Integer backLeftMin) {
		this.backLeftMin = backLeftMin;
	}
	
	@NotNull(message="后左秒不能为空")
	public Double getBackLeftSec() {
		return backLeftSec;
	}

	public void setBackLeftSec(Double backLeftSec) {
		this.backLeftSec = backLeftSec;
	}
	
	@NotNull(message="后右度不能为空")
	public Integer getBackRightDeg() {
		return backRightDeg;
	}

	public void setBackRightDeg(Integer backRightDeg) {
		this.backRightDeg = backRightDeg;
	}
	
	@NotNull(message="后右分不能为空")
	public Integer getBackRightMin() {
		return backRightMin;
	}

	public void setBackRightMin(Integer backRightMin) {
		this.backRightMin = backRightMin;
	}
	
	@NotNull(message="后右秒不能为空")
	public Double getBackRightSec() {
		return backRightSec;
	}

	public void setBackRightSec(Double backRightSec) {
		this.backRightSec = backRightSec;
	}
	
	@NotNull(message="前视平距不能为空")
	public Double getForeDistance() {
		return foreDistance;
	}

	public void setForeDistance(Double foreDistance) {
		this.foreDistance = foreDistance;
	}
	
	@NotNull(message="后视平距不能为空")
	public Double getBackDistance() {
		return backDistance;
	}

	public void setBackDistance(Double backDistance) {
		this.backDistance = backDistance;
	}
	
	public Integer getHalfLeftDeg() {
		return halfLeftDeg;
	}

	public void setHalfLeftDeg(Integer halfLeftDeg) {
		this.halfLeftDeg = halfLeftDeg;
	}
	
	public Integer getHalfLeftMin() {
		return halfLeftMin;
	}

	public void setHalfLeftMin(Integer halfLeftMin) {
		this.halfLeftMin = halfLeftMin;
	}
	
	public Double getHalfLeftSec() {
		return halfLeftSec;
	}

	public void setHalfLeftSec(Double halfLeftSec) {
		this.halfLeftSec = halfLeftSec;
	}
	
	public Integer getHalfRightDeg() {
		return halfRightDeg;
	}

	public void setHalfRightDeg(Integer halfRightDeg) {
		this.halfRightDeg = halfRightDeg;
	}
	
	public Integer getHalfRightMin() {
		return halfRightMin;
	}

	public void setHalfRightMin(Integer halfRightMin) {
		this.halfRightMin = halfRightMin;
	}
	
	public Double getHalfRightSec() {
		return halfRightSec;
	}

	public void setHalfRightSec(Double halfRightSec) {
		this.halfRightSec = halfRightSec;
	}
	
	public Integer getFullValueDeg() {
		return fullValueDeg;
	}

	public void setFullValueDeg(Integer fullValueDeg) {
		this.fullValueDeg = fullValueDeg;
	}
	
	public Integer getFullValueMin() {
		return fullValueMin;
	}

	public void setFullValueMin(Integer fullValueMin) {
		this.fullValueMin = fullValueMin;
	}
	
	public Double getFullValueSec() {
		return fullValueSec;
	}

	public void setFullValueSec(Double fullValueSec) {
		this.fullValueSec = fullValueSec;
	}
	
	public Double getTurningAngRadian() {
		return turningAngRadian;
	}

	public void setTurningAngRadian(Double turningAngRadian) {
		this.turningAngRadian = turningAngRadian;
	}
	
	public Double getDoubleCFore() {
		return doubleCFore;
	}

	public void setDoubleCFore(Double doubleCFore) {
		this.doubleCFore = doubleCFore;
	}
	
	public Double getDoubleCBack() {
		return doubleCBack;
	}

	public void setDoubleCBack(Double doubleCBack) {
		this.doubleCBack = doubleCBack;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getSolutionDate() {
		return solutionDate;
	}

	public void setSolutionDate(Date solutionDate) {
		this.solutionDate = solutionDate;
	}

	public Integer getObservationRound() {
		return observationRound;
	}

	public void setObservationRound(Integer observationRound) {
		this.observationRound = observationRound;
	}

	public Station getStation() {
		return station;
	}

	public void setStation(Station station) {
		this.station = station;
	}

	public Station getForeStn() {
		return foreStn;
	}

	public void setForeStn(Station foreStn) {
		this.foreStn = foreStn;
	}

	public Station getBackStn() {
		return backStn;
	}

	public void setBackStn(Station backStn) {
		this.backStn = backStn;
	}

	public Line getLine() {
		return line;
	}

	public void setLine(Line line) {
		this.line = line;
	}

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}
}