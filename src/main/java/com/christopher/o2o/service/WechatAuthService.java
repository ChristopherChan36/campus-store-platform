package com.christopher.o2o.service;

import com.christopher.o2o.dto.WechatAuthExecution;
import com.christopher.o2o.entity.WechatAuth;
import com.christopher.o2o.exception.WechatAuthOperationException;

/**
 * 微信账户授权创建本系统帐号的业务类
 * @ClassName: WechatAuthService 
 * @author christopher chan
 * @date 2018年12月5日  
 *
 */
public interface WechatAuthService {

	/**
	 * 通过openId查找平台对应的微信帐号
	 * @param openId 微信账户的唯一标识
	 * @return
	 * @date 2018年12月5日
	 */
	WechatAuth getWechatAuthByOpenId(String openId);
	
	/**
	 * 通过微信账户信息注册本平台的微信帐号
	 * @param wechatAuth 微信账户信息
	 * @return 
	 * @throws WechatAuthOperationException
	 * @date 2018年12月5日
	 */
	WechatAuthExecution register(WechatAuth wechatAuth) throws WechatAuthOperationException;
}
