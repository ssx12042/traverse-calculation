/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.data_solution.entity;

import com.ctc.wstx.shaded.msv_core.datatype.xsd.DateTimeType;
import com.jeesite.common.utils.excel.annotation.ExcelField;
import com.jeesite.common.utils.excel.annotation.ExcelFields;
import com.jeesite.modules.group.entity.Group;
import com.jeesite.modules.line.entity.Line;
import com.jeesite.modules.station.entity.Station;
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
 * 数据解算Entity
 * @author su
 * @version 2022-03-15
 */
@Table(name="data_solution", alias="a", columns={
		@Column(name="id", attrName="id", label="id", isPK=true),
		@Column(name="station_id", attrName="stationId", label="测站id"),
		@Column(name="fore_stn_id", attrName="foreStnId", label="前视站点id"),
		@Column(name="back_stn_id", attrName="backStnId", label="后视站点id"),
		@Column(name="avg_distance", attrName="avgDistance", label="平均平距", comment="平均平距（前视）", isQuery=false),
		@Column(name="avg_value_deg", attrName="avgValueDeg", label="各测回平均角值度", isQuery=false),
		@Column(name="avg_value_min", attrName="avgValueMin", label="各测回平均角值分", isQuery=false),
		@Column(name="avg_value_sec", attrName="avgValueSec", label="各测回平均角值秒", isQuery=false),
		@Column(name="turning_ang_radian", attrName="turningAngRadian", label="转折角弧度", isQuery=false),
		@Column(name="cor_ang_radian", attrName="corAngRadian", label="改正后的转折角弧度", isQuery=false),
		@Column(name="azimuth_ang_deg", attrName="azimuthAngDeg", label="方位角度", comment="方位角度（前视）", isQuery=false),
		@Column(name="azimuth_ang_min", attrName="azimuthAngMin", label="方位角分", comment="方位角分（前视）", isQuery=false),
		@Column(name="azimuth_ang_sec", attrName="azimuthAngSec", label="方位角秒", comment="方位角秒（前视）", isQuery=false),
		@Column(name="azimuth_ang_radian", attrName="azimuthAngRadian", label="方位角弧度", comment="方位角弧度（前视）", isQuery=false),
		@Column(name="incre_coord_x", attrName="increCoordX", label="坐标增量X", comment="坐标增量X（米）", isQuery=false),
		@Column(name="incre_coord_y", attrName="increCoordY", label="坐标增量Y", comment="坐标增量Y（米）", isQuery=false),
		@Column(name="cor_inc_coord_x", attrName="corIncCoordX", label="改正后的坐标增量X", comment="改正后的坐标增量X（米）", isQuery=false),
		@Column(name="cor_inc_coord_y", attrName="corIncCoordY", label="改正后的坐标增量Y", comment="改正后的坐标增量Y（米）", isQuery=false),
		@Column(name="final_coord_x", attrName="finalCoordX", label="坐标结果x", comment="坐标结果x（米）", isQuery=false),
		@Column(name="final_coord_y", attrName="finalCoordY", label="坐标结果y", comment="坐标结果y（米）", isQuery=false),
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
						@Column(name = "group_name", label = "小组名称", isQuery = false),
				})
		},

		orderBy="u6.id ASC, u5.id ASC, a.solution_date DESC, a.station_id ASC", extWhereKeys = "dsfOffice"
)
public class DataSolution extends DataEntity<DataSolution> {
	
	private static final long serialVersionUID = 1L;
	private String stationId;		// 测站id
	private String foreStnId;		// 前视站点id
	private String backStnId;		// 后视站点id
	private Double avgDistance;		// 平均平距（前视）
	private Integer avgValueDeg;		// 各测回平均角值度
	private Integer avgValueMin;		// 各测回平均角值分
	private Double avgValueSec;		// 各测回平均角值秒
	private Double turningAngRadian;		// 转折角弧度
	private Double corAngRadian;		// 改正后的转折角弧度
	private Integer azimuthAngDeg;		// 方位角度（前视）
	private Integer azimuthAngMin;		// 方位角分（前视）
	private Double azimuthAngSec;		// 方位角秒（前视）
	private Double azimuthAngRadian;		// 方位角弧度（前视）
	private Double increCoordX;		// 坐标增量X
	private Double increCoordY;		// 坐标增量Y
	private Double corIncCoordX;		// 改正后的坐标增量X
	private Double corIncCoordY;		// 改正后的坐标增量Y
	private Double finalCoordX;		// 坐标结果x
	private Double finalCoordY;		// 坐标结果y
	private Date solutionDate;		// 解算时间

	//2022年3月15日 su添加
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
	// 导线测量等级
	private String traverseClass;
	
	public DataSolution() {
		this(null);
	}

	public DataSolution(String id){
		super(id);
	}

	// 2022年5月3日 su添加
	// 配置所需要输出到Excel的字段
	@Valid
	@ExcelFields({
			@ExcelField(title = "后视站点", attrName = "backStn.stationName", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 3),
			@ExcelField(title = "测站", attrName = "station.stationName", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 4),
			@ExcelField(title = "前视站点", attrName = "foreStn.stationName", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 5),
			@ExcelField(title = "平均平距(m)(前视)", attrName = "avgDistance", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 6, dataFormat = "0.0000"),
			@ExcelField(title = "各测回平均角值-度", attrName = "azimuthAngDeg", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 7),
			@ExcelField(title = "各测回平均角值-分", attrName = "avgValueMin", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 8),
			@ExcelField(title = "各测回平均角值-秒", attrName = "avgValueSec", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 9, dataFormat = "0.0"),
			@ExcelField(title = "方位角(前视)-度", attrName = "azimuthAngDeg", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 10),
			@ExcelField(title = "方位角(前视)-分", attrName = "azimuthAngMin", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 11),
			@ExcelField(title = "方位角(前视)-秒", attrName = "azimuthAngSec", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 12, dataFormat = "0.0"),
			@ExcelField(title = "结果坐标(m)X", attrName = "finalCoordX", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 13, dataFormat = "0.0000"),
			@ExcelField(title = "结果坐标(m)Y", attrName = "finalCoordY", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 14, dataFormat = "0.0000"),
			@ExcelField(title = "解算时间", attrName = "solutionDate", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 80),
			@ExcelField(title = "备注信息", attrName = "remarks", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 90),
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
	
	public Double getAvgDistance() {
		return avgDistance;
	}

	public void setAvgDistance(Double avgDistance) {
		this.avgDistance = avgDistance;
	}
	
	public Integer getAvgValueDeg() {
		return avgValueDeg;
	}

	public void setAvgValueDeg(Integer avgValueDeg) {
		this.avgValueDeg = avgValueDeg;
	}
	
	public Integer getAvgValueMin() {
		return avgValueMin;
	}

	public void setAvgValueMin(Integer avgValueMin) {
		this.avgValueMin = avgValueMin;
	}
	
	public Double getAvgValueSec() {
		return avgValueSec;
	}

	public void setAvgValueSec(Double avgValueSec) {
		this.avgValueSec = avgValueSec;
	}
	
	public Double getTurningAngRadian() {
		return turningAngRadian;
	}

	public void setTurningAngRadian(Double turningAngRadian) {
		this.turningAngRadian = turningAngRadian;
	}
	
	public Double getCorAngRadian() {
		return corAngRadian;
	}

	public void setCorAngRadian(Double corAngRadian) {
		this.corAngRadian = corAngRadian;
	}
	
	public Integer getAzimuthAngDeg() {
		return azimuthAngDeg;
	}

	public void setAzimuthAngDeg(Integer azimuthAngDeg) {
		this.azimuthAngDeg = azimuthAngDeg;
	}
	
	public Integer getAzimuthAngMin() {
		return azimuthAngMin;
	}

	public void setAzimuthAngMin(Integer azimuthAngMin) {
		this.azimuthAngMin = azimuthAngMin;
	}
	
	public Double getAzimuthAngSec() {
		return azimuthAngSec;
	}

	public void setAzimuthAngSec(Double azimuthAngSec) {
		this.azimuthAngSec = azimuthAngSec;
	}
	
	public Double getAzimuthAngRadian() {
		return azimuthAngRadian;
	}

	public void setAzimuthAngRadian(Double azimuthAngRadian) {
		this.azimuthAngRadian = azimuthAngRadian;
	}
	
	public Double getIncreCoordX() {
		return increCoordX;
	}

	public void setIncreCoordX(Double increCoordX) {
		this.increCoordX = increCoordX;
	}
	
	public Double getIncreCoordY() {
		return increCoordY;
	}

	public void setIncreCoordY(Double increCoordY) {
		this.increCoordY = increCoordY;
	}
	
	public Double getCorIncCoordX() {
		return corIncCoordX;
	}

	public void setCorIncCoordX(Double corIncCoordX) {
		this.corIncCoordX = corIncCoordX;
	}
	
	public Double getCorIncCoordY() {
		return corIncCoordY;
	}

	public void setCorIncCoordY(Double corIncCoordY) {
		this.corIncCoordY = corIncCoordY;
	}
	
	public Double getFinalCoordX() {
		return finalCoordX;
	}

	public void setFinalCoordX(Double finalCoordX) {
		this.finalCoordX = finalCoordX;
	}
	
	public Double getFinalCoordY() {
		return finalCoordY;
	}

	public void setFinalCoordY(Double finalCoordY) {
		this.finalCoordY = finalCoordY;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getSolutionDate() {
		return solutionDate;
	}

	public void setSolutionDate(Date solutionDate) {
		this.solutionDate = solutionDate;
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

	public String getTraverseClass() {
		return traverseClass;
	}

	public void setTraverseClass(String traverseClass) {
		this.traverseClass = traverseClass;
	}
}