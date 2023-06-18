/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.result_evaluation_strict.web;

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
import com.jeesite.modules.result_evaluation_strict.entity.ResultEvaluationStrict;
import com.jeesite.modules.result_evaluation_strict.service.ResultEvaluationStrictService;

/**
 * 间接平差的结果评定Controller
 * @author su
 * @version 2022-04-11
 */
@Controller
@RequestMapping(value = "${adminPath}/result_evaluation_strict/resultEvaluationStrict")
public class ResultEvaluationStrictController extends BaseController {

	@Autowired
	private ResultEvaluationStrictService resultEvaluationStrictService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public ResultEvaluationStrict get(String id, boolean isNewRecord) {
		return resultEvaluationStrictService.get(id, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("result_evaluation_strict:resultEvaluationStrict:view")
	@RequestMapping(value = {"list", ""})
	public String list(ResultEvaluationStrict resultEvaluationStrict, Model model) {
		model.addAttribute("resultEvaluationStrict", resultEvaluationStrict);
		return "modules/result_evaluation_strict/resultEvaluationStrictList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("result_evaluation_strict:resultEvaluationStrict:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<ResultEvaluationStrict> listData(ResultEvaluationStrict resultEvaluationStrict, HttpServletRequest request, HttpServletResponse response) {
		resultEvaluationStrict.setPage(new Page<>(request, response));
		Page<ResultEvaluationStrict> page = resultEvaluationStrictService.findPage(resultEvaluationStrict);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("result_evaluation_strict:resultEvaluationStrict:view")
	@RequestMapping(value = "form")
	public String form(ResultEvaluationStrict resultEvaluationStrict, Model model) {
		model.addAttribute("resultEvaluationStrict", resultEvaluationStrict);
		return "modules/result_evaluation_strict/resultEvaluationStrictForm";
	}

	/**
	 * 保存间接平差的结果评定
	 */
	@RequiresPermissions("result_evaluation_strict:resultEvaluationStrict:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated ResultEvaluationStrict resultEvaluationStrict) {
		resultEvaluationStrictService.save(resultEvaluationStrict);
		return renderResult(Global.TRUE, text("保存间接平差的结果评定成功！"));
	}
	
}