/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.line.entity;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import com.jeesite.common.mybatis.annotation.JoinTable;
import com.jeesite.common.utils.excel.annotation.ExcelField;
import com.jeesite.common.utils.excel.annotation.ExcelField.Align;
import com.jeesite.common.utils.excel.annotation.ExcelFields;
import com.jeesite.modules.group.entity.Group;
import com.jeesite.modules.sys.entity.Employee;
import org.hibernate.validator.constraints.Length;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;

/**
 * 导线Entity
 * @author su
 * @version 2022-02-28
 */
@Table(name = "line", alias = "a", columns = {
		@Column(name = "id", attrName = "id", label = "id", isPK = true),
		@Column(name = "group_id", attrName = "groupId", label = "小组id"),
		@Column(name = "line_name", attrName = "lineName", label = "导线名称", queryType = QueryType.LIKE),
		@Column(includeEntity = DataEntity.class),
},

		//2022年3月8日 su添加
		joinTable = {
				@JoinTable(type = JoinTable.Type.LEFT_JOIN, entity = Employee.class, attrName = "employee", alias = "u1",
						on = "u1.emp_code = a.create_by", columns = {
						@Column(name = "emp_code", label = "用户编码", isPK = true),
						@Column(name = "office_code", label = "机构编码", isQuery = false),
						@Column(name = "office_name", label = "机构名称"),
				}),
				@JoinTable(type = JoinTable.Type.LEFT_JOIN, entity = Group.class, attrName = "group", alias = "u2",
						on = "u2.id = a.group_id", columns = {
						@Column(name = "id", label = "小组id", isPK = true),
						@Column(name = "group_name", label = "小组名称", isQuery = false)
				})
		},

		orderBy = "a.group_id ASC, a.id ASC", extWhereKeys = "dsfOffice"
)
public class Line extends DataEntity<Line> {
	
	private static final long serialVersionUID = 1L;
	private String groupId;		// 小组id
	private String lineName;		// 导线名称

	//小组 2022年3月8日 su添加
	private Group group;
	
	public Line() {
		this(null);
	}

	public Line(String id){
		super(id);
	}
	
	@NotBlank(message="小组id不能为空")
	@Length(min=0, max=64, message="小组id长度不能超过 64 个字符")
	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	
	@NotBlank(message="导线名称不能为空")
	@Length(min=0, max=20, message="导线名称长度不能超过 20 个字符")
	public String getLineName() {
		return lineName;
	}

	public void setLineName(String lineName) {
		this.lineName = lineName;
	}

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}
}