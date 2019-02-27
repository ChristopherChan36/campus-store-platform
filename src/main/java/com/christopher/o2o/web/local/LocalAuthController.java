package com.christopher.o2o.web.local;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.christopher.o2o.dto.LocalAuthExecution;
import com.christopher.o2o.entity.LocalAuth;
import com.christopher.o2o.entity.PersonInfo;
import com.christopher.o2o.enums.LocalAuthStateEnum;
import com.christopher.o2o.exception.LocalAuthOperationException;
import com.christopher.o2o.service.LocalAuthService;
import com.christopher.o2o.utils.CodeUtil;
import com.christopher.o2o.utils.HttpServletRequestUtil;
import com.christopher.o2o.utils.MD5;

@RestController
@RequestMapping(value = "/local", method = {RequestMethod.GET, RequestMethod.POST})
public class LocalAuthController {
	@Autowired
	private LocalAuthService localAuthService;
	
	/**
	 * 将用户信息与平台帐号进行绑定
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/bindlocalauth", method = RequestMethod.POST)
	private Map<String, Object> bindLocalAuth(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<>();
		// 验证码校验
		if(!CodeUtil.checkVerifyCode(request)) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "您输入的验证码有误！");
			return modelMap;
		}
		// 获取输入的平台帐号名称
		String username = HttpServletRequestUtil.getString(request, "username");
		// 获取输入的密码
		String password = HttpServletRequestUtil.getString(request, "password");
		// 从session中获取当前用户信息(用户一旦通过微信登录之后，便能获取到用户的信息)
		PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
		
		// 非空判断，要求帐号密码以及当前的用户session中存在用户信息
		if(username != null && password != null && user != null && user.getUserId() != null) {
			// 创建本地帐号 LocalAuth对象并且赋值
			LocalAuth localAuth = new LocalAuth();
			localAuth.setUsername(username);
			localAuth.setPassword(password);
			localAuth.setPersonInfo(user);
			// 绑定帐号
			LocalAuthExecution localAuthExecution = localAuthService.bindLocalAuth(localAuth);
			// 操作成功与否判断
			if(LocalAuthStateEnum.SUCCESS.getState() == localAuthExecution.getState()) {
				modelMap.put("success", true);
			}else {
				modelMap.put("success", false);
				modelMap.put("errMsg", localAuthExecution.getStateInfo());
			}
		}else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "用户名和密码不能为空！");
		}
		return modelMap;
	}
	
	/**
	 * 更改平台帐号密码
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/changelocalpwd", method = RequestMethod.POST)
	private Map<String, Object> changeLocalPassword(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<>();
		// 验证码校验
		if(!CodeUtil.checkVerifyCode(request)) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "您输入的验证码有误！");
			return modelMap;
		}
		// 获取帐号信息
		String username = HttpServletRequestUtil.getString(request, "username");
		String password = HttpServletRequestUtil.getString(request, "password");
		String newPassword = HttpServletRequestUtil.getString(request, "newPassword");
		// 从session中获取当前用户信息(用户一旦通过微信登录之后，便能获取到用户的信息)
		PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
		// 非空判断，要求帐号、新旧密码以及当前的用户信息不能为空，并且新旧密码不能相同
		if(username != null && password != null && newPassword != null && user != null 
				&& user.getUserId() != null && !password.equals(newPassword)) {
			try {
				// 查看原先的帐号，看看与输入的帐号信息是否一直，不一致则认为是非法操作
				LocalAuth localAuth = localAuthService.getLocalAuthByUserId(user.getUserId());
				if(localAuth == null || !localAuth.getUsername().equals(username)) {
					// 不一致则直接退出
					modelMap.put("success", false);
					modelMap.put("errMsg", "您输入的帐号有误！");
					return modelMap;
				}else if(!localAuth.getPassword().equals(MD5.getMd5(password))) {
					// 密码输入错误
					modelMap.put("success", false);
					modelMap.put("errMsg", "输入的原密码错误！");
					return modelMap;
				}
				// 修改平台帐号的用户密码信息
				LocalAuthExecution localAuthExecution = localAuthService.modifyLocalAuth(user.getUserId(), username, password, newPassword);
				// 操作校验
				if(LocalAuthStateEnum.SUCCESS.getState() == localAuthExecution.getState()) {
					modelMap.put("success", true);
				}else {
					modelMap.put("success", false);
					modelMap.put("errMsg", localAuthExecution.getStateInfo());
				}
			} catch (LocalAuthOperationException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
			}
		}else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "用户更改信息不能为空并且新旧密码不能相同！");
		}
		return modelMap;
	}
	
	/**
	 * 平台帐号登录校验
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/logincheck", method = RequestMethod.POST)
	private Map<String, Object> loginCheck(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<>();
		// 获取是否需要进行验证码校验的 标识符
		boolean needVerify = HttpServletRequestUtil.getBoolean(request, "needVerify");
		// 验证码校验
		if(needVerify && !CodeUtil.checkVerifyCode(request)) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "您输入的验证码有误！");
			return modelMap;
		}
		// 获取输入的帐号信息
		String username = HttpServletRequestUtil.getString(request, "username");
		String password = HttpServletRequestUtil.getString(request, "password");
		if(username != null && password != null) {
			// 传入帐号和密码去获取平台帐号信息
			LocalAuth localAuth = localAuthService.getLocalAuthByUsernameAndPwd(username, password);
			if(localAuth != null) {
				// 若能取到帐号信息则登录成功
				modelMap.put("success", true);
				// 同时在session重新设置用户信息
				request.getSession().setAttribute("user", localAuth.getPersonInfo());
			}else {
				modelMap.put("success", false);
				modelMap.put("errMsg", "用户名或密码有误！");
			}
		}else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "用户名和密码不能为空！");
		}
		return modelMap;
	}
	
	/**
	 * 登出功能
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/logout", method = RequestMethod.POST)
	private Map<String, Object> logout(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<>();
		// 将用户session置为空
		request.getSession().setAttribute("user", null);
		modelMap.put("success", true);
		return modelMap;
	}
}
