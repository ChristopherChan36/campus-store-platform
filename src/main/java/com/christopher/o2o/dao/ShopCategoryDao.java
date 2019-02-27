package com.christopher.o2o.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.christopher.o2o.entity.ShopCategory;

public interface ShopCategoryDao {

	/**
	 * 查询店铺分类信息
	 * @param shopCategoryCondition 查询条件
	 * @return
	 * @date 2018年10月31日
	 */
	List<ShopCategory> queryShopCategory(@Param("shopCategoryCondition") 
											ShopCategory shopCategoryCondition);
}
