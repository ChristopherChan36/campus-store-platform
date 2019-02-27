package com.christopher.o2o.service;

import static org.junit.Assert.assertEquals;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import com.christopher.o2o.BaseTest;
import com.christopher.o2o.dto.LocalAuthExecution;
import com.christopher.o2o.entity.LocalAuth;
import com.christopher.o2o.entity.PersonInfo;
import com.christopher.o2o.enums.LocalAuthStateEnum;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LocalAuthServiceTest extends BaseTest {

	@Autowired
	private LocalAuthService localAuthService;
	
	@Test
	public void testABindLocalAuth() {
		// 新增一条平台帐号
		LocalAuth localAuth = new LocalAuth();
		PersonInfo personInfo = new PersonInfo();
		String username = "guest";
		String password = "123456";
		// 给平台帐号设置上用户信息
		// 给用户设置上用户Id，表明是某个用户创建的帐号
		personInfo.setUserId(2L);
		
		localAuth.setPersonInfo(personInfo);
		localAuth.setUsername(username);
		localAuth.setPassword(password);
		// 绑定帐号
		LocalAuthExecution localAuthExecution = localAuthService.bindLocalAuth(localAuth);
		assertEquals(LocalAuthStateEnum.SUCCESS.getState(), localAuthExecution.getState());
		
		// 通过userId找到新增的平台帐号信息
		localAuth = localAuthService.getLocalAuthByUserId(personInfo.getUserId());
		// 打印新增平台帐号的帐号名和密码
		System.out.println("用户昵称：" + localAuth.getPersonInfo().getName());
		System.out.println("平台帐号密码: " + localAuth.getPassword());
	}
	
	@Test
	public void testBModifyLocalAuth() {
		// 设置帐号信息
		long userId = 2L;
		String username = "guest";
		String password = "123456";
		String newPassword = "697395";
		// 修改该帐号对应的密码
		LocalAuthExecution localAuthExecution = localAuthService.modifyLocalAuth(userId, username, password, newPassword);
		assertEquals(LocalAuthStateEnum.SUCCESS.getState(), localAuthExecution.getState());
		// 通过帐号名称和密码找到修改后的平台帐号信息
		LocalAuth localAuth = localAuthService.getLocalAuthByUsernameAndPwd(username, newPassword);
		// 打印平台帐号信息
		System.out.println("平台帐号名称：" + localAuth.getUsername());
		System.out.println("平台帐号密码: " + localAuth.getPassword());
	}
}
