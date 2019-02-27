package com.christopher.o2o.service;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.christopher.o2o.BaseTest;
import com.christopher.o2o.dto.ImageHolder;
import com.christopher.o2o.dto.ProductExecution;
import com.christopher.o2o.entity.Product;
import com.christopher.o2o.entity.ProductCategory;
import com.christopher.o2o.entity.Shop;
import com.christopher.o2o.enums.ProductStateEnum;
import com.christopher.o2o.exception.ShopOperationException;

public class ProductServiceTest extends BaseTest {
	
	@Autowired
	private ProductService productService;
	
	@Test
	public void testAddProduct() throws ShopOperationException, FileNotFoundException {
		// 创建shopId为1且productCategoryId为1的商品实例并且给其成员变量赋值
		Product product = new Product();
		Shop shop = new Shop();
		shop.setShopId(1L);
		ProductCategory productCategory = new ProductCategory();
		productCategory.setProductCategoryId(1L);
		product.setShop(shop);
		product.setProductCategory(productCategory);
		product.setProductName("商品4");
		product.setProductDesc("测试商品4");
		product.setPriority(20);
		product.setCreateTime(new Date());
		product.setLastEditTime(new Date());
		product.setEnableStatus(ProductStateEnum.SUCCESS.getState());
		
		//创建缩略图文件流
		File thumbnailFile = new File("E:/picture/image/xiaohuangren.jpg");
		InputStream inputStream = new FileInputStream(thumbnailFile);
		ImageHolder thumbnail = new ImageHolder(thumbnailFile.getName(), inputStream);
		//创建两个商品详情图片文件流并且将它们添加到详情列表中
		File productImg1 = new File("E:/picture/image/xiaohuangren.jpg");
		InputStream is1 = new FileInputStream(productImg1);
		File productImg2 = new File("E:/picture/image/dabai.jpg");
		InputStream is2 = new FileInputStream(productImg2);
		List<ImageHolder> productImgList = new ArrayList<>();
		productImgList.add(new ImageHolder(productImg1.getName(), is1));
		productImgList.add(new ImageHolder(productImg2.getName(), is2));
		
		//添加商品信息并且验证
		ProductExecution productExecution = 
				productService.addProduct(product, thumbnail, productImgList);
		assertEquals(ProductStateEnum.SUCCESS.getState(), productExecution.getState());
	}
	
	@Test
	public void testModifyProduct() throws FileNotFoundException {
		// 创建shopId为1且productCategory为1的商品实例并且给其成员变量赋值
		Product product = new Product();
		Shop shop = new Shop();
		shop.setShopId(1L);
		ProductCategory productCategory = new ProductCategory();
		productCategory.setProductCategoryId(3L);
		
		product.setProductId(1L);
		product.setShop(shop);
		product.setProductCategory(productCategory);
		product.setProductName("商品-测试");
		product.setProductDesc("测试ProductService层的商品信息");
		
		//创建缩略图文件流
		File thumbnailFile = new File("E:/picture/image/xiaohuangren.jpg");
		InputStream inputStream = new FileInputStream(thumbnailFile);
		ImageHolder thumbnail = new ImageHolder(thumbnailFile.getName(), inputStream);
		//创建两个商品详情图片文件流并将它们添加到详情图片列表中
		File productImg1 = new File("E:/picture/image/lufei.jpg");
		InputStream is1 = new FileInputStream(productImg1);
		File productImg2 = new File("E:/picture/image/dabai.jpg");
		InputStream is2 = new FileInputStream(productImg2);
		List<ImageHolder> productImgList = new ArrayList<>();
		productImgList.add(new ImageHolder(productImg1.getName(), is1));
		productImgList.add(new ImageHolder(productImg2.getName(), is2));
		
		//修改商品信息并且验证
		ProductExecution productExecution = 
				productService.modifyProduct(product, thumbnail, productImgList);
		assertEquals(ProductStateEnum.SUCCESS.getState(), productExecution.getState());
	}
}
