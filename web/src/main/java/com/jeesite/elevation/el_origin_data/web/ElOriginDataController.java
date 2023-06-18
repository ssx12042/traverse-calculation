/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.elevation.el_origin_data.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jeesite.elevation.el_line.entity.ELLine;
import com.jeesite.elevation.el_line.service.ELLineService;
import com.jeesite.elevation.el_origin_data.dao.ElOriginDataDao;
import com.jeesite.elevation.el_station.entity.ELStation;
import com.jeesite.elevation.el_station.service.ELStationService;
import com.jeesite.modules.group.entity.Group;
import com.jeesite.modules.group.service.GroupService;
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
import com.jeesite.elevation.el_origin_data.entity.ElOriginData;
import com.jeesite.elevation.el_origin_data.service.ElOriginDataService;

import java.util.LinkedList;
import java.util.List;

/**
 * 高程——起始数据Controller
 * @author su
 * @version 2022-05-30
 */
@Controller
@RequestMapping(value = "${adminPath}/el_origin_data/elOriginData")
public class ElOriginDataController extends BaseController {

	@Autowired
	private ElOriginDataService elOriginDataService;

	@Autowired
	private ElOriginDataDao elOriginDataDao;

	@Autowired
	private GroupService groupService;

	@Autowired
	private ELLineService elLineService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public ElOriginData get(String id, boolean isNewRecord) {
		return elOriginDataService.get(id, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("el_origin_data:elOriginData:view")
	@RequestMapping(value = {"list", ""})
	public String list(ElOriginData elOriginData, Model model) {
		// 先查询下拉框信息 2022年5月30日 su添加
		model = queryDropDownOptions(model);

		model.addAttribute("elOriginData", elOriginData);
		return "elevation/el_origin_data/elOriginDataList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("el_origin_data:elOriginData:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<ElOriginData> listData(ElOriginData elOriginData, HttpServletRequest request, HttpServletResponse response) {
		elOriginData.setPage(new Page<>(request, response));

		//数据权限过滤 2022年5月30日 su添加
		elOriginDataService.addDataScopeFilter(elOriginData);

		Page<ElOriginData> page = elOriginDataService.findPage(elOriginData);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("el_origin_data:elOriginData:view")
	@RequestMapping(value = "form")
	public String form(ElOriginData elOriginData, Model model) {
		// 跳转form表单页面时先查询下拉框信息 2022年5月30日 su添加
		model = queryDropDownOptions(model);

		model.addAttribute("elOriginData", elOriginData);
		return "elevation/el_origin_data/elOriginDataForm";
	}

	/**
	 * 保存高程——起始数据
	 */
	@RequiresPermissions("el_origin_data:elOriginData:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated ElOriginData elOriginData) {
		// 2022年6月7日 su修改
		String id = elOriginData.getId();
		// 如果是新数据，得查一下该导线是否已添加过起始数据
		if (id == null) {
			String lineId = elOriginData.getLine().getId();
			Boolean haveOriginData = isHaveOriginData(lineId);
			// 如果该导线已添加过起始数据
			if (haveOriginData) {
				return renderResult(Global.FALSE, text("该导线已经添加过起始数据啦，请换一条导线"));
			} else {
				elOriginDataService.save(elOriginData);
				return renderResult(Global.TRUE, text("保存高程——起始数据成功！"));
			}
		}
		// 如果是旧数据，直接更新
		else {
			elOriginDataService.save(elOriginData);
			return renderResult(Global.TRUE, text("更新高程——起始数据成功！"));
		}
	}
	
	/**
	 * 删除高程——起始数据
	 */
	@RequiresPermissions("el_origin_data:elOriginData:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(ElOriginData elOriginData) {
		elOriginDataService.delete(elOriginData);
		return renderResult(Global.TRUE, text("删除高程——起始数据成功！"));
	}

	/**
	 * 该导线是否已添加起始数据
	 * @param lineId 导线id
	 */
	public Boolean isHaveOriginData(String lineId) {
		ElOriginData _elod = new ElOriginData();
		_elod.setLine(new ELLine(lineId));
		_elod.setStatus(ElOriginData.STATUS_NORMAL);
		ElOriginData elOriginData = elOriginDataDao.getByEntity(_elod);
		return elOriginData != null;
	}

	/**
	 * 查询下拉框信息 2022年5月30日 su添加
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
	
}