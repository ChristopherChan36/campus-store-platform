package com.christopher.o2o.dao;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import com.christopher.o2o.BaseTest;
import com.christopher.o2o.entity.LocalAuth;
import com.christopher.o2o.entity.PersonInfo;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LocalAuthDaoTest extends BaseTest {

	@Autowired
	private LocalAuthDao localAuthDao;

	private static final String username = "admin";
	private static final String password = "newadmin";

	@Test
	public void testAInsertLocalAuth() throws Exception {
		// 新增一条平台帐号信息
		LocalAuth localAuth = new LocalAuth();
		PersonInfo personInfo = new PersonInfo();
		personInfo.setUserId(1L);
		// 给平台帐号绑定上用户信息
		localAuth.setPersonInfo(personInfo);
		// 设置上用户名和密码
		localAuth.setUsername(username);
		localAuth.setPassword(password);
		localAuth.setCreateTime(new Date());
		int effectedNum = localAuthDao.insertLocalAuth(localAuth);
		assertEquals(1, effectedNum);
	}

	@Test
	public void testBQueryLocalByUserNameAndPwd() {
		// 按照帐号和密码查询用户信息
		LocalAuth localAuth = localAuthDao.queryLocalByUsernameAndPwd(username, password);
		assertEquals("admin", localAuth.getPersonInfo().getName());
	}

	@Test
	public void testCQueryLocalByUserId() {
		// 按照用户id查询平台帐号，进而获取到用户信息
		LocalAuth localAuth = localAuthDao.queryLocalByUserId(1L);
		assertEquals("admin", localAuth.getPersonInfo().getName());
	}

	@Test
	public void testDUpdateLocalAuth() {
		// 依据用户Id、平台帐号、以及旧密码修改平台帐号密码
		Date now = new Date();
		int effectedNum = localAuthDao.updateLocalAuth(1L, username, password, "new" + password, now);
		assertEquals(1, effectedNum);
		// 查询平台中该帐号的最新信息
		LocalAuth localAuth = localAuthDao.queryLocalByUserId(1L);
		System.out.println(localAuth.getPassword());
	}

}
