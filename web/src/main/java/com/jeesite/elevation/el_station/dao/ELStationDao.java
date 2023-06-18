/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.elevation.el_station.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.elevation.el_station.entity.ELStation;

/**
 * 高程——测站DAO接口
 * @author su
 * @version 2022-05-17
 */
@MyBatisDao
public interface ELStationDao extends CrudDao<ELStation> {
	
}