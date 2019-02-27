package com.christopher.o2o.dao;

import java.util.Date;

import org.apache.ibatis.annotations.Param;

import com.christopher.o2o.entity.LocalAuth;

/**
 * 本地帐号绑定
 * 
 * @ClassName: LocalAuthDao
 * @author christopher chan
 * @date 2018年12月12日
 *
 */
public interface LocalAuthDao {

	/**
	 * 通过帐号和密码查询对应的本地账户信息，登录用
	 * @param username 用户名称
	 * @param password 密码
	 * @return
	 */
	LocalAuth queryLocalByUsernameAndPwd(@Param("username") String username, @Param("password") String password);
	
	/**
	 * 通过用户id查询对应的localauth
	 * @param userId 用户id
	 * @return
	 */
	LocalAuth queryLocalByUserId(@Param("userId") long userId);
	
	/**
	 * 添加平台帐号
	 * @param localAuth 平台帐号信息
	 * @return
	 */
	int insertLocalAuth(LocalAuth localAuth);
	
	/**
	 * 通过userId,username,password更改密码
	 * @param userId 用户id
	 * @param userName 用户名
	 * @param password 密码
	 * @param newPassword 新密码
	 * @param lastEditTime 更新时间
	 * @return
	 */
	int updateLocalAuth(@Param("userId") Long userId, 
			@Param("username") String username,
			@Param("password") String password,
			@Param("newPassword") String newPassword,
			@Param("lastEditTime") Date lastEditTime);
}
