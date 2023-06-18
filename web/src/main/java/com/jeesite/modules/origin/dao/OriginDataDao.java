/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.origin.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.origin.entity.OriginData;

/**
 * 起始数据DAO接口
 * @author su
 * @version 2022-03-10
 */
@MyBatisDao
public interface OriginDataDao extends CrudDao<OriginData> {
	
}