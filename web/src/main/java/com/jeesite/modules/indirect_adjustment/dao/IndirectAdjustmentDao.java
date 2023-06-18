/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.indirect_adjustment.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.indirect_adjustment.entity.IndirectAdjustment;

import java.util.Date;

/**
 * 间接平差DAO接口
 * @author su
 * @version 2022-04-06
 */
@MyBatisDao
public interface IndirectAdjustmentDao extends CrudDao<IndirectAdjustment> {

}