package com.christopher.o2o.service;

import java.util.List;

import com.christopher.o2o.dto.ProductCategoryExecution;
import com.christopher.o2o.entity.ProductCategory;
import com.christopher.o2o.exception.ProductCategoryOperationException;

/**
 * @ClassName: ProductCategoryService 
 * @Description: 店铺的商品类别业务层管理 
 * @author christopher chan
 * @date 2018年10月28日  
 *
 */
public interface ProductCategoryService {

	/**
	 * 根据店铺id查询指定店铺下的商品的类别列表
	 * @param shopId 
	 * @return List<ProductCategory> 商品类别列表
	 * @date 2018年10月28日
	 */
	List<ProductCategory> getProductCategoryList(long shopId);
	
	/**
	 * 批量添加 商品类别
	 * @param productCategoryList 商品类别集合
	 * @return ProductCategoryExecution
	 * @throws ProductCategoryOperationException
	 * @date 2018年10月29日
	 */
	ProductCategoryExecution batchAddProductCategory(List<ProductCategory> productCategoryList)
		throws ProductCategoryOperationException; 
	
	/**
	 * 将指定商品类别下的商品信息中的 类别id置为空， 再删除该商品类别信息
	 * @param productCategoryId 商品类别id
	 * @param shopId 店铺id
	 * @return
	 * @throws ProductCategoryOperationException
	 * @date 2018年10月31日
	 */
	ProductCategoryExecution deleteProductCategory(long productCategoryId, long shopId)
			throws ProductCategoryOperationException;
}
