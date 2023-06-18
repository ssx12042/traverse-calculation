/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.station.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.druid.support.spring.stat.annotation.Stat;
import com.jeesite.modules.group.entity.Group;
import com.jeesite.modules.group.service.GroupService;
import com.jeesite.modules.line.entity.Line;
import com.jeesite.modules.line.service.LineService;
import com.jeesite.modules.station.dao.StationDao;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.station.entity.Station;
import com.jeesite.modules.station.service.StationService;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 测站Controller
 * @author su
 * @version 2022-03-01
 */
@Controller
@RequestMapping(value = "${adminPath}/station/station")
public class StationController extends BaseController {

	@Autowired
	private StationService stationService;

	@Autowired
	private GroupService groupService;

	@Autowired
	private LineService lineService;

	@Autowired
	private StationDao stationDao;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public Station get(String id, boolean isNewRecord) {
		return stationService.get(id, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("station:station:view")
	@RequestMapping(value = {"list", ""})
	public String list(Station station, Model model) {
		// 查询下拉框信息 2022年3月8日 su添加
		model = queryDropDownOptions(model);

		model.addAttribute("station", station);
		return "modules/station/stationList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("station:station:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<Station> listData(Station station, HttpServletRequest request, HttpServletResponse response) {
		station.setPage(new Page<>(request, response));

		//数据权限过滤 2022年3月8日 su添加
		stationService.addDataScopeFilter(station);

		Page<Station> page = stationService.findPage(station);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("station:station:view")
	@RequestMapping(value = "form")
	public String form(Station station, Model model) {
		// 跳转form表单页面时先查询下拉框信息 2022年3月8日 su添加
		model = queryDropDownOptions(model);

		model.addAttribute("station", station);
		return "modules/station/stationForm";
	}

	/**
	 * 测站链表单
	 */
	@RequiresPermissions("station:station:view")
	@RequestMapping(value = "chainForm")
	public String chainForm(Station station, Model model) {
		// 跳转form表单页面时先查询下拉框信息 2022年3月8日 su添加
		model = queryDropDownOptions(model);

		model.addAttribute("station", station);
		return "modules/station/stationChainForm";
	}

	/**
	 * 保存测站
	 */
	@RequiresPermissions("station:station:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated Station station) {
		stationService.save(station);
		return renderResult(Global.TRUE, text("保存测站成功！"));
	}

	/**
	 * 保存测站链
	 */
	@RequiresPermissions("station:station:edit")
	@PostMapping(value = "saveStationChain")
	@ResponseBody
	@Transactional(readOnly=false)
	public String saveStationChain(String lineId, String stationsStr) {
		List<Station> originEndSta = findOriginEndStaByLineId(lineId);
		if (originEndSta.get(0) != null) {
			return renderResult(Global.FALSE, text("该导线已经创建过测站链啦，请换一条导线"));
		}
		String[] stations_name = stationsStr.split(",");
		// 先save，会有id
		for (String sta_name : stations_name) {
			Station station = new Station();
			station.setLineId(lineId);
			station.setStationName(sta_name);
			stationService.save(station);
		}
		// 再取出来
		Station station = new Station();
		station.setStatus(Station.STATUS_NORMAL);
		station.setLineId(lineId);
		List<Station> stations = stationService.findList(station);
		// 再存储测站的前后站id
		// 起始站的前后站
		stations.get(0).setForeStnId(stations.get(1).getId());
		stations.get(0).setBackStnId("0");
		// 终点站的前后站
		stations.get(stations.size() - 1).setForeStnId("0");
		stations.get(stations.size() - 1).setBackStnId(stations.get(stations.size() - 2).getId());
		// 中间站的前后站
		for (int i = 1; i < stations.size() - 1; i++) {
			stations.get(i).setForeStnId(stations.get(i + 1).getId());
			stations.get(i).setBackStnId(stations.get(i - 1).getId());
		}
		// 更新测站
		for (Station sta : stations) {
			stationService.save(sta);
		}

		// stationService.save(station);
		return renderResult(Global.TRUE, text("保存测站链成功！"));
	}
	
	/**
	 * 删除测站
	 */
	@RequiresPermissions("station:station:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(Station station) {
		stationService.delete(station);
		return renderResult(Global.TRUE, text("删除测站成功！"));
	}

	/**
	 * 查询下拉框信息 2022年3月8日 su添加
	 * @param model
	 */
	public Model queryDropDownOptions(Model model) {
		// 小组下拉
		Group group = new Group();
		//数据过滤，只能看到自己添加的小组
		groupService.addDataScopeFilter(group);

		//导线下拉
		Line line = new Line();
		//数据过滤，只能看到自己添加的导线
		lineService.addDataScopeFilter(line);

		model.addAttribute("groupList", groupService.findList(group));
		model.addAttribute("lineList", lineService.findList(line));

		return model;
	}

	/**
	 * 根据导线ID查询测站 2022年3月11日 su添加
	 * @param lineId 导线id
	 * @return 测站数组
	 */
	@RequiresPermissions("station:station:view")
	@RequestMapping(value = "findStationListByLineId")
	public List<Station> findStationListByLineId(String lineId){
		List<Station> stationList = new ArrayList<>();
		//如果接收到的lineId为空，则返回空的stationList
		if (!"".equals(lineId)) {
			Station station = new Station();
			station.setStatus(Station.STATUS_NORMAL);
			station.setLineId(lineId);
			stationList = stationService.findList(station);
		}
		return stationList;
	}

	/**
	 * 根据导线ID查询起始和终点测站 2022年5月30日 su添加
	 * @param lineId 导线id
	 * @return 测站数组
	 */
	@RequiresPermissions("station:station:view")
	@RequestMapping(value = "findOriginEndStaByLineId")
	public List<Station> findOriginEndStaByLineId(String lineId){
		List<Station> elStations = new ArrayList<>();
		//如果接收到的lineId为空，则返回空的stations
		if (!"".equals(lineId)) {
			Station station = new Station();
			station.setStatus(Station.STATUS_NORMAL);
			station.setLineId(lineId);
			station.setBackStnId("0");
			Station originStation = stationDao.getByEntity(station);
			elStations.add(originStation);

			station = new Station();
			station.setStatus(Station.STATUS_NORMAL);
			station.setLineId(lineId);
			station.setForeStnId("0");
			Station endStation = stationDao.getByEntity(station);
			elStations.add(endStation);
		}
		return elStations;
	}

	/**
	 * 根据测站ID查询前后测站 2022年5月23日 su添加
	 * @param stationId 测站id
	 * @return 前后测站数组
	 */
	@RequiresPermissions("station:station:view")
	@RequestMapping(value = "findForeBackStaByStaId")
	public List<Station> findForeBackStaByStaId(String stationId){
		List<Station> stations = new ArrayList<>();
		//如果接收到的stationId为空，则返回空的stations
		if (!"".equals(stationId)) {
			// 获取当前测站
			Station _sta = new Station();
			_sta.setStatus(Station.STATUS_NORMAL);
			_sta.setId(stationId);
			Station elStation = stationDao.getByEntity(_sta);
			String foreStnId = elStation.getForeStnId();
			String backStnId = elStation.getBackStnId();
			// 获取前站
			_sta = new Station();
			_sta.setStatus(Station.STATUS_NORMAL);
			_sta.setId(foreStnId);
			Station foreStation = stationDao.getByEntity(_sta);
			stations.add(foreStation);
			// 获取后站
			_sta = new Station();
			_sta.setStatus(Station.STATUS_NORMAL);
			_sta.setId(backStnId);
			Station backStation = stationDao.getByEntity(_sta);
			stations.add(backStation);
		}
		return stations;
	}

	/**
	 * 根据测站ID查询测站 2022年5月23日 su添加
	 * @param stationId 测站id
	 * @return 测站
	 */
	@RequiresPermissions("station:station:view")
	@RequestMapping(value = "getStaByStaId")
	public Station getStaByStaId(String stationId){
		Station station = new Station();
		//如果接收到的stationId为空，则返回空的测站名称
		if (!"".equals(stationId)) {
			// 获取当前测站
			Station _sta = new Station();
			_sta.setId(stationId);
			station = stationDao.getByEntity(_sta);
		}
		return station;
	}
	
}