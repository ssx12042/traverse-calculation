/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.elevation.el_station.service;

import com.jeesite.common.entity.DataScope;
import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.elevation.el_line.service.ELLineService;
import com.jeesite.elevation.el_station.dao.ELStationDao;
import com.jeesite.elevation.el_station.entity.ELStation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 高程——测站Service
 * @author su
 * @version 2022-05-17
 */
@Service
@Transactional(readOnly=true)
public class ELStationService extends CrudService<ELStationDao, ELStation> {

	@Autowired
	private ELLineService elLineService;

	/**
	 * 获取单条数据
	 * @param eLStation
	 * @return
	 */
	@Override
	public ELStation get(ELStation eLStation) {
		return super.get(eLStation);
	}
	
	/**
	 * 查询分页数据
	 * @param eLStation 查询条件
	 * @return
	 */
	@Override
	public Page<ELStation> findPage(ELStation eLStation) {
		return super.findPage(eLStation);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param eLStation
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(ELStation eLStation) {
		super.save(eLStation);
	}
	
	/**
	 * 更新状态
	 * @param eLStation
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(ELStation eLStation) {
		super.updateStatus(eLStation);
	}
	
	/**
	 * 删除数据
	 * @param eLStation
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(ELStation eLStation) {
		// 2022年5月30日 su修改
		String lineId = eLStation.getLineId();
		// 删除导线的全部测站及其所有数据
		elLineService.deleteAllData(lineId);

		// super.delete(eLStation);
	}

	/**
	 * 添加数据权限过滤条件
	 * 2022年5月17日 su添加
	 */
	@Override
	public void addDataScopeFilter(ELStation elStation) {
		// 举例4：用户、员工（自己创建的）数据权限根据部门过滤，实体类@Table注解extWhereKeys="dsfOffice"
		elStation.getSqlMap().getDataScope().addFilter("dsfOffice", "Office",
				"u1.office_code", "a.create_by", DataScope.CTRL_PERMI_HAVE);
	}
	
}