package com.christopher.o2o.intercepter.shopadmin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.christopher.o2o.entity.Shop;

/**
 * 店家管理系统操作验证拦截器
 * @ClassName: ShopPermissionInterceptor 
 * @author christopher chan
 * @date 2018年12月19日  
 *
 */
public class ShopPermissionInterceptor extends HandlerInterceptorAdapter {

	/**
	 * 当用户进行店铺操作时，对操作请求进行拦截，验证该用户是否具有操作此店铺的权限
	 * @param request
	 * @param response
	 * @param handler
	 * @throws Exception
	 * @Date 2018年12月19日
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// 从session中获取当前选择的店铺信息
		Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
		// 从session中获取当前用户可操作的店铺列表
		@SuppressWarnings("unchecked")
		List<Shop> shopList = (List<Shop>) request.getSession().getAttribute("shopList");
		// 非空判断
		if(currentShop != null && currentShop.getShopId() > 0 
				&& shopList != null && shopList.size() > 0) {
			// 遍历可操作的店铺列表
			for (Shop shop : shopList) {
				// 如果当前店铺在可操作的列表里则返回true，进行接下来的用户操作
				if(shop.getShopId() == currentShop.getShopId()) {
					return true;
				}
			}
		}
		// 若不满足拦截器的权限验证则返回false，终止用户的操作
		return false;
	}
}
