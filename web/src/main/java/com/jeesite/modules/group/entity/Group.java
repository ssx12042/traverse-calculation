/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.group.entity;

import javax.validation.constraints.NotBlank;

import com.jeesite.common.mybatis.annotation.JoinTable;
import com.jeesite.modules.sys.entity.Employee;
import org.hibernate.validator.constraints.Length;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;

/**
 * 组管理Entity
 * @author su
 * @version 2022-02-28
 */
@Table(name = "group_management", alias = "a", columns = {
		@Column(name = "id", attrName = "id", label = "id", isPK = true),
		@Column(name = "group_name", attrName = "groupName", label = "小组名称", queryType = QueryType.LIKE),
		@Column(name = "group_leader", attrName = "groupLeader", label = "负责人", queryType = QueryType.LIKE),
		@Column(includeEntity = DataEntity.class),
},
		//2022年3月8日 su添加
		joinTable = {
				@JoinTable(type = JoinTable.Type.LEFT_JOIN, entity = Employee.class, attrName = "employee", alias = "u1",
						on = "u1.emp_code = a.create_by", columns = {
						@Column(name = "emp_code", label = "用户编码", isPK = true),
						@Column(name = "office_code", label = "机构编码", isQuery = false),
						@Column(name = "office_name", label = "机构名称"),
				})
		},

		orderBy = "a.id ASC", extWhereKeys = "dsfOffice"
)
public class Group extends DataEntity<Group> {
	
	private static final long serialVersionUID = 1L;
	private String groupName;		// 小组名称
	private String groupLeader;		// 负责人
	
	public Group() {
		this(null);
	}

	public Group(String id){
		super(id);
	}
	
	@NotBlank(message="小组名称不能为空")
	@Length(min=0, max=20, message="小组名称长度不能超过 20 个字符")
	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	
	@NotBlank(message="负责人不能为空")
	@Length(min=0, max=20, message="负责人长度不能超过 20 个字符")
	public String getGroupLeader() {
		return groupLeader;
	}

	public void setGroupLeader(String groupLeader) {
		this.groupLeader = groupLeader;
	}
	
}