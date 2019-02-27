package com.christopher.o2o.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.christopher.o2o.dao.ShopDao;
import com.christopher.o2o.dto.ImageHolder;
import com.christopher.o2o.dto.ShopExecution;
import com.christopher.o2o.entity.Shop;
import com.christopher.o2o.enums.ShopStateEnum;
import com.christopher.o2o.exception.ShopOperationException;
import com.christopher.o2o.service.ShopService;
import com.christopher.o2o.utils.ImageUtil;
import com.christopher.o2o.utils.PageCalculator;
import com.christopher.o2o.utils.PathUtil;

/**
 * @ClassName: ShopServiceImpl
 * @Description: 店铺管理业务接口的实现类
 * @author christopher chan
 * @date 2018年10月18日
 *
 */
@Service
public class ShopServiceImpl implements ShopService {

	@Autowired
	private ShopDao shopDao;

	@Override
	public ShopExecution getShopList(Shop shopCondition, int pageIndex, int pageSize) {
		// 将页码转换成行码
		int rowIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
		// 根据查询条件，调用dao层返回相关的店铺列表
		List<Shop> shopList = shopDao.queryShopList(shopCondition, rowIndex, pageSize);
		// 根据相同的查询条件，返回店铺列表信息
		int count = shopDao.queryShopCount(shopCondition);
		ShopExecution shopExecution = new ShopExecution();
		if(shopList != null) {
			shopExecution.setShopList(shopList);
			shopExecution.setCount(count);
		}else {
			shopExecution.setState(ShopStateEnum.INNER_ERROR.getState());
		}
		return shopExecution;
	}
	
	/**
	 * 添加店铺信息
	 * 
	 * @param shop
	 *            店铺信息
	 * @param shopImg
	 *            店铺缩略图信息
	 * @return shopExecution
	 * @Date 2018年10月18日
	 */
	@Override
	@Transactional
	public ShopExecution addShop(Shop shop, ImageHolder shopImg) {
		// 1.空值判断
		if (shop == null) {
			return new ShopExecution(ShopStateEnum.NULL_SHOP);
		} else if (shop.getArea() == null) {
			return new ShopExecution(ShopStateEnum.NULL_AREA);
		} else if (shop.getShopCategory() == null) {
			return new ShopExecution(ShopStateEnum.NULL_SHOPCATEGORY);
		}
		try {
			// 2.给店铺信息赋 初始值
			// 审核中
			shop.setAdvice("审核中");
			shop.setEnableStatus(0);
			shop.setCreateTime(new Date());
			shop.setLastEditTime(new Date());
			// 3.添加店铺信息
			int effectedNum = shopDao.insertShop(shop);
			if (effectedNum <= 0) {
				// 抛出 ShopOperationException 终止事务
				throw new ShopOperationException("店铺创建失败！");
			} else {
				if (shopImg.getImageInputStream() != null) {
					// 4.存储图片
					try {
						addShopImg(shop, shopImg);
					} catch (Exception e) {
						throw new ShopOperationException("addShopImg error: " + e.getMessage());
					}
					// 5.更新店铺的图片地址
					effectedNum = shopDao.updateShop(shop);
					if (effectedNum <= 0) {
						throw new ShopOperationException("更新图片地址失败！");
					}
				}
			}
		} catch (Exception e) {
			throw new ShopOperationException("addShop error: " + e.getMessage());
		}
		return new ShopExecution(ShopStateEnum.CHECK, shop);
	}

	/**
	 * 上传处理图片
	 * 
	 * @param shop
	 *            店铺信息
	 * @param shopImg
	 *            上传的店铺缩略图信息
	 * @date 2018年10月18日
	 */
	private void addShopImg(Shop shop, ImageHolder shopImg) {
		// 获取shop图片目录的相对值路径
		String dest = PathUtil.getShopImagePath(shop.getShopId());
		String shopImgAddr = ImageUtil.generateThumbnail(shopImg, dest);
		shop.setShopImg(shopImgAddr);
	}

	@Override
	public Shop getByShopId(long shopId) {

		return shopDao.queryByShopId(shopId);
	}

	@Override
	@Transactional
	public ShopExecution modifyShop(Shop shop, ImageHolder shopImg)
			throws ShopOperationException {
		if (shop == null || shop.getShopId() == null) {
			return new ShopExecution(ShopStateEnum.NULL_SHOP);
		} else {
			try {
				// 1. 判断是否需要处理图片
				if (shopImg.getImageInputStream() != null &&
						shopImg.getImageName() != null &&
						!"".equals(shopImg.getImageName())) {
					Shop tempShop = shopDao.queryByShopId(shop.getShopId());
					if (tempShop.getShopImg() != null) {
						ImageUtil.deleteFileOrPath(tempShop.getShopImg());
					}
					// 添加店铺图片信息
					addShopImg(shop, shopImg);
				}
				// 2. 更新店铺信息
				shop.setLastEditTime(new Date());
				int effectedNum = shopDao.updateShop(shop);
				if (effectedNum <= 0) {
					return new ShopExecution(ShopStateEnum.INNER_ERROR);
				} else {
					shop = shopDao.queryByShopId(shop.getShopId());
					return new ShopExecution(ShopStateEnum.SUCCESS, shop);
				}
			} catch (Exception e) {
				throw new ShopOperationException("modifyShop err: " + e.getMessage());
			}
		}
	}

}
