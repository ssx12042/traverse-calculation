/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.elevation.el_result_evaluation.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.jeesite.elevation.el_result_evaluation.entity.ElResultEvaluation;
import com.jeesite.elevation.el_result_evaluation.service.ElResultEvaluationService;

/**
 * 高程——结果评定Controller
 * @author su
 * @version 2022-06-07
 */
@Controller
@RequestMapping(value = "${adminPath}/el_result_evaluation/elResultEvaluation")
public class ElResultEvaluationController extends BaseController {

	@Autowired
	private ElResultEvaluationService elResultEvaluationService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public ElResultEvaluation get(String id, boolean isNewRecord) {
		return elResultEvaluationService.get(id, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("el_result_evaluation:elResultEvaluation:view")
	@RequestMapping(value = {"list", ""})
	public String list(ElResultEvaluation elResultEvaluation, Model model) {
		model.addAttribute("elResultEvaluation", elResultEvaluation);
		return "elevation/el_result_evaluation/elResultEvaluationList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("el_result_evaluation:elResultEvaluation:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<ElResultEvaluation> listData(ElResultEvaluation elResultEvaluation, HttpServletRequest request, HttpServletResponse response) {
		elResultEvaluation.setPage(new Page<>(request, response));
		Page<ElResultEvaluation> page = elResultEvaluationService.findPage(elResultEvaluation);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("el_result_evaluation:elResultEvaluation:view")
	@RequestMapping(value = "form")
	public String form(ElResultEvaluation elResultEvaluation, Model model) {
		model.addAttribute("elResultEvaluation", elResultEvaluation);
		return "elevation/el_result_evaluation/elResultEvaluationForm";
	}

	/**
	 * 保存高程——结果评定
	 */
	@RequiresPermissions("el_result_evaluation:elResultEvaluation:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated ElResultEvaluation elResultEvaluation) {
		elResultEvaluationService.save(elResultEvaluation);
		return renderResult(Global.TRUE, text("保存高程——结果评定成功！"));
	}
	
}