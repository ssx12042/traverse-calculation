/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.result_evaluation_strict.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.result_evaluation_strict.entity.ResultEvaluationStrict;
import com.jeesite.modules.result_evaluation_strict.dao.ResultEvaluationStrictDao;

/**
 * 间接平差的结果评定Service
 * @author su
 * @version 2022-04-11
 */
@Service
@Transactional(readOnly=true)
public class ResultEvaluationStrictService extends CrudService<ResultEvaluationStrictDao, ResultEvaluationStrict> {
	
	/**
	 * 获取单条数据
	 * @param resultEvaluationStrict
	 * @return
	 */
	@Override
	public ResultEvaluationStrict get(ResultEvaluationStrict resultEvaluationStrict) {
		return super.get(resultEvaluationStrict);
	}
	
	/**
	 * 查询分页数据
	 * @param resultEvaluationStrict 查询条件
	 * @param resultEvaluationStrict.page 分页对象
	 * @return
	 */
	@Override
	public Page<ResultEvaluationStrict> findPage(ResultEvaluationStrict resultEvaluationStrict) {
		return super.findPage(resultEvaluationStrict);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param resultEvaluationStrict
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(ResultEvaluationStrict resultEvaluationStrict) {
		super.save(resultEvaluationStrict);
	}
	
	/**
	 * 更新状态
	 * @param resultEvaluationStrict
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(ResultEvaluationStrict resultEvaluationStrict) {
		super.updateStatus(resultEvaluationStrict);
	}
	
	/**
	 * 删除数据
	 * @param resultEvaluationStrict
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(ResultEvaluationStrict resultEvaluationStrict) {
		super.delete(resultEvaluationStrict);
	}
	
}