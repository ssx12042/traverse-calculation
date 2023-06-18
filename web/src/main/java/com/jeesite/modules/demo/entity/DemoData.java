/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.demo.entity;

import com.jeesite.common.mybatis.annotation.JoinTable;
import com.jeesite.modules.sys.entity.*;
import com.jeesite.common.entity.BaseEntity;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;

/**
 * 数据权限演示Entity
 * @author su
 * @version 2022-02-22
 */
@Table(name = "demo_data", alias = "a", columns = {
		@Column(name = "id", attrName = "id", label = "编号", isPK = true),
		@Column(name = "demo_user_code", attrName = "demoUserCode.userCode", label = "用户选择"),
		@Column(name = "demo_office_code", attrName = "demoOfficeCode.officeCode", label = "机构选择"),
		@Column(name = "demo_area_code", attrName = "demoAreaCode.areaCode", label = "区域选择"),
		@Column(name = "demo_company_code", attrName = "demoCompanyCode.companyCode", label = "企业选择"),
		@Column(includeEntity = DataEntity.class),
		@Column(includeEntity = BaseEntity.class),
}, joinTable = {
		@JoinTable(type = JoinTable.Type.LEFT_JOIN, entity = UserRole.class, attrName = "demoUserRoleCode", alias = "u1",
				on = "u1.user_code = a.create_by", columns = {
				@Column(name = "user_code", label = "用户编码", isPK = true),
				@Column(name = "role_code", label = "角色编码", isPK = true),
		}),
		@JoinTable(type = JoinTable.Type.LEFT_JOIN, entity = Role.class, attrName = "demoRoleCode", alias = "r1",
				on = "r1.role_code = u1.role_code", columns = {
				@Column(name = "role_code", label = "角色编码", isPK = true),
				@Column(name = "role_name", label = "角色名称", isQuery = false),
		}),
		@JoinTable(type = JoinTable.Type.LEFT_JOIN, entity = User.class, attrName = "demoUserCode", alias = "u2",
				on = "u2.user_code = a.demo_user_code", columns = {
				@Column(name = "user_code", label = "用户编码", isPK = true),
				@Column(name = "user_name", label = "用户名称", isQuery = false),
		}),
		@JoinTable(type = JoinTable.Type.LEFT_JOIN, entity = Office.class, attrName = "demoOfficeCode", alias = "u3",
				on = "u3.office_code = a.demo_office_code", columns = {
				@Column(name = "office_code", label = "机构编码", isPK = true),
				@Column(name = "office_name", label = "机构名称", isQuery = false),
		}),
		@JoinTable(type = JoinTable.Type.LEFT_JOIN, entity = Area.class, attrName = "demoAreaCode", alias = "u4",
				on = "u4.area_code = a.demo_area_code", columns = {
				@Column(name = "area_code", label = "机构编码", isPK = true),
				@Column(name = "area_name", label = "机构名称", isQuery = false),
		}),
		@JoinTable(type = JoinTable.Type.LEFT_JOIN, entity = Company.class, attrName = "demoCompanyCode", alias = "u5",
				on = "u5.company_code = a.demo_company_code", columns = {
				@Column(name = "company_code", label = "机构编码", isPK = true),
				@Column(name = "company_name", label = "机构名称", isQuery = false),
		}),
	}, orderBy = "a.update_date DESC", extWhereKeys = "dsfOffice"
)
public class DemoData extends DataEntity<DemoData> {
	
	private static final long serialVersionUID = 1L;
	private UserRole demoUserRoleCode;	// 用户角色
	private Role demoRoleCode;		// 角色
	private User demoUserCode;		// 用户选择
	private Office demoOfficeCode;		// 机构选择
	private Area demoAreaCode;		// 区域选择
	private Company demoCompanyCode;		// 企业选择
	
	public DemoData() {
		this(null);
	}

	public DemoData(String id){
		super(id);
	}

	public UserRole getDemoUserRoleCode() {
		return demoUserRoleCode;
	}

	public void setDemoUserRoleCode(UserRole demoUserRoleCode) {
		this.demoUserRoleCode = demoUserRoleCode;
	}

	public Role getDemoRoleCode() {
		return demoRoleCode;
	}

	public void setDemoRoleCode(Role demoRoleCode) {
		this.demoRoleCode = demoRoleCode;
	}

	public User getDemoUserCode() {
		return demoUserCode;
	}

	public void setDemoUserCode(User demoUserCode) {
		this.demoUserCode = demoUserCode;
	}
	
	public Office getDemoOfficeCode() {
		return demoOfficeCode;
	}

	public void setDemoOfficeCode(Office demoOfficeCode) {
		this.demoOfficeCode = demoOfficeCode;
	}
	
	public Area getDemoAreaCode() {
		return demoAreaCode;
	}

	public void setDemoAreaCode(Area demoAreaCode) {
		this.demoAreaCode = demoAreaCode;
	}
	
	public Company getDemoCompanyCode() {
		return demoCompanyCode;
	}

	public void setDemoCompanyCode(Company demoCompanyCode) {
		this.demoCompanyCode = demoCompanyCode;
	}
	
}