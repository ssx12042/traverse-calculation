/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.indirect_adjustment.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jeesite.common.lang.DateUtils;
import com.jeesite.modules.data_solution.dao.DataSolutionDao;
import com.jeesite.modules.data_solution.entity.DataSolution;
import com.jeesite.modules.group.dao.GroupDao;
import com.jeesite.modules.group.entity.Group;
import com.jeesite.modules.group.service.GroupService;
import com.jeesite.modules.line.entity.Line;
import com.jeesite.modules.line.service.LineService;
import com.jeesite.modules.observation.dao.ObservationDataDao;
import com.jeesite.modules.observation.entity.ObservationData;
import com.jeesite.modules.origin.dao.OriginDataDao;
import com.jeesite.modules.origin.entity.OriginData;
import com.jeesite.modules.result_evaluation.dao.ResultEvaluationDao;
import com.jeesite.modules.result_evaluation.entity.ResultEvaluation;
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
import com.jeesite.modules.indirect_adjustment.entity.IndirectAdjustment;
import com.jeesite.modules.indirect_adjustment.service.IndirectAdjustmentService;

import java.util.ArrayList;
import java.util.List;

/**
 * 间接平差Controller
 * @author su
 * @version 2022-04-06
 */
@Controller
@RequestMapping(value = "${adminPath}/indirect_adjustment/indirectAdjustment")
public class IndirectAdjustmentController extends BaseController {

	@Autowired
	private IndirectAdjustmentService indirectAdjustmentService;

	@Autowired
	private GroupService groupService;

	@Autowired
	private LineService lineService;

	@Autowired
	private UserRoleDao userRoleDao;

	@Autowired
	private OriginDataDao originDataDao;

	@Autowired
	private ObservationDataDao observationDataDao;

	@Autowired
	private DataSolutionDao dataSolutionDao;

	@Autowired
	private ResultEvaluationDao resultEvaluationDao;

	@Autowired
	private GroupDao groupDao;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public IndirectAdjustment get(String id, boolean isNewRecord) {
		return indirectAdjustmentService.get(id, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("indirect_adjustment:indirectAdjustment:view")
	@RequestMapping(value = {"list", ""})
	public String list(IndirectAdjustment indirectAdjustment, Model model) {
		// 先查询下拉框信息 2022年3月28日 su添加
		model = queryDropDownOptions(model);

		model.addAttribute("indirectAdjustment", indirectAdjustment);
		return "modules/indirect_adjustment/indirectAdjustmentList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("indirect_adjustment:indirectAdjustment:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<IndirectAdjustment> listData(IndirectAdjustment indirectAdjustment, HttpServletRequest request, HttpServletResponse response) {
		// 历史数据权限过滤 2022年4月5日 su添加
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
			indirectAdjustment.setStatus(DataSolution.STATUS_NORMAL);
		}
		// 其他
		else {
			// 没有筛选状态的时候需要列出所有状态的数据
			if (indirectAdjustment.getStatus() == null) {
				indirectAdjustment.setStatus("");
			}
		}

		indirectAdjustment.setPage(new Page<>(request, response));

		//数据权限过滤 2022年4月11日 su添加
		indirectAdjustmentService.addDataScopeFilter(indirectAdjustment);

		Page<IndirectAdjustment> page = indirectAdjustmentService.findPage(indirectAdjustment);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("indirect_adjustment:indirectAdjustment:view")
	@RequestMapping(value = "form")
	public String form(IndirectAdjustment indirectAdjustment, Model model) {
		// 跳转form表单页面时先查询下拉框信息 2022年3月28日 su添加
		model = queryDropDownOptions(model);

		model.addAttribute("indirectAdjustment", indirectAdjustment);
		return "modules/indirect_adjustment/indirectAdjustmentForm";
	}

	/**
	 * 保存间接平差
	 */
	@RequiresPermissions("indirect_adjustment:indirectAdjustment:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated IndirectAdjustment indirectAdjustment) {
		// 先判断间接平差的条件是否充分（简易平差的数据解算）
		// 2022年6月23日 su添加
		String lineId = indirectAdjustment.getLine().getId();
		DataSolution dataSolution = new DataSolution();
		dataSolution.setLine(new Line(lineId));
		dataSolution.setStatus(DataSolution.STATUS_NORMAL);
		List<DataSolution> dsList = dataSolutionDao.findList(dataSolution);
		if (dsList.size() == 0) {
			return renderResult(Global.FALSE, text("该导线还没有进行简易平差，请在数据解算页面进行数据解算！"));
		}

		indirectAdjustmentService.save(indirectAdjustment);
		return renderResult(Global.TRUE, text("保存间接平差成功！"));
	}

	/**
	 * 查询下拉框信息 2022年3月28日 su添加
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
	 * 导出导线数据
	 * 2022年4月25日 su添加
	 */
	@RequiresPermissions("indirect_adjustment:indirectAdjustment:view")
	@RequestMapping(value = "exportData")
	public void exportData(IndirectAdjustment indirectAdjustment, Boolean isAll, String ctrlPermi, HttpServletResponse response, String lineId) {
		// 起始数据
		OriginData originData = new OriginData();
		originData.setLine(new Line(lineId));
		originData.setStatus(OriginData.STATUS_NORMAL);
		List<OriginData> originDataList = originDataDao.findList(originData);

		// 观测数据
		ObservationData observationData = new ObservationData();
		observationData.setLine(new Line(lineId));
		observationData.setStatus(ObservationData.STATUS_NORMAL);
		List<ObservationData> observationDataList = observationDataDao.findList(observationData);

		// 数据解算
		DataSolution dataSolution = new DataSolution();
		dataSolution.setLine(new Line(lineId));
		dataSolution.setStatus(DataSolution.STATUS_NORMAL);
		List<DataSolution> dataSolutionList = dataSolutionDao.findList(dataSolution);

		// 结果评定
		ResultEvaluation resEval = new ResultEvaluation();
		resEval.setLine(new Line(lineId));
		resEval.setStatus(ResultEvaluation.STATUS_NORMAL);
		List<ResultEvaluation> resEvalList = resultEvaluationDao.findList(resEval);

		// 间接平差
		indirectAdjustment.setLine(new Line(lineId));
		indirectAdjustment.setStatus(ResultEvaluation.STATUS_NORMAL);
		List<IndirectAdjustment> indAdjList = indirectAdjustmentService.findList(indirectAdjustment);

		// 获取基本信息
		String groupName = resEvalList.get(0).getGroup().getGroupName();		// 小组名称
		String lineName = resEvalList.get(0).getLine().getLineName();			// 导线名称
		String groupId = resEvalList.get(0).getGroup().getId();
		Group group = new Group();
		group.setId(groupId);
		Group group1 = groupDao.getByEntity(group);
		String leader = group1.getGroupLeader();								// 负责人
		String traverseClass = resEvalList.get(0).getTraverseClass();			// 导线测量等级
		String currentDate = DateUtils.getDate("yyyy-MM-dd HH:mm:ss");	// 生成报告的时间

		List<String> baseInfoList = new ArrayList<>();
		baseInfoList.add("小组：" + groupName);
		baseInfoList.add("负责人：" + leader);
		baseInfoList.add("导线：" + lineName);
		baseInfoList.add("导线测量等级：" + traverseClass);
		baseInfoList.add("报告时间：" + currentDate);

		// 生成Excel表
		String fileName = groupName + "的" + lineName + "的导线测量报告" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
		try (ExcelUtils.MyExcelExport ee = new ExcelUtils.MyExcelExport(groupName + "的" + lineName + "的导线测量报告", ObservationData.class)) {
			// 基本信息
			ee.createBaseInfoRow(baseInfoList);
			// 起始数据
			ee.createHeader(OriginData.class, "起始数据");
			ee.setDataList(originDataList);
			// 观测数据
			ee.createHeader(ObservationData.class, "观测数据");
			ee.setDataList(observationDataList);
			// 数据解算
			ee.createHeader(DataSolution.class, "数据解算");
			ee.setDataList(dataSolutionList);
			// 结果评定
			ee.createHeader(ResultEvaluation.class, "结果评定");
			ee.setDataList(resEvalList);
			// 间接平差
			ee.createHeader(IndirectAdjustment.class, "间接平差");
			ee.setDataList(indAdjList);
			// 输出到客户端
			ee.write(response, fileName);
		}
	}
	
}