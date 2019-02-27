package com.christopher.o2o.utils;

import javax.servlet.http.HttpServletRequest;

import com.google.code.kaptcha.Constants;

/**
 * @ClassName: CodeUtil 
 * @Description: 验证码的校验  
 * @author christopher chan
 * @date 2018年10月24日  
 *
 */
public class CodeUtil {

	public static boolean checkVerifyCode(HttpServletRequest request) {
		//从request域对象中取出kaptcha实际生成的验证码
		String verifyCodeExpected = (String) request.getSession().
								getAttribute(Constants.KAPTCHA_SESSION_KEY);
		//用户输入的验证码
		String verifyCodeActual = HttpServletRequestUtil.getString(request, "verifyCodeActual");
		if(verifyCodeActual == null || !verifyCodeActual.equalsIgnoreCase(verifyCodeExpected)) {
			return false;
		}
		return true;
	}
}
