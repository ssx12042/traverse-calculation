/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.elevation.el_indirect_adjustment.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jeesite.common.lang.DateUtils;
import com.jeesite.elevation.el_data_solution.dao.ElDataSolutionDao;
import com.jeesite.elevation.el_data_solution.entity.ElDataSolution;
import com.jeesite.elevation.el_line.entity.ELLine;
import com.jeesite.elevation.el_line.service.ELLineService;
import com.jeesite.elevation.el_observe_data.dao.ElObserveDataDao;
import com.jeesite.elevation.el_observe_data.entity.ElObserveData;
import com.jeesite.elevation.el_origin_data.dao.ElOriginDataDao;
import com.jeesite.elevation.el_origin_data.entity.ElOriginData;
import com.jeesite.elevation.el_result_evaluation.dao.ElResultEvaluationDao;
import com.jeesite.elevation.el_result_evaluation.entity.ElResultEvaluation;
import com.jeesite.modules.data_solution.entity.DataSolution;
import com.jeesite.modules.group.dao.GroupDao;
import com.jeesite.modules.group.entity.Group;
import com.jeesite.modules.group.service.GroupService;
import com.jeesite.modules.sys.dao.UserRoleDao;
import com.jeesite.modules.sys.entity.User;
import com.jeesite.modules.sys.entity.UserRole;
import com.jeesite.modules.sys.utils.UserUtils;
import com.jeesite.utils.ExcelUtils;
import com.jeesite.utils.MyExcelUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
import com.jeesite.elevation.el_indirect_adjustment.entity.ElIndirectAdjustment;
import com.jeesite.elevation.el_indirect_adjustment.service.ElIndirectAdjustmentService;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 高程——间接平差Controller
 * @author su
 * @version 2022-07-04
 */
@Controller
@RequestMapping(value = "${adminPath}/el_indirect_adjustment/elIndirectAdjustment")
public class ElIndirectAdjustmentController extends BaseController {

	@Autowired
	private ElIndirectAdjustmentService elIndirectAdjustmentService;

	@Autowired
	private GroupService groupService;

	@Autowired
	private ELLineService elLineService;

	@Autowired
	private UserRoleDao userRoleDao;

	@Autowired
	private ElOriginDataDao elOriginDataDao;

	@Autowired
	private ElObserveDataDao elObserveDataDao;

	@Autowired
	private ElDataSolutionDao elDataSolutionDao;

	@Autowired
	private ElResultEvaluationDao elResultEvaluationDao;

	@Autowired
	private GroupDao groupDao;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public ElIndirectAdjustment get(String id, boolean isNewRecord) {
		return elIndirectAdjustmentService.get(id, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("el_indirect_adjustment:elIndirectAdjustment:view")
	@RequestMapping(value = {"list", ""})
	public String list(ElIndirectAdjustment elIndirectAdjustment, Model model) {
		// 先查询下拉框信息 2022年7月7日 su添加
		model = queryDropDownOptions(model);

		model.addAttribute("elIndirectAdjustment", elIndirectAdjustment);
		return "elevation/el_indirect_adjustment/elIndirectAdjustmentList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("el_indirect_adjustment:elIndirectAdjustment:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<ElIndirectAdjustment> listData(ElIndirectAdjustment elIndirectAdjustment, HttpServletRequest request, HttpServletResponse response) {
		elIndirectAdjustment.setPage(new Page<>(request, response));

		// 历史数据权限过滤 2022年7月7日 su添加
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
			elIndirectAdjustment.setStatus(ElIndirectAdjustment.STATUS_NORMAL);
		}
		// 其他
		else {
			// 没有筛选状态的时候需要列出所有状态的数据
			if (elIndirectAdjustment.getStatus() == null) {
				elIndirectAdjustment.setStatus("");
			}
		}


		//数据权限过滤 2022年7月7日 su添加
		elIndirectAdjustmentService.addDataScopeFilter(elIndirectAdjustment);

		Page<ElIndirectAdjustment> page = elIndirectAdjustmentService.findPage(elIndirectAdjustment);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("el_indirect_adjustment:elIndirectAdjustment:view")
	@RequestMapping(value = "form")
	public String form(ElIndirectAdjustment elIndirectAdjustment, Model model) {
		// 跳转form表单页面时先查询下拉框信息 2022年7月4日 su添加
		model = queryDropDownOptions(model);

		model.addAttribute("elIndirectAdjustment", elIndirectAdjustment);
		return "elevation/el_indirect_adjustment/elIndirectAdjustmentForm";
	}

	/**
	 * 保存高程——间接平差
	 */
	@RequiresPermissions("el_indirect_adjustment:elIndirectAdjustment:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated ElIndirectAdjustment elIndirectAdjustment) {
		elIndirectAdjustmentService.save(elIndirectAdjustment);
		return renderResult(Global.TRUE, text("保存高程——间接平差成功！"));
	}

	/**
	 * 查询下拉框信息 2022年7月4日 su添加
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

	/**
	 * 导出导线数据
	 * 2022年4月25日 su添加
	 */
	@RequiresPermissions("el_indirect_adjustment:elIndirectAdjustment:view")
	@RequestMapping(value = "exportData")
	public void exportData(ElIndirectAdjustment elIndirectAdjustment, Boolean isAll, String ctrlPermi, HttpServletResponse response, String lineId) {
		// 起始数据
		ElOriginData elOriginData = new ElOriginData();
		elOriginData.setLine(new ELLine(lineId));
		elOriginData.setStatus(ElOriginData.STATUS_NORMAL);
		List<ElOriginData> elOriginDataList = elOriginDataDao.findList(elOriginData);

		// 观测数据
		ElObserveData elObserveData = new ElObserveData();
		elObserveData.setLine(new ELLine(lineId));
		elObserveData.setStatus(ElObserveData.STATUS_NORMAL);
		List<ElObserveData> elObserveDataList = elObserveDataDao.findList(elObserveData);

		// 数据解算
		ElDataSolution elDataSolution = new ElDataSolution();
		elDataSolution.setLine(new ELLine(lineId));
		elDataSolution.setStatus(ElDataSolution.STATUS_NORMAL);
		List<ElDataSolution> elDataSolutionList = elDataSolutionDao.findList(elDataSolution);

		// 结果评定
		ElResultEvaluation resEval = new ElResultEvaluation();
		resEval.setLineId(lineId);
		resEval.setStatus(ElResultEvaluation.STATUS_NORMAL);
		List<ElResultEvaluation> elResEvalList = elResultEvaluationDao.findList(resEval);

		// 间接平差
		elIndirectAdjustment.setLine(new ELLine(lineId));
		elIndirectAdjustment.setStatus(ElResultEvaluation.STATUS_NORMAL);
		List<ElIndirectAdjustment> elIndAdjList = elIndirectAdjustmentService.findList(elIndirectAdjustment);

		// 获取基本信息
		String groupName = elIndAdjList.get(0).getGroup().getGroupName();		// 小组名称
		String lineName = elIndAdjList.get(0).getLine().getLineName();			// 导线名称
		String groupId = elIndAdjList.get(0).getGroup().getId();
		Group group = new Group();
		group.setId(groupId);
		Group group1 = groupDao.getByEntity(group);
		String leader = group1.getGroupLeader();								// 负责人
		String traverseClass = elResEvalList.get(0).getTraverseClass();			// 导线测量等级
		String currentDate = DateUtils.getDate("yyyy-MM-dd HH:mm:ss");	// 生成报告的时间

		List<String> baseInfoList = new ArrayList<>();
		baseInfoList.add("小组：" + groupName);
		baseInfoList.add("负责人：" + leader);
		baseInfoList.add("导线：" + lineName);
		baseInfoList.add("导线测量等级：" + traverseClass);
		baseInfoList.add("报告时间：" + currentDate);

		// 生成Excel表
		String titleName = groupName + "的" + lineName + "的导线测量报告";
		String fileName = titleName + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
		try (ExcelUtils.MyExcelExport ee = new ExcelUtils.MyExcelExport(titleName, ElObserveData.class)) {
			// 基本信息
			ee.createBaseInfoRow(baseInfoList);
			// 起始数据
			ee.createHeader(ElOriginData.class, "高程-起始数据");
			ee.setDataList(elOriginDataList);
			// 观测数据
			ee.createHeader(ElObserveData.class, "高程-观测数据");
			ee.setDataList(elObserveDataList);
			// 数据解算
			ee.createHeader(ElDataSolution.class, "高程-数据解算");
			ee.setDataList(elDataSolutionList);
			// 间接平差
			ee.createHeader(ElIndirectAdjustment.class, "高程-间接平差");
			ee.setDataList(elIndAdjList);
			// 输出到客户端
			ee.write(response, fileName);
		}
	}
	
}