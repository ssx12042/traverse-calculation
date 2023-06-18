/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.data.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;

import com.jeesite.common.entity.DataScope;
import com.jeesite.modules.data.dao.Data2Dao;
import com.jeesite.modules.data.entity.Data2;
import com.jeesite.modules.group.entity.Group;
import com.jeesite.modules.group.service.GroupService;
import com.jeesite.modules.line.entity.Line;
import com.jeesite.modules.line.service.LineService;
import com.jeesite.modules.observation.entity.ObservationData;
import com.jeesite.modules.observation.service.ObservationDataService;
import com.jeesite.modules.station.entity.Station;
import com.jeesite.modules.station.service.StationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;

/**
 * data_2Service
 * @author hu
 * @version 2022-05-30
 */
@Service
@Transactional(readOnly=true)
public class Data2Service extends CrudService<Data2Dao, Data2> {
	@Autowired
	GroupService groupService;
	@Autowired
	LineService lineService;
	@Autowired
	StationService stationService;
	@Autowired
	ObservationDataService observationDataService;
	/**
	 * 获取单条数据
	 * @param data2
	 * @return
	 */
	@Override
	public Data2 get(Data2 data2) {
		return super.get(data2);
	}

	/**
	 * 查询分页数据
	 * @param data2 查询条件
	 * @param data2.page 分页对象
	 * @return
	 */
	@Override
	public Page<Data2> findPage(Data2 data2) {
		return super.findPage(data2);
	}

	/**
	 * 保存数据（插入或更新）
	 * @param data2
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(Data2 data2) {
		//获取小组id
		String groupId = data2.getGroupId();
		//获取导线id
		String lineId = data2.getLineId();
		String g=null;
		String l = null;
		Group group=new Group();
		List<Group> gList= groupService.findList(group);
		for (Group x:gList){
			if (groupId.equals(x.getId()))
			g=x.getGroupName();
			System.out.println(g);
		}
		Line line=new Line();
		List<Line> lList= lineService.findList(line);
		for (Line x:lList){
			if (lineId.equals(x.getId()))
				l=x.getLineName();
			System.out.println(l);
			System.out.println(x.getLineName()+" "+x.getId()+" "+groupId);
		}
		Station station=new Station();
		station.setGroup(new Group(groupId));
		station.setLine(new Line(lineId));
		station.setLineId(lineId);
		List<Station>sList= stationService.findList(station);
		ObservationData observationData=new ObservationData();
		observationData.setGroup(new Group(groupId));
		observationData.setLine(new Line(lineId));
		List<ObservationData> oList=observationDataService.findList(observationData);
		Long Deg; 		// 度数
		double Min; 		// 分
		double Sec; 		// 秒
		long m;
		double x;
		int sflag=0;
		int ocount=0;
		String sta=null,stb=null;
		try {
			//获取文件路径，并获取文件数量
			String paths=getFilePath(data2);
			String[] path = paths.split("[|]");
			int cnt=path.length;
			// 初始化化BufferedReader输入流
			BufferedReader[]readers=new BufferedReader[cnt];
			String str;
			System.out.println(cnt);
			for (int i=0;i<cnt;i++){
				System.out.println(path[i]);
				readers[i] = new BufferedReader(new FileReader(path[i]));
			}
			System.out.println();
			for (BufferedReader reader:readers){
				ocount=0;
				str = null;
				//先遍历文件的开头，直到遇到第一个STN行
				while ((str = reader.readLine()) != null) {
					if (str.contains("STN")) {  //直到遇到第一个STN行
						String[] s = str.split("\\s+");
						String[] stnStr = s[1].split(",");
					//	stn.setYigao(Double.parseDouble(stnStr[1]));
						for(Station sl:sList){
							System.out.println("slgetname"+sl.getStationName()+" "+stnStr[0]);
							sta=sl.getStationName().replaceAll("\\p{C}", "");
							stb=stnStr[0].replaceAll("\\p{C}", "");
							System.out.println(sl.getStatus().equals("0"));
							if(sta.equals(stb)&&sl.getStatus().equals("0")){
								sflag=1;
								observationData.setStationId(sl.getId());
								System.out.println("sl.getid"+sl.getId()+" "+observationData.getStationId());
								break;
							}
						}
						if(sflag==0) {
							station.setStationName(stnStr[0]);
							stationService.save(station);
							observationData.setStationId(station.getId());
						}
						sflag=0;
						sList=stationService.findList(station);
						for(ObservationData ol:oList){
							for(Station sl:sList) {
								if(sl.getId().equals(ol.getStationId())) {
									sta =sl.getStationName().replaceAll("\\p{C}", "");
									stb = stnStr[0].replaceAll("\\p{C}", "");
									System.out.println(ol.getStationId() + " " + stnStr[0]);
									if (sta.equals(stb) && ocount < ol.getObservationRound()) {
										ocount = ol.getObservationRound();
										System.out.println("ocount" + ocount + " " + ol.getObservationRound());
									}
								}
							}
						}
						ocount++;
						observationData.setObservationRound(ocount);
						break;
					}
				}
				System.out.println("1");
				//继续遍历文件
				int stncnt=0;
				int hdcnt=0;
				int max=0;
				while (true) {
					station=new Station();
					station.setGroup(new Group(groupId));
					station.setLine(new Line(lineId));
					station.setLineId(lineId);
					sList=stationService.findList(station);
					ocount=0;
//					System.out.println("flag"+data2.getLineId()+" "+data2.getGroupId()+" "+flag);
					str = reader.readLine();
					if(str == null){
						//save(stn);
						if (observationData.getForeLeftDeg()==null){
							break;
						}
						observationDataService.save(observationData);
						super.save(data2);
						//lineNumber = reader.getLineNumber() - 1;
						//reader.setLineNumber(0);
						//重新初始化stn对象，代表又一项数据开始存入
						data2 = new Data2();
						break;
					}  else if (str.contains("SS")) {
						String[] si = str.split("\\s+");
						String[] st = si[1].split(",");
						//	stn.setJingggao(Double.parseDouble(String.format("%.3f", Double.parseDouble(st[1]))));
						if(max==0) {
							System.out.println("stn++"+st[0]);
							for(Station sl:sList){
								System.out.println("slgetname"+sl.getStationName()+" "+st[0]);
								sta=sl.getStationName().replaceAll("\\p{C}", "");
								stb=st[0].replaceAll("\\p{C}", "");
								System.out.println(sl.getStatus().equals("0"));
								if(sta.equals(stb)&&sl.getStatus().equals("0")){
									sflag=1;
									observationData.setForeStnId(sl.getId());
									break;
								}
							}
							if(sflag==0) {
								station.setStationName(st[0]);
								stationService.save(station);
								observationData.setForeStnId(station.getId());
							}
							sflag=0;
						}else if(max==1){
							observationData.setBackStnId(st[0]);
							System.out.println("stn++"+st[0]);
							for(Station sl:sList){
								System.out.println("slgetname"+sl.getStationName()+" "+st[0]);
								sta=sl.getStationName().replaceAll("\\p{C}", "");
								stb=st[0].replaceAll("\\p{C}", "");
								System.out.println(sl.getStatus().equals("0"));
								if(sta.equals(stb)&&sl.getStatus().equals("0")){
									sflag=1;
									observationData.setBackStnId(sl.getId());
									break;
								}
							}
							if(sflag==0) {
								station.setStationName(st[0]);
								stationService.save(station);
								observationData.setBackStnId(station.getId());
							}
							sflag=0;
						}

					}  else if (str.contains("SD")) {
						String[] si = str.split("\\s+");
						String[] st = si[1].split(",");
						double s1=0,s2 = 0;
						Deg = Long.valueOf((int) Double.parseDouble(st[0]));
						Min = (Double.parseDouble(st[0]) - Deg) * 60;
						Sec = Double.parseDouble(String.valueOf(Min-(int)Min)) * 60;
						if(stncnt==0) {
							observationData.setForeLeftDeg(new Integer(String.valueOf(Deg)));
							observationData.setForeLeftMin(new Integer((int)Min));
							observationData.setForeLeftSec(Sec);
							s1=Sec;
							observationData.setForeDistance(Math.abs(Double.parseDouble(st[2])*Math.sin(Double.parseDouble(st[1]))));
							System.out.println("sssss"+st[0]+" "+Deg);
						}else if(stncnt==1){
							observationData.setBackLeftDeg(new Integer(String.valueOf(Deg)));
							observationData.setBackLeftMin(new Integer(new Integer((int)Min)));
							observationData.setBackLeftSec(Sec);
							System.out.println("obbbb"+observationData.getBackLeftDeg()+" "+observationData.getBackLeftMin()+" "+observationData.getBackLeftSec());
							s2=Sec;
							observationData.setBackDistance(Math.abs(Double.parseDouble(st[2])*Math.sin(Double.parseDouble(st[1]))));
						}else if(stncnt==2){
							observationData.setBackRightDeg(new Integer(String.valueOf(Deg)));
							observationData.setBackRightMin(new Integer(new Integer((int)Min)));
							observationData.setBackRightSec(Sec);
							System.out.println("zzzz"+Deg+" "+Min+" "+Sec+" "+Double.parseDouble(st[0]));
						}else if(stncnt==3){
							observationData.setForeRightDeg(new Integer(String.valueOf(Deg)));
							observationData.setForeRightMin(new Integer(new Integer((int)Min)));
							observationData.setForeRightSec(Sec);
						}
						stncnt++;
							max=stncnt;
					}else if(str.contains("HD")){
						String[] si = str.split("\\s+");
						String[] st = si[1].split(",");
						double s1=0,s2 = 0;
						Deg = Long.valueOf((int) Double.parseDouble(st[0]));
						Min = (Double.parseDouble(st[0]) - Deg) * 60;
						Sec = Double.parseDouble(String.valueOf(Min-(int)Min)) * 60;
						if(hdcnt==0) {
							observationData.setForeLeftDeg(new Integer(String.valueOf(Deg)));
							observationData.setForeLeftMin(new Integer((int)Min));
							observationData.setForeLeftSec(Sec);
							s1=Sec;
							observationData.setForeDistance(Double.parseDouble(st[1]));
							System.out.println("sssss"+st[0]+" "+Deg);
						}else if(hdcnt==1){
							observationData.setBackLeftDeg(new Integer(String.valueOf(Deg)));
							observationData.setBackLeftMin(new Integer(new Integer((int)Min)));
							observationData.setBackLeftSec(Sec);
							System.out.println("obbbb"+observationData.getBackLeftDeg()+" "+observationData.getBackLeftMin()+" "+observationData.getBackLeftSec());
							s2=Sec;
							observationData.setBackDistance(Double.parseDouble(st[1]));
						}else if(hdcnt==2){
							observationData.setBackRightDeg(new Integer(String.valueOf(Deg)));
							observationData.setBackRightMin(new Integer(new Integer((int)Min)));
							observationData.setBackRightSec(Sec);
							System.out.println("zzzz"+Deg+" "+Min+" "+Sec+" "+Double.parseDouble(st[0]));
						}else if(hdcnt==3){
							observationData.setForeRightDeg(new Integer(String.valueOf(Deg)));
							observationData.setForeRightMin(new Integer(new Integer((int)Min)));
							observationData.setForeRightSec(Sec);
						}
						hdcnt++;
							max=hdcnt;
					}
					else if (str.contains("STN") ) { //遇到下一行也是以STN开头
						//save(stn);
						data2.setLineName(l);
						data2.setGroupName(g);
						System.out.println("sss"+data2.getGroupName()+" "+data2.getLineName());
						if(observationData.getForeLeftDeg()==null){
							continue;
						}
						System.out.println("================================");
						System.out.println(observationData.getStationId());
						System.out.println("================================");
						observationDataService.save(observationData);
						observationData=new ObservationData();
						observationData.setGroup(new Group(groupId));
						observationData.setLine(new Line(lineId));
						oList=observationDataService.findList(observationData);
						super.save(data2);
						//重新初始化stn对象，代表又一项数据开始存入
						data2 = new Data2();
						stncnt=0;
						max=0;
						hdcnt=0;
						String[] s = str.split("\\s+");
						String[] stnStr = s[1].split(",");
						for(Station sl:sList){
							System.out.println("slgetname"+sl.getStationName()+" "+stnStr[0]);
							sta=sl.getStationName().replaceAll("\\p{C}", "");
							stb=stnStr[0].replaceAll("\\p{C}", "");
							System.out.println(sl.getStatus().equals("0"));
							if(sta.equals(stb)&&sl.getStatus().equals("0")){
								sflag=1;
								observationData.setStationId(sl.getId());
								break;
							}
						}
						if(sflag==0) {
							station.setStationName(stnStr[0]);
							stationService.save(station);
							observationData.setStationId(station.getId());
						}
						sflag=0;
						sList=stationService.findList(station);
						for(ObservationData ol:oList){
							for(Station sl:sList) {
								if(sl.getId().equals(ol.getStationId())) {
									sta =sl.getStationName().replaceAll("\\p{C}", "");
									stb = stnStr[0].replaceAll("\\p{C}", "");
									System.out.println(ol.getStationId() + " " + stnStr[0]);
									if (sta.equals(stb) && ocount < ol.getObservationRound()) {
										ocount = ol.getObservationRound();
										System.out.println("ocount" + ocount + " " + ol.getObservationRound());
									}
								}
							}
						}
						ocount++;
						observationData.setObservationRound(ocount);
					}

				}
//				reader.close();  //关闭输入流
//				in.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 更新状态
	 * @param data2
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(Data2 data2) {
		super.updateStatus(data2);
	}

	/**
	 * 删除数据
	 * @param data2
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(Data2 data2) {
		super.delete(data2);
	}
	/**
	 * 读取文件路径
	 */
	public String getFilePath(Data2 data2) {
		String str=data2.getSj();
		String si = str.replace("/js","D:/jeesite");
		System.out.println(si);
		return si;
	}
	/**
	 * 添加数据权限过滤条件
	 */
	@Override
	public void addDataScopeFilter(Data2 data2) {
		// 举例4：用户、员工（自己创建的）数据权限根据部门过滤，实体类@Table注解extWhereKeys="dsfOffice"
		data2.getSqlMap().getDataScope().addFilter("dsfOffice", "Office",
				"u1.office_code", "a.create_by", DataScope.CTRL_PERMI_HAVE);
	}
}