/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.elevation.el_observe_data.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jeesite.elevation.el_line.entity.ELLine;
import com.jeesite.elevation.el_line.service.ELLineService;
import com.jeesite.elevation.el_station.entity.ELStation;
import com.jeesite.elevation.el_station.service.ELStationService;
import com.jeesite.modules.data_solution.entity.DataSolution;
import com.jeesite.modules.group.entity.Group;
import com.jeesite.modules.group.service.GroupService;
import com.jeesite.modules.line.entity.Line;
import com.jeesite.modules.station.entity.Station;
import com.jeesite.modules.sys.dao.UserRoleDao;
import com.jeesite.modules.sys.entity.User;
import com.jeesite.modules.sys.entity.UserRole;
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
import com.jeesite.elevation.el_observe_data.entity.ElObserveData;
import com.jeesite.elevation.el_observe_data.service.ElObserveDataService;

import java.util.LinkedList;
import java.util.List;

/**
 * 高程——观测数据Controller
 * @author su
 * @version 2022-05-23
 */
@Controller
@RequestMapping(value = "${adminPath}/el_observe_data/elObserveData")
public class ElObserveDataController extends BaseController {

	@Autowired
	private ElObserveDataService elObserveDataService;

	@Autowired
	private GroupService groupService;

	@Autowired
	private ELLineService elLineService;

	@Autowired
	private ELStationService elStationService;

	@Autowired
	private UserRoleDao userRoleDao;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public ElObserveData get(String id, boolean isNewRecord) {
		return elObserveDataService.get(id, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("el_observe_data:elObserveData:view")
	@RequestMapping(value = {"list", ""})
	public String list(ElObserveData elObserveData, Model model) {
		// 先查询下拉框信息 2022年5月23日 su添加
		model = queryDropDownOptions(model);

		model.addAttribute("elObserveData", elObserveData);
		return "elevation/el_observe_data/elObserveDataList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("el_observe_data:elObserveData:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<ElObserveData> listData(ElObserveData elObserveData, HttpServletRequest request, HttpServletResponse response) {
		elObserveData.setPage(new Page<>(request, response));

		// 历史数据权限过滤 2022年5月30日 su添加
		// 获取当前登录的用户
		User user = UserUtils.getUser();
		// 获取用户编码
		String userCode = user.getUserCode();
		// 查找该用户的角色编码
		UserRole userRole = new UserRole();
		userRole.setUserCode(userCode);
		List<UserRole> userRoleList = userRoleDao.findList(userRole);
		// 只有一个身份且是学生身份，它就是学生
		boolean isStudent = userRoleList.size() == 1 && "student".equals(userRoleList.get(0).getRoleCode());
		// 学生
		if (isStudent) {
			// 只能看正常状态的数据
			elObserveData.setStatus(DataSolution.STATUS_NORMAL);
		}
		// 其他
		else {
			// 没有筛选状态的时候需要列出所有状态的数据
			if (elObserveData.getStatus() == null) {
				elObserveData.setStatus("");
			}
		}

		//数据权限过滤 2022年5月23日 su添加
		elObserveDataService.addDataScopeFilter(elObserveData);

		Page<ElObserveData> page = elObserveDataService.findPage(elObserveData);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("el_observe_data:elObserveData:view")
	@RequestMapping(value = "form")
	public String form(ElObserveData elObserveData, Model model) {
		// 跳转form表单页面时先查询下拉框信息 2022年5月23日 su添加
		model = queryDropDownOptions(model);

		model.addAttribute("elObserveData", elObserveData);
		return "elevation/el_observe_data/elObserveDataForm";
	}

	/**
	 * 保存高程——观测数据
	 */
	@RequiresPermissions("el_observe_data:elObserveData:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated ElObserveData elObserveData) {
		elObserveDataService.save(elObserveData);
		return renderResult(Global.TRUE, text("保存高程——观测数据成功！"));
	}
	
	/**
	 * 删除高程——观测数据
	 */
	@RequiresPermissions("el_observe_data:elObserveData:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(ElObserveData elObserveData) {
		elObserveDataService.delete(elObserveData);
		return renderResult(Global.TRUE, text("删除高程——观测数据成功！"));
	}

	/**
	 * 查询下拉框信息 2022年5月23日 su添加
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

		//测站下拉
		List<ELStation> stationList = new LinkedList<>();
		ELStation elStation = new ELStation();
		//数据过滤，只能看到自己添加的导线
		elStationService.addDataScopeFilter(elStation);
		stationList.addAll(elStationService.findList(elStation));

		model.addAttribute("groupList", groupService.findList(group));
		model.addAttribute("lineList", elLineService.findList(elLine));
		model.addAttribute("stationList", stationList);

		return model;
	}
	
}