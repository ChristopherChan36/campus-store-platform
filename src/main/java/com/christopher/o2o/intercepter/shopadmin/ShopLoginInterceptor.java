package com.christopher.o2o.intercepter.shopadmin;


import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.christopher.o2o.entity.PersonInfo;

/**
 * 店家管理系统登录验证拦截器
 * @ClassName: ShopLoginInterceptor 
 * @author christopher chan
 * @date 2018年12月19日  
 *
 */
public class ShopLoginInterceptor extends HandlerInterceptorAdapter {

	/**
	 * 主要在handler执行前进行拦截操作，即用户操作发生前，改写preHandle里的逻辑，进行拦截
	 * @param request
	 * @param response
	 * @param handler
	 * @throws Exception
	 * @Date 2018年12月19日
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// 从session中获取用户信息
		Object userObj = request.getSession().getAttribute("user");
		if(userObj != null) {
			// 若用户信息不为空，则将session里的用户信息转换成PersonInfo实体类对象
			PersonInfo user = (PersonInfo) userObj;
			// 做空值判断，确保userId不为空并且该帐号的可用状态为1，并且用户类型为店家
			if(user.getUserId() != null && user.getUserId() > 0 && user.getEnableStatus() == 1
					&& user.getUserType() == 2) {
				// 若通过验证则返回true，拦截器放行，用户接下来的操作得以正常执行
				return true;
			}
		}
		// 若不满足登录验证，则直接跳转到帐号登录页面
		PrintWriter out = response.getWriter();
		out.println("<html>");
		out.println("<script>");
		out.println("window.open ('" + request.getContextPath() + "/local/login?usertype=2','_self')");
		out.println("</script>");
		out.println("</html>");
		return false;
	}
}
