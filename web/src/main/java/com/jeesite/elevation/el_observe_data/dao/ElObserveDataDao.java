/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.elevation.el_observe_data.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.elevation.el_observe_data.entity.ElObserveData;

/**
 * 高程——观测数据DAO接口
 * @author su
 * @version 2022-05-23
 */
@MyBatisDao
public interface ElObserveDataDao extends CrudDao<ElObserveData> {
	
}