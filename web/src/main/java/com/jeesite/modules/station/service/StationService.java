/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.station.service;

import java.util.List;

import com.jeesite.common.entity.DataScope;
import com.jeesite.modules.group.entity.Group;
import com.jeesite.modules.line.entity.Line;
import com.jeesite.modules.line.service.LineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.station.entity.Station;
import com.jeesite.modules.station.dao.StationDao;
import org.springframework.ui.Model;

/**
 * 测站Service
 * @author su
 * @version 2022-03-01
 */
@Service
@Transactional(readOnly=true)
public class StationService extends CrudService<StationDao, Station> {

	@Autowired
	private LineService lineService;
	
	/**
	 * 获取单条数据
	 * @param station
	 * @return
	 */
	@Override
	public Station get(Station station) {
		return super.get(station);
	}
	
	/**
	 * 查询分页数据
	 * @param station 查询条件
	 * @return
	 */
	@Override
	public Page<Station> findPage(Station station) {
		return super.findPage(station);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param station
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(Station station) {
		super.save(station);
	}
	
	/**
	 * 更新状态
	 * @param station
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(Station station) {
		super.updateStatus(station);
	}
	
	/**
	 * 删除数据
	 * @param station
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(Station station) {
		// super.delete(station);

		// 2022年5月30日 su修改
		String lineId = station.getLineId();
		// 删除导线的全部测站及其所有数据
		lineService.deleteAllData(lineId);
	}

	/**
	 * 添加数据权限过滤条件
	 * 2022年3月8日 su添加
	 */
	@Override
	public void addDataScopeFilter(Station station) {
		// 举例4：用户、员工（自己创建的）数据权限根据部门过滤，实体类@Table注解extWhereKeys="dsfOffice"
		station.getSqlMap().getDataScope().addFilter("dsfOffice", "Office",
				"u1.office_code", "a.create_by", DataScope.CTRL_PERMI_HAVE);
	}
	
}