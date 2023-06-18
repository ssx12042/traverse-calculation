/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.data_solution.web;

import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.data_solution.entity.DataSolution;
import com.jeesite.modules.data_solution.service.DataSolutionService;
import com.jeesite.modules.group.entity.Group;
import com.jeesite.modules.group.service.GroupService;
import com.jeesite.modules.line.entity.Line;
import com.jeesite.modules.line.service.LineService;
import com.jeesite.modules.observation.dao.ObservationDataDao;
import com.jeesite.modules.observation.entity.ObservationData;
import com.jeesite.modules.origin.dao.OriginDataDao;
import com.jeesite.modules.origin.entity.OriginData;
import com.jeesite.modules.station.dao.StationDao;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 数据解算Controller
 * @author su
 * @version 2022-03-15
 */
@Controller
@RequestMapping(value = "${adminPath}/data_solution/dataSolution")
public class DataSolutionController extends BaseController {

	@Autowired
	private DataSolutionService dataSolutionService;

	@Autowired
	private GroupService groupService;

	@Autowired
	private LineService lineService;

	@Autowired
	private UserRoleDao userRoleDao;

	@Autowired
	private StationDao stationDao;

	@Autowired
	private OriginDataDao originDataDao;

	@Autowired
	private ObservationDataDao observationDataDao;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public DataSolution get(String id, boolean isNewRecord) {
		return dataSolutionService.get(id, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("data_solution:dataSolution:view")
	@RequestMapping(value = {"list", ""})
	public String list(DataSolution dataSolution, Model model) {
		// 先查询下拉框信息 2022年3月15日 su添加
		model = queryDropDownOptions(model);

		model.addAttribute("dataSolution", dataSolution);
		return "modules/data_solution/dataSolutionList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("data_solution:dataSolution:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<DataSolution> listData(DataSolution dataSolution, HttpServletRequest request, HttpServletResponse response) {
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
			dataSolution.setStatus(DataSolution.STATUS_NORMAL);
		}
		// 其他
		else {
			// 没有筛选状态的时候需要列出所有状态的数据
			if (dataSolution.getStatus() == null) {
				dataSolution.setStatus("");
			}
		}

		/*if (("system".equals(userCode) || userCode.startsWith("teacher"))) {    //超管或者老师
			if (dataSolution.getStatus() == null) {    //没有筛选状态的时候需要列出所有数据
				dataSolution.setStatus("");    // 列出所有状态的数据
			}
		} else {    //普通用户
			dataSolution.setStatus(DataSolution.STATUS_NORMAL);
		}*/


		dataSolution.setPage(new Page<>(request, response));

		//数据权限过滤 2022年3月15日 su添加
		dataSolutionService.addDataScopeFilter(dataSolution);

		Page<DataSolution> page = dataSolutionService.findPage(dataSolution);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("data_solution:dataSolution:view")
	@RequestMapping(value = "form")
	public String form(DataSolution dataSolution, Model model) {
		// 跳转form表单页面时先查询下拉框信息 2022年3月15日 su添加
		model = queryDropDownOptions(model);

		model.addAttribute("dataSolution", dataSolution);
		return "modules/data_solution/dataSolutionForm";
	}

	/**
	 * 保存数据解算
	 */
	@RequiresPermissions("data_solution:dataSolution:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated DataSolution dataSolution) {
		// 先判断解算的条件是否充分（测站，观测数据，起始数据）
		// 2022年6月23日 su添加
		String lineId = dataSolution.getLine().getId();
		// 查找该导线是否创建了测站
		Station station = new Station();
		station.setLineId(lineId);
		station.setStatus(Station.STATUS_NORMAL);
		List<Station> stationList = stationDao.findList(station);
		if (stationList.size() == 0) {
			return renderResult(Global.FALSE, text("该导线还没有测站，请在测站管理页面进行创建！"));
		}
		// 查找该导线是否添加了起始数据
		OriginData originData = new OriginData();
		originData.setLine(new Line(lineId));
		originData.setStatus(OriginData.STATUS_NORMAL);
		List<OriginData> originDataList = originDataDao.findList(originData);
		if (originDataList.size() == 0) {
			return renderResult(Global.FALSE, text("该导线还没有起始数据，请在起始数据页面进行添加！"));
		}
		// 查找该导线是否添加了观测数据
		for (Station sta : stationList) {
			ObservationData od = new ObservationData();
			od.setStationId(sta.getId());
			od.setStatus(ObservationData.STATUS_NORMAL);
			List<ObservationData> odList = observationDataDao.findList(od);
			if (odList.size() == 0) {
				return renderResult(Global.FALSE,
						text(sta.getStationName() + " 测站还没有录入观测数据，请在观测数据页面进行录入！"));
			}
		}

		dataSolutionService.save(dataSolution);
		return renderResult(Global.TRUE, text("保存数据解算成功！"));
	}

	/**
	 * 查询下拉框信息 2022年3月15日 su添加
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
	
}