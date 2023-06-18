/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.elevation.el_result_evaluation.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.elevation.el_result_evaluation.entity.ElResultEvaluation;
import com.jeesite.elevation.el_result_evaluation.dao.ElResultEvaluationDao;

/**
 * 高程——结果评定Service
 * @author su
 * @version 2022-06-07
 */
@Service
@Transactional(readOnly=true)
public class ElResultEvaluationService extends CrudService<ElResultEvaluationDao, ElResultEvaluation> {
	
	/**
	 * 获取单条数据
	 * @param elResultEvaluation
	 * @return
	 */
	@Override
	public ElResultEvaluation get(ElResultEvaluation elResultEvaluation) {
		return super.get(elResultEvaluation);
	}
	
	/**
	 * 查询分页数据
	 * @param elResultEvaluation 查询条件
	 * @param elResultEvaluation.page 分页对象
	 * @return
	 */
	@Override
	public Page<ElResultEvaluation> findPage(ElResultEvaluation elResultEvaluation) {
		return super.findPage(elResultEvaluation);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param elResultEvaluation
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(ElResultEvaluation elResultEvaluation) {
		super.save(elResultEvaluation);
	}
	
	/**
	 * 更新状态
	 * @param elResultEvaluation
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(ElResultEvaluation elResultEvaluation) {
		super.updateStatus(elResultEvaluation);
	}
	
	/**
	 * 删除数据
	 * @param elResultEvaluation
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(ElResultEvaluation elResultEvaluation) {
		super.delete(elResultEvaluation);
	}
	
}