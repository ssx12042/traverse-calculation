/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.origin.service;

import com.jeesite.common.entity.DataScope;
import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.origin.dao.OriginDataDao;
import com.jeesite.modules.origin.entity.OriginData;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 起始数据Service
 * @author su
 * @version 2022-03-10
 */
@Service
@Transactional(readOnly=true)
public class OriginDataService extends CrudService<OriginDataDao, OriginData> {
	
	/**
	 * 获取单条数据
	 * @param originData
	 * @return
	 */
	@Override
	public OriginData get(OriginData originData) {
		return super.get(originData);
	}
	
	/**
	 * 查询分页数据
	 * @param originData 查询条件
	 * @return
	 */
	@Override
	public Page<OriginData> findPage(OriginData originData) {
		return super.findPage(originData);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param originData
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(OriginData originData) {
		super.save(originData);
	}
	
	/**
	 * 更新状态
	 * @param originData
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(OriginData originData) {
		super.updateStatus(originData);
	}
	
	/**
	 * 删除数据
	 * @param originData
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(OriginData originData) {
		super.delete(originData);
	}

	/**
	 * 添加数据权限过滤条件
	 * 2022年3月11日 su添加
	 */
	@Override
	public void addDataScopeFilter(OriginData originData) {
		// 举例4：用户、员工（自己创建的）数据权限根据部门过滤，实体类@Table注解extWhereKeys="dsfOffice"
		originData.getSqlMap().getDataScope().addFilter("dsfOffice", "Office",
				"u1.office_code", "a.create_by", DataScope.CTRL_PERMI_HAVE);
	}
	
}