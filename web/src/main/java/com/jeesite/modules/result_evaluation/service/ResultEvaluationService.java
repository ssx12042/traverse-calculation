/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.result_evaluation.service;

import java.util.List;

import com.jeesite.common.entity.DataScope;
import com.jeesite.modules.data_solution.entity.DataSolution;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.result_evaluation.entity.ResultEvaluation;
import com.jeesite.modules.result_evaluation.dao.ResultEvaluationDao;

/**
 * 结果评定Service
 * @author su
 * @version 2022-03-15
 */
@Service
@Transactional(readOnly=true)
public class ResultEvaluationService extends CrudService<ResultEvaluationDao, ResultEvaluation> {
	
	/**
	 * 获取单条数据
	 * @param resultEvaluation
	 * @return
	 */
	@Override
	public ResultEvaluation get(ResultEvaluation resultEvaluation) {
		return super.get(resultEvaluation);
	}
	
	/**
	 * 查询分页数据
	 * @param resultEvaluation 查询条件
	 * @return
	 */
	@Override
	public Page<ResultEvaluation> findPage(ResultEvaluation resultEvaluation) {
		return super.findPage(resultEvaluation);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param resultEvaluation
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(ResultEvaluation resultEvaluation) {
		super.save(resultEvaluation);
	}
	
	/**
	 * 更新状态
	 * @param resultEvaluation
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(ResultEvaluation resultEvaluation) {
		super.updateStatus(resultEvaluation);
	}
	
	/**
	 * 删除数据
	 * @param resultEvaluation
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(ResultEvaluation resultEvaluation) {
		super.delete(resultEvaluation);
	}

	/**
	 * 添加数据权限过滤条件
	 * 2022年3月15日 su添加
	 */
	@Override
	public void addDataScopeFilter(ResultEvaluation resultEvaluation) {
		// 举例4：用户、员工（自己创建的）数据权限根据部门过滤，实体类@Table注解extWhereKeys="dsfOffice"
		resultEvaluation.getSqlMap().getDataScope().addFilter("dsfOffice", "Office",
				"u1.office_code", "a.create_by", DataScope.CTRL_PERMI_HAVE);
	}
	
}