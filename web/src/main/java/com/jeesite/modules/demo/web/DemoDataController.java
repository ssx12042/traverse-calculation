/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.demo.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.jeesite.modules.demo.entity.DemoData;
import com.jeesite.modules.demo.service.DemoDataService;

/**
 * 数据权限演示Controller
 * @author su
 * @version 2022-02-22
 */
@Controller
@RequestMapping(value = "${adminPath}/demo/demoData")
public class DemoDataController extends BaseController {

	@Autowired
	private DemoDataService demoDataService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public DemoData get(String id, boolean isNewRecord) {
		return demoDataService.get(id, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("demo:demoData:view")
	@RequestMapping(value = {"list", ""})
	public String list(DemoData demoData, Model model) {
		model.addAttribute("demoData", demoData);
		return "modules/demo/demoDataList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("demo:demoData:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<DemoData> listData(DemoData demoData, HttpServletRequest request, HttpServletResponse response) {
		demoData.setPage(new Page<>(request, response));
		//数据过滤
		demoDataService.addDataScopeFilter(demoData);
		Page<DemoData> page = demoDataService.findPage(demoData);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("demo:demoData:view")
	@RequestMapping(value = "form")
	public String form(DemoData demoData, Model model) {
		model.addAttribute("demoData", demoData);
		return "modules/demo/demoDataForm";
	}

	/**
	 * 保存数据权限演示
	 */
	@RequiresPermissions("demo:demoData:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated DemoData demoData) {
		demoDataService.save(demoData);
		return renderResult(Global.TRUE, text("保存数据权限演示成功！"));
	}
	
	/**
	 * 停用数据权限演示
	 */
	@RequiresPermissions("demo:demoData:edit")
	@RequestMapping(value = "disable")
	@ResponseBody
	public String disable(DemoData demoData) {
		demoData.setStatus(DemoData.STATUS_DISABLE);
		demoDataService.updateStatus(demoData);
		return renderResult(Global.TRUE, text("停用数据权限演示成功"));
	}
	
	/**
	 * 启用数据权限演示
	 */
	@RequiresPermissions("demo:demoData:edit")
	@RequestMapping(value = "enable")
	@ResponseBody
	public String enable(DemoData demoData) {
		demoData.setStatus(DemoData.STATUS_NORMAL);
		demoDataService.updateStatus(demoData);
		return renderResult(Global.TRUE, text("启用数据权限演示成功"));
	}
	
	/**
	 * 删除数据权限演示
	 */
	@RequiresPermissions("demo:demoData:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(DemoData demoData) {
		demoDataService.delete(demoData);
		return renderResult(Global.TRUE, text("删除数据权限演示成功！"));
	}
	
}