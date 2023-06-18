/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.elevation.data.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jeesite.elevation.data.entity.Data;
import com.jeesite.elevation.el_line.entity.ELLine;
import com.jeesite.elevation.el_line.service.ELLineService;
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
import com.jeesite.elevation.data.service.DataService;

/**
 * dataController
 * @author hu
 * @version 2022-06-16
 */
@Controller
@RequestMapping(value = "${adminPath}/data/data")
public class DataController extends BaseController {

	@Autowired
	private DataService dataService;
	@Autowired
	private GroupService groupService;
	@Autowired
	private ELLineService elLineService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public Data get(String id, boolean isNewRecord) {
		return dataService.get(id, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("data:data:view")
	@RequestMapping(value = {"list", ""})
	public String list(Data data, Model model) {
		model = queryDropDownOptions(model);
		model.addAttribute("data", data);
		return "elevation/data/dataList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("data:data:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<Data> listData(Data data, HttpServletRequest request, HttpServletResponse response) {
		data.setPage(new Page<>(request, response));
		dataService.addDataScopeFilter(data);
		Page<Data> page = dataService.findPage(data);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("data:data:view")
	@RequestMapping(value = "form")
	public String form(Data data, Model model) {
		model = queryDropDownOptions(model);
		model.addAttribute("data", data);
		return "elevation/data/dataForm";
	}

	/**
	 * 保存data
	 */
	@RequiresPermissions("data:data:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated Data data) {
		dataService.save(data);
		return renderResult(Global.TRUE, text("保存data成功！"));
	}
	
	/**
	 * 删除data
	 */
	@RequiresPermissions("data:data:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(Data data) {
		dataService.delete(data);
		return renderResult(Global.TRUE, text("删除data成功！"));
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
	
}