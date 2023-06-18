/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.elevation.el_data_solution.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jeesite.elevation.el_line.entity.ELLine;
import com.jeesite.elevation.el_line.service.ELLineService;
import com.jeesite.elevation.el_observe_data.dao.ElObserveDataDao;
import com.jeesite.elevation.el_observe_data.entity.ElObserveData;
import com.jeesite.elevation.el_origin_data.dao.ElOriginDataDao;
import com.jeesite.elevation.el_origin_data.entity.ElOriginData;
import com.jeesite.elevation.el_station.dao.ELStationDao;
import com.jeesite.elevation.el_station.entity.ELStation;
import com.jeesite.modules.data_solution.entity.DataSolution;
import com.jeesite.modules.group.entity.Group;
import com.jeesite.modules.group.service.GroupService;
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
import com.jeesite.elevation.el_data_solution.entity.ElDataSolution;
import com.jeesite.elevation.el_data_solution.service.ElDataSolutionService;

import java.util.List;

/**
 * 高程——数据解算Controller
 * @author su
 * @version 2022-05-24
 */
@Controller
@RequestMapping(value = "${adminPath}/el_data_solution/elDataSolution")
public class ElDataSolutionController extends BaseController {

	@Autowired
	private ElDataSolutionService elDataSolutionService;

	@Autowired
	private GroupService groupService;

	@Autowired
	private ELLineService elLineService;

	@Autowired
	private UserRoleDao userRoleDao;

	@Autowired
	private ELStationDao elStationDao;

	@Autowired
	private ElOriginDataDao elOriginDataDao;

	@Autowired
	private ElObserveDataDao elObserveDataDao;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public ElDataSolution get(String id, boolean isNewRecord) {
		return elDataSolutionService.get(id, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("el_data_solution:elDataSolution:view")
	@RequestMapping(value = {"list", ""})
	public String list(ElDataSolution elDataSolution, Model model) {
		// 先查询下拉框信息 2022年5月30日 su添加
		model = queryDropDownOptions(model);

		model.addAttribute("elDataSolution", elDataSolution);
		return "elevation/el_data_solution/elDataSolutionList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("el_data_solution:elDataSolution:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<ElDataSolution> listData(ElDataSolution elDataSolution, HttpServletRequest request, HttpServletResponse response) {
		elDataSolution.setPage(new Page<>(request, response));

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
			elDataSolution.setStatus(DataSolution.STATUS_NORMAL);
		}
		// 其他
		else {
			// 没有筛选状态的时候需要列出所有状态的数据
			if (elDataSolution.getStatus() == null) {
				elDataSolution.setStatus("");
			}
		}

		//数据权限过滤 2022年5月30日 su添加
		elDataSolutionService.addDataScopeFilter(elDataSolution);

		Page<ElDataSolution> page = elDataSolutionService.findPage(elDataSolution);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("el_data_solution:elDataSolution:view")
	@RequestMapping(value = "form")
	public String form(ElDataSolution elDataSolution, Model model) {
		// 跳转form表单页面时先查询下拉框信息 2022年5月30日 su添加
		model = queryDropDownOptions(model);

		model.addAttribute("elDataSolution", elDataSolution);
		return "elevation/el_data_solution/elDataSolutionForm";
	}

	/**
	 * 保存高程——数据解算
	 */
	@RequiresPermissions("el_data_solution:elDataSolution:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated ElDataSolution elDataSolution) {
		// 先判断解算的条件是否充分（测站，观测数据，起始数据）
		// 2022年6月23日 su添加
		String lineId = elDataSolution.getLine().getId();
		// 查找该导线是否创建了测站
		ELStation station = new ELStation();
		station.setLineId(lineId);
		station.setStatus(ELStation.STATUS_NORMAL);
		List<ELStation> stationList = elStationDao.findList(station);
		if (stationList.size() == 0) {
			return renderResult(Global.FALSE, text("该导线还没有测站，请在测站管理页面进行创建！"));
		}
		// 查找该导线是否添加了起始数据
		ElOriginData originData = new ElOriginData();
		originData.setLine(new ELLine(lineId));
		originData.setStatus(ElOriginData.STATUS_NORMAL);
		List<ElOriginData> originDataList = elOriginDataDao.findList(originData);
		if (originDataList.size() == 0) {
			return renderResult(Global.FALSE, text("该导线还没有起始数据，请在起始数据页面进行添加！"));
		}
		// 查找该导线是否添加了观测数据
		for (ELStation sta : stationList) {
			ElObserveData od = new ElObserveData();
			od.setStationId(sta.getId());
			od.setStatus(ElObserveData.STATUS_NORMAL);
			List<ElObserveData> odList = elObserveDataDao.findList(od);
			if (odList.size() == 0) {
				return renderResult(Global.FALSE,
						text(sta.getStationName() + " 测站还没有录入观测数据，请在观测数据页面进行录入！"));
			}
		}

		elDataSolutionService.save(elDataSolution);
		return renderResult(Global.TRUE, text("保存高程——数据解算成功！"));
	}

	/**
	 * 查询下拉框信息 2022年5月30日 su添加
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