package com.christopher.o2o.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.christopher.o2o.entity.Shop;

/**
 * @ClassName: ShopDao 
 * @Description: 店铺Dao接口 
 * @author christopher chan
 * @date 2018年10月15日  
 *
 */
public interface ShopDao {

	/**
	 * 分页查询店铺信息，支持分页功能
	 * 可输入的查询条件有： 店铺名称（模糊）、店铺状态、店铺类别、区域ID、owner（用户）
	 * @param shopCondition 查询条件对象
	 * @param rowIndex 分页从第几行取数据
	 * @param pageSize 每页显示size
	 * @return shopList 店铺列表
	 * @date 2018年10月27日
	 */
	List<Shop> queryShopList(@Param("shopCondition") Shop shopCondition, @Param("rowIndex") int rowIndex,
								@Param("pageSize") int pageSize);
	
	/**
	 * 根据查询条件获取满足查询结果的条数
	 * @param shopCondition 查询条件
	 * @return count 查询结果个数
	 * @date 2018年10月27日
	 */
	int queryShopCount(@Param("shopCondition") Shop shopCondition);
	
	/**
	 * 根据shop id 查询店铺信息
	 * @param shopId 店铺id
	 * @return ShopInfo 店铺信息
	 * @date 2018年10月25日
	 */
	Shop queryByShopId(long shopId);
	
	/**
	 * 新增店铺信息
	 * @param shop
	 * @return int 新增个数
	 * @date 2018年10月15日
	 */
	int insertShop(Shop shop);
	
	/**
	 * 更新店铺信息
	 * @param shop
	 * @return int 更新店铺的个数
	 * @date 2018年10月15日
	 */
	int updateShop(Shop shop);
}
