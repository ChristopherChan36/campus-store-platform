package com.christopher.o2o.dao;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import com.christopher.o2o.BaseTest;
import com.christopher.o2o.entity.ProductCategory;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProductCategoryDaoTest extends BaseTest {

	@Autowired
	private ProductCategoryDao productCategoryDao;
	
	@Test
	public void testBQueryProductCategory() {
		long shopId = 2L;
		List<ProductCategory> productCategoryList = 
				productCategoryDao.queryProductCategoryList(shopId);
		System.out.println("该店铺下自定义的商品类别的个数： " + productCategoryList.size());
	}
	
	@Test
	public void testAInsertProductCategoryy() {
		ProductCategory productCategory = new ProductCategory();
		productCategory.setProductCategoryName("海鲜");
		productCategory.setProductCategoryDesc("新鲜的海产品");
		productCategory.setPriority(1);
		productCategory.setShopId(2L);
		productCategory.setCreateTime(new Date());
		productCategory.setLastEditTime(new Date());
		
		ProductCategory productCategory1 = new ProductCategory();
		productCategory1.setProductCategoryName("无肉不欢");
		productCategory1.setProductCategoryDesc("喜欢肉肉的小伙伴进来排队！");
		productCategory1.setPriority(2);
		productCategory1.setShopId(2L);
		productCategory1.setCreateTime(new Date());
		productCategory1.setLastEditTime(new Date());
		
		List<ProductCategory> productCategoryList = new ArrayList<>();
		productCategoryList.add(productCategory);
		productCategoryList.add(productCategory1);
		
		int effectedNum = productCategoryDao.batchInsertProductCategory(productCategoryList);
		assertEquals(2, effectedNum);
	}
	
	@Test
	public void testCDeleteProductCategory() {
		long shopId = 2L;
		List<ProductCategory> productCategoryList = productCategoryDao.queryProductCategoryList(shopId);
		for (ProductCategory productCategory : productCategoryList) {
			if("海鲜".equals(productCategory.getProductCategoryName()) ||
					"无肉不欢".equals(productCategory.getProductCategoryName())) {
				int effectedNum = productCategoryDao.deleteProductCategory(productCategory.getProductCategoryId(), shopId);
				assertEquals(1, effectedNum);
			}
		}
	}

}
