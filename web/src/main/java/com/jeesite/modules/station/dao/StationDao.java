/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.station.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.station.entity.Station;

/**
 * 测站DAO接口
 * @author su
 * @version 2022-03-01
 */
@MyBatisDao
public interface StationDao extends CrudDao<Station> {
	
}