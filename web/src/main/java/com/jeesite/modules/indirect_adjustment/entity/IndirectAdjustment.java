/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.indirect_adjustment.entity;

import com.jeesite.common.utils.excel.annotation.ExcelField;
import com.jeesite.common.utils.excel.annotation.ExcelFields;
import com.jeesite.modules.group.entity.Group;
import com.jeesite.modules.line.entity.Line;
import com.jeesite.modules.result_evaluation_strict.entity.ResultEvaluationStrict;
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
 * 间接平差Entity
 * @author su
 * @version 2022-04-06
 */
@Table(name="indirect_adjustment", alias="a", columns={
		@Column(name="id", attrName="id", label="id", isPK=true),
		@Column(name="station_id", attrName="stationId", label="测站id"),
		@Column(name="fore_stn_id", attrName="foreStnId", label="前视站点id"),
		@Column(name="back_stn_id", attrName="backStnId", label="后视站点id"),
		@Column(name="distance", attrName="distance", label="平距", comment="平距（前视）（米）", isQuery=false),
		@Column(name="strict_distance", attrName="strictDistance", label="严密平距", comment="严密平距（前视）（米）", isQuery=false),
		@Column(name="ang_deg", attrName="angDeg", label="转折角度", isQuery=false),
		@Column(name="ang_min", attrName="angMin", label="转折角分", isQuery=false),
		@Column(name="ang_sec", attrName="angSec", label="转折角秒", isQuery=false),
		@Column(name="strict_ang_deg", attrName="strictAngDeg", label="严密转折角度", isQuery=false),
		@Column(name="strict_ang_min", attrName="strictAngMin", label="严密转折角分", isQuery=false),
		@Column(name="strict_ang_sec", attrName="strictAngSec", label="严密转折角秒", isQuery=false),
		@Column(name="final_coord_x", attrName="finalCoordX", label="坐标结果x", comment="坐标结果x（米）", isQuery=false),
		@Column(name="final_coord_y", attrName="finalCoordY", label="坐标结果y", comment="坐标结果y（米）", isQuery=false),
		@Column(name="strict_coord_x", attrName="strictCoordX", label="严密坐标x", comment="严密坐标x（米）", isQuery=false),
		@Column(name="strict_coord_y", attrName="strictCoordY", label="严密坐标y", comment="严密坐标y（米）", isQuery=false),
		@Column(name="medium_error_x", attrName="mediumErrorX", label="待定点坐标平差值的中误差x", comment="待定点坐标平差值的中误差x（毫米）", isQuery=false),
		@Column(name="medium_error_y", attrName="mediumErrorY", label="待定点坐标平差值的中误差y", comment="待定点坐标平差值的中误差y（毫米）", isQuery=false),
		@Column(name="solution_date", attrName="solutionDate", label="解算时间", queryType=QueryType.LIKE),
		@Column(includeEntity=DataEntity.class),
	},

		//2022年3月22日 su添加
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
				}),
				@JoinTable(type = JoinTable.Type.LEFT_JOIN, entity = ResultEvaluationStrict.class, attrName = "resEvaStrict", alias = "u7",
						on = "u7.line_id = u5.id and u7.solution_date = a.solution_date", columns = {
						@Column(name = "mediumError_unitWeight", label = "单位权中误差", isQuery = false),
						@Column(name = "traverse_class", label = "导线测量等级", isQuery = false),
						@Column(name = "result_evaluation", label = "结果评价", isQuery = false),
				})
		},

		orderBy="u6.id ASC, u5.id ASC, a.solution_date DESC, a.station_id ASC", extWhereKeys = "dsfOffice"
)
public class IndirectAdjustment extends DataEntity<IndirectAdjustment> {
	
	private static final long serialVersionUID = 1L;
	private String stationId;		// 测站id
	private String foreStnId;		// 前视站点id
	private String backStnId;		// 后视站点id
	private Double distance;		// 平距（前视）（米）
	private Double strictDistance;		// 严密平距（前视）（米）
	private Integer angDeg;		// 转折角度
	private Integer angMin;		// 转折角分
	private Double angSec;		// 转折角秒
	private Integer strictAngDeg;		// 严密转折角度
	private Integer strictAngMin;		// 严密转折角分
	private Double strictAngSec;		// 严密转折角秒
	private Double finalCoordX;		// 坐标结果x
	private Double finalCoordY;		// 坐标结果y
	private Double strictCoordX;		// 严密坐标x
	private Double strictCoordY;		// 严密坐标y
	private Double mediumErrorX;		// 待定点坐标平差值的中误差x
	private Double mediumErrorY;		// 待定点坐标平差值的中误差y
	private Date solutionDate;		// 解算时间

	//2022年3月22日 su添加
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
	// 结果评价
	private ResultEvaluationStrict resEvaStrict;
	
	public IndirectAdjustment() {
		this(null);
	}

	public IndirectAdjustment(String id){
		super(id);
	}

	// 2022年6月22日 su添加
	// 配置所需要输出到Excel的字段
	@Valid
	@ExcelFields({
			@ExcelField(title = "后视站点", attrName = "backStn.stationName", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 10),
			@ExcelField(title = "测站", attrName = "station.stationName", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 20),
			@ExcelField(title = "前视站点", attrName = "foreStn.stationName", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 30),
			@ExcelField(title = "前视平距(m)-简易", attrName = "distance", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 40, dataFormat = "0.0000"),
			@ExcelField(title = "前视平距(m)-严密", attrName = "strictDistance", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 41, dataFormat = "0.0000"),
			@ExcelField(title = "简易-转折角-度", attrName = "angDeg", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 50),
			@ExcelField(title = "简易-转折角-分", attrName = "angMin", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 60),
			@ExcelField(title = "简易-转折角-秒", attrName = "angSec", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 70, dataFormat = "0.0"),
			@ExcelField(title = "严密-转折角-度", attrName = "strictAngDeg", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 71),
			@ExcelField(title = "严密-转折角-分", attrName = "strictAngMin", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 72),
			@ExcelField(title = "严密-转折角-秒", attrName = "strictAngSec", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 73, dataFormat = "0.0"),
			@ExcelField(title = "简易-坐标(m)-X", attrName = "finalCoordX", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 80, dataFormat = "0.0000"),
			@ExcelField(title = "简易-坐标(m)-Y", attrName = "finalCoordY", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 90, dataFormat = "0.0000"),
			@ExcelField(title = "严密-坐标(m)-X", attrName = "strictCoordX", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 91, dataFormat = "0.0000"),
			@ExcelField(title = "严密-坐标(m)-Y", attrName = "strictCoordY", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 92, dataFormat = "0.0000"),
			@ExcelField(title = "点位中误差(mm)-X", attrName = "mediumErrorX", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 100, dataFormat = "0.0"),
			@ExcelField(title = "点位中误差(mm)-Y", attrName = "mediumErrorY", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 110, dataFormat = "0.0000"),
			@ExcelField(title = "单位权中误差(mm)", attrName = "resEvaStrict.mediumerrorUnitweight", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 120, dataFormat = "0.0000"),
			@ExcelField(title = "结果评价", attrName = "resEvaStrict.resultEvaluation", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 121, dataFormat = "0.0000"),
			@ExcelField(title = "解算时间", attrName = "solutionDate", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 130),
			@ExcelField(title = "备注信息", attrName = "remarks", width = 6 * 256, align = ExcelField.Align.CENTER, sort = 140),
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
	
	public Double getDistance() {
		return distance;
	}

	public void setDistance(Double distance) {
		this.distance = distance;
	}
	
	public Double getStrictDistance() {
		return strictDistance;
	}

	public void setStrictDistance(Double strictDistance) {
		this.strictDistance = strictDistance;
	}
	
	public Integer getAngDeg() {
		return angDeg;
	}

	public void setAngDeg(Integer angDeg) {
		this.angDeg = angDeg;
	}
	
	public Integer getAngMin() {
		return angMin;
	}

	public void setAngMin(Integer angMin) {
		this.angMin = angMin;
	}
	
	public Double getAngSec() {
		return angSec;
	}

	public void setAngSec(Double angSec) {
		this.angSec = angSec;
	}
	
	public Integer getStrictAngDeg() {
		return strictAngDeg;
	}

	public void setStrictAngDeg(Integer strictAngDeg) {
		this.strictAngDeg = strictAngDeg;
	}
	
	public Integer getStrictAngMin() {
		return strictAngMin;
	}

	public void setStrictAngMin(Integer strictAngMin) {
		this.strictAngMin = strictAngMin;
	}
	
	public Double getStrictAngSec() {
		return strictAngSec;
	}

	public void setStrictAngSec(Double strictAngSec) {
		this.strictAngSec = strictAngSec;
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
	
	public Double getStrictCoordX() {
		return strictCoordX;
	}

	public void setStrictCoordX(Double strictCoordX) {
		this.strictCoordX = strictCoordX;
	}
	
	public Double getStrictCoordY() {
		return strictCoordY;
	}

	public void setStrictCoordY(Double strictCoordY) {
		this.strictCoordY = strictCoordY;
	}
	
	public Double getMediumErrorX() {
		return mediumErrorX;
	}

	public void setMediumErrorX(Double mediumErrorX) {
		this.mediumErrorX = mediumErrorX;
	}
	
	public Double getMediumErrorY() {
		return mediumErrorY;
	}

	public void setMediumErrorY(Double mediumErrorY) {
		this.mediumErrorY = mediumErrorY;
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

	public ResultEvaluationStrict getResEvaStrict() {
		return resEvaStrict;
	}

	public void setResEvaStrict(ResultEvaluationStrict resEvaStrict) {
		this.resEvaStrict = resEvaStrict;
	}
}