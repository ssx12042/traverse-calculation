/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.observation.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.observation.entity.ObservationData;

/**
 * 观测数据DAO接口
 * @author su
 * @version 2022-03-14
 */
@MyBatisDao
public interface ObservationDataDao extends CrudDao<ObservationData> {
	
}