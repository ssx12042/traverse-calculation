/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.group.service;

import java.util.List;

import com.jeesite.common.entity.DataScope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.group.entity.Group;
import com.jeesite.modules.group.dao.GroupDao;

/**
 * 组管理Service
 * @author su
 * @version 2022-02-28
 */
@Service
@Transactional(readOnly=true)
public class GroupService extends CrudService<GroupDao, Group> {
	
	/**
	 * 获取单条数据
	 * @param group
	 * @return
	 */
	@Override
	public Group get(Group group) {
		return super.get(group);
	}
	
	/**
	 * 查询分页数据
	 * @param group 查询条件
	 * @return
	 */
	@Override
	public Page<Group> findPage(Group group) {
		return super.findPage(group);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param group
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(Group group) {
		super.save(group);
	}
	
	/**
	 * 更新状态
	 * @param group
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(Group group) {
		super.updateStatus(group);
	}
	
	/**
	 * 删除数据
	 * @param group
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(Group group) {
		super.delete(group);
	}

	/**
	 * 添加数据权限过滤条件
	 * 2022年3月8日 su添加
	 */
	@Override
	public void addDataScopeFilter(Group group){
		// 举例4：用户、员工（自己创建的）数据权限根据部门过滤，实体类@Table注解extWhereKeys="dsfOffice"
		group.getSqlMap().getDataScope().addFilter("dsfOffice", "Office",
				"u1.office_code", "a.create_by", DataScope.CTRL_PERMI_HAVE);
	}
	
}