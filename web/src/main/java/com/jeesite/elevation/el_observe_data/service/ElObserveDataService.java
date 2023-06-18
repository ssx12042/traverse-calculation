/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.elevation.el_observe_data.service;

import java.util.List;

import com.jeesite.common.entity.DataScope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.elevation.el_observe_data.entity.ElObserveData;
import com.jeesite.elevation.el_observe_data.dao.ElObserveDataDao;

/**
 * 高程——观测数据Service
 * @author su
 * @version 2022-05-23
 */
@Service
@Transactional(readOnly=true)
public class ElObserveDataService extends CrudService<ElObserveDataDao, ElObserveData> {
	
	/**
	 * 获取单条数据
	 * @param elObserveData
	 * @return
	 */
	@Override
	public ElObserveData get(ElObserveData elObserveData) {
		return super.get(elObserveData);
	}
	
	/**
	 * 查询分页数据
	 * @param elObserveData 查询条件
	 * @return
	 */
	@Override
	public Page<ElObserveData> findPage(ElObserveData elObserveData) {
		return super.findPage(elObserveData);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param elObserveData
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(ElObserveData elObserveData) {
		// 2022年5月30日 su添加
		//获取当前测站观测数据录入了多少测回
		String stationId = elObserveData.getStationId();
		// 获取该测站的全部测回
		ElObserveData elObserveData1 = new ElObserveData();
		elObserveData1.setStatus(ElObserveData.STATUS_NORMAL);
		elObserveData1.setStationId(stationId);
		List<ElObserveData> elObserveDataList = findList(elObserveData1);

		// 设置当前录入的测站的测回
		if (elObserveData.getObserveRound() == null) { //新纪录
			elObserveData.setObserveRound(elObserveDataList.size() + 1);
		}

		// 设置垂直角度的符号 以及 垂直角的度
		// 与前站
		String verticalAngSignForeStr = elObserveData.getVerticalAngDegForeStr();	// 可能包含有 “-” 号
		int degFore;
		if (verticalAngSignForeStr.startsWith("-")) {
			// 符号设置为 -1
			elObserveData.setVerticalAngSignFore(-1);
			degFore = Integer.parseInt(verticalAngSignForeStr.substring(1));
		} else {
			// 符号设置为 1   2022年6月24日 su添加
			elObserveData.setVerticalAngSignFore(1);
			degFore = Integer.parseInt(verticalAngSignForeStr);
		}
		elObserveData.setVerticalAngDegFore(degFore);
		//与后站
		String verticalAngSignBackStr = elObserveData.getVerticalAngDegBackStr();	// 可能包含有 “-” 号
		int degBack;
		if (verticalAngSignBackStr.startsWith("-")) {
			// 符号设置为 -1
			elObserveData.setVerticalAngSignBack(-1);
			degBack = Integer.parseInt(verticalAngSignBackStr.substring(1));
		} else {
			// 符号设置为 1   2022年6月24日 su添加
			elObserveData.setVerticalAngSignBack(1);
			degBack = Integer.parseInt(verticalAngSignBackStr);
		}
		elObserveData.setVerticalAngDegBack(degBack);

		super.save(elObserveData);
	}
	
	/**
	 * 更新状态
	 * @param elObserveData
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(ElObserveData elObserveData) {
		super.updateStatus(elObserveData);
	}
	
	/**
	 * 删除数据
	 * @param elObserveData
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(ElObserveData elObserveData) {
		super.delete(elObserveData);

		// 2022年4月5日 su添加
		// 删除了数据，需要重新编排一下测回数，因为本来是1,2,3。 删除了1应该变为1,2
		// 获取删除的测站ID
		String stationId = elObserveData.getStationId();
		// 获取删除的测站的测回
		Integer observationRound = elObserveData.getObserveRound();
		// 获取该测站的全部观测数据（状态正常的）
		ElObserveData elObserveData1 = new ElObserveData();
		elObserveData1.setStationId(stationId);
		elObserveData1.setStatus(ElObserveData.STATUS_NORMAL);
		List<ElObserveData> elObserveDataList = findList(elObserveData1);

		// 如果删除的数据不是最后一个测回，才需要重新编排测回数
		if (elObserveDataList.size() + 1 != observationRound) {
			int i = 1;
			for (ElObserveData od : elObserveDataList) {
				od.setObserveRound(i);
				i++;
				// 数据入库
				super.save(od);
			}
		}

	}

	/**
	 * 添加数据权限过滤条件
	 * 2022年5月23日 su添加
	 */
	@Override
	public void addDataScopeFilter(ElObserveData elObserveData) {
		// 举例4：用户、员工（自己创建的）数据权限根据部门过滤，实体类@Table注解extWhereKeys="dsfOffice"
		elObserveData.getSqlMap().getDataScope().addFilter("dsfOffice", "Office",
				"u1.office_code", "a.create_by", DataScope.CTRL_PERMI_HAVE);
	}
	
}