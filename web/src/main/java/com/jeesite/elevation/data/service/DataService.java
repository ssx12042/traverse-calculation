/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.elevation.data.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;

import com.jeesite.common.entity.DataScope;
import com.jeesite.elevation.data.entity.Data;
import com.jeesite.elevation.el_line.entity.ELLine;
import com.jeesite.elevation.el_line.service.ELLineService;
import com.jeesite.elevation.el_observe_data.entity.ElObserveData;
import com.jeesite.elevation.el_observe_data.service.ElObserveDataService;
import com.jeesite.elevation.el_station.entity.ELStation;
import com.jeesite.elevation.el_station.service.ELStationService;
import com.jeesite.modules.group.entity.Group;
import com.jeesite.modules.group.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.elevation.data.dao.DataDao;

/**
 * dataService
 * @author hu
 * @version 2022-06-16
 */
@Service
@Transactional(readOnly=true)
public class DataService extends CrudService<DataDao, Data> {
	@Autowired
	ELStationService elStationService;
	@Autowired
	GroupService groupService;
	@Autowired
	ELLineService elLineService;
	@Autowired
	ElObserveDataService elObserveDataService;
	/**
	 * 获取单条数据
	 * @param data
	 * @return
	 */
	@Override
	public Data get(Data data) {
		return super.get(data);
	}
	
	/**
	 * 查询分页数据
	 * @param data 查询条件
	 * @param data.page 分页对象
	 * @return
	 */
	@Override
	public Page<Data> findPage(Data data) {
		return super.findPage(data);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param data
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(Data data) {
		//获取小组id
		String groupId = data.getGroupId();
		//获取导线id
		String lineId = data.getLineId();
		String g=null;
		String l = null;
		Group group=new Group();
		List<Group> gList= groupService.findList(group);
		for (Group x:gList){
			if (groupId.equals(x.getId()))
				g=x.getGroupName();
			System.out.println(g);
		}
		ELLine line=new ELLine();
		List<ELLine> lList= elLineService.findList(line);
		for (ELLine x:lList){
			if (lineId.equals(x.getId()))
				l=x.getLineName();
			System.out.println(l);
			System.out.println(x.getLineName()+" "+x.getId()+" "+groupId);
		}
		ELStation elstation=new ELStation();
		elstation.setGroup(new Group(groupId));
		elstation.setLine(new ELLine(lineId));
		elstation.setLineId(lineId);
		List<ELStation>sList= elStationService.findList(elstation);
		ElObserveData observationData=new ElObserveData();
		observationData.setGroup(new Group(groupId));
		observationData.setLine(new ELLine(lineId));
		List<ElObserveData> oList=elObserveDataService.findList(observationData);
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
			String paths=getFilePath(data);
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
						observationData.setHeightInstrumentFore(Double.parseDouble(stnStr[1]));
						observationData.setHeightInstrumentBack(Double.parseDouble(stnStr[1]));
						for(ELStation sl:sList){
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
							elstation.setStationName(stnStr[0]);
							elStationService.save(elstation);
							observationData.setStationId(elstation.getId());
						}
						sflag=0;
						sList=elStationService.findList(elstation);
						for(ElObserveData ol:oList){
							for(ELStation sl:sList) {
								if(sl.getId().equals(ol.getStationId())) {
									sta =sl.getStationName().replaceAll("\\p{C}", "");
									stb = stnStr[0].replaceAll("\\p{C}", "");
									System.out.println(ol.getStationId() + " " + stnStr[0]);
									if (sta.equals(stb) && ocount < ol.getObserveRound()) {
										ocount = ol.getObserveRound();
										System.out.println("ocount" + ocount + " " + ol.getObserveRound());
									}
								}
							}
						}
						ocount++;
						observationData.setObserveRound(ocount);
						break;
					}
				}
				System.out.println("1");
				//继续遍历文件
				int stncnt=0;
				int hdcnt=0;
				int max=0;
				while (true) {
					elstation=new ELStation();
					elstation.setGroup(new Group(groupId));
					elstation.setLine(new ELLine(lineId));
					elstation.setLineId(lineId);
					sList=elStationService.findList(elstation);
					ocount=0;
//					System.out.println("flag"+data2.getLineId()+" "+data2.getGroupId()+" "+flag);
					str = reader.readLine();
					if(str == null){
						//save(stn);
						if (observationData.getHeightLensBack()==null&&observationData.getHeightLensFore()==null){
							break;
						}
						elObserveDataService.save(observationData);
						super.save(data);
						//lineNumber = reader.getLineNumber() - 1;
						//reader.setLineNumber(0);
						//重新初始化stn对象，代表又一项数据开始存入
						data = new Data();
						break;
					}  else if (str.contains("SS")) {
						String[] si = str.split("\\s+");
						String[] st = si[1].split(",");
						if(max==0) {
							observationData.setHeightLensFore(Double.parseDouble(String.format("%.3f", Double.parseDouble(st[1]))));
							System.out.println("stn++"+st[0]);
							for(ELStation sl:sList){
								System.out.println("slgetname"+sl.getStationName()+" "+st[0]);
								sta=sl.getStationName().replaceAll("\\p{C}", "");
								stb=st[0].replaceAll("\\p{C}", "");
								System.out.println(sl.getStatus().equals("0"));
								if(sta.equals(stb)&&sl.getStatus().equals("0")){
									sflag=1;
									observationData.setForeStnId(sl.getId());
									System.out.println("yyyys");
									break;
								}
							}
							if(sflag==0) {
								elstation.setStationName(st[0]);
								elStationService.save(elstation);
								observationData.setForeStnId(elstation.getId());
							}
							sflag=0;
						}else if(max==1){
							observationData.setHeightLensBack(Double.parseDouble(String.format("%.3f", Double.parseDouble(st[1]))));
							observationData.setBackStnId(st[0]);
							System.out.println("stn++"+st[0]);
							for(ELStation sl:sList){
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
								elstation.setStationName(st[0]);
								elStationService.save(elstation);
								observationData.setBackStnId(elstation.getId());
							}
							sflag=0;
						}
					}else if(str.contains("HV")){
						String[] si = str.split("\\s+");
						String[] st = si[1].split(",");
						double s1=0,s2 = 0;
						Deg = Long.valueOf((int) Double.parseDouble(st[0]));
						Min = (Double.parseDouble(st[0]) - Deg) * 60;
						Sec = Double.parseDouble(String.valueOf(Min-(int)Min)) * 60;
						if(hdcnt==0) {
							observationData.setVerticalAngDegFore(new Integer(String.valueOf(Deg)));
							observationData.setVerticalAngDegForeStr(String.valueOf(Deg));
							observationData.setVerticalAngMinFore(new Integer((int)Min));
							observationData.setVerticalAngSecFore(Sec);
							System.out.println("sssss"+st[0]+" "+Deg);
						}else if(hdcnt==1){
							observationData.setVerticalAngDegBack(new Integer(String.valueOf(Deg)));
							observationData.setVerticalAngDegBackStr(String.valueOf(Deg));
							observationData.setVerticalAngMinBack(new Integer((int)Min));
							observationData.setVerticalAngSecBack(Sec);
						}
					}
					else if (str.contains("SD")) {
						String[] si = str.split("\\s+");
						String[] st = si[1].split(",");
						double s1=0,s2 = 0;
						Deg = Long.valueOf((int) Double.parseDouble(st[1]));
						Min = (Double.parseDouble(st[1]) - Deg) * 60;
						Sec = Double.parseDouble(String.valueOf(Min-(int)Min)) * 60;
						if(stncnt==0) {
							observationData.setVerticalAngDegFore(new Integer(String.valueOf(Deg)));
							observationData.setVerticalAngDegForeStr(String.valueOf(Deg));
							observationData.setVerticalAngMinFore(new Integer((int)Min));
							observationData.setVerticalAngSecFore(Sec);
							observationData.setDistanceFore(Math.abs(Double.parseDouble(st[2])*Math.sin(Double.parseDouble(st[1]))));
							System.out.println("sssss"+st[0]+" "+Deg);
						}else if(stncnt==1){
							observationData.setVerticalAngDegBack(new Integer(String.valueOf(Deg)));
							observationData.setVerticalAngDegBackStr(String.valueOf(Deg));
							observationData.setVerticalAngMinBack(new Integer((int)Min));
							observationData.setVerticalAngSecBack(Sec);
							observationData.setDistanceBack(Math.abs(Double.parseDouble(st[2])*Math.sin(Double.parseDouble(st[1]))));
						}
						stncnt++;
						max=stncnt;
					}
					else if(str.contains("HD")){
						String[] si = str.split("\\s+");
						String[] st = si[1].split(",");
						double s1=0,s2 = 0;
						Deg = Long.valueOf((int) Double.parseDouble(st[0]));
						Min = (Double.parseDouble(st[0]) - Deg) * 60;
						Sec = Double.parseDouble(String.valueOf(Min-(int)Min)) * 60;
						if(hdcnt==0) {
							observationData.setDistanceFore(Double.parseDouble(st[1]));
							System.out.println("sssss"+st[0]+" "+Deg);
						}else if(hdcnt==1){
							observationData.setDistanceBack(Double.parseDouble(st[1]));
						}
						hdcnt++;
						max=hdcnt;
					}
					else if (str.contains("STN") ) { //遇到下一行也是以STN开头
						//save(stn);
						if(observationData.getHeightLensBack()==null&&observationData.getHeightLensFore()==null){
							continue;
						}
						System.out.println("================================");
						System.out.println(observationData.getStationId());
						System.out.println("================================");
						elObserveDataService.save(observationData);
						observationData=new ElObserveData();
						observationData.setGroup(new Group(groupId));
						observationData.setLine(new ELLine(lineId));
						oList=elObserveDataService.findList(observationData);
						super.save(data);
						//重新初始化stn对象，代表又一项数据开始存入
						data = new Data();
						stncnt=0;
						max=0;
						hdcnt=0;
						String[] s = str.split("\\s+");
						String[] stnStr = s[1].split(",");
						observationData.setHeightInstrumentFore(Double.parseDouble(stnStr[1]));
						observationData.setHeightInstrumentBack(Double.parseDouble(stnStr[1]));
						for(ELStation sl:sList){
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
							elstation.setStationName(stnStr[0]);
							elStationService.save(elstation);
							observationData.setStationId(elstation.getId());
						}
						sflag=0;
						sList=elStationService.findList(elstation);
						for(ElObserveData ol:oList){
							for(ELStation sl:sList) {
								if(sl.getId().equals(ol.getStationId())) {
									sta =sl.getStationName().replaceAll("\\p{C}", "");
									stb = stnStr[0].replaceAll("\\p{C}", "");
									System.out.println(ol.getStationId() + " " + stnStr[0]);
									if (sta.equals(stb) && ocount < ol.getObserveRound()) {
										ocount = ol.getObserveRound();
										System.out.println("ocount" + ocount + " " + ol.getObserveRound());
									}
								}
							}
						}
						ocount++;
						observationData.setObserveRound(ocount);
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
	 * @param data
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(Data data) {
		super.updateStatus(data);
	}
	
	/**
	 * 删除数据
	 * @param data
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(Data data) {
		super.delete(data);
	}
	/**
	 * 添加数据权限过滤条件
	 * 2022年5月17日 su添加
	 */
	@Override
	public void addDataScopeFilter(Data data) {
		// 举例4：用户、员工（自己创建的）数据权限根据部门过滤，实体类@Table注解extWhereKeys="dsfOffice"
		data.getSqlMap().getDataScope().addFilter("dsfOffice", "Office",
				"u1.office_code", "a.create_by", DataScope.CTRL_PERMI_HAVE);
	}
	/**
	 * 读取文件路径
	 */
	public String getFilePath(Data data) {
		String str=data.getSj();
		String si = str.replace("/js","D:/jeesite");
		System.out.println(si);
		return si;
	}
}