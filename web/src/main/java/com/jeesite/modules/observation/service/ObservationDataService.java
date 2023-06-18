/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.observation.service;

import java.util.List;

import com.jeesite.common.entity.DataScope;
import com.jeesite.modules.data_solution.entity.DataSolution;
import com.jeesite.modules.group.entity.Group;
import com.jeesite.modules.line.entity.Line;
import com.jeesite.modules.origin.entity.OriginData;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.observation.entity.ObservationData;
import com.jeesite.modules.observation.dao.ObservationDataDao;

/**
 * 观测数据Service
 * @author su
 * @version 2022-03-14
 */
@Service
@Transactional(readOnly=true)
public class ObservationDataService extends CrudService<ObservationDataDao, ObservationData> {
	
	/**
	 * 获取单条数据
	 * @param observationData
	 * @return
	 */
	@Override
	public ObservationData get(ObservationData observationData) {
		return super.get(observationData);
	}
	
	/**
	 * 查询分页数据
	 * @param observationData 查询条件
	 * @return
	 */
	@Override
	public Page<ObservationData> findPage(ObservationData observationData) {
		return super.findPage(observationData);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param observationData
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(ObservationData observationData) {
		double fdegreeLeft;			// 前左度数(分秒为小数)
		double fradianLeft;			// 前左弧度
		double fdegreeRight; 		// 前右度数(分秒为小数)
		double fradianRight;		// 前右弧度

		double bdegreeLeft; 		// 后左度数(分秒为小数)
		double bradianLeft;			// 后左弧度
		double bdegreeRight; 		// 后右度数(分秒为小数)
		double bradianRight;		// 后右弧度

		double resultradianLeft;	// 盘左未取整半测回弧度
		double resultDegLeft; 		// 盘左未取整半测回度数
		double resultMinLeft; 		// 盘左未取整半测回分
		double resultSecLeft; 		// 盘左未取整半测回秒

		double resultradianRight;	// 盘右未取整半测回弧度
		double resultDegRight; 		// 盘右未取整半测回度数
		double resultMinRight; 		// 盘右未取整半测回分
		double resultSecRight; 		// 盘右未取整半测回秒

		double resultFullDeg; 		// 未取整一测回度
		double resultFullMin; 		// 未取整一测回分
		double resultFullSec; 		// 未取整一测回秒

		//获取当前测站观测数据录入了多少测回
		//String groupId = observationData.getGroup().getId();
		//String lineId = observationData.getLine().getId();
		String stationId = observationData.getStationId();
		//List<Integer> observationRoundList = getObservationRound(groupId, lineId, stationId);	//获取测回列表
		// 获取该测站的全部测回
		ObservationData observationData1 = new ObservationData();
		observationData1.setStationId(stationId);
		observationData1.setStatus(ObservationData.STATUS_NORMAL);
		List<ObservationData> observationDataList = findList(observationData1);

		//设置当前录入的测站的测回
		if (observationData.getObservationRound() == null) { //新纪录
			/*for (int i = 1; i < 100; i++){
				if (!observationRoundList.contains(i)) {
					observationData.setObservationRound(i);
					break;
				}
			}*/
			observationData.setObservationRound(observationDataList.size() + 1);
		}



		/*--------------------------------- 半测回角值计算 ---------------------------------*/
		//------------------- 盘左计算
		// 前视盘左以度数为单位
		fdegreeLeft = (double) observationData.foreLeftDeg + (double) observationData.foreLeftMin / 60
				+ (double) observationData.foreLeftSec / 3600;
		// 前视盘左以弧数为单位
		//fradianLeft = Math.toRadians(fdegreeLeft);
		// 后视盘左以度数为单位
		bdegreeLeft = (double) observationData.backLeftDeg + (double) observationData.backLeftMin / 60
				+ (double) observationData.backLeftSec / 3600;
		// 后视盘左以弧度为单位
		//bradianLeft = Math.toRadians(bdegreeLeft);	//角度转弧度
		if (fdegreeLeft > bdegreeLeft) {
			resultDegLeft = fdegreeLeft - bdegreeLeft;
		} else {
			resultDegLeft = fdegreeLeft + 360 - bdegreeLeft;
		}
		// 盘左未取整半测回度数
		//resultDegLeft = Math.toDegrees(resultradianLeft);	//弧度转角度
		if (resultDegLeft < 1) {
			resultDegLeft += 180;
		}
		// 盘左半测回角值度
		observationData.halfLeftDeg = (int) Math.floor(resultDegLeft);	//Math.floor取整
		// 分=（不取整度数-取整度数）*60
		resultMinLeft = (resultDegLeft - observationData.halfLeftDeg) * 60;
		// 盘左半测回角值分
		observationData.halfLeftMin = (int) Math.floor(resultMinLeft);
		// 秒=（不取整分数-取整分数）*60
		resultSecLeft = (resultMinLeft - observationData.halfLeftMin) * 60;
		// 盘左半测回角值秒
		//observationData.halfLeftSec = (int) Math.round(resultSecLeft);
		observationData.halfLeftSec = Double.parseDouble(String.format("%.1f", resultSecLeft));
		//进位
		if (observationData.halfLeftSec == 60) {
			observationData.halfLeftSec = 0.;
			observationData.halfLeftMin += 1;
			if (observationData.halfLeftMin == 60) {
				observationData.halfLeftMin = 0;
				observationData.halfLeftDeg += 1;
			}
		}

		//------------------- 盘右计算
		// 前视盘右以度数为单位
		fdegreeRight = (double) observationData.foreRightDeg + (double) observationData.foreRightMin / 60
				+ (double) observationData.foreRightSec / 3600;
		// 前视盘右以弧度为单位
		//fradianRight = Math.toRadians(fdegreeRight);
		// 后视盘右以度数为单位
		bdegreeRight = (double) observationData.backRightDeg + (double) observationData.backRightMin / 60
				+ (double) observationData.backRightSec / 3600;
		// 后视盘右以弧度为单位
		//bradianRight = Math.toRadians(bdegreeRight);
		if (fdegreeRight > bdegreeRight) {
			resultDegRight = fdegreeRight - bdegreeRight;
		} else {
			resultDegRight = fdegreeRight + 360 - bdegreeRight;
		}
		// 盘右未取整半测回度数
		//resultDegRight = Math.toDegrees(resultradianRight);
		if (resultDegRight < 1) {
			resultDegRight += 180;
		}
		// 盘右半测回角值度
		observationData.halfRightDeg = (int) Math.floor(resultDegRight);
		// 分=（不取整度数-取整度数）*60
		resultMinRight = (resultDegRight - observationData.halfRightDeg) * 60;
		// 盘右半测回角值分
		observationData.halfRightMin = (int) Math.floor(resultMinRight);
		// 秒=（不取整分数-取整分数）*60
		resultSecRight = (double) (resultMinRight - observationData.halfRightMin) * 60;
		// 盘右半测回角值秒
		//observationData.halfRightSec = (int) Math.round(resultSecRight);
		observationData.halfRightSec = Double.parseDouble(String.format("%.1f", resultSecRight));
		//进位
		if (observationData.halfRightSec == 60) {
			observationData.halfRightSec = 0.;
			observationData.halfRightMin += 1;
			if (observationData.halfRightMin == 60) {
				observationData.halfRightMin = 0;
				observationData.halfRightDeg += 1;
			}
		}



		/*--------------------------------- 一测回角值计算 ---------------------------------*/
		// 未取整一测回度
		resultFullDeg = (resultDegLeft + resultDegRight) / 2;
		// 一测回角值度
		observationData.fullValueDeg = (int) Math.floor(resultFullDeg);
		// 未取整一测回分
		resultFullMin = (resultFullDeg - observationData.fullValueDeg) * 60;
		// 一测回角值分
		observationData.fullValueMin = (int) Math.floor(resultFullMin);
		// 未取整一测回秒
		resultFullSec = (resultFullMin - observationData.fullValueMin) * 60;
		// 一测回角值秒
		//observationData.fullValueSec = (int) Math.round(resultFullSec);
		observationData.fullValueSec = Double.parseDouble(String.format("%.1f", resultFullSec));
		//进位
		if (observationData.fullValueSec == 60) {
			observationData.fullValueSec = 0.;
			observationData.fullValueMin += 1;
			if (observationData.fullValueMin == 60) {
				observationData.fullValueMin = 0;
				observationData.fullValueDeg += 1;
			}
		}
		// 转折角弧度计算
		observationData.turningAngRadian = Math.toRadians((double) observationData.fullValueDeg
				+ (double) observationData.fullValueMin / 60 + (double) observationData.fullValueSec / 3600);



		/*--------------------------------- 2C互差计算 ---------------------------------*/
		//------------------- 前视2C互差
		double doublecFDeg = Math.abs(fdegreeLeft - fdegreeRight);
		if (doublecFDeg >= 1) {
			doublecFDeg = Math.abs(doublecFDeg - 180);
		}
		//observationData.doubleCFore = (int) (Math.round(doublecFDeg * 3600));	//转换为以秒为单位
		observationData.doubleCFore = Double.parseDouble(String.format("%.1f", doublecFDeg * 3600));	//转换为以秒为单位
		System.out.println("前视2C互差为" + observationData.doubleCFore);

		//------------------- 后视2C互差
		double doublecBDeg = Math.abs(bdegreeLeft - bdegreeRight);
		if (doublecBDeg >= 1) {
			doublecBDeg = Math.abs(doublecBDeg - 180);
		}
		//observationData.doubleCBack = (int) (Math.round(doublecBDeg * 3600));	//转换为以秒为单位
		observationData.doubleCBack = Double.parseDouble(String.format("%.1f", doublecBDeg * 3600));	//转换为以秒为单位
		System.out.println("后视2C互差为" + observationData.doubleCBack);


		super.save(observationData);
	}
	
	/**
	 * 更新状态
	 * @param observationData
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(ObservationData observationData) {
		super.updateStatus(observationData);
	}
	
	/**
	 * 删除数据
	 * @param observationData
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(ObservationData observationData) {
		super.delete(observationData);

		// 2022年4月5日 su添加
		// 删除了数据，需要重新编排一下测回数，因为本来是1,2,3。 删除了1应该变为1,2
		// 获取删除的测站ID
		String stationId = observationData.getStationId();
		// 获取删除的测站的测回
		Integer observationRound = observationData.getObservationRound();
		// 获取该测站的全部观测数据（状态正常的）
		ObservationData observationData1 = new ObservationData();
		observationData1.setStationId(stationId);
		observationData1.setStatus(ObservationData.STATUS_NORMAL);
		List<ObservationData> odList = findList(observationData1);

		// 如果删除的数据不是最后一个测回，才需要重新编排测回数
		if (odList.size() + 1 != observationRound) {
			int i = 1;
			for (ObservationData od : odList) {
				od.setObservationRound(i);
				i++;
				// 数据入库
				super.save(od);
			}
		}

	}

	/**
	 * 添加数据权限过滤条件
	 * 2022年3月14日 su添加
	 */
	@Override
	public void addDataScopeFilter(ObservationData observationData) {
		// 举例4：用户、员工（自己创建的）数据权限根据部门过滤，实体类@Table注解extWhereKeys="dsfOffice"
		observationData.getSqlMap().getDataScope().addFilter("dsfOffice", "Office",
				"u1.office_code", "a.create_by", DataScope.CTRL_PERMI_HAVE);
	}
	
}