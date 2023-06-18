/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.data_solution.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.data_solution.entity.DataSolution;

/**
 * 数据解算DAO接口
 * @author su
 * @version 2022-03-15
 */
@MyBatisDao
public interface DataSolutionDao extends CrudDao<DataSolution> {
	
}