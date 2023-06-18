/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.observation.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jeesite.common.lang.DateUtils;
import com.jeesite.common.utils.excel.ExcelExport;
import com.jeesite.modules.data_solution.entity.DataSolution;
import com.jeesite.modules.group.entity.Group;
import com.jeesite.modules.group.service.GroupService;
import com.jeesite.modules.line.entity.Line;
import com.jeesite.modules.line.service.LineService;
import com.jeesite.modules.station.entity.Station;
import com.jeesite.modules.station.service.StationService;
import com.jeesite.modules.sys.dao.UserRoleDao;
import com.jeesite.modules.sys.entity.User;
import com.jeesite.modules.sys.entity.UserRole;
import com.jeesite.modules.sys.utils.UserUtils;
import com.jeesite.utils.ExcelUtils;
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
import com.jeesite.modules.observation.entity.ObservationData;
import com.jeesite.modules.observation.service.ObservationDataService;

import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;

/**
 * 观测数据Controller
 * @author su
 * @version 2022-03-14
 */
@Controller
@RequestMapping(value = "${adminPath}/observation/observationData")
public class ObservationDataController extends BaseController {

	@Autowired
	private ObservationDataService observationDataService;

	@Autowired
	private GroupService groupService;

	@Autowired
	private LineService lineService;

	@Autowired
	private StationService stationService;

	@Autowired
	private UserRoleDao userRoleDao;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public ObservationData get(String id, boolean isNewRecord) {
		return observationDataService.get(id, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("observation:observationData:view")
	@RequestMapping(value = {"list", ""})
	public String list(ObservationData observationData, Model model) {
		// 先查询下拉框信息 2022年3月14日 su添加
		model = queryDropDownOptions(model);

		model.addAttribute("observationData", observationData);
		return "modules/observation/observationDataList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("observation:observationData:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<ObservationData> listData(ObservationData observationData, HttpServletRequest request, HttpServletResponse response) {
		// 历史数据权限过滤 2022年3月22日 su添加
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
			observationData.setStatus(DataSolution.STATUS_NORMAL);
		}
		// 其他
		else {
			// 没有筛选状态的时候需要列出所有状态的数据
			if (observationData.getStatus() == null) {
				observationData.setStatus("");
			}
		}

		observationData.setPage(new Page<>(request, response));

		//数据权限过滤 2022年3月14日 su添加
		observationDataService.addDataScopeFilter(observationData);

		Page<ObservationData> page = observationDataService.findPage(observationData);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("observation:observationData:view")
	@RequestMapping(value = "form")
	public String form(ObservationData observationData, Model model) {
		// 跳转form表单页面时先查询下拉框信息 2022年3月14日 su添加
		model = queryDropDownOptions(model);

		model.addAttribute("observationData", observationData);
		return "modules/observation/observationDataForm";
	}

	/**
	 * 保存观测数据
	 */
	@RequiresPermissions("observation:observationData:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated ObservationData observationData) {
		observationDataService.save(observationData);
		return renderResult(Global.TRUE, text("保存观测数据成功！"));
	}
	
	/**
	 * 删除观测数据
	 */
	@RequiresPermissions("observation:observationData:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(ObservationData observationData) {
		observationDataService.delete(observationData);
		return renderResult(Global.TRUE, text("删除观测数据成功！"));
	}

	/**
	 * 查询下拉框信息 2022年3月14日 su添加
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
		List<Station> stationList = new LinkedList<>();
		// “无”项
		Station station = new Station();
		station.setId("0");
		station.setStationName("无");
		stationList.add(station);
		//数据项
		Station station2 = new Station();
		//数据过滤，只能看到自己添加的导线
		stationService.addDataScopeFilter(station2);
		stationList.addAll(stationService.findList(station2));

		model.addAttribute("groupList", groupService.findList(group));
		model.addAttribute("lineList", lineService.findList(line));
		model.addAttribute("stationList", stationList);

		return model;
	}
	
}