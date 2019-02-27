package com.christopher.o2o.service;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.christopher.o2o.BaseTest;
import com.christopher.o2o.dto.ImageHolder;
import com.christopher.o2o.dto.ShopExecution;
import com.christopher.o2o.entity.Area;
import com.christopher.o2o.entity.PersonInfo;
import com.christopher.o2o.entity.Shop;
import com.christopher.o2o.entity.ShopCategory;
import com.christopher.o2o.enums.ShopStateEnum;
import com.christopher.o2o.exception.ShopOperationException;

public class ShopServiceTest extends BaseTest {

	@Autowired
	private ShopService shopService;
	
	@Test
	public void testGetShopList() {
		Shop shopCondition = new Shop();
		
		ShopCategory shopCategory = new ShopCategory();
		shopCategory.setShopCategoryId(2L);
		shopCondition.setShopCategory(shopCategory);
		
		ShopExecution shopExecution = shopService.getShopList(shopCondition, 1, 3);
		
		System.out.println("店铺列表的个数： " + shopExecution.getShopList().size());
		System.out.println("店铺总数： " + shopExecution.getCount());
	}
	
	@Test
	@Ignore
	public void testModifyShop() throws ShopOperationException, FileNotFoundException {
		Shop shop = new Shop();
		shop.setShopId(4L);
		shop.setShopName("修改后的店铺名称");

		File shopImg = new File("E:/picture/temp/20170605233021865310.jpg");
		InputStream inputStream = new FileInputStream(shopImg);
		ImageHolder shopImgHolder = new ImageHolder("20170605233021865310.jpg", inputStream);
		
		ShopExecution shopExecution =
				shopService.modifyShop(shop, shopImgHolder);
		System.out.println("新的图片地址为：" + shopExecution.getShop().getShopImg());
	}
	
	@Test
	public void testAddShop() throws FileNotFoundException {
		Shop shop = new Shop();
		PersonInfo owner = new PersonInfo();
		Area area = new Area();
		ShopCategory shopCategory = new ShopCategory();
		owner.setUserId(1L);
		area.setAreaId(1);
		shopCategory.setShopCategoryId(1L);
		
		shop.setOwner(owner);
		shop.setArea(area);
		shop.setShopCategory(shopCategory);
		shop.setShopName("测试的店铺2");
		shop.setShopDesc("test2");
		shop.setShopAddr("test2");
		shop.setPhone("18732358351");
		shop.setCreateTime(new Date());
		shop.setEnableStatus(ShopStateEnum.CHECK.getState());
		shop.setAdvice("审核中");
		
		File shopImg = new File("E:/picture/image/xiaohuangren.jpg");
		InputStream inputStream = new FileInputStream(shopImg);
		ImageHolder shopImgHolder = new ImageHolder(shopImg.getName(), inputStream);
		ShopExecution shopExecution = shopService.addShop(shop, shopImgHolder);
		assertEquals(ShopStateEnum.CHECK.getState(), shopExecution.getState());
	}
}
