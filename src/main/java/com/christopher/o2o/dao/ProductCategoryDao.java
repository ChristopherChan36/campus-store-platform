package com.christopher.o2o.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.christopher.o2o.entity.ProductCategory;

/**
 * @ClassName: ProductCategoryDao 
 * @Description: 商品类别管理  
 * @author christopher chan
 * @date 2018年10月28日  
 *
 */
public interface ProductCategoryDao {

	/**
	 * 根据商铺id 查询其下的商品类别结果
	 * @param shopId 商铺id
	 * @return productCategoryList 商品类别列表
	 * @date 2018年10月28日
	 */
	List<ProductCategory> queryProductCategoryList(long shopId);
	
	/**
	 * 批量新增 商品类别
	 * @param productCategoryList 商品类别信息
	 * @return int 添加的影响行数
	 * @date 2018年10月29日
	 */
	int batchInsertProductCategory(List<ProductCategory> productCategoryList);
	
	/**
	 * 删除指定的商品类别
	 * @param productCategoryId 商品类别id
	 * @param shopId 店铺id
	 * @return int 添加的影响行数
	 * @date 2018年10月31日
	 */
	int deleteProductCategory(@Param("productCategoryId") long productCategoryId, 
			@Param("shopId") long shopId);
}
