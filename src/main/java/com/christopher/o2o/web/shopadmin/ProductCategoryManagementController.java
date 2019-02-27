package com.christopher.o2o.web.shopadmin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.christopher.o2o.dto.ProductCategoryExecution;
import com.christopher.o2o.dto.Result;
import com.christopher.o2o.entity.ProductCategory;
import com.christopher.o2o.entity.Shop;
import com.christopher.o2o.enums.ProductCategoryStateEnum;
import com.christopher.o2o.exception.ProductCategoryOperationException;
import com.christopher.o2o.service.ProductCategoryService;

/**
 * @ClassName: ProductCategoryManagementController 
 * @Description:  店铺的商品类别 controller
 * @author christopher chan
 * @date 2018年10月28日  
 *
 */
@Controller
@RequestMapping("/shopadmin")
public class ProductCategoryManagementController {

	@Autowired
	private ProductCategoryService productCategoryService;
	
	/**
	 * 批量添加商品类别信息
	 * @param productCategoryList 商品类别信息
	 * @param request
	 * @return
	 * @date 2018年10月29日
	 */
	@RequestMapping(value="addproductcategories", method=RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> addProductCategories(@RequestBody List<ProductCategory> productCategoryList, 
			HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<>();
		//从session中取出当前店铺信息
		Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
		//对前台传递过来的 商品信心添加商铺id
		for (ProductCategory productCategory : productCategoryList) {
			productCategory.setShopId(currentShop.getShopId());
		}
		
		//商品类别信息为空判断
		if(productCategoryList != null && productCategoryList.size() > 0) {
			try {
				ProductCategoryExecution productCategoryExecution = productCategoryService.batchAddProductCategory(productCategoryList);
				if(productCategoryExecution.getState() == ProductCategoryStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
				}else {
					modelMap.put("success", false);
					modelMap.put("errMsg", productCategoryExecution.getStateInfo());
				}
			} catch (ProductCategoryOperationException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
				return modelMap;
			}
		}else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "请至少输入一个商品类别！");
		}
		return modelMap;
	}
	
	/**
	 * 获取商铺的商品类别 信息
	 * @param request
	 * @return Result<List<ProductCategory>>
	 * @date 2018年10月29日
	 */
	@RequestMapping(value="getproductcategorylist", method=RequestMethod.GET)
	@ResponseBody
	private Result<List<ProductCategory>> getProductCategoryList(HttpServletRequest request) {
		//从session中获取店铺id
		Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
		List<ProductCategory> productCategoryList = null;
		if(currentShop != null && currentShop.getShopId() > 0) {
			productCategoryList = productCategoryService.
					getProductCategoryList(currentShop.getShopId());
			return new Result<List<ProductCategory>>(true, productCategoryList);
		}else {
			ProductCategoryStateEnum productCategoryStateEnum = ProductCategoryStateEnum.INNER_ERROR;
			return new Result<List<ProductCategory>>(false, productCategoryStateEnum.getState(),
					productCategoryStateEnum.getStateInfo());
		}
	}
	
	/**
	 * 移除店铺 指定的 商品类别信息
	 * @param productCategoryId 商品类别id
	 * @param request
	 * @return
	 * @date 2018年10月31日
	 */
	@RequestMapping(value="/removeproductcategory", method=RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> removeProductCategory(Long productCategoryId, HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<>();
		if(productCategoryId != null && productCategoryId > 0) {
			try {
				//从session域中获取当前的店铺信息
				Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
				//根据店铺id以及商品类别id删除该商品类别信息
				ProductCategoryExecution productCategoryExecution = productCategoryService.deleteProductCategory(productCategoryId,
						currentShop.getShopId());
				//判断执行结果
				if(productCategoryExecution.getState() == ProductCategoryStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
				}else {
					modelMap.put("success", false);
					modelMap.put("errMsg", productCategoryExecution.getStateInfo());
				}
			} catch (ProductCategoryOperationException e) {
				modelMap.put("success", true);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}
		}else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "请至少选择一个商品类别！");
		}
		return modelMap;
	}
}
