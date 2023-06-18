/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.group.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.group.entity.Group;

/**
 * 组管理DAO接口
 * @author su
 * @version 2022-02-28
 */
@MyBatisDao
public interface GroupDao extends CrudDao<Group> {
	
}