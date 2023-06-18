/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.result_evaluation.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.result_evaluation.entity.ResultEvaluation;

/**
 * 结果评定DAO接口
 * @author su
 * @version 2022-03-15
 */
@MyBatisDao
public interface ResultEvaluationDao extends CrudDao<ResultEvaluation> {
	
}