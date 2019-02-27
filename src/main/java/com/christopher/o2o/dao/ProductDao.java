package com.christopher.o2o.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.christopher.o2o.entity.Product;

public interface ProductDao {

	/**
	 * 查询商品列表信息并且支持分页功能，可输入的条件有： 商品名称（模糊）、商品状态、店铺id、商品类别
	 * @param productCondition 查询条件
	 * @param rowIndex 起始页
	 * @param pageSize 每页显示数量
	 * @return productList 商品列表信息
	 * @date 2018年11月12日
	 */
	List<Product> queryProductList(@Param("productCondition") Product productCondition, @Param("rowIndex") int rowIndex,
			@Param("pageSize") int pageSize);
	
	/**
	 * 查询对应的商品总数
	 * @param productCondition
	 * @return
	 * @date 2018年11月12日
	 */
	int queryProductCount(@Param("productCondition") Product productCondition);
	
	/**
	 * 插入商品信息
	 * 
	 * @param product
	 *            商品信息对象
	 * @return
	 */
	int insertProduct(Product product);

	/**
	 * 根据商品id查询指定的商品信息
	 * 
	 * @param productId
	 *            商品id
	 * @return
	 */
	Product queryProductById(long productId);

	/**
	 * 更新商品信息
	 * 
	 * @param product
	 *            商品信息对象
	 * @return
	 */
	int updateProduct(Product product);

	/**
	 * 删除商品类别之前，将商品信息中的商品类别ID置为空
	 * 
	 * @param productCategoryId
	 *            商品类别id
	 * @return
	 */
	int updateProductCategoryToNull(long productCategoryId);

	/**
	 * 删除商品
	 * 
	 * @param productId
	 *            商品id
	 * @return
	 */
	int deleteProduct(@Param("productId") long productId, @Param("shopId") long shopId);

}
