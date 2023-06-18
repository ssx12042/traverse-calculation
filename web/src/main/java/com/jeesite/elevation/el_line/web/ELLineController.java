/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.elevation.el_line.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jeesite.modules.group.entity.Group;
import com.jeesite.modules.group.service.GroupService;
import com.jeesite.modules.line.entity.Line;
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
import com.jeesite.elevation.el_line.entity.ELLine;
import com.jeesite.elevation.el_line.service.ELLineService;

import java.util.ArrayList;
import java.util.List;

/**
 * 高程——导线Controller
 * @author su
 * @version 2022-05-17
 */
@Controller
@RequestMapping(value = "${adminPath}/el_line/eLLine")
public class ELLineController extends BaseController {

	@Autowired
	private ELLineService eLLineService;

	@Autowired
	private GroupService groupService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public ELLine get(String id, boolean isNewRecord) {
		return eLLineService.get(id, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("el_line:eLLine:view")
	@RequestMapping(value = {"list", ""})
	public String list(ELLine eLLine, Model model) {
		// 查询下拉框信息 2022年5月17日 su添加
		// 组下拉
		Group group = new Group();
		//数据过滤，只能看到自己添加的组
		groupService.addDataScopeFilter(group);
		model.addAttribute("groupList", groupService.findList(group));

		model.addAttribute("eLLine", eLLine);
		return "elevation/el_line/eLLineList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("el_line:eLLine:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<ELLine> listData(ELLine eLLine, HttpServletRequest request, HttpServletResponse response) {
		eLLine.setPage(new Page<>(request, response));

		//数据权限过滤 2022年5月17日 su添加
		eLLineService.addDataScopeFilter(eLLine);

		Page<ELLine> page = eLLineService.findPage(eLLine);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("el_line:eLLine:view")
	@RequestMapping(value = "form")
	public String form(ELLine eLLine, Model model) {
		// 跳转form表单页面时先查询下拉框信息 2022年5月17日 su添加
		Group group = new Group();
		//数据过滤，只能看到自己添加的组
		groupService.addDataScopeFilter(group);
		model.addAttribute("groupList", groupService.findList(group));

		model.addAttribute("eLLine", eLLine);
		return "elevation/el_line/eLLineForm";
	}

	/**
	 * 保存高程——导线
	 */
	@RequiresPermissions("el_line:eLLine:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated ELLine eLLine) {
		eLLineService.save(eLLine);
		return renderResult(Global.TRUE, text("保存高程——导线成功！"));
	}
	
	/**
	 * 删除高程——导线
	 */
	@RequiresPermissions("el_line:eLLine:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(ELLine eLLine) {
		eLLineService.delete(eLLine);
		return renderResult(Global.TRUE, text("删除高程——导线成功！"));
	}

	/**
	 * 根据小组ID查询导线圈 2022年5月17日 su添加
	 * @param groupId 小组id
	 * @return 导线数组
	 */
	@RequiresPermissions("el_line:eLLine:view")
	@RequestMapping(value = "findLineListByGroupId")
	public List<ELLine> findLineListByGroupId(String groupId){
		List<ELLine> elLines = new ArrayList<>();
		//如果接收到的groupId为空，则返回空的lineList
		if (!"".equals(groupId)) {
			ELLine elLine = new ELLine();
			elLine.setGroupId(groupId);
			elLine.setStatus(ELLine.STATUS_NORMAL);
			elLines = eLLineService.findList(elLine) ;
		}
		return elLines;
	}
	
}