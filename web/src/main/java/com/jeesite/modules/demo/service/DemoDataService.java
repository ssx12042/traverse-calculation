/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.demo.service;

import java.util.List;

import com.jeesite.common.entity.DataScope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.demo.entity.DemoData;
import com.jeesite.modules.demo.dao.DemoDataDao;

/**
 * 数据权限演示Service
 * @author su
 * @version 2022-02-22
 */
@Service
@Transactional(readOnly=true)
public class DemoDataService extends CrudService<DemoDataDao, DemoData> {

	/**
	 * 获取单条数据
	 * @param demoData
	 * @return
	 */
	@Override
	public DemoData get(DemoData demoData) {
		return super.get(demoData);
	}

	/**
	 * 查询分页数据
	 * @param demoData 查询条件
	 * @param demoData.page 分页对象
	 * @return
	 */
	@Override
	public Page<DemoData> findPage(DemoData demoData) {
		return super.findPage(demoData);
	}

	/**
	 * 保存数据（插入或更新）
	 * @param demoData
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(DemoData demoData) {
		super.save(demoData);
	}

	/**
	 * 更新状态
	 * @param demoData
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(DemoData demoData) {
		super.updateStatus(demoData);
	}

	/**
	 * 删除数据
	 * @param demoData
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(DemoData demoData) {
		super.delete(demoData);
	}

	/**
	 * 添加数据权限过滤条件
	 */
	@Override
	public void addDataScopeFilter(DemoData demoData){

        // 举例2：部门数据权限过滤，实体类@Table注解extWhereKeys="dsf"
        //demoData.getSqlMap().getDataScope().addFilter("dsfCreateBy", "Office", "a.demo_office_code", DataScope.CTRL_PERMI_HAVE);
        demoData.getSqlMap().getDataScope().addFilter("dsfOffice", "Office", "a.demo_office_code", "a.create_by", DataScope.CTRL_PERMI_HAVE);

		// 举例4：用户、员工（自己创建的）数据权限根据部门过滤，实体类@Table注解extWhereKeys="dsfOffice"
		demoData.getSqlMap().getDataScope().addFilter("dsfOffice", "Office", "e.office_code", "a.create_by", DataScope.CTRL_PERMI_HAVE);
	}


}