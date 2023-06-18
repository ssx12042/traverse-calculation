/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.wrong.service;

import java.util.List;

import com.jeesite.common.entity.DataScope;
import com.jeesite.modules.group.entity.Group;
import com.jeesite.modules.indirect_adjustment.entity.IndirectAdjustment;
import com.jeesite.modules.indirect_adjustment.service.IndirectAdjustmentService;
import com.jeesite.modules.line.entity.Line;
import com.jeesite.modules.origin.entity.OriginData;
import com.jeesite.modules.origin.service.OriginDataService;
import com.jeesite.modules.station.entity.Station;
import com.jeesite.modules.station.service.StationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.wrong.entity.WrongResult;
import com.jeesite.modules.wrong.dao.WrongResultDao;

/**
 * 错误边角查询Service
 * @author hu
 * @version 2022-05-03
 */
@Service
@Transactional(readOnly=true)
public class WrongResultService extends CrudService<WrongResultDao, WrongResult> {
//	@Autowired
//	IndirectAdjustment indirectAdjustment;
	@Autowired
	OriginDataService originDataService;
	@Autowired
	IndirectAdjustmentService indirectAdjustmentService;
	@Autowired
	StationService stationService;
	/**
	 * 获取单条数据
	 * @param wrongResult
	 * @return
	 */
	@Override
	public WrongResult get(WrongResult wrongResult) {
		return super.get(wrongResult);
	}
	
	/**
	 * 查询分页数据
	 * @param wrongResult 查询条件
	 * @param wrongResult.page 分页对象
	 * @return
	 */
	@Override
	public Page<WrongResult> findPage(WrongResult wrongResult) {
		return super.findPage(wrongResult);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param wrongResult
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(WrongResult wrongResult) {
		//获取小组id
		String groupId = wrongResult.getGroupId();
		//获取导线id
		String lineId = wrongResult.getLineId();

		int n=0;
		int flag=-1;
		double a1=0 ;
		double h=0 ;
		double a0=0 ;
		double zc = 0;
		double mix=1000000;
		String cz="";
		double zz=0;
		IndirectAdjustment indirectAdjustment=new IndirectAdjustment();
		indirectAdjustment.setGroup(new Group(groupId));
		indirectAdjustment.setLine(new Line(lineId));
		List<IndirectAdjustment>iaList2=indirectAdjustmentService.findList(indirectAdjustment);
		List<IndirectAdjustment>iaList=indirectAdjustmentService.findList(indirectAdjustment);

		OriginData originData=new OriginData();
		originData.setGroup(new Group(groupId));
		originData.setLine(new Line(lineId));
		List<OriginData>odList= originDataService.findList(originData);

		Station station=new Station();
		station.setLine(new Line(lineId));
		station.setGroup(new Group(groupId));
		System.out.println("aaa"+station.getLine());
		List<Station>sList= stationService.findList(station);

		//测站数
		for (IndirectAdjustment ia2: iaList2) {
			n++;
		}
		System.out.println(n);
		double[] dx=new double[n];
		double[] dy=new double[n];
		//坐标增量
		double[] cx=new double[n+1];
		double[] cy=new double[n+1];
		//顺时针计算坐标
		double[] cnx=new double[n+1];
		double[] cny=new double[n+1];
		//逆时针计算坐标
		double[] d=new double[n];
		//观测角
		double[] traverse = new double[n];
		//方位角
		double[] fwj=new double[n+1];
		//平距
		double []s=new double[n+1];
		//测站
		String[] observation=new String[n];
		int j = 0;
		for (IndirectAdjustment ia: iaList) {
				traverse[j] = ia.getStrictAngDeg() + ia.getStrictAngMin() / 60.0 + ia.getStrictAngSec() / 3600;
				//获取平距
			if(j<n-1) {
				s[j] = ia.getStrictDistance();
				System.out.println(s[j]);
			}
			j++;
		}
		for(OriginData od:odList) {
			fwj[0] = od.getOriginAzimuthDeg() + od.getOriginAzimuthMin() / 60.0 + od.getOriginAzimuthSec() / 3600;
			fwj[n - 1] = od.getEndAzimuthDeg() + od.getEndAzimuthMin() / 60.0 + od.getEndAzimuthSec() / 3600;
			cx[0] = od.getOriginCoordX();
			cy[0] = od.getOriginCoordY();
			cx[n - 1] = od.getEndCoordX();
			cy[n - 1] = od.getEndCoordY();
			System.out.println("Min"+od.getOriginAzimuthMin());
		}
		int m=0;
		for (Station sl: sList){
			//获取测站名
			observation[m] = sl.getStationName();
			m++;
		}
		//计算观测角之和
		for(int i=0;i<n;i++){
			zz += traverse[i];
			System.out.println(traverse[i]+" "+cx[i]+" "+fwj[i]+" "+observation[i]);
		}
		//当flag为0时为闭合导线，当flag为1时为附和导线
		flag = -1;
		if (fwj[0] == fwj[n-1] || fwj[n-1] == 0) {
			flag = 0;
		} else {
			flag = 1;
		}
		System.out.println(flag+" "+fwj[0]+" "+fwj[n-1] );
		double zl;
		System.out.println();
		//计算闭合差
		if (flag == 0) {
			zl = (n - 1) * 180;
			zc = Math.abs(zz - zl) * 3600;
			System.out.println("zc"+zc);
		} else if (flag == 1) {
		//	zl = (n+1) * 180;
		zl=1080;
			zc = Math.abs(fwj[0] +zz -  fwj[n-1] - zl) * 3600;

			System.out.println("zc"+zc+" "+zz+" "+zl+" "+fwj[n-1]+" "+fwj[0]);
		}
		a1 = 0;
		//容许闭合差h
		h = 40 * Math.sqrt(n);
		System.out.println(h);
		a0 = fwj[n-1];
		cnx[0] =cx[n - 1] ;
		cny[0] =cy[n - 1] ;
		//角错误
		if (zc>h) {
			//用初始方位角和各观测站观测角顺时针计算各坐标方位角
			for(int i=0;i<n;i++){
				if(i==0){
					fwj[i + 1] = fwj[i]  -360 + traverse[i];
				}else {
					fwj[i + 1] = fwj[i] - 180 + traverse[i ];
				}
				while (fwj[i+1]>360) {
					fwj[i+1]-=360;
				}
				while (fwj[i+1]<0) {
					fwj[i+1]+=360;
				}
				//角度转弧度
				if(flag==0) {
					a1 = Math.toRadians(fwj[i+1]);
				}else if(flag==1){
					a1 = Math.toRadians(fwj[i+1]);
				}
				//计算坐标增量与坐标
				if(flag==0) {
					//平距乘坐标方位角为坐标增量
					dx[i] = s[i] * Math.cos(a1);
					dy[i] = s[i] * Math.sin(a1);
				}else if(flag==1){
					dx[i] = s[i] * Math.cos(a1);
					dy[i] = s[i] * Math.sin(a1);
				}
				cx[i+1] = cx[i] + dx[i];
				cy[i+1] = cy[i] + dy[i];
				System.out.println("fwj"+fwj[i]+" "+fwj[i+1]+" "+cx[i]+" "+cy[i]+" "+traverse[i]+" "+s[i]);
			}
			System.out.println(fwj[n]);
			double a2=0;
			fwj[n]=a0;
			System.out.println();
			for(int i=n-1;i>=0;i--){

				if(i>0){//用终点方位角和各观测站观测角逆时针计算坐标方位角
					fwj[i] = fwj[i + 1] + 180 - traverse[i ];
				}else {
					fwj[i] = fwj[i + 1] + 360 - traverse[i ];
				}
				while (fwj[i]>360) {
					fwj[i]-=360;
				}
				while (fwj[i]<0) {
					fwj[i]+=360;
				}
				//角度转弧度
				if(flag==0) {
					a2 = Math.toRadians(fwj[i+1]);
				}else if(flag==1){
					a2 = Math.toRadians(fwj[i+1]);
				}
				//逆时针计算坐标增量与坐标
				if(flag==0) {
					//平距乘坐标方位角为坐标增量
					dx[i] = s[i] * Math.cos(a2);
					dy[i] = s[i] * Math.sin(a2);
				}else if(flag==1){
					dx[i] = s[i] * Math.cos(a2);
					dy[i] = s[i] * Math.sin(a2);
				}
				cnx[n - i] = cnx[n - i - 1] - dx[i];
				cny[n - i] = cny[n - i - 1] - dy[i];
				System.out.println("nfwj"+fwj[i]+" "+fwj[i+1]+" "+dx[i]+" "+cnx[n - i-1]+" "+dy[i]+" "+cny[n - i-1]);
			}
			System.out.println(
			);
			//将获得的两组坐标数据进行对比找出差值最小的那一组数据
			for(int i=0;i<n;i++){
				d[i] = Math.sqrt(Math.pow((cx[i] - cnx[n - i]), 2) + Math.pow((cy[i] - cny[n - i]), 2));
				System.out.println(cx[i]+" "+ cnx[n - i]+" "+cy[i]+" "+ cny[n - i]);
				System.out.println((cx[i] - cnx[n - i])+" "+(cy[i] - cny[n - i]));
				System.out.println(observation[i] + "--" + d[i]);
				System.out.println(traverse[i]+" "+cx[i]+" "+cnx[n-i]+" "+cy[i]+" "+cny[n-i]);
				System.out.println(fwj[i]);
				System.out.println();
			}
			System.out.println();
			//输出错误角位置
			for(int i=0;i<n;i++){
				if(flag==1) {
					if (d[i] < mix) {
						mix = d[i];
						cz = observation[i ];
					}
				}
				else{
					if (d[i] < mix) {
						mix = d[i];
						cz = observation[i ];
					}
				}
				System.out.println(mix);
			}
			wrongResult.setWrong("错误角的测站为"+cz);
		}
		System.out.println();
		//边错误计算
		if(zc<h)
		{
			double fx = 0,fy=0;
			//用初始方位角和各观测站观测角顺时针计算各坐标方位角
			for(int i=0;i<n;i++){
				if(i==0){
					fwj[i + 1] = fwj[i]  -360 + traverse[i];
				}else {
					fwj[i + 1] = fwj[i] - 180 + traverse[i];
				}
				while (fwj[i+1]>360) {
					fwj[i+1]-=360;
				}
				while (fwj[i+1]<0) {
					fwj[i+1]+=360;
				}
				System.out.println("fwj"+fwj[i+1]);

				//计算坐标
				if(flag==0)
				{
					//角度转弧度
					a1 = Math.toRadians(fwj[i+1]);
					//平距乘坐标方位角为坐标增量
					dx[i] = s[i] * Math.cos(a1);
					dy[i] = s[i] * Math.sin(a1);
				}else {
					a1 = Math.toRadians(fwj[i+1]);
					dx[i] = s[i] * Math.cos(a1);
					dy[i] = s[i] * Math.sin(a1);
				}
				//计算所有坐标增量和
				fx+=dx[i];
				fy+=dy[i];
				System.out.println(s[i]);
				System.out.println(dx[i]+" "+dy[i]+" "+s[i+1]+" "+a1);
			}
			System.out.println();
			System.out.println(fx+" "+fy);
			System.out.println();
			//计算起始坐标和终点坐标之差，并求出该差值与坐标增量之差
			double xd=cx[0]-cx[n-1];
			System.out.println("xd"+xd);
			xd=xd+fx;
			double yd=cy[0]-cy[n-1];
			System.out.println("yd"+yd);
			yd=yd+fy;
			System.out.println(xd+" "+yd);
			System.out.println();
			//求斜率
			double x=yd/xd;
			//求角度
			double A = Math.atan(x) * 180 / Math.PI;
			double Af=0;
			//计算方位角
			if(xd>0&&yd>0){
				Af=A;
			}else if(xd<0&&yd>0){
				Af=180+A;
			}else if(xd<0&&yd<0){
				Af=180+A;
			}else if(xd>0&&yd<0){
				Af=360+A;
			}
			j=0;
			//将各个位置的坐标方位角与求得的方位角进行比较得出与之最相近的坐标方位角，与之对应的平距就是错误边位置
			if(flag==1) {
				for (int i = 1; i < n; i++) {
					System.out.println(fwj[i] + "," + Af);
					if(Math.abs(fwj[i]-Af)<mix){
						mix=Math.abs(fwj[i]-Af);
						j=i;
					}
				}
				if(mix>10)
				{
					Af-=180;
					for (int i = 1; i < n; i++) {
						System.out.println(fwj[i] + "," + Af);
						if(Math.abs(fwj[i]-Af)<mix){
							mix=Math.abs(fwj[i]-Af);
							j=i;
						}
					}
				}
			}else {
				for (int i = 1; i < n; i++) {
					System.out.println(fwj[i] + "," + Af);
					if(Math.abs(fwj[i]-Af)<mix){
						mix=Math.abs(fwj[i]-Af);
						j=i;
						System.out.println(j+" "+mix);
					}
				}
				if(mix>10){
					Af-=180;
					while(Af<0){
						Af+=360;
					}
					for (int i = 0; i < n; i++) {
						System.out.println(fwj[i] + "," + Af);
						if(Math.abs(fwj[i]-Af)<mix){
							mix=Math.abs(fwj[i]-Af);
							j=i;
							System.out.println(j);
						}
					}
				}
			}
			wrongResult.setWrong("错误角边的平距为"+s[j-1]);
			System.out.println(s[1]+" "+s[0]);
			System.out.println(j);
		}
		super.save(wrongResult);
	}
	
	/**
	 * 更新状态
	 * @param wrongResult
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(WrongResult wrongResult) {
		super.updateStatus(wrongResult);
	}
	
	/**
	 * 删除数据
	 * @param wrongResult
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(WrongResult wrongResult) {
		super.delete(wrongResult);
	}
	/**
	 * 添加数据权限过滤条件
	 */
	@Override
	public void addDataScopeFilter(WrongResult wrongResult) {
		// 举例4：用户、员工（自己创建的）数据权限根据部门过滤，实体类@Table注解extWhereKeys="dsfOffice"
		wrongResult.getSqlMap().getDataScope().addFilter("dsfOffice", "Office",
				"u1.office_code", "a.create_by", DataScope.CTRL_PERMI_HAVE);
	}
}