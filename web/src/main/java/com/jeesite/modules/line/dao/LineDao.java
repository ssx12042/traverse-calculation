/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.line.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.line.entity.Line;

/**
 * 导线DAO接口
 * @author su
 * @version 2022-02-28
 */
@MyBatisDao
public interface LineDao extends CrudDao<Line> {
	
}