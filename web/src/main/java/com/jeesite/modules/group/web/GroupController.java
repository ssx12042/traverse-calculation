/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.group.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jeesite.modules.sys.entity.User;
import com.jeesite.modules.sys.utils.UserUtils;
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
import com.jeesite.modules.group.entity.Group;
import com.jeesite.modules.group.service.GroupService;

import java.util.List;

/**
 * 组管理Controller
 * @author su
 * @version 2022-02-28
 */
@Controller
@RequestMapping(value = "${adminPath}/group/group")
public class GroupController extends BaseController {

	@Autowired
	private GroupService groupService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public Group get(String id, boolean isNewRecord) {
		return groupService.get(id, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("group:group:view")
	@RequestMapping(value = {"list", ""})
	public String list(Group group, Model model) {
		model.addAttribute("group", group);
		return "modules/group/groupList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("group:group:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<Group> listData(Group group, HttpServletRequest request, HttpServletResponse response) {
		group.setPage(new Page<>(request, response));

		//数据过滤 2022年3月8日 su添加
		groupService.addDataScopeFilter(group);

		Page<Group> page = groupService.findPage(group);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("group:group:view")
	@RequestMapping(value = "form")
	public String form(Group group, Model model) {
		//新纪录 2022年3月8日 su添加
		if (group.getId() == null) {
			//获取当前登录的用户
			User user = UserUtils.getUser();
			//获取用户编码
			String userCode = user.getUserCode();
			//查询该用户是否已经已经创建了组
			Group g = new Group();
			g.setCreateBy(userCode);
			List<Group> list = groupService.findList(g);
			if (list.size() > 0) {
				return renderResult(Global.FALSE, text("已经创建过小组啦。"));
			} else {
				model.addAttribute("group", group);
				return "modules/group/groupForm";
			}
		}
		//已存在的记录
		else {
			model.addAttribute("group", group);
			return "modules/group/groupForm";
		}
	}

	/**
	 * 保存组管理
	 */
	@RequiresPermissions("group:group:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated Group group) {
		groupService.save(group);
		return renderResult(Global.TRUE, text("保存组管理成功！"));
	}
	
	/**
	 * 删除组管理
	 */
	@RequiresPermissions("group:group:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(Group group) {
		groupService.delete(group);
		return renderResult(Global.TRUE, text("删除组管理成功！"));
	}
	
}