package com.christopher.o2o.web.wechat;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.christopher.o2o.dto.UserAccessToken;
import com.christopher.o2o.dto.WechatAuthExecution;
import com.christopher.o2o.dto.WechatUser;
import com.christopher.o2o.entity.PersonInfo;
import com.christopher.o2o.entity.WechatAuth;
import com.christopher.o2o.enums.WechatAuthStateEnum;
import com.christopher.o2o.service.PersonInfoService;
import com.christopher.o2o.service.WechatAuthService;
import com.christopher.o2o.utils.wechat.WechatUtil;

/**
 * 获取关注公众号之后的微信用户信息的接口，如果在微信浏览器中访问
 * https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxef4d51981996b443&redirect_uri=http://campus.antaixin.com.cn/electronic-shop/wechatlogin/logincheck&response_type=code&scope=snsapi_userinfo&state=1#wechat_redirect
 * 则这里将会获取到code，之后再通过code获取到access_token 进而获取到用户信息
 * 
 * @ClassName: WechatLoginController
 * @author christopher chan
 * @date 2018年12月2日
 *
 */
@Controller
@RequestMapping("/wechatlogin")
public class WechatLoginController {

	private static Logger log = LoggerFactory.getLogger(WechatLoginController.class);
	// 前端展示系统 
	private static final String FRONTEND = "1";
	// 店家管理系统
	private static final String SHOPADMIN = "2";
	
	@Autowired
	private PersonInfoService personInfoService;
	@Autowired
	private WechatAuthService wechatAuthService;
	
	@RequestMapping(value = "/logincheck", method = { RequestMethod.GET })
	public String doGet(HttpServletRequest request, HttpServletResponse response) {
		log.debug("wechat login get...");
		// 获取微信公众号传输过来的code,通过code换取网页授权access_token,进而获取用户信息
		String code = request.getParameter("code");
		// state | 1：前端展示系统        2：店家管理系统
		String roleType = request.getParameter("state");
		log.debug("wechat login code:" + code);
		// 微信用户实体类
		WechatUser user = null;
		// 公众号的唯一标识
		String openId = null;
		WechatAuth auth = null;
		// 非空判断
		if (null != code) {
			UserAccessToken token;
			try {
				// 通过code获取access_token
				token = WechatUtil.getUserAccessToken(code);
				log.debug("wechat login token:" + token.toString());
				// 通过token获取accessToken
				String accessToken = token.getAccessToken();
				// 通过token获取openId
				openId = token.getOpenId();
				// 通过access_token和openId获取用户昵称等信息
				user = WechatUtil.getUserInfo(accessToken, openId);
				log.debug("wechat login user:" + user.toString());
				request.getSession().setAttribute("openId", openId);
				// 根据openId获取系统中的微信账户
				auth = wechatAuthService.getWechatAuthByOpenId(openId);
			} catch (IOException e) {
				log.error("error in getUserAccessToken or getUserInfo or findByOpenId: " + e.toString());
				e.printStackTrace();
			}
		}
		if(auth == null) {
			PersonInfo personInfo = WechatUtil.getPersonInfoFromRequest(user);
			if(FRONTEND.equals(roleType)) { // 用户身份
				personInfo.setUserType(1);
			}else if(SHOPADMIN.equals(roleType)) { // 商家身份
				personInfo.setUserType(2);
			}
			auth = new WechatAuth();
			auth.setOpenId(openId);
			auth.setPersonInfo(personInfo);
			// 通过微信账户信息在系统中创建本系统的微信账户信息以及用户信息
			WechatAuthExecution we = wechatAuthService.register(auth);
			// 操作成功校验
			if(we.getState() != WechatAuthStateEnum.SUCCESS.getState()) {
				return null;
			}else {
				// 操作成功，保存微信账户信息到session中
				personInfo = personInfoService.getPersonInfoById(auth.getPersonInfo().getUserId());
				request.getSession().setAttribute("user", personInfo);
			}
		}
		// 判断当前登录用户身份
		if(FRONTEND.equals(roleType)) {// 客户
			return "frontend/index";
		}else if(SHOPADMIN.equals(roleType)) {// 商户
			return "shop/shoplist";
		}else {
			return null;
		}
	}
}
