/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.elevation.el_station.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jeesite.elevation.el_line.dao.ELLineDao;
import com.jeesite.elevation.el_line.entity.ELLine;
import com.jeesite.elevation.el_line.service.ELLineService;
import com.jeesite.elevation.el_origin_data.dao.ElOriginDataDao;
import com.jeesite.elevation.el_origin_data.entity.ElOriginData;
import com.jeesite.elevation.el_station.dao.ELStationDao;
import com.jeesite.modules.group.entity.Group;
import com.jeesite.modules.group.service.GroupService;
import com.jeesite.modules.line.entity.Line;
import com.jeesite.modules.station.entity.Station;
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
import com.jeesite.elevation.el_station.entity.ELStation;
import com.jeesite.elevation.el_station.service.ELStationService;

import java.util.ArrayList;
import java.util.List;

/**
 * 高程——测站Controller
 * @author su
 * @version 2022-05-17
 */
@Controller
@RequestMapping(value = "${adminPath}/el_station/eLStation")
public class ELStationController extends BaseController {

	@Autowired
	private ELStationService eLStationService;

	@Autowired
	private ELStationDao elStationDao;

	@Autowired
	private GroupService groupService;

	@Autowired
	private ELLineService elLineService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public ELStation get(String id, boolean isNewRecord) {
		return eLStationService.get(id, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("el_station:eLStation:view")
	@RequestMapping(value = {"list", ""})
	public String list(ELStation eLStation, Model model) {
		// 查询下拉框信息 2022年5月17日 su添加
		model = queryDropDownOptions(model);

		model.addAttribute("eLStation", eLStation);
		return "elevation/el_station/eLStationList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("el_station:eLStation:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<ELStation> listData(ELStation eLStation, HttpServletRequest request, HttpServletResponse response) {
		eLStation.setPage(new Page<>(request, response));

		//数据权限过滤 2022年5月17日 su添加
		eLStationService.addDataScopeFilter(eLStation);

		Page<ELStation> page = eLStationService.findPage(eLStation);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("el_station:eLStation:view")
	@RequestMapping(value = "form")
	public String form(ELStation eLStation, Model model) {
		// 跳转form表单页面时先查询下拉框信息 2022年5月17日 su添加
		model = queryDropDownOptions(model);

		model.addAttribute("eLStation", eLStation);
		return "elevation/el_station/eLStationForm";
	}

	/**
	 * 编辑表单
	 * 2022年5月30日 su添加
	 */
	@RequiresPermissions("el_station:eLStation:view")
	@RequestMapping(value = "editForm")
	public String editForm(ELStation eLStation, Model model) {
		// 跳转form表单页面时先查询下拉框信息 2022年5月17日 su添加
		model = queryDropDownOptions(model);

		model.addAttribute("eLStation", eLStation);
		return "elevation/el_station/eLStationFormEdit";
	}

	/**
	 * 保存高程——测站链
	 * 2022年5月17日 su修改
	 */
	@RequiresPermissions("el_station:eLStation:edit")
	@PostMapping(value = "save")
	@ResponseBody
	@Transactional(readOnly=false)
	// public String save(@Validated ELStation eLStation) {
	public String save(String lineId, String stationsStr) {
		String[] stations_name = stationsStr.split(",");
		// 先save，会有id
		for (String sta_name : stations_name) {
			ELStation elStation = new ELStation();
			elStation.setLineId(lineId);
			elStation.setStationName(sta_name);
			eLStationService.save(elStation);
		}
		// 再取出来
		ELStation elStation = new ELStation();
		elStation.setStatus(ELStation.STATUS_NORMAL);
		elStation.setLineId(lineId);
		List<ELStation> elStations = eLStationService.findList(elStation);
		// 再存储测站的前后站id
		// 起始站的前后站
		elStations.get(0).setForeStnId(elStations.get(1).getId());
		elStations.get(0).setBackStnId("0");
		// 终点站的前后站
		elStations.get(elStations.size() - 1).setForeStnId("0");
		elStations.get(elStations.size() - 1).setBackStnId(elStations.get(elStations.size() - 2).getId());
		// 中间站的前后站
		for (int i = 1; i < elStations.size() - 1; i++) {
			elStations.get(i).setForeStnId(elStations.get(i + 1).getId());
			elStations.get(i).setBackStnId(elStations.get(i - 1).getId());
		}
		// 更新测站
		for (ELStation sta : elStations) {
			eLStationService.save(sta);
		}
		// eLStationService.save(eLStation);
		return renderResult(Global.TRUE, text("保存高程——测站链成功！"));
	}

	/**
	 * 更新高程——测站
	 * 2022年5月30日 su添加
	 */
	@RequiresPermissions("el_station:eLStation:edit")
	@PostMapping(value = "saveStation")
	@ResponseBody
	public String saveStation(@Validated ELStation eLStation) {
		eLStationService.save(eLStation);
		return renderResult(Global.TRUE, text("更新高程——测站成功！"));
	}
	
	/**
	 * 删除高程——测站链
	 */
	@RequiresPermissions("el_station:eLStation:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(ELStation eLStation) {
		eLStationService.delete(eLStation);
		return renderResult(Global.TRUE, text("删除高程——测站链成功！"));
	}

	/**
	 * 查询下拉框信息 2022年5月17日 su添加
	 * @param model
	 */
	public Model queryDropDownOptions(Model model) {
		// 小组下拉
		Group group = new Group();
		//数据过滤，只能看到自己添加的小组
		groupService.addDataScopeFilter(group);

		//导线下拉
		ELLine elLine = new ELLine();
		//数据过滤，只能看到自己添加的导线
		elLineService.addDataScopeFilter(elLine);

		model.addAttribute("groupList", groupService.findList(group));
		model.addAttribute("lineList", elLineService.findList(elLine));

		return model;
	}

	/**
	 * 根据导线ID查询测站 2022年5月17日 su添加
	 * @param lineId 导线id
	 * @return 测站数组
	 */
	@RequiresPermissions("el_station:eLStation:view")
	@RequestMapping(value = "findStationListByLineId")
	public List<ELStation> findStationListByLineId(String lineId){
		List<ELStation> elStations = new ArrayList<>();
		//如果接收到的lineId为空，则返回空的elStations
		if (!"".equals(lineId)) {
			ELStation elStation = new ELStation();
			elStation.setStatus(ELStation.STATUS_NORMAL);
			elStation.setLineId(lineId);
			elStations = eLStationService.findList(elStation);
		}
		return elStations;
	}

	/**
	 * 根据导线ID查询起始和终点测站 2022年5月30日 su添加
	 * @param lineId 导线id
	 * @return 测站数组
	 */
	@RequiresPermissions("el_station:eLStation:view")
	@RequestMapping(value = "findOriginEndStaByLineId")
	public List<ELStation> findOriginEndStaByLineId(String lineId){
		List<ELStation> elStations = new ArrayList<>();
		//如果接收到的lineId为空，则返回空的elStations
		if (!"".equals(lineId)) {
			ELStation elStation = new ELStation();
			elStation.setStatus(ELStation.STATUS_NORMAL);
			elStation.setLineId(lineId);
			elStation.setBackStnId("0");
			ELStation originStation = elStationDao.getByEntity(elStation);
			elStations.add(originStation);

			elStation = new ELStation();
			elStation.setStatus(ELStation.STATUS_NORMAL);
			elStation.setLineId(lineId);
			elStation.setForeStnId("0");
			ELStation endStation = elStationDao.getByEntity(elStation);
			elStations.add(endStation);
		}
		return elStations;
	}

	/**
	 * 根据测站ID查询前后测站 2022年5月23日 su添加
	 * @param stationId 测站id
	 * @return 前后测站数组
	 */
	@RequiresPermissions("el_station:eLStation:view")
	@RequestMapping(value = "findForeBackStaByStaId")
	public List<ELStation> findForeBackStaByStaId(String stationId){
		List<ELStation> elStations = new ArrayList<>();
		//如果接收到的stationId为空，则返回空的elStations
		if (!"".equals(stationId)) {
			// 获取当前测站
			ELStation _elSta = new ELStation();
			_elSta.setStatus(ELStation.STATUS_NORMAL);
			_elSta.setId(stationId);
			ELStation elStation = elStationDao.getByEntity(_elSta);
			String foreStnId = elStation.getForeStnId();
			String backStnId = elStation.getBackStnId();
			// 获取前站
			_elSta = new ELStation();
			_elSta.setStatus(ELStation.STATUS_NORMAL);
			_elSta.setId(foreStnId);
			ELStation foreStation = elStationDao.getByEntity(_elSta);
			elStations.add(foreStation);
			// 获取后站
			_elSta = new ELStation();
			_elSta.setStatus(ELStation.STATUS_NORMAL);
			_elSta.setId(backStnId);
			ELStation backStation = elStationDao.getByEntity(_elSta);
			elStations.add(backStation);
		}
		return elStations;
	}

	/**
	 * 根据测站ID查询测站 2022年5月23日 su添加
	 * @param stationId 测站id
	 * @return 测站
	 */
	@RequiresPermissions("el_station:eLStation:view")
	@RequestMapping(value = "getStaByStaId")
	public ELStation getStaByStaId(String stationId){
		ELStation elStation = new ELStation();
		//如果接收到的stationId为空，则返回空的测站名称
		if (!"".equals(stationId)) {
			// 获取当前测站
			ELStation _elSta = new ELStation();
			_elSta.setId(stationId);
			elStation = elStationDao.getByEntity(_elSta);
		}
		return elStation;
	}
	
}