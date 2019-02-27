package com.christopher.o2o.web.frontend;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.christopher.o2o.entity.Product;
import com.christopher.o2o.service.ProductService;

@RestController
@RequestMapping("/frontend")
public class ProductDetailController {

	@Autowired
	private ProductService productService;

	/**
	 * 根据商品id获取商品的详细信息
	 * @param productId 商品id
	 * @return
	 * @date 2018年11月22日
	 */
	@RequestMapping(value = "/listproductdetailpageinfo", method = RequestMethod.GET)
	private Map<String, Object> listProductDetailPageInfo(@RequestParam(value = "productId") Long productId) {
		Map<String, Object> modelMap = new HashMap<>();
		// 用于保存商品详细信息
		Product product = null;
		// 商品id的非空判断
		if(productId != null) {
			// 获取 商品id为productId的商品信息
			product = productService.getProductById(productId);
			// 将获取的商品信息储存
			modelMap.put("product", product);
			modelMap.put("success", true);
		}else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty productId");
		}
		return modelMap;
	}
}
