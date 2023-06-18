/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.elevation.el_indirect_adjustment.service;

import java.util.ArrayList;
import java.util.List;

import Jama.Matrix;
import com.jeesite.common.entity.DataScope;
import com.jeesite.elevation.el_data_solution.dao.ElDataSolutionDao;
import com.jeesite.elevation.el_data_solution.entity.ElDataSolution;
import com.jeesite.elevation.el_line.entity.ELLine;
import com.jeesite.elevation.el_origin_data.dao.ElOriginDataDao;
import com.jeesite.elevation.el_origin_data.entity.ElOriginData;
import com.jeesite.modules.group.entity.Group;
import com.jeesite.modules.indirect_adjustment.entity.IndirectAdjustment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.elevation.el_indirect_adjustment.entity.ElIndirectAdjustment;
import com.jeesite.elevation.el_indirect_adjustment.dao.ElIndirectAdjustmentDao;

/**
 * 高程——间接平差Service
 * @author su
 * @version 2022-07-04
 */
@Service
@Transactional(readOnly=true)
public class ElIndirectAdjustmentService extends CrudService<ElIndirectAdjustmentDao, ElIndirectAdjustment> {

	@Autowired
	private ElDataSolutionDao elDataSolutionDao;

	@Autowired
	private ElOriginDataDao elOriginDataDao;
	
	/**
	 * 获取单条数据
	 * @param elIndirectAdjustment
	 * @return
	 */
	@Override
	public ElIndirectAdjustment get(ElIndirectAdjustment elIndirectAdjustment) {
		return super.get(elIndirectAdjustment);
	}
	
	/**
	 * 查询分页数据
	 * @param elIndirectAdjustment 查询条件
	 * @return
	 */
	@Override
	public Page<ElIndirectAdjustment> findPage(ElIndirectAdjustment elIndirectAdjustment) {
		return super.findPage(elIndirectAdjustment);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param elIndirectAdjustment
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(ElIndirectAdjustment elIndirectAdjustment) {
		//获取小组id
		String groupId = elIndirectAdjustment.getGroup().getId();
		//获取导线id
		String lineId = elIndirectAdjustment.getLine().getId();
		//获取导线测量等级
		String traverseClass = elIndirectAdjustment.getTraverseClass();
		//获取备注信息
		String remarks = elIndirectAdjustment.getRemarks();

		double C = 5;



		/*--------------------------------- 将上一次的间接平差保存为历史记录 ---------------------------------*/
		// 查找导线为lineId且status为0的严密平差记录
		ElIndirectAdjustment ia_ = new ElIndirectAdjustment();
		ia_.setLine(new ELLine(lineId));
		ia_.setStatus(ElIndirectAdjustment.STATUS_NORMAL);
		List<ElIndirectAdjustment> iaList_ = findList(ia_);
		// 将status置为2，即为停用
		for (ElIndirectAdjustment ia2 : iaList_) {
			ia2.setStatus(ElIndirectAdjustment.STATUS_DISABLE);
			updateStatus(ia2);
		}



		// 取出数据解算的记录
		ElDataSolution elDataSolution = new ElDataSolution();
		elDataSolution.setGroup(new Group(groupId));
		elDataSolution.setLine(new ELLine(lineId));
		elDataSolution.setStatus(ElDataSolution.STATUS_NORMAL);
		List<ElDataSolution> dsList = elDataSolutionDao.findList(elDataSolution);

		// 一维：观测的高差的数量。二维：待定点高程的数量
		int dim_x = dsList.size() - 1;
		int dim_y = dsList.size() - 2;
		double[][] matrix_B_fore = new double[dim_x][dim_y];
		double[][] matrix_L_fore = new double[dim_x][1];
		double[][] matrix_B_back = new double[dim_x][dim_y];
		double[][] matrix_L_back = new double[dim_x][1];
		double[][] matrix_P = new double[dim_x][dim_x];

		// 存放IndirectAdjustment列表
		List<ElIndirectAdjustment> iaList = new ArrayList<>();



		/*--------------------------------- 获取起始测站和终点测站的数据 ---------------------------------*/
		ElOriginData elOriginData_ = new ElOriginData();
		elOriginData_.setGroup(new Group(groupId));
		elOriginData_.setLine(new ELLine(lineId));
		elOriginData_.setStatus(ElOriginData.STATUS_NORMAL);
		ElOriginData elOriginData = elOriginDataDao.getByEntity(elOriginData_);
		String originStationId = elOriginData.getOriginStationId();		// 起始测站ID
		Double originElevation = elOriginData.getOriginElevation();		// 起始测站高程
		String endStationId = elOriginData.getEndStationId();			// 终点测站ID
		Double endElevation = elOriginData.getEndElevation();			// 终点测站高程



		/*------------------------------- 往测 -------------------------------*/
		/*--------------------------------- 求高差观测方程的系数 ---------------------------------*/
		String currentStationId = originStationId;	// 当前测站ID（从起始测站开始算起）
		// 存储各测站的前视高差（无终点测站的高差）
		double[] elevationDiffFore = new double[dim_x];
		// 存储各测站的前视高程（无终点测站的高程） 0 ~ dsList.size()-2   0是起始测站，dsList.size()-2 是终点测站的后站
		double[] elevationFore = new double[dim_x];
		for (int i = 0; i < dim_x; i++) {
			// 当前测站的解算记录
			ElDataSolution ds1 = new ElDataSolution();
			ds1.setGroup(new Group(groupId));
			ds1.setLine(new ELLine(lineId));
			ds1.setStationId(currentStationId);
			ds1.setStatus(ElDataSolution.STATUS_NORMAL);
			ElDataSolution ds = elDataSolutionDao.getByEntity(ds1);
			// 存储前视高差
			elevationDiffFore[i] = ds.getElevationDiffFore();
			// 存储前视高程
			elevationFore[i] = ds.getElevationFore();

			// 获取前站的解算记录
			ElDataSolution ds2 = new ElDataSolution();
			ds2.setGroup(ds.getGroup());
			ds2.setLine(ds.getLine());
			ds2.setStatus(ElDataSolution.STATUS_NORMAL);
			// x站的后站是当前测站，则x站是当前测站的前站
			ds2.setBackStnId(ds.getStationId());
			ElDataSolution ds_fore = elDataSolutionDao.getByEntity(ds2);

			// 设置ELIndirectAdjustment的基本信息
			ElIndirectAdjustment ia = new ElIndirectAdjustment();
			// 基本信息
			ia.setStationId(ds.getStationId());
			ia.setForeStnId(ds.getForeStnId());
			ia.setBackStnId(ds.getBackStnId());
			ia.setSolutionDate(ds.getSolutionDate());

			// 把间接平差记录存入iaList
			iaList.add(ia);

			// X_current_top 的系数
			double a_current = -1;
			// X_fore_top 的系数
			double a_fore = 1;
			// 常数项：（前站高程 - 当前高程）- 高差
			double const_b = ds_fore.getElevationFore() - ds.getElevationFore() - ds.getElevationDiffFore();

			// L矩阵
			matrix_L_fore[i][0] = const_b;

			// P矩阵
			matrix_P[i][i] = C / ds.getDistanceForeAvg();

			// B矩阵
			// 如果是起始测站，只有第一个待定点的高程改正数的系数
			if (ds.getStationId().equals(originStationId)) {
				// 前站系数
				matrix_B_fore[i][0] = a_fore;
			}
			// 如果前站是终点站，只有倒数第一个待定点的高程改正数的系数
			else if (ds.getForeStnId().equals(endStationId)) {
				// 当前测站系数
				matrix_B_fore[i][dim_y - 1] = a_current;
			}
			// 其余测站情况（第2个测站开始的）
			else {
				// 当前测站系数
				matrix_B_fore[i][i - 1] = a_current;
				// 前站系数
				matrix_B_fore[i][i] = a_fore;
			}

			// 更新当前测站ID
			currentStationId = ds.getForeStnId();
		}

		/*--------------------------------- 求解未知数 ---------------------------------*/
		Matrix P = new Matrix(matrix_P);
		Matrix B_fore = new Matrix(matrix_B_fore);
		Matrix L_fore = new Matrix(matrix_L_fore).times(-1); // V = Bx - l
		Matrix B_fore_tran = B_fore.transpose();

		Matrix N_BB_fore = B_fore_tran.times(P).times(B_fore);
		// 未知数矩阵：(B转PB)逆B转PL，这里就是高程改正数
		Matrix X_fore = N_BB_fore.inverse().times(B_fore_tran).times(P).times(L_fore);
		// 高差观测值平差值的协因数阵，行和列都是 h1, h2, h3, ... , hn

		/*--------------------------------- 计算高差改正数 ---------------------------------*/
		Matrix V_fore = B_fore.times(X_fore).plus(L_fore);

		/*--------------------------------- 计算严密高差、高程（前视） ---------------------------------*/
		int j = 0;
		for (ElIndirectAdjustment ia : iaList) {
			// 严密高差
			ia.setElevationDiffForeCor(elevationDiffFore[j] + V_fore.get(j, 0));
			// 严密高程
			if (ia.getStationId().equals(originStationId)) {
				// 起始测站的前视高程是给出的，不需要改正
				ia.setElevationFore(elevationFore[j]);
			} else {
				ia.setElevationFore(elevationFore[j] + X_fore.get(j - 1, 0));
			}
			j++;
		}



		/*------------------------------- 返测 -------------------------------*/
		/*--------------------------------- 求高差观测方程的系数 ---------------------------------*/
		currentStationId = endStationId;	// 当前测站ID（从终点测站开始算起）
		// 存储各测站的前视高差（无起始测站的高差）
		double[] elevationDiffBack = new double[dim_x];
		// 存储各测站的前视高程（无起始测站的高程） 0 ~ dsList.size()-2   0是起始测站的前站，dsList.size()-2 是终点测站
		double[] elevationBack = new double[dim_x];
		for (int i = dim_x - 1; i >= 0; i--) {
			// 当前测站的解算记录
			ElDataSolution ds1 = new ElDataSolution();
			ds1.setGroup(new Group(groupId));
			ds1.setLine(new ELLine(lineId));
			ds1.setStationId(currentStationId);
			ds1.setStatus(ElDataSolution.STATUS_NORMAL);
			ElDataSolution ds = elDataSolutionDao.getByEntity(ds1);
			// 存储后视高差
			elevationDiffBack[i] = ds.getElevationDiffBack();
			// 存储后视高程
			elevationBack[i] = ds.getElevationBack();

			// 获取后站的解算记录
			ElDataSolution ds2 = new ElDataSolution();
			ds2.setGroup(ds.getGroup());
			ds2.setLine(ds.getLine());
			ds2.setStatus(ElDataSolution.STATUS_NORMAL);
			// x站的前站是当前测站，则x站是当前测站的后站
			ds2.setForeStnId(ds.getStationId());
			ElDataSolution ds_back = elDataSolutionDao.getByEntity(ds2);

			// 给终点测站设置ELIndirectAdjustment的基本信息
			if (ds.getStationId().equals(endStationId)) {
				// 设置ELIndirectAdjustment的基本信息
				ElIndirectAdjustment ia = new ElIndirectAdjustment();
				// 基本信息
				ia.setStationId(ds.getStationId());
				ia.setForeStnId(ds.getForeStnId());
				ia.setBackStnId(ds.getBackStnId());
				ia.setSolutionDate(ds.getSolutionDate());

				// 把间接平差记录存入iaList
				iaList.add(ia);
			}

			// X_current_top 的系数
			double a_current = -1;
			// X_back_top 的系数
			double a_back = 1;
			// 常数项：（后站高程 - 当前高程）- 高差
			double const_b = ds_back.getElevationBack() - ds.getElevationBack() - ds.getElevationDiffBack();

			// L矩阵
			matrix_L_back[i][0] = const_b;

			// B矩阵
			// 如果是终点测站，只有倒数第一个待定点的高程改正数的系数
			if (ds.getStationId().equals(endStationId)) {
				// 后站系数
				matrix_B_back[i][dim_y - 1] = a_back;
			}
			// 如果后站是起点站，只有第一个待定点的高程改正数的系数
			else if (ds.getBackStnId().equals(originStationId)) {
				// 当前测站系数
				matrix_B_back[i][0] = a_current;
			}
			// 其余测站情况（倒数第2个测站开始的）
			else {
				// 当前测站系数
				matrix_B_back[i][i - 1] = a_back;
				// 前站系数
				matrix_B_back[i][i] = a_current;
			}

			// 更新当前测站ID
			currentStationId = ds.getBackStnId();
		}



		/*--------------------------------- 求解未知数 ---------------------------------*/
		Matrix B_back = new Matrix(matrix_B_back);
		Matrix L_back = new Matrix(matrix_L_back).times(-1); // V = Bx - l
		Matrix B_back_tran = B_back.transpose();

		Matrix N_BB_back = B_back_tran.times(P).times(B_back);
		// 未知数矩阵：(B转PB)逆B转PL，这里就是高程改正数
		Matrix X_back = N_BB_back.inverse().times(B_back_tran).times(P).times(L_back);
		// 高差观测值平差值的协因数阵，行和列都是 h1, h2, h3, ... , hn

		/*--------------------------------- 计算高差改正数 ---------------------------------*/
		Matrix V_back = B_back.times(X_back).plus(L_back);

		/*--------------------------------- 计算严密高差、高程（后视） ---------------------------------*/
		j = 0;
		// 先跳过起始测站
		for (ElIndirectAdjustment ia : iaList) {
			if (ia.getStationId().equals(originStationId)) {
				continue;
			}
			// 严密高差
			ia.setElevationDiffBackCor(elevationDiffBack[j] + V_back.get(j, 0));
			// 严密高程
			if (ia.getStationId().equals(endStationId)) {
				// 终点测站的后视高程是给出的，不需要改正
				ia.setElevationBack(elevationBack[j]);
			} else {
				ia.setElevationBack(elevationBack[j] + X_back.get(j, 0));
			}
			j++;
		}
		// 再计算 起始测站的严密高差、高程（后视） 和 终点测站的严密高差、高程（前视）
		for (int i = 0; i < iaList.size(); i++) {
			if (iaList.get(i).getStationId().equals(originStationId)) {
				iaList.get(i).setElevationDiffBackCor(0.);
				// 起始测站的严密高程(后视) = 前站的高程(后视) + 前站的严密高差(后视)
				ElIndirectAdjustment ia_fore = iaList.get(i + 1);
				double elBack = ia_fore.getElevationBack() + ia_fore.getElevationDiffBackCor();
				iaList.get(i).setElevationBack(elBack);
			}
			if (iaList.get(i).getStationId().equals(endStationId)) {
				iaList.get(i).setElevationDiffForeCor(0.);
				// 终点测站的严密高程(前视) = 后站的高程(前视) + 后站的严密高差(前视)
				ElIndirectAdjustment ia_back = iaList.get(i - 1);
				double elFore = ia_back.getElevationFore() + ia_back.getElevationDiffForeCor();
				iaList.get(i).setElevationFore(elFore);
			}
		}
		// 最后计算全部测站的高程，  高程 = (前视高程 + 后视高程) / 2
		for (ElIndirectAdjustment ia : iaList) {
			ia.setElevation((ia.getElevationFore() + ia.getElevationBack()) / 2);

			/*--------------------------------- 数据入库 ---------------------------------*/
			super.save(ia);
		}

		System.out.println();
		// super.save(elIndirectAdjustment);
	}
	
	/**
	 * 更新状态
	 * @param elIndirectAdjustment
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(ElIndirectAdjustment elIndirectAdjustment) {
		super.updateStatus(elIndirectAdjustment);
	}
	
	/**
	 * 删除数据
	 * @param elIndirectAdjustment
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(ElIndirectAdjustment elIndirectAdjustment) {
		super.delete(elIndirectAdjustment);
	}

	/**
	 * 添加数据权限过滤条件
	 * 2022年7月5日 su添加
	 */
	@Override
	public void addDataScopeFilter(ElIndirectAdjustment elIndirectAdjustment) {
		// 举例4：用户、员工（自己创建的）数据权限根据部门过滤，实体类@Table注解extWhereKeys="dsfOffice"
		elIndirectAdjustment.getSqlMap().getDataScope().addFilter("dsfOffice", "Office",
				"u1.office_code", "a.create_by", DataScope.CTRL_PERMI_HAVE);
	}
	
}