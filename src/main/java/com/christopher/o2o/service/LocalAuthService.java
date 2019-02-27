package com.christopher.o2o.service;


import com.christopher.o2o.dto.LocalAuthExecution;
import com.christopher.o2o.entity.LocalAuth;
import com.christopher.o2o.exception.LocalAuthOperationException;

/**
 * 本地帐号业务类
 * 
 * @ClassName: LocalAuthService
 * @author christopher chan
 * @date 2018年12月12日
 *
 */
public interface LocalAuthService {

	/**
	 * 通过帐号和密码获取平台帐号信息
	 * 
	 * @param username
	 *            帐号名称
	 * @param password
	 *            密码
	 * @return
	 */
	LocalAuth getLocalAuthByUsernameAndPwd(String username, String password);

	/**
	 * 通过用户id获取平台帐号信息
	 * 
	 * @param userId
	 *            用户id
	 * @return
	 */
	LocalAuth getLocalAuthByUserId(long userId);

	/**
	 * 绑定微信用户，生成平台专属的帐号信息
	 * 
	 * @param localAuth
	 *            本地帐号信息
	 * @return
	 * @throws LocalAuthOperationException
	 */
	LocalAuthExecution bindLocalAuth(LocalAuth localAuth) throws LocalAuthOperationException;

	/**
	 * 修改平台帐号的登录密码
	 * 
	 * @param userId
	 *            用户id
	 * @param username
	 *            账户名
	 * @param password
	 *            密码
	 * @param newPassword
	 *            新密码
	 * @param lastEditTime
	 *            更新时间
	 * @return
	 * @throws LocalAuthOperationException
	 */
	LocalAuthExecution modifyLocalAuth(Long userId, String username, String password, String newPassword)
			throws LocalAuthOperationException;
}
