/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.elevation.el_indirect_adjustment.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.elevation.el_indirect_adjustment.entity.ElIndirectAdjustment;

/**
 * 高程——间接平差DAO接口
 * @author su
 * @version 2022-07-04
 */
@MyBatisDao
public interface ElIndirectAdjustmentDao extends CrudDao<ElIndirectAdjustment> {
	
}