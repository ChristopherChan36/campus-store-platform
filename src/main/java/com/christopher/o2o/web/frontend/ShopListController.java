package com.christopher.o2o.web.frontend;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.christopher.o2o.dto.ShopExecution;
import com.christopher.o2o.entity.Area;
import com.christopher.o2o.entity.Shop;
import com.christopher.o2o.entity.ShopCategory;
import com.christopher.o2o.service.AreaService;
import com.christopher.o2o.service.ShopCategoryService;
import com.christopher.o2o.service.ShopService;
import com.christopher.o2o.utils.HttpServletRequestUtil;

@RestController
@RequestMapping("/frontend")
public class ShopListController {

	@Autowired
	private AreaService areaService;
	@Autowired
	private ShopCategoryService shopCategoryService;
	@Autowired
	private ShopService shopService;

	/**
	 * 返回店铺列表页里的 商铺类别列表(二级或者一级) 以及 区域列表信息
	 * 
	 * @param parentId
	 *            店铺类别的父类别id
	 * @param request
	 * @return
	 * @date 2018年11月14日
	 */
	@RequestMapping(value = "/listshopspageinfo", method = RequestMethod.GET)
	private Map<String, Object> listShopsPageInfo(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<>();
		long parentId = HttpServletRequestUtil.getLong(request, "parentId");
		// 1.首先查询符合筛选条件的商铺类别信息
		List<ShopCategory> shopCategoryList = null;
		if (parentId > -1) {
			// 1.1 如果 一级店铺大类存在，则取出该一级ShopCategory下的二级ShopCategory列表
			try {
				ShopCategory shopCategoryCondition = new ShopCategory();
				ShopCategory parentCategory = new ShopCategory();
				parentCategory.setShopCategoryId(parentId);
				shopCategoryCondition.setParent(parentCategory);
				shopCategoryList = shopCategoryService.getShopCategoryList(shopCategoryCondition);
			} catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
			}
		} else {
			try {
				// 1.2 如果 一级店铺大类不存在，则取出所有一级ShopCategory(用户在首页选择的是全部商店列表)
				shopCategoryList = shopCategoryService.getShopCategoryList(null);
			} catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
			}
		}
		modelMap.put("shopCategoryList", shopCategoryList);
		// 2.查询相关的区域列表信息
		List<Area> areaList = null;
		try {
			// 获取区域列表信息
			areaList = areaService.getAreaList();
			modelMap.put("areaList", areaList);
			modelMap.put("success", true);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
		}
		return modelMap;
	}

	/**
	 * 获取指定查询条件下的店铺列表
	 * 
	 * @param request
	 * @return
	 * @date 2018年11月14日
	 */
	@RequestMapping(value = "/listshops", method = RequestMethod.GET)
	private Map<String, Object> listShops(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<>();
		// 获取分页页码
		int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
		// 获取每页显示的最大数据size
		int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
		// 非空判断
		if ((pageIndex > -1) && (pageSize > -1)) {
			// 获取一级类别id
			long parentId = HttpServletRequestUtil.getLong(request, "parentId");
			// 获取选择的特定二级类别id
			long shopCategoryId = HttpServletRequestUtil.getLong(request, "shopCategoryId");
			// 获取区域id
			int areaId = HttpServletRequestUtil.getInt(request, "areaId");
			// 获取模糊查询的店铺名称
			String shopName = HttpServletRequestUtil.getString(request, "shopName");
			// 对上述的查询条件进行组合，获取各单个条件综合之后的查询条件
			Shop shopCondition = compactShopCondition4Search(parentId, shopCategoryId, areaId, shopName);
			// 根据查询条件和分页信息获取店铺列表，并且返回总数
			ShopExecution shopExecution = shopService.getShopList(shopCondition, pageIndex, pageSize);
			modelMap.put("shopList", shopExecution.getShopList());
			modelMap.put("count", shopExecution.getCount());
			modelMap.put("success", true);
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty pageSize or pageIndex");
		}
		return modelMap;
	}

	/**
	 * 组合查询条件，并且将条件封装到ShopCondition对象中
	 * 
	 * @param parentId
	 *            一级类别id
	 * @param shopCategoryId
	 *            选择的特定二级类别id
	 * @param areaId
	 *            区域id
	 * @param shopName
	 *            店铺名称
	 * @return
	 * @date 2018年11月14日
	 */
	private Shop compactShopCondition4Search(long parentId, long shopCategoryId, int areaId, String shopName) {
		Shop shopCondition = new Shop();
		if (parentId != -1L) {
			// 查询某个一级ShopCategory下面的所有二级ShopCategory里面的店铺列表
			ShopCategory childCategory = new ShopCategory();
			ShopCategory parentCategory = new ShopCategory();
			parentCategory.setShopCategoryId(parentId);
			childCategory.setParent(parentCategory);
			shopCondition.setShopCategory(childCategory);
		}
		// 查询某个二级ShopCategory下面的店铺列表
		if (shopCategoryId != -1L) {
			ShopCategory shopCategory = new ShopCategory();
			shopCategory.setShopCategoryId(shopCategoryId);
			shopCondition.setShopCategory(shopCategory);
		}
		// 查询位于某个区域id下的店铺列表
		if (areaId != -1) {
			Area area = new Area();
			area.setAreaId(areaId);
			shopCondition.setArea(area);
		}
		// 模糊查询店铺名称
		if (shopName != null) {
			shopCondition.setShopName(shopName);
		}
		// 前端展示的店铺都是审核通过的店铺
		shopCondition.setEnableStatus(1);
		return shopCondition;
	}
}
