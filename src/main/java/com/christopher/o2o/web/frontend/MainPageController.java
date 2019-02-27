package com.christopher.o2o.web.frontend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.christopher.o2o.entity.HeadLine;
import com.christopher.o2o.entity.ShopCategory;
import com.christopher.o2o.service.HeadLineService;
import com.christopher.o2o.service.ShopCategoryService;

@RestController
@RequestMapping("/frontend")
public class MainPageController {

	@Autowired
	private HeadLineService headLineService;
	@Autowired
	private ShopCategoryService shopCategoryService;

	/**
	 * 初始化前端展示系统的主页信息，包括获取一级店铺类别列表一级头条列表信息
	 * 
	 * @return
	 * @date 2018年11月13日
	 */
	@RequestMapping(value = "/listmainpageinfo", method = RequestMethod.GET)
	private Map<String, Object> list1stShopCategory() {
		Map<String, Object> modelMap = new HashMap<>();
		List<ShopCategory> shopCategoryList = new ArrayList<>();
		try {
			// 获取一级店铺类别列表（即parentId为空的ShopCategory）
			shopCategoryList = shopCategoryService.getShopCategoryList(null);
			modelMap.put("shopCategoryList", shopCategoryList);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
			return modelMap;
		}
		List<HeadLine> headLineList = new ArrayList<>();
		try {
			// 获取状态为 可用(1)的头条列表信息
			HeadLine headLineCondition = new HeadLine();
			headLineCondition.setEnableStatus(1);
			headLineList = headLineService.getHeadLineList(headLineCondition);
			modelMap.put("headLineList", headLineList);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
			return modelMap;
		}
		modelMap.put("success", true);
		return modelMap;
	}
}
