/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.elevation.data.entity;

import com.jeesite.common.mybatis.annotation.JoinTable;
import com.jeesite.elevation.el_line.entity.ELLine;
import com.jeesite.modules.group.entity.Group;
import com.jeesite.modules.line.entity.Line;
import com.jeesite.modules.sys.entity.Employee;
import org.hibernate.validator.constraints.Length;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;

/**
 * dataEntity
 * @author hu
 * @version 2022-06-17
 */
@Table(name="data", alias="a", columns={
		@Column(name="id", attrName="id", label="编号", isPK=true),
		@Column(name="group_id", attrName="groupId", label="小组id"),
		@Column(name="line_id", attrName="lineId", label="导线id"),
		@Column(name="sj", attrName="sj", label="上传数据"),
		@Column(includeEntity=DataEntity.class),
	}, joinTable = {
		@JoinTable(type = JoinTable.Type.LEFT_JOIN, entity = Employee.class, attrName = "employee", alias = "u1",
				on = "u1.emp_code = a.create_by", columns = {
				@Column(name = "emp_code", label = "用户编码", isPK = true),
				@Column(name = "office_code", label = "机构编码", isQuery = false),
				@Column(name = "office_name", label = "机构名称"),
		}),
		@JoinTable(type = JoinTable.Type.LEFT_JOIN, entity = ELLine.class, attrName = "line", alias = "u2",
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
}, orderBy = "u3.id ASC, u2.id ASC, a.id ASC", extWhereKeys = "dsfOffice"
)
public class Data extends DataEntity<Data> {
	
	private static final long serialVersionUID = 1L;
	private String groupId;		// 小组id
	private String lineId;		// 导线id
	private String sj;		// 上传数据
	//小组
	private Group group;
	//导线
	private ELLine line;
	public Data() {
		this(null);
	}

	public Data(String id){
		super(id);
	}
	
	@Length(min=0, max=64, message="小组id长度不能超过 64 个字符")
	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	
	@Length(min=0, max=20, message="导线id长度不能超过 20 个字符")
	public String getLineId() {
		return lineId;
	}

	public void setLineId(String lineId) {
		this.lineId = lineId;
	}
	
	@Length(min=0, max=255, message="上传数据长度不能超过 255 个字符")
	public String getSj() {
		return sj;
	}

	public void setSj(String sj) {
		this.sj = sj;
	}
	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	public ELLine getLine() {
		return line;
	}

	public void setLine(ELLine line) {
		this.line = line;
	}
}