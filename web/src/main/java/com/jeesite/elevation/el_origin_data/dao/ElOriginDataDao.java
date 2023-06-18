/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.elevation.el_origin_data.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.elevation.el_origin_data.entity.ElOriginData;

/**
 * 高程——起始数据DAO接口
 * @author su
 * @version 2022-05-30
 */
@MyBatisDao
public interface ElOriginDataDao extends CrudDao<ElOriginData> {
	
}