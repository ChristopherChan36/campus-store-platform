package com.christopher.o2o.entity;

import java.util.Date;

/**
 * @ClassName: WechatAuth 
 * @Description: 微信帐号实体类  
 * @author christopher chan
 * @date 2018年10月4日  
 *
 */
public class WechatAuth {

	//ID
	private Long wechatAuthId;
	//OpenId- 用于微信号与微信公众号绑定的唯一标识;
	private String openId;
	//创建时间
	private Date createTime;
	//用户关联对象
	private PersonInfo personInfo;
	
	
	//============================== getter and setter ============================
	public Long getWechatAuthId() {
		return wechatAuthId;
	}
	public void setWechatAuthId(Long wechatAuthId) {
		this.wechatAuthId = wechatAuthId;
	}
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public PersonInfo getPersonInfo() {
		return personInfo;
	}
	public void setPersonInfo(PersonInfo personInfo) {
		this.personInfo = personInfo;
	}
	//============================== getter and setter ============================
	
	
}
