/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.wrong.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.wrong.entity.WrongResult;

/**
 * 错误边角查询DAO接口
 * @author hu
 * @version 2022-05-03
 */
@MyBatisDao
public interface WrongResultDao extends CrudDao<WrongResult> {
	
}