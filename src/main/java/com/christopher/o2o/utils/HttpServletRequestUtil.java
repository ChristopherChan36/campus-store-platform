package com.christopher.o2o.utils;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName: HttpServletRequestUtil 
 * @Description: 处理请求参数的工具类
 * @author christopher chan
 * @date 2018年10月19日  
 *
 */
public class HttpServletRequestUtil {

	/**
	 * 接受处理int类型的参数
	 * @param request 请求对象
	 * @param key 参数
	 * @return 处理后的参数
	 * @date 2018年10月19日
	 */
	public static int getInt(HttpServletRequest request, String key) {
		try {
			return Integer.decode(request.getParameter(key));
		} catch (Exception e) {
			return -1;
		}
	}
	
	/**
	 * 接收处理long类型的参数
	 * @param request 请求对象
	 * @param key 参数
	 * @return 处理后的参数
	 * @date 2018年10月19日
	 */
	public static long getLong(HttpServletRequest request, String key) {
		try {
			return Long.valueOf(request.getParameter(key));
		} catch (Exception e) {
			return -1;
		}
	}
	
	/**
	 * 接收处理double类型的参数
	 * @param request 请求对象
	 * @param key  参数
	 * @return  处理后的参数
	 * @date 2018年10月19日
	 */
	public static Double getDouble(HttpServletRequest request, String key) {
		try {
			return Double.valueOf(request.getParameter(key));
		} catch (Exception e) {
			return -1d;
		}
	}

	/**
	 * 接收处理boolean类型的参数
	 * @param request 请求对象
	 * @param key  参数
	 * @return  处理后的参数
	 * @date 2018年10月19日
	 */
	public static Boolean getBoolean(HttpServletRequest request, String key) {
		try {
			return Boolean.valueOf(request.getParameter(key));
		} catch (Exception e) {
			return false;
		}
	}
	
	
	/**
	 * 接收处理String类型的参数
	 * @param request 请求对象
	 * @param key  参数
	 * @return  处理后的参数
	 * @date 2018年10月19日
	 */
	public static String getString(HttpServletRequest request, String key) {
		try {
			String result = request.getParameter(key).replaceAll("['\"<>]", "");
			if(result != null) {
				result = result.trim();
			}else if("".equals(result)) {
				result = null;
			}
			return result;
		} catch (Exception e) {
			return null;
		}
	}
	
}
