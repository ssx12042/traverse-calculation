/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.demo.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.demo.entity.DemoData;

/**
 * 数据权限演示DAO接口
 * @author su
 * @version 2022-02-22
 */
@MyBatisDao
public interface DemoDataDao extends CrudDao<DemoData> {
	
}