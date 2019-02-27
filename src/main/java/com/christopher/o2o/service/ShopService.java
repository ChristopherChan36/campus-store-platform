package com.christopher.o2o.service;

import com.christopher.o2o.dto.ImageHolder;
import com.christopher.o2o.dto.ShopExecution;
import com.christopher.o2o.entity.Shop;
import com.christopher.o2o.exception.ShopOperationException;

/**
 * @ClassName: ShopService 
 * @Description: 店铺管理的业务类接口
 * @author christopher chan
 * @date 2018年10月18日  
 *
 */
public interface ShopService {

	/**
	 * 分页查询店铺信息，支持分页功能
	 * 可输入的查询条件有： 店铺名称（模糊）、店铺状态、店铺类别、区域ID、owner（用户）
	 * @param shopCondition 查询条件
	 * @param pageIndex 当前页
	 * @param pageSize 每页显示size
	 * @return shopList 店铺列表
	 * @date 2018年10月27日
	 */
	public ShopExecution getShopList(Shop shopCondition, int pageIndex, int pageSize);
	
	/**
	 * 根据店铺id获取店铺信息
	 * @param shopId 店铺id
	 * @return Shop 店铺信息
	 * @date 2018年10月25日
	 */
	Shop getByShopId(long shopId);
	
	/**
	 * 更新店铺信息，包括对图片的处理
	 * @param shop 店铺信息
	 * @param shopImg 店铺图片信息
	 * @return shopExecution 执行状态
	 * @throws ShopOperationException
	 * @date 2018年10月25日
	 */
	ShopExecution modifyShop(Shop shop, ImageHolder shopImg) throws ShopOperationException;
	
	/**
	 * 添加店铺信息,包括图片处理
	 * @param shop 店铺信息
	 * @param shopImg 店铺缩略图信息
	 * @return ShopExecution 
	 * @throws ShopOperationException
	 * @date 2018年10月23日
	 */
	ShopExecution addShop(Shop shop, ImageHolder shopImg) throws ShopOperationException;
}
