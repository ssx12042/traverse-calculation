/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.data_solution.service;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

import com.jeesite.common.entity.DataScope;
import com.jeesite.modules.group.dao.GroupDao;
import com.jeesite.modules.group.entity.Group;
import com.jeesite.modules.line.entity.Line;
import com.jeesite.modules.observation.entity.ObservationData;
import com.jeesite.modules.observation.service.ObservationDataService;
import com.jeesite.modules.origin.dao.OriginDataDao;
import com.jeesite.modules.origin.entity.OriginData;
import com.jeesite.modules.result_evaluation.entity.ResultEvaluation;
import com.jeesite.modules.result_evaluation.service.ResultEvaluationService;
import com.jeesite.utils.AngleUtils;
import com.jeesite.utils.WordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.data_solution.entity.DataSolution;
import com.jeesite.modules.data_solution.dao.DataSolutionDao;

/**
 * 数据解算Service
 * @author su
 * @version 2022-03-15
 */
@Service
@Transactional(readOnly=true)
public class DataSolutionService extends CrudService<DataSolutionDao, DataSolution> {

	@Autowired
	private ObservationDataService observationDataService;

	@Autowired
	private OriginDataDao originDataDao;

	@Autowired
	private DataSolutionDao dataSolutionDao;

	@Autowired
	private ResultEvaluationService resultEvaluationService;

	@Autowired
	private GroupDao groupDao;

	//@Autowired
	//private SolutionResultService solutionResultService;
	
	/**
	 * 获取单条数据
	 * @param dataSolution
	 * @return
	 */
	@Override
	public DataSolution get(DataSolution dataSolution) {
		return super.get(dataSolution);
	}
	
	/**
	 * 查询分页数据
	 * @param dataSolution 查询条件
	 * @return
	 */
	@Override
	public Page<DataSolution> findPage(DataSolution dataSolution) {
		return super.findPage(dataSolution);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param dataSolution
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(DataSolution dataSolution) {
		//获取小组id
		String groupId = dataSolution.getGroup().getId();
		//获取负责人
		Group g1 = new Group();
		g1.setId(groupId);
		Group g2 = groupDao.getByEntity(g1);
		String principal = g2.getGroupLeader();
		//获取导线id
		String lineId = dataSolution.getLine().getId();
		//获取备注信息
		String remarks = dataSolution.getRemarks();

		//获取该导线的全部观测数据（状态正常的）
		ObservationData observationData = new ObservationData();
		observationData.setGroup(new Group(groupId));
		observationData.setLine(new Line(lineId));
		observationData.setStatus(ObservationData.STATUS_NORMAL);
		List<ObservationData> odList = observationDataService.findList(observationData);


		//当前时间
		Date currentDate = new Date();

		/*--------------------------------- 添加解算记录（status字段为2，即为停用） ---------------------------------*/
		//将之前的数据解算和解算结果记录（状态正常的）的 status 字段设为 2（停用）
		//数据解算模块
		// 查找导线为lineId且status为0的记录
		DataSolution ds_ = new DataSolution();
		ds_.setLine(new Line(lineId));
		ds_.setStatus(DataSolution.STATUS_NORMAL);
		List<DataSolution> dsList_ = findList(ds_);
		//将status置为2，即为停用
		for (DataSolution ds2: dsList_) {
			ds2.setStatus(DataSolution.STATUS_DISABLE);
			updateStatus(ds2);
		}
		// 结果评定模块
		// 查找导线为lineId且status为0的结果评定记录
		ResultEvaluation resEva = new ResultEvaluation();
		resEva.setLineId(lineId);
		resEva.setStatus(ResultEvaluation.STATUS_NORMAL);
		List<ResultEvaluation> srList = resultEvaluationService.findList(resEva);
		//将status置为2
		for (ResultEvaluation re: srList) {
			re.setStatus(ResultEvaluation.STATUS_DISABLE);
			resultEvaluationService.updateStatus(re);
		}

		//将当前的观测数据复刻为用于解算的观测数据记录
		List<ObservationData> odList1 = new ArrayList<>();
		try {
			odList1 = deepCopy(odList);
		} catch (Exception e) {
			e.printStackTrace();
			// 抛出异常，使得事务回滚
			throw new RuntimeException(e);
		}
		for (ObservationData od: odList1) {
			od.setId(null);		//强行生成新纪录（无id插入会生成新纪录）
			od.setStatus(ObservationData.STATUS_DISABLE);
			od.setSolutionDate(currentDate);
			observationDataService.insert(od);
		}


		//存放dataSolution列表
		List<DataSolution> dsList = new ArrayList<>();

		DataSolution ds;
		//ObservationData od;


		/*--------------------------------- 平均平距和各测回平均角值计算 ---------------------------------*/
		int j = 0;	//数据解算记录下标
		//对odList按测站id进行升序排序
		odList.sort((od1, od2) -> {
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
		for (ObservationData od : odList) {
			//od = observationData;
			//只要遇到新的测站就新建一条数据解算记录
			if (!od.getStationId().equals(lastStationId)) {
				ds = new DataSolution();    //new一个DataSolution对象
				//给该DataSolution对象设好基本信息
				ds.setGroup(od.getGroup());
				ds.setLine(od.getLine());
				ds.setStationId(od.getStationId());
				ds.setForeStnId(od.getForeStnId());
				ds.setBackStnId(od.getBackStnId());
				//时间
				ds.setCreateDate(currentDate);
				ds.setUpdateDate(currentDate);
				ds.setSolutionDate(currentDate);
				//备注
				ds.setRemarks(remarks);

				//计算平均平距（前视）
				if ("0".equals(ds.getForeStnId())) {	//附合导线的终点测站无前视平距
					ds.setAvgDistance(0.);
				} else {
					// A-B 平距
					ObservationData od1 = new ObservationData();
					od1.setStationId(ds.getStationId());
					od1.setStatus(ObservationData.STATUS_NORMAL);
					List<ObservationData> od1list = observationDataService.findList(od1);
					double sum1 = 0;
					int num1 = 0;
					for (ObservationData od_ : od1list) {
						sum1 += od_.getForeDistance();
						num1++;
					}
					double avgDistance1 = sum1 / num1;
					// B-A 平距
					ObservationData od2 = new ObservationData();
					od2.setStationId(ds.getForeStnId());
					od2.setStatus(ObservationData.STATUS_NORMAL);
					List<ObservationData> od2list = observationDataService.findList(od2);
					double sum2 = 0;
					int num2 = 0;
					for (ObservationData od_ : od2list) {
						sum2 += od_.getBackDistance();
						num2++;
					}
					double avgDistance2 = sum2 / num2;
					// 求平均
					ds.setAvgDistance((avgDistance1 + avgDistance2) / 2);
				}

				//计算各测回平均角值
				ObservationData od3 = new ObservationData();
				od3.setStationId(ds.getStationId());
				od3.setStatus(ObservationData.STATUS_NORMAL);
				List<ObservationData> od3list = observationDataService.findList(od3);
				double sum3 = 0;
				int num3 = 0;
				for (ObservationData od_ : od3list) {
					sum3 += od_.getTurningAngRadian();
					num3++;
				}
				double turningAngRadian = sum3 / num3;	//各测回平均的转角弧度

				// 根据转折角弧度获取度、分、秒
				double[] azimuthAng = AngleUtils.radian2angle(turningAngRadian);
				ds.setAvgValueDeg((int) azimuthAng[0]);
				ds.setAvgValueMin((int) azimuthAng[1]);
				ds.setAvgValueSec(azimuthAng[2]);

				/*double turningAng = Math.toDegrees(turningAngRadian);    //转换为度数
				int turningAngDeg = (int) Math.floor(turningAng);    //度
				ds.setAvgValueDeg(turningAngDeg);
				double _turningAngMin = (turningAng - turningAngDeg) * 60;    //未取整分
				int turningAngMin = (int) Math.floor(_turningAngMin);    //分
				ds.setAvgValueMin(turningAngMin);
				double _turningAngSec = (_turningAngMin - turningAngMin) * 60;    //未取整秒
				//int turningAngSec = (int) Math.round(_turningAngSec);    //秒
				//2021年12月19日
				double turningAngSec = Double.parseDouble(String.format("%.1f", _turningAngSec));    //秒
				ds.setAvgValueSec(turningAngSec);*/

				// 转折角弧度计算
				double turningAngRadian2 = Math.toRadians((double) ds.getAvgValueDeg()
						+ (double) ds.getAvgValueMin() / 60 + (double) ds.getAvgValueSec() / 3600);
				ds.setTurningAngRadian(turningAngRadian2);

				//将DataSolution2对象添加到dataSolution列表
				dsList.add(ds);

				//更新上一个测站的id
				lastStationId = od.getStationId();
			}
		}



		/*--------------------------------- 数据入库 ---------------------------------*/
		//先入库，后面取出来继续算
		for (DataSolution ds2 : dsList) {
			super.save(ds2);
		}



		/*--------------------------------- 改正的转折角弧度计算 ---------------------------------*/
		//先获取起始测站和终点测站的方位角和坐标
		//OriginData originData = originDataDao.findOriginDataByGLId(groupId, lineId);
		OriginData originData_ = new OriginData();
		originData_.setGroup(new Group(groupId));
		originData_.setLine(new Line(lineId));
		originData_.setStatus(OriginData.STATUS_NORMAL);
		OriginData originData = originDataDao.getByEntity(originData_);
		String originStationId = originData.getOriginStationId();		//起始测站ID
		Integer originAngDeg = originData.getOriginAzimuthDeg();	//起始方位角度
		Integer originAngMin = originData.getOriginAzimuthMin();	//起始方位角分
		Double originAngSec = originData.getOriginAzimuthSec();		//起始方位角秒
		Double originCoordX = originData.getOriginCoordX();		//起始坐标X
		Double originCoordY = originData.getOriginCoordY();		//起始坐标Y
		double originAzimuthAngRadian = Math.toRadians((double) originAngDeg
				+ (double) originAngMin / 60 + (double) originAngSec / 3600);	//起始方位角弧度
		String endStationId = originData.getEndStationId();			//终点测站ID
		Integer endAngDeg = originData.getEndAzimuthDeg();			//终点方位角度
		Integer endAngMin = originData.getEndAzimuthMin();			//终点方位角分
		Double endAngSec = originData.getEndAzimuthSec();			//终点方位角秒
		Double endCoordX = originData.getEndCoordX();			//终点坐标X
		Double endCoordY = originData.getEndCoordY();			//终点坐标Y
		double endAzimuthAngRadian = Math.toRadians((double) endAngDeg
				+ (double) endAngMin / 60 + (double) endAngSec / 3600);		//终点方位角弧度
		// 起点-终点的坐标增量X
		double sumICX_target = endCoordX - originCoordX;
		// 起点-终点的坐标增量Y
		double sumICY_target = endCoordY - originCoordY;
		// 统计转折角个数
		int count = dsList.size();

		// 计算改正的转折角弧度
		lastStationId = "0";								//上一测站ID（起始测站无上一测站）
		double lastAzimuthAngRadian = originAzimuthAngRadian;	//上一测站方位角弧度
		int stationNum = dsList.size();							//测站个数
		dsList.clear();											//清空dsList
		double endAzimuthAngRadian_cal = 0;						//通过计算得到的终点方位角弧度
		for (int i = 0; i < stationNum; i++) {
			//DataSolution _ds = getDataSolution2ByBackStnId(groupId, lineId, lastStationId);    //取出 后视站为lastStationId 的数据解算记录
			//取出 后视站为lastStationId 的数据解算记录
			DataSolution ds1 = new DataSolution();
			ds1.setGroup(new Group(groupId));
			ds1.setLine(new Line(lineId));
			ds1.setBackStnId(lastStationId);
			ds1.setStatus(DataSolution.STATUS_NORMAL);
			DataSolution _ds = dataSolutionDao.getByEntity(ds1);

			//------------------- 计算当前测站的方位角
			if (!"0".equals(lastStationId)) {		// 如果不是起始方位角，则需要转向
				if (lastAzimuthAngRadian >= Math.PI) {
					lastAzimuthAngRadian -= Math.PI;
				} else {
					lastAzimuthAngRadian += Math.PI;
				}
			}
			double azimuthAngRadian = lastAzimuthAngRadian + _ds.getTurningAngRadian();	//当前测站方位角弧度
			if (azimuthAngRadian >= 2 * Math.PI) {
				azimuthAngRadian -= 2 * Math.PI;
			}
			endAzimuthAngRadian_cal = azimuthAngRadian;			//for循环结束即是通过计算得到的终点方位角弧度

			dsList.add(_ds);

			lastStationId = _ds.getStationId();				//更新上一测站ID
			lastAzimuthAngRadian = azimuthAngRadian;	//更新上一测站方位角弧度
		}
		//闭合差计算
		// 角度闭合差计算（弧度），用算出的终点方位角 减去 给定的终点的方位角
		double angcloerr = endAzimuthAngRadian_cal - endAzimuthAngRadian;
		// 每个转折角的角度闭合差计算（弧度）
		//double angcloerr_every = (endAzimuthAngRadian_cal - endAzimuthAngRadian) / count;
		double angcloerr_every = angcloerr / count;
		// 改正的转折角弧度计算
		for (DataSolution ds2 : dsList) {
			ds2.setCorAngRadian(ds2.getTurningAngRadian() - angcloerr_every);
		}



		/*--------------------------------- 数据入库 ---------------------------------*/
		//先入库，后面取出来继续算
		for (DataSolution ds2 : dsList) {
			super.save(ds2);
		}



		/*--------------------------------- 方位角弧度计算 ---------------------------------*/
		//计算各测站的方位角
		lastStationId = "0";							//上一测站ID，起始测站的上一测站ID为0，即无
		lastAzimuthAngRadian = originAzimuthAngRadian;	//上一测站方位角弧度
		//stationNum = dsList.size();							//测站个数
		dsList.clear();											//清空dsList
		double sumICX = 0;										//坐标增量X求和
		double sumICY = 0;										//坐标增量Y求和
		double sumDS = 0;										//边长求和
		for (int i = 0; i < stationNum; i++) {
			//DataSolution _ds = getDataSolution2ByBackStnId(groupId, lineId, lastStationId);    //取出 后视站为lastStationId 的数据解算记录
			//取出 后视站为lastStationId 的数据解算记录
			DataSolution ds1 = new DataSolution();
			ds1.setGroup(new Group(groupId));
			ds1.setLine(new Line(lineId));
			ds1.setBackStnId(lastStationId);
			ds1.setStatus(DataSolution.STATUS_NORMAL);
			DataSolution _ds = dataSolutionDao.getByEntity(ds1);

			//------------------- 计算当前测站的方位角
			//2021年12月7日19:38:55
			if (!"0".equals(lastStationId)) {		// 如果不是起始方位角，则需要转向
				if (lastAzimuthAngRadian >= Math.PI) {
					lastAzimuthAngRadian -= Math.PI;
				} else {
					lastAzimuthAngRadian += Math.PI;
				}
			}
			double azimuthAngRadian = lastAzimuthAngRadian + _ds.getCorAngRadian();	//当前测站方位角弧度
			if (azimuthAngRadian >= 2 * Math.PI) {
				azimuthAngRadian -= 2 * Math.PI;
			}
			//徐欢的代码
			/*if (lastAzimuthAngRadian + Math.PI - _ds.getCorAngRadian() < 0) {
				azimuthAngRadian = lastAzimuthAngRadian + 3 * Math.PI - _ds.getCorAngRadian();
			} else if (lastAzimuthAngRadian + Math.PI - _ds.getCorAngRadian() > 2 * Math.PI) {
				azimuthAngRadian = lastAzimuthAngRadian - Math.PI - _ds.getCorAngRadian();
			} else {
				azimuthAngRadian = lastAzimuthAngRadian + Math.PI - _ds.getCorAngRadian();
			}*/
			_ds.setAzimuthAngRadian(azimuthAngRadian);
			// 根据方位角弧度获取度、分、秒
			double[] azimuthAng = AngleUtils.radian2angle(azimuthAngRadian);
			_ds.setAzimuthAngDeg((int) azimuthAng[0]);
			_ds.setAzimuthAngMin((int) azimuthAng[1]);
			_ds.setAzimuthAngSec(azimuthAng[2]);


			/*double azimuthAng = Math.toDegrees(azimuthAngRadian);    //转换为度数
			int azimuthAngDeg = (int) azimuthAng;    //度
			_ds.setAzimuthAngDeg(azimuthAngDeg);
			double _azimuthAngMin = (azimuthAng - azimuthAngDeg) * 60;    //未取整分
			int azimuthAngMin = (int) _azimuthAngMin;    //分
			_ds.setAzimuthAngMin(azimuthAngMin);
			double _azimuthAngSec = (_azimuthAngMin - azimuthAngMin) * 60;    //未取整秒
			//int azimuthAngSec = (int) Math.round(_azimuthAngSec);    //秒
			//2021年12月19日
			double azimuthAngSec = Double.parseDouble(String.format("%.1f", _azimuthAngSec));    //秒
			_ds.setAzimuthAngSec(azimuthAngSec);*/


			//------------------- 计算当前测站的坐标增量
			// 坐标增量X计算
			double increCoordX = _ds.getAvgDistance() * Math.cos(azimuthAngRadian);
			_ds.setIncreCoordX(increCoordX);
			// 坐标增量Y计算
			double increCoordY = _ds.getAvgDistance() * Math.sin(azimuthAngRadian);
			_ds.setIncreCoordY(increCoordY);

			//求和
			sumICX += increCoordX;			//坐标增量X求和
			sumICY += increCoordY;			//坐标增量Y求和
			sumDS += _ds.getAvgDistance();	//边长求和

			dsList.add(_ds);


			lastStationId = _ds.getStationId();				//更新上一测站ID
			lastAzimuthAngRadian = azimuthAngRadian;	//更新上一测站方位角弧度
		}
		//------------------- 计算各测站改正后的坐标增量
		for (DataSolution ds2 : dsList) {
			// 改正后的坐标增量X计算
			double corIncCoordX = ds2.getIncreCoordX() - (sumICX - sumICX_target) * ds2.getAvgDistance() / sumDS;
			// 改正后的坐标增量Y计算
			double corIncCoordY = ds2.getIncreCoordY() - (sumICY - sumICY_target) * ds2.getAvgDistance() / sumDS;

			ds2.setCorIncCoordX(corIncCoordX);
			ds2.setCorIncCoordY(corIncCoordY);
		}



		/*--------------------------------- 数据入库 ---------------------------------*/
		//先入库，后面取出来继续算
		for (DataSolution ds2 : dsList) {
			super.save(ds2);
		}



		/*--------------------------------- 坐标计算 ---------------------------------*/
		lastStationId = "0";				//上一测站ID，起始测站的上一测站ID为0，即无
		double lastCoordx = originCoordX;	//上一测站的坐标X
		double lastCoordy = originCoordY;	//上一测站的坐标Y
		double lastCorIncCoordX = 0.;		//上一测站的坐标增量
		double lastCorIncCoordY = 0.;		//上一测站的坐标增量
		dsList.clear();						//清空dsList
		for (int i = 0; i < stationNum; i++) {
			//DataSolution _ds = getDataSolution2ByBackStnId(groupId, lineId, lastStationId);    //取出后视站为上一测站的数据解算记录
			//取出 后视站为lastStationId 的数据解算记录
			DataSolution ds1 = new DataSolution();
			ds1.setGroup(new Group(groupId));
			ds1.setLine(new Line(lineId));
			ds1.setBackStnId(lastStationId);
			ds1.setStatus(DataSolution.STATUS_NORMAL);
			DataSolution _ds = dataSolutionDao.getByEntity(ds1);

			if ("0".equals(lastStationId)) {			//附合导线，应从第二个测站开始算坐标
				//起始测站设置好相应坐标
				_ds.setFinalCoordX(originCoordX);
				_ds.setFinalCoordY(originCoordY);

				dsList.add(_ds);

				lastStationId = _ds.getStationId();			//更新上一测站ID
				lastCorIncCoordX = _ds.getCorIncCoordX();	//更新上一测站的坐标增量
				lastCorIncCoordY = _ds.getCorIncCoordY();	//更新上一测站的坐标增量
				continue;
			}

			double finalCoordX = lastCoordx + lastCorIncCoordX;	//当前测站结果坐标X
			double finalCoordY = lastCoordy + lastCorIncCoordY;	//当前测站结果坐标Y

			_ds.setFinalCoordX(finalCoordX);
			_ds.setFinalCoordY(finalCoordY);

			dsList.add(_ds);

			lastStationId = _ds.getStationId();			//更新上一测站ID
			lastCoordx = finalCoordX;					//更新上一测站的坐标X
			lastCoordy = finalCoordY;					//更新上一测站的坐标Y
			lastCorIncCoordX = _ds.getCorIncCoordX();	//更新上一测站的坐标增量
			lastCorIncCoordY = _ds.getCorIncCoordY();	//更新上一测站的坐标增量
		}



		/*--------------------------------- 数据入库 ---------------------------------*/
		for (DataSolution ds2 : dsList) {
			super.save(ds2);
		}



		/*--------------------------------------------------------------------------*/
		/*--------------------------------- 结果评定 ---------------------------------*/
		/*--------------------------------------------------------------------------*/
		//new一个ResultEvaluation对象，设置好相应的信息
		ResultEvaluation resultEvaluation = new ResultEvaluation();
		resultEvaluation.setGroup(new Group(groupId));
		resultEvaluation.setLineId(lineId);
		//时间
		resultEvaluation.setCreateDate(currentDate);
		resultEvaluation.setUpdateDate(currentDate);
		resultEvaluation.setSolutionDate(currentDate);
		//备注信息
		resultEvaluation.setRemarks(remarks);


		/*--------------------------------- 角度闭合差计算 ---------------------------------*/
		//角度闭合差计算（弧度）
		angcloerr = angcloerr * (180 / Math.PI) * 60 * 60;    //转换为秒
		resultEvaluation.setAngleClosureError(angcloerr);


		/*--------------------------------- 坐标增量闭合差X ---------------------------------*/
		double closErrOfCoordincrex = sumICX - sumICX_target;
		resultEvaluation.setClosErrOfCoordincrex(closErrOfCoordincrex * 1000);	// 转成毫米级


		/*--------------------------------- 坐标增量闭合差Y ---------------------------------*/
		double closErrOfCoordincrey = sumICY - sumICY_target;
		resultEvaluation.setClosErrOfCoordincrey(closErrOfCoordincrey * 1000);	// 转成毫米级


		/*--------------------------------- 导线全长闭合差 ---------------------------------*/
		double a = Math.pow(resultEvaluation.getClosErrOfCoordincrex(), 2)
				+ Math.pow(resultEvaluation.getClosErrOfCoordincrey(), 2);
		// 由于坐标增量闭合差已经是毫米级，所以导线全长闭合差也是毫米级
		resultEvaluation.setTotalLengthClosError(Math.sqrt(a));


		/*--------------------------------- 导线相对闭合差 ---------------------------------*/
		// 因为导线全长闭合差是毫米级，所以sumDS也要转换为毫米级
		resultEvaluation.setRelaClosError(resultEvaluation.getTotalLengthClosError() / (sumDS * 1000));


		/*--------------------------------- 结果评定 ---------------------------------*/
		String traverseClass = dataSolution.getTraverseClass();    //导线等级
		resultEvaluation.setTraverseClass(traverseClass);                            //设置导线等级
		double angcloerrStandard = 30;        //角度闭合差标准(秒)
		double relaClosErrorStandard = 2000;    //导线相对闭合差标准
		switch (traverseClass) {
			case "三等":
				angcloerrStandard = 3.6 * Math.sqrt(count);
				relaClosErrorStandard = 55000;
				break;
			case "四等":
				angcloerrStandard = 5 * Math.sqrt(count);
				relaClosErrorStandard = 35000;
				break;
			case "一级":
				angcloerrStandard = 10 * Math.sqrt(count);
				relaClosErrorStandard = 15000;
				break;
			case "二级":
				angcloerrStandard = 16 * Math.sqrt(count);
				relaClosErrorStandard = 10000;
				break;
			case "三级":
				angcloerrStandard = 24 * Math.sqrt(count);
				relaClosErrorStandard = 5000;
				break;
			default:
		}
		String result = "";    //结果评价
		if (Math.abs(resultEvaluation.getAngleClosureError()) > angcloerrStandard) {
			result += "角度闭合差过大！";
		}
		double v = 1 / resultEvaluation.getRelaClosError();
		if (v < relaClosErrorStandard) {
			result += "导线相对闭合差比例过大！";
		}
		if ("".equals(result)) {
			result = "√";
		}
		resultEvaluation.setResultEvaluation(result);

		resultEvaluationService.save(resultEvaluation);



		/*--------------------------------- 生成报告 ---------------------------------*/
		/*SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Map<String, Object> data = new HashMap<>();
		data.put("date", dateFormat.format(currentDate));
		data.put("week", "周二");
		data.put("group_name", dsList.get(0).getGroup().getGroupName());
		data.put("principal", principal);
		data.put("line_name", dsList.get(0).getLine().getLineName());
		data.put("start_data", originData);
		data.put("observation_data", principal);
		data.put("data_solution", principal);
		data.put("result_evaluation", principal);
		// 输入到word
		WordUtils.createWord(data, WordUtils.XML, WordUtils.DEST);*/


		//super.save(dataSolution);
	}
	
	/**
	 * 更新状态
	 * @param dataSolution
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(DataSolution dataSolution) {
		super.updateStatus(dataSolution);
	}
	
	/**
	 * 删除数据
	 * @param dataSolution
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(DataSolution dataSolution) {
		super.delete(dataSolution);
	}

	/**
	 * 添加数据权限过滤条件
	 * 2022年3月15日 su添加
	 */
	@Override
	public void addDataScopeFilter(DataSolution dataSolution) {
		// 举例4：用户、员工（自己创建的）数据权限根据部门过滤，实体类@Table注解extWhereKeys="dsfOffice"
		dataSolution.getSqlMap().getDataScope().addFilter("dsfOffice", "Office",
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