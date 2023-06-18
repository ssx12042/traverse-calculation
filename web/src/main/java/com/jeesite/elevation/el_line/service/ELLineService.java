/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.elevation.el_line.service;

import java.util.List;

import com.jeesite.common.entity.DataScope;
import com.jeesite.elevation.el_data_solution.dao.ElDataSolutionDao;
import com.jeesite.elevation.el_data_solution.entity.ElDataSolution;
import com.jeesite.elevation.el_observe_data.dao.ElObserveDataDao;
import com.jeesite.elevation.el_observe_data.entity.ElObserveData;
import com.jeesite.elevation.el_origin_data.dao.ElOriginDataDao;
import com.jeesite.elevation.el_origin_data.entity.ElOriginData;
import com.jeesite.elevation.el_result_evaluation.dao.ElResultEvaluationDao;
import com.jeesite.elevation.el_result_evaluation.entity.ElResultEvaluation;
import com.jeesite.elevation.el_station.dao.ELStationDao;
import com.jeesite.elevation.el_station.entity.ELStation;
import com.jeesite.modules.line.entity.Line;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.elevation.el_line.entity.ELLine;
import com.jeesite.elevation.el_line.dao.ELLineDao;

/**
 * 高程——导线Service
 * @author su
 * @version 2022-05-17
 */
@Service
@Transactional(readOnly=true)
public class ELLineService extends CrudService<ELLineDao, ELLine> {

	@Autowired
	private ELStationDao elStationDao;

	@Autowired
	private ElOriginDataDao elOriginDataDao;

	@Autowired
	private ElObserveDataDao elObserveDataDao;

	@Autowired
	private ElDataSolutionDao elDataSolutionDao;

	@Autowired
	private ElResultEvaluationDao elResultEvaluationDao;
	
	/**
	 * 获取单条数据
	 * @param eLLine
	 * @return
	 */
	@Override
	public ELLine get(ELLine eLLine) {
		return super.get(eLLine);
	}
	
	/**
	 * 查询分页数据
	 * @param eLLine 查询条件
	 * @return
	 */
	@Override
	public Page<ELLine> findPage(ELLine eLLine) {
		return super.findPage(eLLine);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param eLLine
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(ELLine eLLine) {
		super.save(eLLine);
	}
	
	/**
	 * 更新状态
	 * @param eLLine
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(ELLine eLLine) {
		super.updateStatus(eLLine);
	}
	
	/**
	 * 删除数据
	 * @param eLLine
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(ELLine eLLine) {
		super.delete(eLLine);

		// 2022年5月30日 su修改
		String lineId = eLLine.getId();
		// 删除导线的全部测站及其所有数据
		deleteAllData(lineId);
	}

	/**
	 * 添加数据权限过滤条件
	 * 2022年5月17日 su添加
	 */
	@Override
	public void addDataScopeFilter(ELLine elLine){
		// 举例4：用户、员工（自己创建的）数据权限根据部门过滤，实体类@Table注解extWhereKeys="dsfOffice"
		elLine.getSqlMap().getDataScope().addFilter("dsfOffice", "Office",
				"u1.office_code", "a.create_by", DataScope.CTRL_PERMI_HAVE);
	}

	/**
	 * 删除导线的全部测站及其所有数据
	 * @param lineId 导线id
	 */
	public void deleteAllData(String lineId) {
		// 获取该导线下所有的测站并删除
		ELStation _elSta = new ELStation();
		_elSta.setLineId(lineId);
		List<ELStation> elStationList = elStationDao.findList(_elSta);
		for (ELStation els : elStationList) {
			// 删除
			elStationDao.delete(els);
		}

		// 获取该导线下的起始数据并删除
		ElOriginData _elOriData = new ElOriginData();
		_elOriData.setLine(new ELLine(lineId));
		List<ElOriginData> elOriDataList = elOriginDataDao.findList(_elOriData);
		for (ElOriginData elord : elOriDataList) {
			// 删除
			elOriginDataDao.delete(elord);
		}

		// 获取该导线下的观测数据并删除
		ElObserveData _elObsData = new ElObserveData();
		_elObsData.setLine(new ELLine(lineId));
		List<ElObserveData> elObsDataList = elObserveDataDao.findList(_elObsData);
		for (ElObserveData elobd : elObsDataList) {
			// 删除
			elObserveDataDao.delete(elobd);
		}

		// 获取该导线下的数据解算并删除
		ElDataSolution _elDS = new ElDataSolution();
		_elDS.setLine(new ELLine(lineId));
		List<ElDataSolution> elDSList = elDataSolutionDao.findList(_elDS);
		for (ElDataSolution elds : elDSList) {
			// 删除
			elDataSolutionDao.delete(elds);
		}

		// 获取该导线下的结果评定并删除
		ElResultEvaluation _elRE = new ElResultEvaluation();
		_elRE.setLineId(lineId);
		List<ElResultEvaluation> elREList = elResultEvaluationDao.findList(_elRE);
		for (ElResultEvaluation elre : elREList) {
			// 删除
			elResultEvaluationDao.delete(elre);
		}
	}
	
}