/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.line.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jeesite.common.lang.DateUtils;
import com.jeesite.common.utils.excel.ExcelExport;
import com.jeesite.modules.group.entity.Group;
import com.jeesite.modules.group.service.GroupService;
import com.jeesite.modules.sys.entity.EmpUser;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.line.entity.Line;
import com.jeesite.modules.line.service.LineService;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 导线Controller
 * @author su
 * @version 2022-02-28
 */
@Controller
@RequestMapping(value = "${adminPath}/line/line")
public class LineController extends BaseController {

	@Autowired
	private LineService lineService;

	@Autowired
	private GroupService groupService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public Line get(String id, boolean isNewRecord) {
		return lineService.get(id, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("line:line:view")
	@RequestMapping(value = {"list", ""})
	public String list(Line line, Model model) {
		// 查询下拉框信息 2022年3月8日 su添加
		// 组下拉
		Group group = new Group();
		//数据过滤，只能看到自己添加的组
		groupService.addDataScopeFilter(group);
		model.addAttribute("groupList", groupService.findList(group));

		model.addAttribute("line", line);
		return "modules/line/lineList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("line:line:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<Line> listData(Line line, HttpServletRequest request, HttpServletResponse response) {
		line.setPage(new Page<>(request, response));

		//数据权限过滤 2022年3月8日 su添加
		lineService.addDataScopeFilter(line);

		Page<Line> page = lineService.findPage(line);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("line:line:view")
	@RequestMapping(value = "form")
	public String form(Line line, Model model) {
		// 跳转form表单页面时先查询下拉框信息 2022年3月8日 su添加
		Group group = new Group();
		//数据过滤，只能看到自己添加的组
		groupService.addDataScopeFilter(group);
		model.addAttribute("groupList", groupService.findList(group));

		model.addAttribute("line", line);
		return "modules/line/lineForm";
	}

	/**
	 * 保存导线
	 */
	@RequiresPermissions("line:line:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated Line line) {
		lineService.save(line);
		return renderResult(Global.TRUE, text("保存导线成功！"));
	}
	
	/**
	 * 删除导线
	 */
	@RequiresPermissions("line:line:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(Line line) {
		lineService.delete(line);
		return renderResult(Global.TRUE, text("删除导线成功！"));
	}

	/**
	 * 根据小组ID查询导线圈 2022年3月8日 su添加
	 * @param groupId 小组id
	 * @return 导线数组
	 */
	@RequiresPermissions("line:line:view")
	@RequestMapping(value = "findLineListByGroupId")
	public List<Line> findLineListByGroupId(String groupId){
		List<Line> lineList = new ArrayList<>();
		//如果接收到的groupId为空，则返回空的lineList
		if (!"".equals(groupId)) {
			Line line = new Line();
			line.setGroupId(groupId);
			line.setStatus(Line.STATUS_NORMAL);
			lineList = lineService.findList(line) ;
		}
		return lineList;
	}
	
}