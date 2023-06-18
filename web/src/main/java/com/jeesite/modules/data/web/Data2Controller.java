/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.data.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jeesite.modules.data.entity.Data2;
import com.jeesite.modules.group.entity.Group;
import com.jeesite.modules.group.service.GroupService;
import com.jeesite.modules.line.entity.Line;
import com.jeesite.modules.line.service.LineService;
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
import com.jeesite.modules.data.service.Data2Service;

import java.util.ArrayList;
import java.util.List;

/**
 * data_2Controller
 * @author hu
 * @version 2022-06-07
 */
@Controller
@RequestMapping(value = "${adminPath}/data/data2")
public class Data2Controller extends BaseController {

	@Autowired
	private Data2Service data2Service;
	@Autowired
	private GroupService groupService;

	@Autowired
	private LineService lineService;
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public Data2 get(String id, boolean isNewRecord) {
		return data2Service.get(id, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("data:data2:view")
	@RequestMapping(value = {"list", ""})
	public String list(Data2 data2, Model model) {
		model = queryDropDownOptions(model);
		model.addAttribute("data2", data2);
		return "modules/data/data2List";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("data:data2:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<Data2> listData(Data2 data2, HttpServletRequest request, HttpServletResponse response) {
		data2.setPage(new Page<>(request, response));
		data2Service.addDataScopeFilter(data2);
		Page<Data2> page = data2Service.findPage(data2);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("data:data2:view")
	@RequestMapping(value = "form")
	public String form(Data2 data2, Model model) {
		model = queryDropDownOptions(model);
		model.addAttribute("data2", data2);
		return "modules/data/data2Form";
	}

	/**
	 * 保存data_2
	 */
	@RequiresPermissions("data:data2:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated Data2 data2) {
		data2Service.save(data2);
		return renderResult(Global.TRUE, text("保存data_2成功！"));
	}
	
	/**
	 * 删除data_2
	 */
	@RequiresPermissions("data:data2:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(Data2 data2) {
		data2Service.delete(data2);
		return renderResult(Global.TRUE, text("删除data_2成功！"));
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
	@RequiresPermissions("data:data2:view")
	@RequestMapping(value = "finddata2tListByLineId")
	public List<Data2> findWrongResultListByLineId(String lineId){
		List<Data2> data2sList = new ArrayList<>();
		//如果接收到的lineId为空，则返回空的stationList
		if (!"".equals(lineId)) {
			Data2 data2 = new Data2();
			data2.setLineId(lineId);
			data2sList = data2Service.findList(data2);
		}
		return data2sList;
	}

}