package com.christopher.o2o.service;

import java.util.List;

import com.christopher.o2o.dto.ImageHolder;
import com.christopher.o2o.dto.ProductExecution;
import com.christopher.o2o.entity.Product;
import com.christopher.o2o.exception.ProductOperationException;

/**
 * 商品管理业务接口
 * @ClassName: ProductService 
 * @Description: 商品管理业务接口
 * @author christopher chan
 * @date 2018年11月1日  
 */
public interface ProductService {

	/**
	 * 添加商品信息以及 商品详情图片上传功能
	 * @param product 商品信息
	 * @param thumbnail 商品缩略图信息
	 * @param productImgList 商品详情图片信息
	 * @return 
	 * @throws ProductOperationExeception
	 * @date 2018年11月2日
	 */
	ProductExecution addProduct(Product product, ImageHolder thumbnail,
			List<ImageHolder> productImgList) throws ProductOperationException;
	
	/**
	 * 通过商品id查询指定的商品信息
	 * @param productId 商品id
	 * @return Product 指定商品信息
	 * @date 2018年11月8日
	 */
	Product getProductById(long productId);
	
	/**
	 * 修改商品信息以及缩略图和详情图片的处理
	 * @param product 商品信息
	 * @param thumbnail 缩略图文件流
	 * @param productImgList 详情图片文件流
	 * @return
	 * @throws ProductOperationException
	 * @date 2018年11月8日
	 */
	ProductExecution modifyProduct(Product product, ImageHolder thumbnail,
			List<ImageHolder> productImgList) throws ProductOperationException;
	
	/**
	 * 查询商品列表信息并且支持分页功能，可输入的条件有： 商品名称（模糊）、商品状态、店铺id、商品类别
	 * @param productCondition 查询条件
	 * @param pageIndex 起始页
	 * @param pageSize 每页显示数
	 * @return
	 * @date 2018年11月12日
	 */
	ProductExecution getProductList(Product productCondition, int pageIndex, int pageSize);
}
