/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.elevation.el_data_solution.service;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.jeesite.common.entity.DataScope;
import com.jeesite.elevation.el_indirect_adjustment.entity.ElIndirectAdjustment;
import com.jeesite.elevation.el_indirect_adjustment.service.ElIndirectAdjustmentService;
import com.jeesite.elevation.el_line.entity.ELLine;
import com.jeesite.elevation.el_observe_data.dao.ElObserveDataDao;
import com.jeesite.elevation.el_observe_data.entity.ElObserveData;
import com.jeesite.elevation.el_origin_data.dao.ElOriginDataDao;
import com.jeesite.elevation.el_origin_data.entity.ElOriginData;
import com.jeesite.elevation.el_result_evaluation.entity.ElResultEvaluation;
import com.jeesite.elevation.el_result_evaluation.service.ElResultEvaluationService;
import com.jeesite.modules.group.entity.Group;
import com.jeesite.modules.observation.entity.ObservationData;
import com.jeesite.utils.AngleUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.elevation.el_data_solution.entity.ElDataSolution;
import com.jeesite.elevation.el_data_solution.dao.ElDataSolutionDao;

/**
 * 高程——数据解算Service
 * @author su
 * @version 2022-05-24
 */
@Service
@Transactional(readOnly=true)
public class ElDataSolutionService extends CrudService<ElDataSolutionDao, ElDataSolution> {

	@Autowired
	private ElObserveDataDao elObserveDataDao;

	@Autowired
	private ElOriginDataDao elOriginDataDao;

	@Autowired
	private ElDataSolutionDao elDataSolutionDao;

	@Autowired
	private ElResultEvaluationService elResEvalService;

	@Autowired
	private ElIndirectAdjustmentService elIndirectAdjustmentService;
	
	/**
	 * 获取单条数据
	 * @param elDataSolution
	 * @return
	 */
	@Override
	public ElDataSolution get(ElDataSolution elDataSolution) {
		return super.get(elDataSolution);
	}
	
	/**
	 * 查询分页数据
	 * @param elDataSolution 查询条件
	 * @return
	 */
	@Override
	public Page<ElDataSolution> findPage(ElDataSolution elDataSolution) {
		return super.findPage(elDataSolution);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param elDataSolution
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(ElDataSolution elDataSolution) {
		// 2022年5月31日 su修改
		//获取小组id
		String groupId = elDataSolution.getGroup().getId();
		//获取导线id
		String lineId = elDataSolution.getLine().getId();
		// 获取导线等级
		String traverseClass = elDataSolution.getTraverseClass();
		// 是否考虑球气差（1是 0否）
		Integer isQQC = elDataSolution.getIsQQC();
		// 大气折光系数（默认）
		double K = 0;
		// 地球半径(km)（默认）
		double R = 6371;
		//获取备注信息
		String remarks = elDataSolution.getRemarks();
		// 球气差系数
		double C = 0;
		if (isQQC == 1) {
			// double K = 0.12;
			// double R = 6.371e6;				// 米
			K = elDataSolution.getCoefficient_K();
			R = elDataSolution.getCoefficient_R();	// km千米
			C = (1 - K) / (2 * R * 1000);			// m的-1次方
		}

		//获取该导线的全部观测数据（状态正常的）
		ElObserveData elObserveData = new ElObserveData();
		elObserveData.setGroup(new Group(groupId));
		elObserveData.setLine(new ELLine(lineId));
		elObserveData.setStatus(ElObserveData.STATUS_NORMAL);
		List<ElObserveData> elObDList = elObserveDataDao.findList(elObserveData);


		// 当前时间
		Date currentDate = new Date();

		/*--------------------------------- 添加解算记录（status字段为2，即为停用） ---------------------------------*/
		// 将之前的数据解算和结果评定记录（状态正常的）的 status 字段设为 2（停用）
		// 数据解算模块
		// 查找导线为lineId且status为0的数据解算记录
		ElDataSolution elds_ = new ElDataSolution();
		elds_.setLine(new ELLine(lineId));
		elds_.setStatus(ElDataSolution.STATUS_NORMAL);
		List<ElDataSolution> dsList_ = findList(elds_);
		// 将status设为2，即为停用
		for (ElDataSolution ds: dsList_) {
			ds.setStatus(ElDataSolution.STATUS_DISABLE);
			updateStatus(ds);
		}
		// 结果评定模块
		// 查找导线为lineId且status为0的结果评定记录
		ElResultEvaluation elRE = new ElResultEvaluation();
		elRE.setLineId(lineId);
		elRE.setStatus(ElResultEvaluation.STATUS_NORMAL);
		List<ElResultEvaluation> reList = elResEvalService.findList(elRE);
		// 将status设为2
		for (ElResultEvaluation re : reList) {
			re.setStatus(ElResultEvaluation.STATUS_DISABLE);
			elResEvalService.updateStatus(re);
		}

		// 将当前的观测数据拷贝为用于解算的观测数据历史记录
		List<ElObserveData> obdList = new ArrayList<>();
		try {
			obdList = deepCopy(elObDList);
		} catch (Exception e) {
			e.printStackTrace();
			// 抛出异常，使得事务回滚
			throw new RuntimeException(e);
		}
		for (ElObserveData obd : obdList) {
			obd.setId(null);		//强行生成新纪录（无id插入会生成新纪录）
			obd.setStatus(ObservationData.STATUS_DISABLE);
			obd.setSolutionDate(currentDate);
			elObserveDataDao.insert(obd);
		}


		//存放elDataSolution列表
		List<ElDataSolution> eldsList = new ArrayList<>();



		/*--------------------------------- 各测回平均值（仪器高、镜高、垂直角、平距）计算 ---------------------------------*/
		//对elObDList按测站id进行升序排序
		elObDList.sort((od1, od2) -> {
			long stationId = Long.parseLong(od1.getStationId());
			long stationId1 = Long.parseLong(od2.getStationId());
			long diff = stationId - stationId1;
			if (diff > 0) {
				return 1;
			} else if (diff < 0) {
				return -1;
			}
			return 0;    // 相等为0
		});
		//上一个测站的id
		String lastStationId = "0";
		ElDataSolution elds;
		for (ElObserveData elobd : elObDList) {
			//只要遇到新的测站就新建一条数据解算记录
			if (!elobd.getStationId().equals(lastStationId)) {
				// new一个ELDataSolution对象
				elds = new ElDataSolution();
				// 给该ELDataSolution对象设好基本信息
				elds.setGroup(elobd.getGroup());
				elds.setLine(elobd.getLine());
				elds.setStationId(elobd.getStationId());
				elds.setForeStnId(elobd.getForeStnId());
				elds.setBackStnId(elobd.getBackStnId());
				//时间
				elds.setCreateDate(currentDate);
				elds.setUpdateDate(currentDate);
				elds.setSolutionDate(currentDate);
				//备注
				elds.setRemarks(remarks);

				// 计算平均仪器高、镜高、垂直角
				ElObserveData obd1 = new ElObserveData();
				obd1.setStationId(elds.getStationId());
				obd1.setStatus(ElObserveData.STATUS_NORMAL);
				List<ElObserveData> obd1List = elObserveDataDao.findList(obd1);
				double sumHIF = 0;	// 总的仪器高（观测前站）
				double sumHLF = 0;	// 总的镜高（观测前站）
				double sumHIB = 0;	// 总的仪器高（观测后站）
				double sumHLB = 0;	// 总的镜高（观测后站）
				double sumVARF = 0;	// 总的垂直角弧度（与前站）
				double sumVARB = 0;	// 总的垂直角弧度（与后站）
				int num = 0;
				for (ElObserveData obd : obd1List) {
					// 获取仪器高、镜高
					// （观测前站）
					sumHIF += obd.getHeightInstrumentFore();
					sumHLF += obd.getHeightLensFore();
					// （观测后站）
					sumHIB += obd.getHeightInstrumentBack();
					sumHLB += obd.getHeightLensBack();
					// 获取垂直角
					// （与前站）
					Integer verticalAngSignFore = obd.getVerticalAngSignFore();
					Integer verticalAngDegFore = obd.getVerticalAngDegFore();
					Integer verticalAngMinFore = obd.getVerticalAngMinFore();
					Double verticalAngSecFore = obd.getVerticalAngSecFore();
					double varf = AngleUtils.angle2radian(verticalAngDegFore, verticalAngMinFore, verticalAngSecFore);
					sumVARF += (verticalAngSignFore * varf);
					// （与后站）
					Integer verticalAngSignBack = obd.getVerticalAngSignBack();
					Integer verticalAngDegBack = obd.getVerticalAngDegBack();
					Integer verticalAngMinBack = obd.getVerticalAngMinBack();
					Double verticalAngSecBack = obd.getVerticalAngSecBack();
					double varb = AngleUtils.angle2radian(verticalAngDegBack, verticalAngMinBack, verticalAngSecBack);
					sumVARB += (verticalAngSignBack * varb);

					num++;
				}
				double heightInstrumentForeAvg = sumHIF / num;
				double heightLensForeAvg = sumHLF / num;
				double heightInstrumentBackAvg = sumHIB / num;
				double heightLensBackAvg = sumHLB / num;
				double verticalAngRadianFore = sumVARF / num;
				double verticalAngRadianBack = sumVARB / num;
				elds.setHeightInstrumentForeAvg(heightInstrumentForeAvg);		// 平均仪器高（观测前站）
				elds.setHeightLensForeAvg(heightLensForeAvg);					// 平均镜高（观测前站）
				elds.setHeightInstrumentBackAvg(heightInstrumentBackAvg);		// 平均仪器高（观测后站）
				elds.setHeightLensBackAvg(heightLensBackAvg);					// 平均镜高（观测后站）
				elds.setVerticalAngSignFore(verticalAngRadianFore < 0 ? -1 : 1);	// 平均垂直角的符号（与前站）
				elds.setVerticalAngRadianFore(verticalAngRadianFore);		// 平均垂直角弧度（与前站）
				elds.setVerticalAngSignBack(verticalAngRadianBack < 0 ? -1 : 1);	// 平均垂直角的符号（与后站）
				elds.setVerticalAngRadianBack(verticalAngRadianBack);		// 平均垂直角弧度（与后站）
				double[] verticalAngFore = AngleUtils.radian2angle(Math.abs(verticalAngRadianFore));
				elds.setVerticalAngDegForeAvg((int) verticalAngFore[0]);	// 平均垂直角-度（与前站）
				elds.setVerticalAngMinForeAvg((int) verticalAngFore[1]);	// 平均垂直角-分（与前站）
				elds.setVerticalAngSecForeAvg(verticalAngFore[2]);			// 平均垂直角-秒（与前站）
				double[] verticalAngBack = AngleUtils.radian2angle(Math.abs(verticalAngRadianBack));
				elds.setVerticalAngDegBackAvg((int) verticalAngBack[0]);	// 平均垂直角-度（与后站）
				elds.setVerticalAngMinBackAvg((int) verticalAngBack[1]);	// 平均垂直角-分（与后站）
				elds.setVerticalAngSecBackAvg(verticalAngBack[2]);			// 平均垂直角-秒（与后站）

				// 计算平均平距（与前站）
				if ("0".equals(elds.getForeStnId())) {	// 终点测站 没有 与前站的平距
					elds.setDistanceForeAvg(0.);
				} else {
					// A-B 平距
					double sum1 = 0;
					int num1 = 0;
					for (ElObserveData obd : obd1List) {
						sum1 += obd.getDistanceFore();
						num1++;
					}
					double distanceAvg1 = sum1 / num1;
					// B-A 平距
					ElObserveData obd2 = new ElObserveData();
					obd2.setStationId(elds.getForeStnId());
					obd2.setStatus(ElObserveData.STATUS_NORMAL);
					List<ElObserveData> od2list = elObserveDataDao.findList(obd2);
					double sum2 = 0;
					int num2 = 0;
					for (ElObserveData obd : od2list) {
						sum2 += obd.getDistanceBack();
						num2++;
					}
					double distanceAvg2 = sum2 / num2;
					double distanceForeAvg = (distanceAvg1 + distanceAvg2) / 2;
					// 求平均
					elds.setDistanceForeAvg(distanceForeAvg);
				}

				//将elds添加到eldsList
				eldsList.add(elds);

				//更新上一个测站的id
				lastStationId = elobd.getStationId();
			}
		}

		/*--------------------------------- 数据入库 ---------------------------------*/
		//先入库，后面取出来继续算
		for (ElDataSolution elds2 : eldsList) {
			super.save(elds2);
		}



		/*--------------------------------- 高差计算 ---------------------------------*/
		// 获取起始测站和终点测站的数据
		ElOriginData elOriginData_ = new ElOriginData();
		elOriginData_.setGroup(new Group(groupId));
		elOriginData_.setLine(new ELLine(lineId));
		elOriginData_.setStatus(ElOriginData.STATUS_NORMAL);
		ElOriginData elOriginData = elOriginDataDao.getByEntity(elOriginData_);
		String originStationId = elOriginData.getOriginStationId();		// 起始测站ID
		Double originElevation = elOriginData.getOriginElevation();		// 起始测站高程
		String endStationId = elOriginData.getEndStationId();			// 终点测站ID
		Double endElevation = elOriginData.getEndElevation();			// 终点测站高程

		String currentStationId = originStationId;	// 当前测站ID（从起始测站开始算起）
		int stationNum = eldsList.size();			// 测站个数
		eldsList.clear();							// 清空eldsList
		double sumElevationDiffFore = 0;			// 高差和（前视）
		double sumElevationDiffBack = 0;			// 高差和（后视）
		double sumDS = 0;							// 平距和
		for (int i = 0; i < stationNum; i++) {
			ElDataSolution elds1 = new ElDataSolution();
			elds1.setGroup(new Group(groupId));
			elds1.setLine(new ELLine(lineId));
			elds1.setStationId(currentStationId);
			elds1.setStatus(ElDataSolution.STATUS_NORMAL);
			// 当前测站的数据解算对象
			ElDataSolution celds = elDataSolutionDao.getByEntity(elds1);
			// 计算 高差h(当站->前站)
			if (!"0".equals(celds.getForeStnId())) {
				Double distanceForeAvg = celds.getDistanceForeAvg();                // 平距
				Double verticalAngRadianFore = celds.getVerticalAngRadianFore();    // 垂直角弧度
				Double heightInstrumentForeAvg = celds.getHeightInstrumentForeAvg();	// 仪器高
				Double heightLensForeAvg = celds.getHeightLensForeAvg();              // 镜高
				double h_cf = distanceForeAvg * Math.tan(verticalAngRadianFore)
						+ heightInstrumentForeAvg - heightLensForeAvg + C * Math.pow(distanceForeAvg, 2);
				celds.setElevationDiffFore(h_cf);									// 设置高差
			} else {
				celds.setElevationDiffFore(0.);
			}
			// 计算 高差h(当站->后站)
			if (!"0".equals(celds.getBackStnId())) {
				ElDataSolution elds2 = new ElDataSolution();
				elds2.setGroup(new Group(groupId));
				elds2.setLine(new ELLine(lineId));
				elds2.setStationId(celds.getBackStnId());
				elds2.setStatus(ElDataSolution.STATUS_NORMAL);
				// 当前测站的后视站点的数据解算对象
				ElDataSolution belds = elDataSolutionDao.getByEntity(elds2);
				Double distanceBackAvg = belds.getDistanceForeAvg();				// 平距
				Double verticalAngRadianBack = celds.getVerticalAngRadianBack();	// 垂直角弧度
				Double heightInstrumentBackAvg = celds.getHeightInstrumentBackAvg();	// 仪器高
				Double heightLensBackAvg = celds.getHeightLensBackAvg();				// 镜高
				double h_cb = distanceBackAvg * Math.tan(verticalAngRadianBack)
						+ heightInstrumentBackAvg - heightLensBackAvg + C * Math.pow(distanceBackAvg, 2);
				celds.setElevationDiffBack(h_cb);									// 设置高差
			} else {
				celds.setElevationDiffBack(0.);
			}

			sumElevationDiffFore += celds.getElevationDiffFore();	// 高差和（前视）
			sumElevationDiffBack += celds.getElevationDiffBack();	// 高差和（后视）
			sumDS += celds.getDistanceForeAvg();				// 平距和

			eldsList.add(celds);

			currentStationId = celds.getForeStnId();	// 更新当前测站的ID为前视测站的ID
		}



		/*--------------------------------- 改正后的高差计算，并且数据入库 ---------------------------------*/
		double sumElevationDiffFore_target = endElevation - originElevation;	// 起点——终点的高差
		double sumElevationDiffBack_target = originElevation - endElevation;	// 终点——起点的高差
		// 导线闭合差
		double closErrLineFore = sumElevationDiffFore - sumElevationDiffFore_target;
		double closErrLineBack = sumElevationDiffBack - sumElevationDiffBack_target;
		// 计算改正后的高差，入库，后面取出来继续算
		for (ElDataSolution elds2 : eldsList) {
			// 改正后的高差（前视）
			if (!"0".equals(elds2.getForeStnId())) {
				double elevationDiffForeCor = elds2.getElevationDiffFore()
						- closErrLineFore * elds2.getDistanceForeAvg() / sumDS;
				elds2.setElevationDiffForeCor(elevationDiffForeCor);
			} else {
				elds2.setElevationDiffForeCor(0.);
			}
			// 改正后的高差（后视）
			if (!"0".equals(elds2.getBackStnId())) {
				ElDataSolution _elds = new ElDataSolution();
				_elds.setGroup(new Group(groupId));
				_elds.setLine(new ELLine(lineId));
				_elds.setStationId(elds2.getBackStnId());
				_elds.setStatus(ElDataSolution.STATUS_NORMAL);
				ElDataSolution belds = elDataSolutionDao.getByEntity(_elds);	// 当前测站的后视站点的数据解算对象
				Double distanceBackAvg = belds.getDistanceForeAvg();			// 平距（后视）
				double elevationDiffBackCor = elds2.getElevationDiffBack()
						- closErrLineBack * distanceBackAvg / sumDS;
				elds2.setElevationDiffBackCor(elevationDiffBackCor);
			} else {
				elds2.setElevationDiffBackCor(0.);
			}

			super.save(elds2);
		}



		/*--------------------------------- 高程计算（往测） ---------------------------------*/
		currentStationId = originStationId;			// 当前测站ID（从起始测站开始算起）
		double lastElevation = originElevation;		// 上一测站高程
		double lastElevationDiff = 0;				// 上一测站->当站的高差
		eldsList.clear();							// 清空eldsList
		for (int i = 0; i < stationNum; i++) {
			ElDataSolution elds1 = new ElDataSolution();
			elds1.setGroup(new Group(groupId));
			elds1.setLine(new ELLine(lineId));
			elds1.setStationId(currentStationId);
			elds1.setStatus(ElDataSolution.STATUS_NORMAL);
			// 当前测站的数据解算对象
			ElDataSolution celds = elDataSolutionDao.getByEntity(elds1);

			celds.setElevationFore(lastElevation + lastElevationDiff); // 高程（前视）

			/*--------------------------------- 往返测高差较差(mm)计算 ---------------------------------*/
			if (!"0".equals(celds.getForeStnId())) {
				// 获取 高差h(当站->前站)
				double h_cf = celds.getElevationDiffFore();
				// 获取 高差h(前站->当站)
				ElDataSolution elds2 = new ElDataSolution();
				elds2.setGroup(new Group(groupId));
				elds2.setLine(new ELLine(lineId));
				elds2.setStationId(celds.getForeStnId());
				elds2.setStatus(ElDataSolution.STATUS_NORMAL);
				// 前视测站的数据解算对象
				ElDataSolution felds = elDataSolutionDao.getByEntity(elds2);
				double h_fc = felds.getElevationDiffBack();
				double errElevaDiffRoundTrip = Math.abs(Math.abs(h_cf) - Math.abs(h_fc)) * 1000;    // 毫米
				celds.setErrElevaDiffRoundTripFore(errElevaDiffRoundTrip);	// 设置往返测高差较差
				// 设置标准
				double errElevaDiffRoundTripNorm = 40;	// 往返测高差较差标准(mm)
				double sqrt_D = Math.sqrt(celds.getDistanceForeAvg() / 1000);	// 前视平距的平方根(D的单位为km)
				switch (traverseClass) {
					case "二等":
						errElevaDiffRoundTripNorm = 8 * sqrt_D;
						break;
					case "三等":
						errElevaDiffRoundTripNorm = 24 * sqrt_D;
						break;
					case "四等":
						errElevaDiffRoundTripNorm = 40 * sqrt_D;
						break;
					case "五等":
						errElevaDiffRoundTripNorm = 60 * sqrt_D;
						break;
					default:
				}
				celds.setErrElevaDiffRoundTripForeNorm(errElevaDiffRoundTripNorm);
			} else {
				celds.setErrElevaDiffRoundTripFore(0.);
				celds.setErrElevaDiffRoundTripForeNorm(0.);
			}

			eldsList.add(celds);

			currentStationId = celds.getForeStnId();				// 更新当前测站ID
			lastElevation = celds.getElevationFore();				// 更新上一测站高程
			lastElevationDiff = celds.getElevationDiffForeCor();	// 更新上一测站高差
		}

		/*--------------------------------- 数据入库 ---------------------------------*/
		//先入库，后面取出来继续算
		for (ElDataSolution elds2 : eldsList) {
			super.save(elds2);
		}



		/*--------------------------------- 高程计算（返测）、平均高程计算 ---------------------------------*/
		currentStationId = endStationId;	// 当前测站ID（从终点测站开始算起）
		lastElevation = endElevation;		// 上一测站高程
		lastElevationDiff = 0;				// 上一测站->当站的高差
		eldsList.clear();					// 清空eldsList
		for (int i = 0; i < stationNum; i++) {
			ElDataSolution elds1 = new ElDataSolution();
			elds1.setGroup(new Group(groupId));
			elds1.setLine(new ELLine(lineId));
			elds1.setStationId(currentStationId);
			elds1.setStatus(ElDataSolution.STATUS_NORMAL);
			// 当前测站的数据解算对象
			ElDataSolution celds = elDataSolutionDao.getByEntity(elds1);

			celds.setElevationBack(lastElevation + lastElevationDiff); // 高程（后视）

			double elevation = (celds.getElevationFore() + celds.getElevationBack()) / 2;
			celds.setElevation(elevation);	// 平均高程

			eldsList.add(celds);

			currentStationId = celds.getBackStnId();				// 更新当前测站ID
			lastElevation = celds.getElevationBack();				// 更新上一测站高程
			lastElevationDiff = celds.getElevationDiffBackCor();	// 更新上一测站高差
		}

		/*--------------------------------- 数据入库 ---------------------------------*/
		//先入库，后面取出来继续算
		for (ElDataSolution elds2 : eldsList) {
			super.save(elds2);
		}




		/*--------------------------------------------------------------------------*/
		/*--------------------------------- 结果评定 ---------------------------------*/
		/*--------------------------------------------------------------------------*/
		//new一个ELResultEvaluation对象，设置好相应的信息
		ElResultEvaluation elResEval = new ElResultEvaluation();
		// 设置导线id
		elResEval.setLineId(lineId);
		// 设置导线等级
		elResEval.setTraverseClass(traverseClass);
		// 设置是否考虑球气差（1是 0否）
		elResEval.setIsQQC(isQQC);
		// 设置大气折光系数
		elResEval.setCoefficientK(K);
		// 设置地球半径(km)
		elResEval.setCoefficientR(R);
		// 时间
		elResEval.setCreateDate(currentDate);
		elResEval.setUpdateDate(currentDate);
		elResEval.setSolutionDate(currentDate);
		// 备注信息
		elResEval.setRemarks(remarks);

		/*--------------------------------- 导线闭合差(mm)计算 ---------------------------------*/
		double closErrLine = (Math.abs(closErrLineFore) + Math.abs(closErrLineBack)) / 2 * 1000;	// 毫米
		elResEval.setClosErrLine(closErrLine);

		/*--------------------------------- 每千米高差全中误差(mm)计算 ---------------------------------*/
		double fullMidErrElevaDiffPerKm = Math.sqrt(closErrLine * closErrLine / (sumDS / 1000));	// D总长的单位为km
		elResEval.setFullMidErrElevaDiffPerKm(fullMidErrElevaDiffPerKm);

		/*--------------------------------- 设置标准 ---------------------------------*/
		double fullMidErrElevaDiffPerKmNorm = 999;	// 每千米高差全中误差标准(mm)
		double closErrLineNorm = 999;				// 导线闭合差标准(mm)
		double sqrt_DS = Math.sqrt(sumDS / 1000);	// D总长的单位为km
		switch (traverseClass) {
			case "二等":
				fullMidErrElevaDiffPerKmNorm = 2;
				closErrLineNorm = 4 * sqrt_DS;
				break;
			case "三等":
				fullMidErrElevaDiffPerKmNorm = 6;
				closErrLineNorm = 12 * sqrt_DS;
				break;
			case "四等":
				fullMidErrElevaDiffPerKmNorm = 10;
				closErrLineNorm = 20 * sqrt_DS;
				break;
			case "五等":
				fullMidErrElevaDiffPerKmNorm = 15;
				closErrLineNorm = 30 * sqrt_DS;
				break;
			default:
		}
		elResEval.setFullMidErrElevaDiffPerKmNorm(fullMidErrElevaDiffPerKmNorm);
		elResEval.setClosErrLineNorm(closErrLineNorm);

		/*--------------------------------- 给出评定结果 ---------------------------------*/
		String result = "";    //结果评价
		if (fullMidErrElevaDiffPerKm > fullMidErrElevaDiffPerKmNorm) {
			result += "每千米高差全中误差过大！";
		}
		if (Math.abs(closErrLine) > closErrLineNorm) {
			result += "导线闭合差过大！";
		}
		if ("".equals(result)) {
			result = "√";
		}
		elResEval.setResultEvaluation(result);

		/*--------------------------------- 评定结果入库 ---------------------------------*/
		elResEvalService.save(elResEval);




		/*--------------------------------- 调用一下间接平差，不需要手动解算 ---------------------------------*/
		ElIndirectAdjustment elia = new ElIndirectAdjustment();
		elia.setGroup(new Group(groupId));
		elia.setLine(new ELLine(lineId));
		elia.setTraverseClass(traverseClass);
		elia.setRemarks(remarks);
		elIndirectAdjustmentService.save(elia);


		// 抛出异常，使得事务回滚
		// throw new RuntimeException();
		// System.out.println();
		// super.save(elDataSolution);
	}
	
	/**
	 * 更新状态
	 * @param elDataSolution
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(ElDataSolution elDataSolution) {
		super.updateStatus(elDataSolution);
	}
	
	/**
	 * 删除数据
	 * @param elDataSolution
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(ElDataSolution elDataSolution) {
		super.delete(elDataSolution);
	}

	/**
	 * 添加数据权限过滤条件
	 * 2022年5月23日 su添加
	 */
	@Override
	public void addDataScopeFilter(ElDataSolution elDataSolution) {
		// 举例4：用户、员工（自己创建的）数据权限根据部门过滤，实体类@Table注解extWhereKeys="dsfOffice"
		elDataSolution.getSqlMap().getDataScope().addFilter("dsfOffice", "Office",
				"u1.office_code", "a.create_by", DataScope.CTRL_PERMI_HAVE);
	}

	/**
	 * 深度拷贝list
	 * @param src
	 * @param <>
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static <T> List<T> deepCopy(List<T> src) throws IOException, ClassNotFoundException {
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		ObjectOutputStream out = new ObjectOutputStream(byteOut);
		out.writeObject(src);

		ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
		ObjectInputStream in = new ObjectInputStream(byteIn);
		@SuppressWarnings("unchecked")
		List<T> dest = (List<T>) in.readObject();
		return dest;
	}
	
}