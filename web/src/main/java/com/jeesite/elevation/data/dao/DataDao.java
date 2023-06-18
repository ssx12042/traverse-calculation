/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.elevation.data.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.elevation.data.entity.Data;

/**
 * dataDAO接口
 * @author hu
 * @version 2022-06-16
 */
@MyBatisDao
public interface DataDao extends CrudDao<Data> {
	
}