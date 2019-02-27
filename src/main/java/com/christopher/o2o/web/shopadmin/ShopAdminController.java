package com.christopher.o2o.web.shopadmin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value="/shopadmin", method={RequestMethod.GET,RequestMethod.POST})
public class ShopAdminController {

	/**
	 * 进入店铺列表页面
	 */
	@RequestMapping(value="/shoplist")
	public String shopList() {
		return "/shop/shoplist";
	}
	
	/**
	 * 进入店铺管理操作页面
	 */
	@RequestMapping(value="/shopmanagement")
	public String shopManagement() {
		return "/shop/shopmanagement";
	}
	
	/**
	 * 进入店铺详细信息操作页面
	 * @return
	 * @date 2018年10月27日
	 */
	@RequestMapping(value="/shopoperation")
	public String shopOperation() {
		return "/shop/shopoperation";
	}
	
	/**
	 * 进入商铺的商品类别列表页面
	 * @return
	 * @date 2018年10月29日
	 */
	@RequestMapping(value="/productcategorymanagement", method=RequestMethod.GET)
	public String productCategoryManage() {
		return "/shop/productcategorymanagement";
	}
	
	/**
	 * 进入商品添加/编辑页面
	 * @return
	 * @date 2018年11月7日
	 */
	@RequestMapping(value = "/productoperation")
	public String productOperation() {
		return "/shop/productoperation";
	}
	
	@RequestMapping(value = "/productmanagement")
	public String productManagement() {
		return "/shop/productmanagement";
	}
}
