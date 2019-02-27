package com.christopher.o2o.web.frontend;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.christopher.o2o.dto.ProductExecution;
import com.christopher.o2o.entity.Product;
import com.christopher.o2o.entity.ProductCategory;
import com.christopher.o2o.entity.Shop;
import com.christopher.o2o.service.ProductCategoryService;
import com.christopher.o2o.service.ProductService;
import com.christopher.o2o.service.ShopService;
import com.christopher.o2o.utils.HttpServletRequestUtil;

@RestController
@RequestMapping("/frontend")
public class ShopDetailController {

	@Autowired
	private ShopService shopService;
	@Autowired
	private ProductService productService;
	@Autowired
	private ProductCategoryService productCategoryService;

	/**
	 * 获取店铺信息以及该店铺下的商品类别列表信息
	 * 
	 * @param shopId
	 *            店铺id
	 * @return
	 * @date 2018年11月20日
	 */
	@RequestMapping(value = "/listshopdetailpageinfo", method = RequestMethod.GET)
	private Map<String, Object> listShopDetailPageInfo(@RequestParam(value = "shopId") Long shopId) {
		Map<String, Object> modelMap = new HashMap<>();
		Shop shop = null;
		List<ProductCategory> productCategoryList = null;
		// 店铺id的非空判断
		if (shopId != null) {
			// 获取 店铺id为shopId的店铺信息
			shop = shopService.getByShopId(shopId);
			// 获取店铺下面的商品类别列表信息
			productCategoryList = productCategoryService.getProductCategoryList(shopId);
			// 将获取的店铺信息和店铺下的商品类别列表信息储存
			modelMap.put("shop", shop);
			modelMap.put("productCategoryList", productCategoryList);
			modelMap.put("success", true);
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty shopId");
		}
		return modelMap;
	}

	/**
	 * 根据查询条件分页列出该店铺下面的所有商品信息
	 * 
	 * @param request
	 * @return
	 * @date 2018年11月20日
	 */
	@RequestMapping(value = "/listproductsbyshop", method = RequestMethod.GET)
	private Map<String, Object> listProductsByShop(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<>();
		// 获取页码
		int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
		// 获取每页显示的最大条数
		int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
		// 获取店铺id
		long shopId = HttpServletRequestUtil.getLong(request, "shopId");
		// 空值判断
		if ((pageIndex > -1) && (pageSize > -1) && (shopId > -1)) {
			// 尝试获取商品类别id
			long productCategoryId = HttpServletRequestUtil.getLong(request, "productCategoryId");
			// 尝试获取模糊查询输入的商品名称
			String productName = HttpServletRequestUtil.getString(request, "productName");
			// 根据查询需求，拼接组合查询条件
			Product productCondition = compactProductCondition4Search(shopId, productCategoryId, productName);
			// 按照传入的查询条件以及分页信息返回相应的商品列表和总数
			ProductExecution pe = productService.getProductList(productCondition, pageIndex, pageSize);
			modelMap.put("productList", pe.getProductList());
			modelMap.put("count", pe.getCount());
			modelMap.put("success", true);
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty pageSize or pageIndex or shopId");
		}
		return modelMap;
	}

	/**
	 * 组合查询条件，并且将条件封装到ProductCondition对象中
	 * @param shopId 店铺id
	 * @param productCategoryId 店铺类别id
	 * @param productName 店铺名称
	 * @return
	 * @date 2018年11月20日
	 */
	private Product compactProductCondition4Search(long shopId, long productCategoryId, String productName) {
		Product productCondition = new Product();
		Shop shop = new Shop();
		shop.setShopId(shopId);
		productCondition.setShop(shop);
		if (productCategoryId != -1L) {
			// 查询某个商品类别下面的商品列表
			ProductCategory productCategory = new ProductCategory();
			productCategory.setProductCategoryId(productCategoryId);
			productCondition.setProductCategory(productCategory);
		}
		if (productName != null) {
			// 模糊查询商品名称
			productCondition.setProductName(productName);
		}
		// 只允许选出状态为上架的商品
		productCondition.setEnableStatus(1);
		return productCondition;
	}
}
