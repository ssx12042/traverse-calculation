/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.origin.entity;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import com.jeesite.common.mybatis.annotation.JoinTable;
import com.jeesite.common.utils.excel.annotation.ExcelField;
import com.jeesite.common.utils.excel.annotation.ExcelFields;
import com.jeesite.modules.group.entity.Group;
import com.jeesite.modules.line.entity.Line;
import com.jeesite.modules.station.entity.Station;
import com.jeesite.modules.sys.entity.Employee;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotNull;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;

/**
 * 起始数据Entity
 * @author su
 * @version 2022-03-10
 */
@Table(name="origin_data", alias="a", columns={
		@Column(name="id", attrName="id", label="id", isPK=true),
		@Column(name="origin_station_id", attrName="originStationId", label="起始测站id", queryType=QueryType.LIKE),
		@Column(name="origin_azimuth_deg", attrName="originAzimuthDeg", label="起始方位角度", isQuery=false),
		@Column(name="origin_azimuth_min", attrName="originAzimuthMin", label="起始方位角分", isQuery=false),
		@Column(name="origin_azimuth_sec", attrName="originAzimuthSec", label="起始方位角秒", isQuery=false),
		@Column(name="origin_coord_x", attrName="originCoordX", label="起始坐标X", isQuery=false),
		@Column(name="origin_coord_y", attrName="originCoordY", label="起始坐标Y", isQuery=false),
		@Column(name="end_station_id", attrName="endStationId", label="终点测站id", queryType=QueryType.LIKE),
		@Column(name="end_azimuth_deg", attrName="endAzimuthDeg", label="终点方位角度", isQuery=false),
		@Column(name="end_azimuth_min", attrName="endAzimuthMin", label="终点方位角分", isQuery=false),
		@Column(name="end_azimuth_sec", attrName="endAzimuthSec", label="终点方位角秒", isQuery=false),
		@Column(name="end_coord_x", attrName="endCoordX", label="终点坐标X", isQuery=false),
		@Column(name="end_coord_y", attrName="endCoordY", label="终点坐标Y", isQuery=false),
		@Column(includeEntity=DataEntity.class),
	},

		//2022年3月10日 su添加
		joinTable = {
				@JoinTable(type = JoinTable.Type.LEFT_JOIN, entity = Employee.class, attrName = "employee", alias = "u1",
						on = "u1.emp_code = a.create_by", columns = {
						@Column(name = "emp_code", label = "用户编码", isPK = true),
						@Column(name = "office_code", label = "机构编码", isQuery = false),
						@Column(name = "office_name", label = "机构名称"),
				}),
				@JoinTable(type = JoinTable.Type.LEFT_JOIN, entity = Station.class, attrName = "originStation", alias = "u2",
						on = "u2.id = a.origin_station_id", columns = {
						@Column(name = "line_id", label = "导线名称", isQuery = false),
						@Column(name = "station_name", label = "起始测站名称", isQuery = false),
				}),
				@JoinTable(type = JoinTable.Type.LEFT_JOIN, entity = Station.class, attrName = "endStation", alias = "u3",
						on = "u3.id = a.end_station_id", columns = {
						@Column(name = "station_name", label = "终点测站名称", isQuery = false),
				}),
				@JoinTable(type = JoinTable.Type.LEFT_JOIN, entity = Line.class, attrName = "line", alias = "u4",
						on = "u4.id = u2.line_id", columns = {
						@Column(name = "id", label = "导线id", isPK = true),
						@Column(name = "group_id", label = "小组id", isQuery = false),
						@Column(name = "line_name", label = "导线名称", isQuery = false),
				}),
				@JoinTable(type = JoinTable.Type.LEFT_JOIN, entity = Group.class, attrName = "group", alias = "u5",
						on = "u5.id = u4.group_id", columns = {
						@Column(name = "id", label = "小组id", isPK = true),
						@Column(name = "group_name", label = "小组名称", isQuery = false)
				})
		},

		orderBy="a.update_date DESC", extWhereKeys = "dsfOffice"
)
public class OriginData extends DataEntity<OriginData> {
	
	private static final long serialVersionUID = 1L;
	private String originStationId;		// 起始测站id
	private Integer originAzimuthDeg;		// 起始方位角度
	private Integer originAzimuthMin;		// 起始方位角分
	private Double originAzimuthSec;		// 起始方位角秒
	private Double originCoordX;		// 起始坐标X
	private Double originCoordY;		// 起始坐标Y
	private String endStationId;		// 终点测站id
	private Integer endAzimuthDeg;		// 终点方位角度
	private Integer endAzimuthMin;		// 终点方位角分
	private Double endAzimuthSec;		// 终点方位角秒
	private Double endCoordX;		// 终点坐标X
	private Double endCoordY;		// 终点坐标Y

	//2022年3月11日 su添加
	//起始测站
	private Station originStation;
	//终点测站
	private Station endStation;
	//导线
	private Line line;
	//小组
	private Group group;
	
	public OriginData() {
		this(null);
	}

	public OriginData(String id){
		super(id);
	}

	// 2022年5月3日 su添加
	// 配置所需要输出到Excel的字段
	@Valid
	@ExcelFields({
			@ExcelField(title = "起始测站", attrName = "originStation.stationName", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 3),
			@ExcelField(title = "起始方位角-度", attrName = "originAzimuthDeg", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 4),
			@ExcelField(title = "起始方位角-分", attrName = "originAzimuthMin", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 5),
			@ExcelField(title = "起始方位角-秒", attrName = "originAzimuthSec", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 6, dataFormat = "0.0"),
			@ExcelField(title = "起始坐标X", attrName = "originCoordX", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 7, dataFormat = "0.0000"),
			@ExcelField(title = "起始坐标Y", attrName = "originCoordY", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 8, dataFormat = "0.0000"),
			@ExcelField(title = "终点测站", attrName = "endStation.stationName", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 9),
			@ExcelField(title = "终点方位角-度", attrName = "endAzimuthDeg", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 10),
			@ExcelField(title = "终点方位角-分", attrName = "endAzimuthMin", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 11),
			@ExcelField(title = "终点方位角-秒", attrName = "endAzimuthSec", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 12, dataFormat = "0.0"),
			@ExcelField(title = "终点坐标(m)X", attrName = "endCoordX", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 13, dataFormat = "0.0000"),
			@ExcelField(title = "终点坐标(m)Y", attrName = "endCoordY", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 14, dataFormat = "0.0000"),
			@ExcelField(title = "更新时间", attrName = "updateDate", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 80),
			@ExcelField(title = "备注信息", attrName = "remarks", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 90),
	})
	
	@NotBlank(message="起始测站id不能为空")
	@Length(min=0, max=64, message="起始测站id长度不能超过 64 个字符")
	public String getOriginStationId() {
		return originStationId;
	}

	public void setOriginStationId(String originStationId) {
		this.originStationId = originStationId;
	}
	
	@NotNull(message="起始方位角度不能为空")
	public Integer getOriginAzimuthDeg() {
		return originAzimuthDeg;
	}

	public void setOriginAzimuthDeg(Integer originAzimuthDeg) {
		this.originAzimuthDeg = originAzimuthDeg;
	}
	
	@NotNull(message="起始方位角分不能为空")
	public Integer getOriginAzimuthMin() {
		return originAzimuthMin;
	}

	public void setOriginAzimuthMin(Integer originAzimuthMin) {
		this.originAzimuthMin = originAzimuthMin;
	}
	
	@NotNull(message="起始方位角秒不能为空")
	public Double getOriginAzimuthSec() {
		return originAzimuthSec;
	}

	public void setOriginAzimuthSec(Double originAzimuthSec) {
		this.originAzimuthSec = originAzimuthSec;
	}
	
	@NotNull(message="起始坐标X不能为空")
	public Double getOriginCoordX() {
		return originCoordX;
	}

	public void setOriginCoordX(Double originCoordX) {
		this.originCoordX = originCoordX;
	}
	
	@NotNull(message="起始坐标Y不能为空")
	public Double getOriginCoordY() {
		return originCoordY;
	}

	public void setOriginCoordY(Double originCoordY) {
		this.originCoordY = originCoordY;
	}
	
	@NotBlank(message="终点测站id不能为空")
	@Length(min=0, max=64, message="终点测站id长度不能超过 64 个字符")
	public String getEndStationId() {
		return endStationId;
	}

	public void setEndStationId(String endStationId) {
		this.endStationId = endStationId;
	}
	
	@NotNull(message="终点方位角度不能为空")
	public Integer getEndAzimuthDeg() {
		return endAzimuthDeg;
	}

	public void setEndAzimuthDeg(Integer endAzimuthDeg) {
		this.endAzimuthDeg = endAzimuthDeg;
	}
	
	@NotNull(message="终点方位角分不能为空")
	public Integer getEndAzimuthMin() {
		return endAzimuthMin;
	}

	public void setEndAzimuthMin(Integer endAzimuthMin) {
		this.endAzimuthMin = endAzimuthMin;
	}
	
	@NotNull(message="终点方位角秒不能为空")
	public Double getEndAzimuthSec() {
		return endAzimuthSec;
	}

	public void setEndAzimuthSec(Double endAzimuthSec) {
		this.endAzimuthSec = endAzimuthSec;
	}
	
	@NotNull(message="终点坐标X不能为空")
	public Double getEndCoordX() {
		return endCoordX;
	}

	public void setEndCoordX(Double endCoordX) {
		this.endCoordX = endCoordX;
	}
	
	@NotNull(message="终点坐标Y不能为空")
	public Double getEndCoordY() {
		return endCoordY;
	}

	public void setEndCoordY(Double endCoordY) {
		this.endCoordY = endCoordY;
	}

	public Station getOriginStation() {
		return originStation;
	}

	public void setOriginStation(Station originStation) {
		this.originStation = originStation;
	}

	public Station getEndStation() {
		return endStation;
	}

	public void setEndStation(Station endStation) {
		this.endStation = endStation;
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