/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.indirect_adjustment.service;

import java.util.*;

import Jama.Matrix;
import com.jeesite.common.entity.DataScope;
import com.jeesite.modules.data_solution.dao.DataSolutionDao;
import com.jeesite.modules.data_solution.entity.DataSolution;
import com.jeesite.modules.data_solution.service.DataSolutionService;
import com.jeesite.modules.group.entity.Group;
import com.jeesite.modules.line.entity.Line;
import com.jeesite.modules.origin.dao.OriginDataDao;
import com.jeesite.modules.origin.entity.OriginData;
import com.jeesite.modules.result_evaluation_strict.entity.ResultEvaluationStrict;
import com.jeesite.modules.result_evaluation_strict.service.ResultEvaluationStrictService;
import com.jeesite.utils.AngleUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.indirect_adjustment.entity.IndirectAdjustment;
import com.jeesite.modules.indirect_adjustment.dao.IndirectAdjustmentDao;

/**
 * 间接平差Service
 * @author su
 * @version 2022-04-06
 */
@Service
@Transactional(readOnly=true)
public class IndirectAdjustmentService extends CrudService<IndirectAdjustmentDao, IndirectAdjustment> {

	@Autowired
	private DataSolutionService dataSolutionService;

	@Autowired
	private OriginDataDao originDataDao;

	@Autowired
	private DataSolutionDao dataSolutionDao;

	@Autowired
	private IndirectAdjustmentDao indirectAdjustmentDao;

	@Autowired
	private ResultEvaluationStrictService resEvaStrictService;
	
	/**
	 * 获取单条数据
	 * @param indirectAdjustment
	 * @return
	 */
	@Override
	public IndirectAdjustment get(IndirectAdjustment indirectAdjustment) {
		return super.get(indirectAdjustment);
	}
	
	/**
	 * 查询分页数据
	 * @param indirectAdjustment 查询条件
	 * @return
	 */
	@Override
	public Page<IndirectAdjustment> findPage(IndirectAdjustment indirectAdjustment) {
		return super.findPage(indirectAdjustment);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param indirectAdjustment
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(IndirectAdjustment indirectAdjustment) {
		//获取小组id
		String groupId = indirectAdjustment.getGroup().getId();
		//获取导线id
		String lineId = indirectAdjustment.getLine().getId();
		//获取导线测量等级
		String traverseClass = indirectAdjustment.getTraverseClass();
		//获取备注信息
		String remarks = indirectAdjustment.getRemarks();

		double r = 180 / Math.PI * 60 * 60;

		double sigma_0 = 2.0;	// 测角中误差
		// 距离观测精度
		double accuracy_a = 2.0;
		double accuracy_b = 2.0;



		/*--------------------------------- 将上一次的间接平差保存为历史记录 ---------------------------------*/
		// 查找导线为lineId且status为0的严密平差记录
		IndirectAdjustment ia_ = new IndirectAdjustment();
		ia_.setLine(new Line(lineId));
		ia_.setStatus(IndirectAdjustment.STATUS_NORMAL);
		List<IndirectAdjustment> iaList_ = findList(ia_);
		// 将status置为2，即为停用
		for (IndirectAdjustment ia2 : iaList_) {
			ia2.setStatus(IndirectAdjustment.STATUS_DISABLE);
			updateStatus(ia2);
		}



		//获取起始数据
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
		double originAzimuthArcSec = originAzimuthAngRadian * 60 * 60;			//起始方位角弧秒
		String endStationId = originData.getEndStationId();			//终点测站ID
		Integer endAngDeg = originData.getEndAzimuthDeg();			//终点方位角度
		Integer endAngMin = originData.getEndAzimuthMin();			//终点方位角分
		Double endAngSec = originData.getEndAzimuthSec();			//终点方位角秒
		Double endCoordX = originData.getEndCoordX();			//终点坐标X
		Double endCoordY = originData.getEndCoordY();			//终点坐标Y
		double endAzimuthAngRadian = Math.toRadians((double) endAngDeg
				+ (double) endAngMin / 60 + (double) endAngSec / 3600);		//终点方位角弧度

		// 取出数据解算的记录
		DataSolution dataSolution = new DataSolution();
		dataSolution.setGroup(new Group(groupId));
		dataSolution.setLine(new Line(lineId));
		dataSolution.setStatus(DataSolution.STATUS_NORMAL);
		List<DataSolution> dsList = dataSolutionService.findList(dataSolution);

		//List<List> matrix_B = new ArrayList<>();
		//List<Double> matrix_L = new ArrayList<>();
		// 一维：4个角+3条边。二维：4个未知数（2个测站的x、y坐标）
		int dim_x = 2 * dsList.size() - 1;
		int dim_y = (dsList.size() - 2) * 2;
		double[][] matrix_B = new double[dim_x][dim_y];
		double[][] matrix_L = new double[dim_x][1];
		double[][] matrix_P = new double[dim_x][dim_x];

		// 存放IndirectAdjustment列表
		List<IndirectAdjustment> iaList = new ArrayList<>();



		/*--------------------------------- 求角度测量误差方程的系数 ---------------------------------*/
		for (int i = 0; i < dsList.size(); i++) {
			// 当前测站解算记录
			DataSolution ds = dsList.get(i);

			// 设置IndirectAdjustment的基本数据
			IndirectAdjustment ia = new IndirectAdjustment();
			// 基本信息
			ia.setStationId(ds.getStationId());
			ia.setForeStnId(ds.getForeStnId());
			ia.setBackStnId(ds.getBackStnId());
			// 近似转折角
			ia.setAngDeg(ds.getAvgValueDeg());
			ia.setAngMin(ds.getAvgValueMin());
			ia.setAngSec(ds.getAvgValueSec());
			// 近似坐标
			ia.setFinalCoordX(ds.getFinalCoordX());
			ia.setFinalCoordY(ds.getFinalCoordY());
			// 近似边长
			ia.setDistance(ds.getAvgDistance());
			// 起始测站和终点测站不是待定点，故中误差为0
			if (ds.getStationId().equals(originStationId) || ds.getStationId().equals(endStationId)) {
				ia.setMediumErrorX(0.);
				ia.setMediumErrorY(0.);
			}
			// 由于起始测站和终点测站的坐标是起算数据，所以严密坐标是给出的
			if (ds.getStationId().equals(originStationId) || ds.getStationId().equals(endStationId)) {
				ia.setStrictCoordX(ds.getFinalCoordX());
				ia.setStrictCoordY(ds.getFinalCoordY());
			}
			// 把间接平差记录存入iaList
			iaList.add(ia);

			// 获取前视测站解算记录
			DataSolution ds1 = new DataSolution();
			ds1.setGroup(new Group(groupId));
			ds1.setLine(new Line(lineId));
			ds1.setStatus(DataSolution.STATUS_NORMAL);
			// x站的后站是当前测站，则x站是当前测站的前站
			ds1.setBackStnId(ds.getStationId());
			DataSolution ds_fore = dataSolutionDao.getByEntity(ds1);

			// 获取后视测站解算记录
			DataSolution ds2 = new DataSolution();
			ds2.setGroup(new Group(groupId));
			ds2.setLine(new Line(lineId));
			ds2.setStatus(DataSolution.STATUS_NORMAL);
			// x站的前站是当前测站，则x站是当前测站的后站
			ds2.setForeStnId(ds.getStationId());
			DataSolution ds_back = dataSolutionDao.getByEntity(ds2);

			double delta_current_fore_X = 0;	// 当前与前视测站的X的差距
			double delta_current_fore_Y = 0;	// 当前与前视测站的Y的差距
			double S_current_fore_square = 1.0;	// 当前——前视 的距离 平方
			double azimuthAng_current_fore = ds.getAzimuthAngRadian();		// 方位角 当前->前视
			// double azimuthAng_current_fore = ds.getAzimuthAngRadian() * 3600;		// 方位角 当前->前视
			if (ds_fore != null) {
				delta_current_fore_X = ds.getFinalCoordX() - ds_fore.getFinalCoordX();
				delta_current_fore_Y = ds.getFinalCoordY() - ds_fore.getFinalCoordY();
				// 如果有前站，则说明有当前——前视 的距离
				double S_current_fore = ds.getAvgDistance();					// 当前——前视 的距离
				S_current_fore_square = Math.pow(S_current_fore, 2);			// 当前——前视 的距离 平方
			}

			double delta_current_back_X = 0;	// 当前与后视测站的X的差距
			double delta_current_back_Y = 0;	// 当前与后视测站的Y的差距
			double S_current_back_square = 1.0;								// 当前——后视 的距离 平方
			double azimuthAng_current_back;									// 方位角 当前->后视
			// 后站解算记录不为空，说明有方位角 后站->当前，需要转为 当前->后站
			if (ds_back != null) {
				delta_current_back_X = ds.getFinalCoordX() - ds_back.getFinalCoordX();
				delta_current_back_Y = ds.getFinalCoordY() - ds_back.getFinalCoordY();
				double S_current_back = ds_back.getAvgDistance();
				S_current_back_square = Math.pow(S_current_back, 2);
				azimuthAng_current_back = ds_back.getAzimuthAngRadian();
				// 方位角旋转180°
				if (azimuthAng_current_back >= Math.PI) {
					azimuthAng_current_back -= Math.PI;
				} else {
					azimuthAng_current_back += Math.PI;
				}
				// 转换为 弧秒
				// azimuthAng_current_back *= 3600;
			} else {
				// 否则方位角就是起始方位角（起始方位角不需要转向）
				azimuthAng_current_back = originAzimuthAngRadian;
				// azimuthAng_current_back = originAzimuthArcSec;
			}

			// 观测值——转折角
			double L_i = ds.getTurningAngRadian();
			// double L_i = ds.getTurningAngRadian() * 3600;

			// x_当前 的系数
			double a1 = r * (delta_current_fore_Y / S_current_fore_square - delta_current_back_Y / S_current_back_square);
			// y_当前 的系数
			double a2 = -r * (delta_current_fore_X / S_current_fore_square - delta_current_back_X / S_current_back_square);
			// x_前站 的系数
			double a3 = -r * (delta_current_fore_Y / S_current_fore_square);
			// y_前站 的系数
			double a4 = r * (delta_current_fore_X / S_current_fore_square);
			// x_后站 的系数
			double a5 = r * (delta_current_back_Y / S_current_back_square);
			// y_后站 的系数
			double a6 = -r * (delta_current_back_X / S_current_back_square);
			// 常数项
			double turnAngRadian = azimuthAng_current_fore - azimuthAng_current_back;	// 转折角
			// 转折角不能为负数
			if (turnAngRadian < 0) {
				turnAngRadian += 2 * Math.PI;
			}
			double const_b = -L_i + turnAngRadian;

			// L矩阵
			matrix_L[i][0] = const_b;

			// P矩阵
			// matrix_P[i][i] = Math.pow(sigma_0, 2)
			// 		/ Math.pow(accuracy_a + accuracy_b * 1e-6 * L_i, 2);
			matrix_P[i][i] = 1;

			// B矩阵
			/*
			[]是B矩阵，左右是想象的
			*************************************************************************************************
			        无        QS     [  C1       C2       C3       C4     ......     C9   ]    ZD      无
			QS   后x, 后y, 当x, 当y, [前x, 前y, 0..........................................], ................
			C1   ......., 后x, 后y, [当x, 当y, 前x, 前y, 0.................................], ................
			C2   ................, [后x, 后y, 当x, 当y, 前x, 前y, 0........................], ................
			C3   ................, [ 0,   0, 后x, 后y, 当x, 当y, 前x, 前y, 0...............], ................
			C4   ................, [ 0,   0,  0,   0, 后x, 后y, 当x, 当y, 前x, 前y, 0......], ................
			......
			C9   ................, [ 0,   0,  0,   0.................., 后x, 后y, 当x, 当y], 前x, 前y, .......
			ZD   ................, [ 0,   0, .................................., 后x, 后y], 当x, 当y, 前x, 前y
			*************************************************************************************************
			 */
			// 如果是起始测站，只有第一个未知测站的坐标的系数
			if (ds.getStationId().equals(originStationId)) {
				// 前站系数
				matrix_B[i][0] = a3;
				matrix_B[i][1] = a4;
			}
			// 如果后站是起始测站，只有第一个和第二个未知测站的坐标的系数
			else if (ds.getBackStnId().equals(originStationId)) {
				// 当前测站系数
				matrix_B[i][0] = a1;
				matrix_B[i][1] = a2;
				// 前站系数
				matrix_B[i][2] = a3;
				matrix_B[i][3] = a4;
			}
			// 如果前站是终点站，只有倒数第二个和倒数第一个未知测站的坐标的系数
			else if (ds.getForeStnId().equals(endStationId)) {
				// 后站系数
				matrix_B[i][dim_y - 4] = a5;
				matrix_B[i][dim_y - 3] = a6;
				// 当前测站系数
				matrix_B[i][dim_y - 2] = a1;
				matrix_B[i][dim_y - 1] = a2;
			}
			// 如果是终点测站，只有倒数第一个未知测站的坐标的系数
			else if (ds.getStationId().equals(endStationId)) {
				// 后站系数
				matrix_B[i][dim_y - 2] = a5;
				matrix_B[i][dim_y - 1] = a6;
			}
			// 其余测站情况（第3个测站开始的），乘2代表下一维的系数往后移2位
			else {
				// 后视测站系数
				matrix_B[i][(i - 2) * 2] = a5;
				matrix_B[i][(i - 2) * 2 + 1] = a6;
				// 当前测站系数
				matrix_B[i][(i - 2) * 2 + 2] = a1;
				matrix_B[i][(i - 2) * 2 + 3] = a2;
				// 前视测站系数
				matrix_B[i][(i - 2) * 2 + 4] = a3;
				matrix_B[i][(i - 2) * 2 + 5] = a4;
			}
		}



		/*--------------------------------- 求距离测量误差方程的系数 ---------------------------------*/
		// B、P和L矩阵需要存储数据的开始维度
		int j;
		for (int i = 0; i < dsList.size() - 1; i++) {
			// 当前测站解算记录
			DataSolution ds = dsList.get(i);

			// 获取前视测站解算记录
			DataSolution ds1 = new DataSolution();
			ds1.setGroup(new Group(groupId));
			ds1.setLine(new Line(lineId));
			ds1.setStatus(DataSolution.STATUS_NORMAL);
			// x站的后站是当前测站，则x站是当前测站的前站
			ds1.setBackStnId(ds.getStationId());
			DataSolution ds_fore = dataSolutionDao.getByEntity(ds1);

			double delta_current_fore_X = 0;	// 当前与前视测站的X的差距
			double delta_current_fore_Y = 0;	// 当前与前视测站的Y的差距
			double S_current_fore = ds.getAvgDistance();					// 当前——前视 的距离
			if (ds_fore != null) {
				delta_current_fore_X = ds.getFinalCoordX() - ds_fore.getFinalCoordX();
				delta_current_fore_Y = ds.getFinalCoordY() - ds_fore.getFinalCoordY();
			}

			// 观测值——距离
			double L_i = ds.getAvgDistance();

			// x_当前 的系数
			double a1 = -delta_current_fore_X / S_current_fore;
			// y_当前 的系数
			double a2 = -delta_current_fore_Y / S_current_fore;
			// x_前站 的系数
			double a3 = -a1;
			// y_前站 的系数
			double a4 = -a2;
			// 常数项
			double const_b =  - L_i + Math.sqrt(Math.pow(-delta_current_fore_X, 2) + Math.pow(-delta_current_fore_Y, 2));

			// B、P和L矩阵需要存储数据的开始维度
			j = i + dsList.size();

			// L矩阵
			matrix_L[j][0] = const_b;

			// P矩阵
			matrix_P[j][j] = Math.pow(sigma_0, 2)
					/ Math.pow(accuracy_a + accuracy_b * 1e-6 * L_i, 2);

			// B矩阵
			/*
			[]是B矩阵，左右是想象的
			*************************************************************************************************
			        QS     [  C1       C2       C3       C4     ......     C9   ]    ZD
			QS   当x, 当y, [前x, 前y, 0..........................................], ........
			C1   ......., [当x, 当y, 前x, 前y, 0.................................], ........
			C2   ......., [ 0,   0, 当x, 当y, 前x, 前y, 0........................], ........
			C3   ......., [ 0,   0,  0,   0, 当x, 当y, 前x, 前y, 0...............], ........
			C4   ......., [ 0,   0,  0,   0,  0,   0, 当x, 当y, 前x, 前y, 0......], ........
			......
			C9   ......., [ 0,   0, .................................., 当x, 当y], 前x, 前y
			ZD   .........................................................................
			*************************************************************************************************
			 */
			// 如果是起始测站，只有第一个未知测站的坐标的系数
			if (ds.getStationId().equals(originStationId)) {
				// 前站系数
				matrix_B[j][0] = a3;
				matrix_B[j][1] = a4;
			}
			// 如果前站是终点站，只有倒数第一个未知测站的坐标的系数
			else if (ds.getForeStnId().equals(endStationId)) {
				// 当前测站系数
				matrix_B[j][dim_y - 2] = a1;
				matrix_B[j][dim_y - 1] = a2;
			}
			// 其余测站情况（第2个测站开始的），乘2代表下一维的系数往后移2位
			else {
				// 当前测站系数
				matrix_B[j][(i - 1) * 2] = a1;
				matrix_B[j][(i - 1) * 2 + 1] = a2;
				// 前视测站系数
				matrix_B[j][(i - 1) * 2 + 2] = a3;
				matrix_B[j][(i - 1) * 2 + 3] = a4;
			}
		}



		/*--------------------------------- 求解未知数 ---------------------------------*/
		Matrix B = new Matrix(matrix_B);
		Matrix P = new Matrix(matrix_P);
		Matrix L = new Matrix(matrix_L);
		Matrix B_tran = B.transpose();
		// (B转PB)逆B转PL
		// 待定点坐标平差值的协因数阵，行和列都是 x1, y1, x2, y2, ..., xn, yn
		Matrix Q = B_tran.times(P).times(B).inverse();
		// 未知数矩阵
		Matrix X = Q.times(B_tran).times(P).times(L);



		/*--------------------------------- 计算观测值改正数 ---------------------------------*/
		Matrix V = B.times(X).plus(L);



		/*--------------------------------- 计算严密转折角、严密边长 ---------------------------------*/
		// iaList的顺序就是V矩阵（观测值改正数）行的顺序
		int k = 0;
		for (IndirectAdjustment ia : iaList) {
			double radian = AngleUtils.angle2radian(ia.getAngDeg(), ia.getAngMin(), ia.getAngSec());
			// 改正转折角
			double strictRadian = radian + V.get(k, 0);
			double[] strictAngle = AngleUtils.radian2angle(strictRadian);
			// 设置严密转折角
			ia.setStrictAngDeg((int) strictAngle[0]);
			ia.setStrictAngMin((int) strictAngle[1]);
			ia.setStrictAngSec(strictAngle[2]);
			// 改正边长
			// 终点测站无前视边长
			if (k == iaList.size() - 1) {
				// 设置严密边长
				ia.setStrictDistance(0.0);
			} else {
				double strictDistance = ia.getDistance() + V.get(k + iaList.size(), 0);
				// 设置严密边长
				ia.setStrictDistance(strictDistance);
			}
			k++;
		}


		//当前时间
		Date currentDate = new Date();


		/*--------------------------------- 计算单位权中误差 ---------------------------------*/
		int n = dim_x;	// 转折角个数 + 边个数
		int t = dim_y;	// 参数个数
		Matrix f = V.transpose().times(P).times(V);
		// double temp = f.get(0, 0);
		double temp = f.det();	// 行列式
		// 单位权中误差
		double mediumError_unitWeight = Math.sqrt(temp / (n - t));
		// 单位权中误差入库
		ResultEvaluationStrict resEvaStrict = new ResultEvaluationStrict();
		resEvaStrict.setLineId(lineId);
		resEvaStrict.setMediumerrorUnitweight(mediumError_unitWeight * 1000);	// 转成毫米级
		resEvaStrict.setTraverseClass(traverseClass);
		resEvaStrict.setResultEvaluation("没做评价");	// TODO: 结果评价需要根据精度标准来给出
		resEvaStrict.setSolutionDate(currentDate);
		resEvaStrictService.save(resEvaStrict);



		/*--------------------------------- 计算待定点坐标平差值的中误差、严密坐标 ---------------------------------*/
		// 存放中误差
		Map<String, Double> sigma_top_map = new LinkedHashMap<>();
		// 获取协因数阵的行数， 协因数阵是txt的
		int row = Q.getRowDimension();
		// 协因数阵行列的顺序就是待定点坐标的顺序，即测站链（从起始到终点）排序
		//上一个测站的id
		String lastStationId = originStationId;
		// 计算每一个待定点坐标平差值的中误差
		for (int i = 0; i < row; i++) {
			// 获取待定点测站的名称
			DataSolution ds1 = new DataSolution();
			ds1.setBackStnId(lastStationId);
			ds1.setStatus(DataSolution.STATUS_NORMAL);
			DataSolution station1 = dataSolutionDao.getByEntity(ds1);
			// 协因数阵的对角线元素
			double diagonal_element = Q.get(i, i);
			// 待定点坐标平差值的中误差
			double sigma_ = mediumError_unitWeight * Math.sqrt(diagonal_element);
			sigma_ = sigma_ * 1000;	// 转成毫米级
			// 设置待定点坐标平差值的中误差、严密坐标
			if (i % 2 == 0) {	// 坐标x
				iaList.get(i / 2 + 1).setMediumErrorX(sigma_);
				iaList.get(i / 2 + 1).setStrictCoordX(iaList.get(i / 2 + 1).getFinalCoordX() + X.get(i, 0));
				sigma_top_map.put(station1.getStation().getStationName() + "_x", sigma_);
			} else {			// 坐标y
				iaList.get(i / 2 + 1).setMediumErrorY(sigma_);
				iaList.get(i / 2 + 1).setStrictCoordY(iaList.get(i / 2 + 1).getFinalCoordY() + X.get(i, 0));
				sigma_top_map.put(station1.getStation().getStationName() + "_y", sigma_);
				// 更新上一个测站的id，坐标y之后是下一个测站的坐标x
				lastStationId = station1.getStationId();
			}
		}



		/*--------------------------------- 数据入库 ---------------------------------*/
		for (IndirectAdjustment ia : iaList) {
			// 设置解算时间
			ia.setSolutionDate(currentDate);
			super.save(ia);
		}



		System.out.println();

		// super.save(indirectAdjustment);
	}

	/**
	 * 更新状态
	 * @param indirectAdjustment
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(IndirectAdjustment indirectAdjustment) {
		super.updateStatus(indirectAdjustment);
	}
	
	/**
	 * 删除数据
	 * @param indirectAdjustment
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(IndirectAdjustment indirectAdjustment) {
		super.delete(indirectAdjustment);
	}

	/**
	 * 添加数据权限过滤条件
	 * 2022年4月11日 su添加
	 */
	@Override
	public void addDataScopeFilter(IndirectAdjustment indirectAdjustment) {
		// 举例4：用户、员工（自己创建的）数据权限根据部门过滤，实体类@Table注解extWhereKeys="dsfOffice"
		indirectAdjustment.getSqlMap().getDataScope().addFilter("dsfOffice", "Office",
				"u1.office_code", "a.create_by", DataScope.CTRL_PERMI_HAVE);
	}
	
}