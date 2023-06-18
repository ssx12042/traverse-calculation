/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.wrong.entity;

import javax.validation.constraints.NotBlank;

import com.jeesite.common.mybatis.annotation.JoinTable;
import com.jeesite.modules.group.entity.Group;
import com.jeesite.modules.line.entity.Line;
import com.jeesite.modules.sys.entity.Employee;
import org.hibernate.validator.constraints.Length;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;

/**
 * 错误边角查询Entity
 * @author hu
 * @version 2022-05-03
 */
@Table(name="wrong_result", alias="a", columns={
		@Column(name="id", attrName="id", label="id", isPK=true),
		@Column(name="group_id", attrName="groupId", label="小组"),
		@Column(name="line_id", attrName="lineId", label="导线"),
		@Column(name="create_by", attrName="createBy", label="创建者", isQuery=false),
		@Column(name="wrong", attrName="wrong", label="错误位置", isQuery=false),
	}, 	joinTable = {
		@JoinTable(type = JoinTable.Type.LEFT_JOIN, entity = Employee.class, attrName = "employee", alias = "u1",
				on = "u1.emp_code = a.create_by", columns = {
				@Column(name = "emp_code", label = "用户编码", isPK = true),
				@Column(name = "office_code", label = "机构编码", isQuery = false),
				@Column(name = "office_name", label = "机构名称"),
		}),
		@JoinTable(type = JoinTable.Type.LEFT_JOIN, entity = Line.class, attrName = "line", alias = "u2",
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
},

		orderBy = "u3.id ASC, u2.id ASC, a.id ASC", extWhereKeys = "dsfOffice"
)
public class WrongResult extends DataEntity<WrongResult> {
	
	private static final long serialVersionUID = 1L;
	private String groupId;		// 小组
	private String lineId;		// 导线
	private String wrong;		// 错误位置
	//小组
	private Group group;
	//导线
	private Line line;
	public WrongResult() {
		this(null);
	}

	public WrongResult(String id){
		super(id);
	}
	
	@NotBlank(message="小组不能为空")
	@Length(min=0, max=64, message="小组长度不能超过 64 个字符")
	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	
	@NotBlank(message="导线不能为空")
	@Length(min=0, max=20, message="导线长度不能超过 20 个字符")
	public String getLineId() {
		return lineId;
	}

	public void setLineId(String lineId) {
		this.lineId = lineId;
	}
	
	@Length(min=0, max=64, message="错误位置长度不能超过 64 个字符")
	public String getWrong() {
		return wrong;
	}

	public void setWrong(String wrong) {
		this.wrong = wrong;
	}

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	public Line getLine() {
		return line;
	}

	public void setLine(Line line) {
		this.line = line;
	}
}