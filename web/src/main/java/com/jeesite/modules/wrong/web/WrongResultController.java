/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.wrong.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jeesite.modules.group.entity.Group;
import com.jeesite.modules.group.service.GroupService;
import com.jeesite.modules.line.entity.Line;
import com.jeesite.modules.line.service.LineService;
import com.jeesite.modules.station.entity.Station;
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
import com.jeesite.modules.wrong.entity.WrongResult;
import com.jeesite.modules.wrong.service.WrongResultService;

import java.util.ArrayList;
import java.util.List;

/**
 * 错误边角查询Controller
 * @author hu
 * @version 2022-05-03
 */
@Controller
@RequestMapping(value = "${adminPath}/wrong/wrongResult")
public class WrongResultController extends BaseController {

	@Autowired
	private WrongResultService wrongResultService;
	@Autowired
	private GroupService groupService;

	@Autowired
	private LineService lineService;
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public WrongResult get(String id, boolean isNewRecord) {
		return wrongResultService.get(id, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("wrong:wrongResult:view")
	@RequestMapping(value = {"list", ""})
	public String list(WrongResult wrongResult, Model model) {
		model = queryDropDownOptions(model);
		model.addAttribute("wrongResult", wrongResult);
		return "modules/wrong/wrongResultList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("wrong:wrongResult:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<WrongResult> listData(WrongResult wrongResult, HttpServletRequest request, HttpServletResponse response) {
		wrongResult.setPage(new Page<>(request, response));
		wrongResultService.addDataScopeFilter(wrongResult);
		Page<WrongResult> page = wrongResultService.findPage(wrongResult);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("wrong:wrongResult:view")
	@RequestMapping(value = "form")
	public String form(WrongResult wrongResult, Model model) {
		model = queryDropDownOptions(model);
		model.addAttribute("wrongResult", wrongResult);
		return "modules/wrong/wrongResultForm";
	}

	/**
	 * 保存错误边角查询
	 */
	@RequiresPermissions("wrong:wrongResult:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated WrongResult wrongResult) {
		wrongResultService.save(wrongResult);
		return renderResult(Global.TRUE, text("保存错误边角查询成功！"));
	}
	
	/**
	 * 删除错误边角查询
	 */
	@RequiresPermissions("wrong:wrongResult:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(WrongResult wrongResult) {
		wrongResultService.delete(wrongResult);
		return renderResult(Global.TRUE, text("删除错误边角查询成功！"));
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
	@RequiresPermissions("wrong:wrongResult:view")
	@RequestMapping(value = "findWrongResultListByLineId")
	public List<WrongResult> findWrongResultListByLineId(String lineId){
		List<WrongResult> wrongResultsList = new ArrayList<>();
		//如果接收到的lineId为空，则返回空的stationList
		if (!"".equals(lineId)) {
			WrongResult wrongResult = new WrongResult();
			wrongResult.setLineId(lineId);
			wrongResultsList = wrongResultService.findList(wrongResult);
		}
		return wrongResultsList;
	}


}