/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.elevation.el_origin_data.entity;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import com.jeesite.common.mybatis.annotation.JoinTable;
import com.jeesite.common.utils.excel.annotation.ExcelField;
import com.jeesite.common.utils.excel.annotation.ExcelFields;
import com.jeesite.elevation.el_line.entity.ELLine;
import com.jeesite.elevation.el_station.entity.ELStation;
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
 * 高程——起始数据Entity
 * @author su
 * @version 2022-05-30
 */
@Table(name="el_origin_data", alias="a", columns={
		@Column(name="id", attrName="id", label="id", isPK=true),
		@Column(name="origin_station_id", attrName="originStationId", label="起始测站id"),
		@Column(name="origin_elevation", attrName="originElevation", label="起始高程", comment="起始高程(m)", isQuery=false),
		@Column(name="end_station_id", attrName="endStationId", label="终点测站id"),
		@Column(name="end_elevation", attrName="endElevation", label="终点高程", comment="终点高程(m)", isQuery=false),
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
				@JoinTable(type = JoinTable.Type.LEFT_JOIN, entity = ELStation.class, attrName = "originStation", alias = "u2",
						on = "u2.id = a.origin_station_id", columns = {
						@Column(name = "line_id", label = "导线名称", isQuery = false),
						@Column(name = "station_name", label = "起始测站名称", isQuery = false),
				}),
				@JoinTable(type = JoinTable.Type.LEFT_JOIN, entity = ELStation.class, attrName = "endStation", alias = "u3",
						on = "u3.id = a.end_station_id", columns = {
						@Column(name = "station_name", label = "终点测站名称", isQuery = false),
				}),
				@JoinTable(type = JoinTable.Type.LEFT_JOIN, entity = ELLine.class, attrName = "line", alias = "u4",
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
public class ElOriginData extends DataEntity<ElOriginData> {
	
	private static final long serialVersionUID = 1L;
	private String originStationId;		// 起始测站id
	private Double originElevation;		// 起始高程(m)
	private String endStationId;		// 终点测站id
	private Double endElevation;		// 终点高程(m)

	//2022年5月30日 su添加
	//起始测站
	private ELStation originStation;
	//终点测站
	private ELStation endStation;
	//导线
	private ELLine line;
	//小组
	private Group group;
	
	public ElOriginData() {
		this(null);
	}

	public ElOriginData(String id){
		super(id);
	}

	// 2022年7月7日 su添加
	// 配置所需要输出到Excel的字段
	@Valid
	@ExcelFields({
			@ExcelField(title = "起始测站", attrName = "originStation.stationName", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 10),
			@ExcelField(title = "起始高程(m)", attrName = "originElevation", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 20, dataFormat = "0.0000"),
			@ExcelField(title = "终点测站", attrName = "endStation.stationName", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 30),
			@ExcelField(title = "终点高程(m)", attrName = "endElevation", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 40, dataFormat = "0.0000"),
			@ExcelField(title = "更新时间", attrName = "updateDate", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 50),
			@ExcelField(title = "备注信息", attrName = "remarks", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 60),
	})
	
	@NotBlank(message="起始测站id不能为空")
	@Length(min=0, max=64, message="起始测站id长度不能超过 64 个字符")
	public String getOriginStationId() {
		return originStationId;
	}

	public void setOriginStationId(String originStationId) {
		this.originStationId = originStationId;
	}
	
	@NotNull(message="起始高程不能为空")
	public Double getOriginElevation() {
		return originElevation;
	}

	public void setOriginElevation(Double originElevation) {
		this.originElevation = originElevation;
	}
	
	@NotBlank(message="终点测站id不能为空")
	@Length(min=0, max=64, message="终点测站id长度不能超过 64 个字符")
	public String getEndStationId() {
		return endStationId;
	}

	public void setEndStationId(String endStationId) {
		this.endStationId = endStationId;
	}
	
	@NotNull(message="终点高程不能为空")
	public Double getEndElevation() {
		return endElevation;
	}

	public void setEndElevation(Double endElevation) {
		this.endElevation = endElevation;
	}

	public ELStation getOriginStation() {
		return originStation;
	}

	public void setOriginStation(ELStation originStation) {
		this.originStation = originStation;
	}

	public ELStation getEndStation() {
		return endStation;
	}

	public void setEndStation(ELStation endStation) {
		this.endStation = endStation;
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