/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.origin.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jeesite.modules.group.entity.Group;
import com.jeesite.modules.group.service.GroupService;
import com.jeesite.modules.line.entity.Line;
import com.jeesite.modules.line.service.LineService;
import com.jeesite.modules.origin.dao.OriginDataDao;
import com.jeesite.modules.station.entity.Station;
import com.jeesite.modules.station.service.StationService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.origin.entity.OriginData;
import com.jeesite.modules.origin.service.OriginDataService;

/**
 * 起始数据Controller
 * @author su
 * @version 2022-03-10
 */
@Controller
@RequestMapping(value = "${adminPath}/origin/originData")
public class OriginDataController extends BaseController {

	@Autowired
	private OriginDataService originDataService;

	@Autowired
	private GroupService groupService;

	@Autowired
	private LineService lineService;

	@Autowired
	private StationService stationService;

	@Autowired
	private OriginDataDao originDataDao;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public OriginData get(String id, boolean isNewRecord) {
		return originDataService.get(id, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("origin:originData:view")
	@RequestMapping(value = {"list", ""})
	public String list(OriginData originData, Model model) {
		// 查询下拉框信息 2022年3月11日 su添加
		model = queryDropDownOptions(model);

		model.addAttribute("originData", originData);
		return "modules/origin/originDataList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("origin:originData:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<OriginData> listData(OriginData originData, HttpServletRequest request, HttpServletResponse response) {
		originData.setPage(new Page<>(request, response));

		//数据权限过滤 2022年3月8日 su添加
		originDataService.addDataScopeFilter(originData);

		Page<OriginData> page = originDataService.findPage(originData);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("origin:originData:view")
	@RequestMapping(value = "form")
	public String form(OriginData originData, Model model) {
		// 跳转form表单页面时先查询下拉框信息 2022年3月11日 su添加
		model = queryDropDownOptions(model);

		model.addAttribute("originData", originData);
		return "modules/origin/originDataForm";
	}

	/**
	 * 保存起始数据
	 */
	@RequiresPermissions("origin:originData:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated OriginData originData) {
		// 2022年6月7日 su修改
		String id = originData.getId();
		// 如果是新数据，得查一下该导线是否已添加过起始数据
		if (id == null) {
			String lineId = originData.getLine().getId();
			Boolean haveOriginData = isHaveOriginData(lineId);
			// 如果该导线已添加过起始数据
			if (haveOriginData) {
				return renderResult(Global.FALSE, text("该导线已经添加过起始数据啦，请换一条导线"));
			} else {
				originDataService.save(originData);
				return renderResult(Global.TRUE, text("保存起始数据成功！"));
			}
		}
		// 如果是旧数据，直接更新
		else {
			originDataService.save(originData);
			return renderResult(Global.TRUE, text("保存起始数据成功！"));
		}

		// originDataService.save(originData);
		// return renderResult(Global.TRUE, text("保存起始数据成功！"));
	}
	
	/**
	 * 删除起始数据
	 */
	@RequiresPermissions("origin:originData:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(OriginData originData) {
		originDataService.delete(originData);
		return renderResult(Global.TRUE, text("删除起始数据成功！"));
	}

	/**
	 * 该导线是否已添加起始数据
	 * @param lineId 导线id
	 */
	public Boolean isHaveOriginData(String lineId) {
		OriginData _od = new OriginData();
		_od.setLine(new Line(lineId));
		_od.setStatus(OriginData.STATUS_NORMAL);
		OriginData elOriginData = originDataDao.getByEntity(_od);
		return elOriginData != null;
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

		//测站下拉
		// Station station = new Station();
		//数据过滤，只能看到自己添加的导线
		// stationService.addDataScopeFilter(station);

		model.addAttribute("groupList", groupService.findList(group));
		model.addAttribute("lineList", lineService.findList(line));
		// model.addAttribute("stationList", stationService.findList(station));

		return model;
	}
	
}