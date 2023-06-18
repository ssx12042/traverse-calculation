/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.line.service;

import java.util.List;

import com.jeesite.common.entity.DataScope;
import com.jeesite.modules.data_solution.dao.DataSolutionDao;
import com.jeesite.modules.data_solution.entity.DataSolution;
import com.jeesite.modules.group.entity.Group;
import com.jeesite.modules.observation.dao.ObservationDataDao;
import com.jeesite.modules.observation.entity.ObservationData;
import com.jeesite.modules.origin.dao.OriginDataDao;
import com.jeesite.modules.origin.entity.OriginData;
import com.jeesite.modules.result_evaluation.dao.ResultEvaluationDao;
import com.jeesite.modules.result_evaluation.entity.ResultEvaluation;
import com.jeesite.modules.station.dao.StationDao;
import com.jeesite.modules.station.entity.Station;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.line.entity.Line;
import com.jeesite.modules.line.dao.LineDao;

/**
 * 导线Service
 * @author su
 * @version 2022-02-28
 */
@Service
@Transactional(readOnly=true)
public class LineService extends CrudService<LineDao, Line> {

	@Autowired
	private StationDao stationDao;

	@Autowired
	private OriginDataDao originDataDao;

	@Autowired
	private ObservationDataDao observationDataDao;

	@Autowired
	private DataSolutionDao dataSolutionDao;

	@Autowired
	private ResultEvaluationDao resultEvaluationDao;
	
	/**
	 * 获取单条数据
	 * @param line
	 * @return
	 */
	@Override
	public Line get(Line line) {
		return super.get(line);
	}
	
	/**
	 * 查询分页数据
	 * @param line 查询条件
	 * @return
	 */
	@Override
	public Page<Line> findPage(Line line) {
		return super.findPage(line);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param line
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(Line line) {
		super.save(line);
	}
	
	/**
	 * 更新状态
	 * @param line
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(Line line) {
		super.updateStatus(line);
	}
	
	/**
	 * 删除数据
	 * @param line
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(Line line) {
		super.delete(line);

		// 2022年6月20日 su修改
		String lineId = line.getId();
		// 删除导线的全部测站及其所有数据
		deleteAllData(lineId);
	}

	/**
	 * 添加数据权限过滤条件
	 * 2022年3月8日 su添加
	 */
	@Override
	public void addDataScopeFilter(Line line){
		// 举例4：用户、员工（自己创建的）数据权限根据部门过滤，实体类@Table注解extWhereKeys="dsfOffice"
		line.getSqlMap().getDataScope().addFilter("dsfOffice", "Office",
				"u1.office_code", "a.create_by", DataScope.CTRL_PERMI_HAVE);
	}

	/**
	 * 删除导线的全部测站及其所有数据
	 * @param lineId 导线id
	 */
	public void deleteAllData(String lineId) {
		// 获取该导线下所有的测站并删除
		Station _sta = new Station();
		_sta.setLineId(lineId);
		List<Station> elStationList = stationDao.findList(_sta);
		for (Station s : elStationList) {
			// 删除
			stationDao.delete(s);
		}

		// 获取该导线下的起始数据并删除
		OriginData _oriData = new OriginData();
		_oriData.setLine(new Line(lineId));
		List<OriginData> elOriDataList = originDataDao.findList(_oriData);
		for (OriginData ord : elOriDataList) {
			// 删除
			originDataDao.delete(ord);
		}

		// 获取该导线下的观测数据并删除
		ObservationData _elObsData = new ObservationData();
		_elObsData.setLine(new Line(lineId));
		List<ObservationData> elObsDataList = observationDataDao.findList(_elObsData);
		for (ObservationData elobd : elObsDataList) {
			// 删除
			observationDataDao.delete(elobd);
		}

		// 获取该导线下的数据解算并删除
		DataSolution _elDS = new DataSolution();
		_elDS.setLine(new Line(lineId));
		List<DataSolution> elDSList = dataSolutionDao.findList(_elDS);
		for (DataSolution elds : elDSList) {
			// 删除
			dataSolutionDao.delete(elds);
		}

		// 获取该导线下的结果评定并删除
		ResultEvaluation _elRE = new ResultEvaluation();
		_elRE.setLineId(lineId);
		List<ResultEvaluation> elREList = resultEvaluationDao.findList(_elRE);
		for (ResultEvaluation elre : elREList) {
			// 删除
			resultEvaluationDao.delete(elre);
		}
	}
	
}