package com.christopher.o2o.dao;

import java.util.List;

import com.christopher.o2o.entity.ProductImg;

public interface ProductImgDao {

	/**
	 * 查询商品详情图片信息
	 * @param productId 商品id
	 * @return
	 * @date 2018年10月31日
	 */
	List<ProductImg> queryProductImgList(long productId);

	/**
	 * 批量添加商品详情图片信息
	 * @param productImgList 商品详情图片信息
	 * @return
	 * @date 2018年10月31日
	 */
	int batchInsertProductImg(List<ProductImg> productImgList);

	/**
	 * 根据商品id 删除指定的商品详情图片信息
	 * @param productId
	 * @return
	 * @date 2018年10月31日
	 */
	int deleteProductImgByProductId(long productId);
}
