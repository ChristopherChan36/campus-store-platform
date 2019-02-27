package com.christopher.o2o.entity;

import java.util.Date;

/**
 * @ClassName: LocalAuth 
 * @Description: 本地帐号实体类  
 * @author christopher chan
 * @date 2018年10月4日  
 *
 */
public class LocalAuth {

	//ID
	private Long localAuthId;
	//用户名称
	private String username;
	//密码
	private String password;
	//创建时间
	private Date createTime;
	//修改时间
	private Date lastEditTime;
	//用户ID -用户关联对象
	private PersonInfo personInfo;
	
	
	//============================== getter and setter ============================
	public Long getLocalAuthId() {
		return localAuthId;
	}
	public void setLocalAuthId(Long localAuthId) {
		this.localAuthId = localAuthId;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getLastEditTime() {
		return lastEditTime;
	}
	public void setLastEditTime(Date lastEditTime) {
		this.lastEditTime = lastEditTime;
	}
	public PersonInfo getPersonInfo() {
		return personInfo;
	}
	public void setPersonInfo(PersonInfo personInfo) {
		this.personInfo = personInfo;
	}
	//============================== getter and setter ============================
	
}
