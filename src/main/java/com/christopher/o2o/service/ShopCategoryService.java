package com.christopher.o2o.service;

import java.util.List;

import com.christopher.o2o.entity.ShopCategory;

/**
 * 商铺类别管理业务接口
 * @ClassName: ShopCategoryService 
 * @author christopher chan
 * @date 2018年11月1日  
 *
 */
public interface ShopCategoryService {

<<<<<<< HEAD
	public static final String SCLISTKEY = "shopcategorylist";
	
	/**
	 * 根据查询条件查询商铺类别列表信息，优先从缓存获取
=======
	/**
	 * 根据查询条件查询商铺类别列表信息
>>>>>>> 89f2009fde0685066fd0d4fcffe0ddb1799f5084
	 * @param shopCategoryCondition 查询条件
	 * @return shopCategoryList 商铺类别列表
	 * @date 2018年11月1日
	 */
	List<ShopCategory> getShopCategoryList(ShopCategory shopCategoryCondition);
}
