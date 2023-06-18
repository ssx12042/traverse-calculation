/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.elevation.el_origin_data.service;

import java.util.List;

import com.jeesite.common.entity.DataScope;
import com.jeesite.modules.origin.entity.OriginData;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.elevation.el_origin_data.entity.ElOriginData;
import com.jeesite.elevation.el_origin_data.dao.ElOriginDataDao;

/**
 * 高程——起始数据Service
 * @author su
 * @version 2022-05-30
 */
@Service
@Transactional(readOnly=true)
public class ElOriginDataService extends CrudService<ElOriginDataDao, ElOriginData> {
	
	/**
	 * 获取单条数据
	 * @param elOriginData
	 * @return
	 */
	@Override
	public ElOriginData get(ElOriginData elOriginData) {
		return super.get(elOriginData);
	}
	
	/**
	 * 查询分页数据
	 * @param elOriginData 查询条件
	 * @return
	 */
	@Override
	public Page<ElOriginData> findPage(ElOriginData elOriginData) {
		return super.findPage(elOriginData);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param elOriginData
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(ElOriginData elOriginData) {
		super.save(elOriginData);
	}
	
	/**
	 * 更新状态
	 * @param elOriginData
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(ElOriginData elOriginData) {
		super.updateStatus(elOriginData);
	}
	
	/**
	 * 删除数据
	 * @param elOriginData
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(ElOriginData elOriginData) {
		super.delete(elOriginData);
	}

	/**
	 * 添加数据权限过滤条件
	 * 2022年5月30日 su添加
	 */
	@Override
	public void addDataScopeFilter(ElOriginData elOriginData) {
		// 举例4：用户、员工（自己创建的）数据权限根据部门过滤，实体类@Table注解extWhereKeys="dsfOffice"
		elOriginData.getSqlMap().getDataScope().addFilter("dsfOffice", "Office",
				"u1.office_code", "a.create_by", DataScope.CTRL_PERMI_HAVE);
	}
	
}