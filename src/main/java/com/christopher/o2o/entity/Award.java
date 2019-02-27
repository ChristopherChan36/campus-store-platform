package com.christopher.o2o.entity;

import java.util.Date;

/**
 * 奖品实体类
 * 
 * @ClassName: Award 
 * @Description:  
 * @author christopher chan
 * @date 2019年2月11日  
 *
 */
public class Award {
	// 主键ID
	private Long awardId;
	// 奖品名称
	private String awardName;
	// 奖品描述
	private String awardDesc;
	// 奖品图标地址
	private String awardImg;
	// 奖品积分
	private Integer point;
	// 权重
	private Integer priority;
	// 创建时间
	private Date createTime;
	// 最新的修改时间
	private Date lastEditTime;
	// 可用状态    0：不可用     1：可用
	private Integer enableStatus;
	// 所属店铺
	private Long shopId;
	
	
	public Long getAwardId() {
		return awardId;
	}
	public void setAwardId(Long awardId) {
		this.awardId = awardId;
	}
	public String getAwardName() {
		return awardName;
	}
	public void setAwardName(String awardName) {
		this.awardName = awardName;
	}
	public String getAwardDesc() {
		return awardDesc;
	}
	public void setAwardDesc(String awardDesc) {
		this.awardDesc = awardDesc;
	}
	public String getAwardImg() {
		return awardImg;
	}
	public void setAwardImg(String awardImg) {
		this.awardImg = awardImg;
	}
	public Integer getPoint() {
		return point;
	}
	public void setPoint(Integer point) {
		this.point = point;
	}
	public Integer getPriority() {
		return priority;
	}
	public void setPriority(Integer priority) {
		this.priority = priority;
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
	public Integer getEnableStatus() {
		return enableStatus;
	}
	public void setEnableStatus(Integer enableStatus) {
		this.enableStatus = enableStatus;
	}
	public Long getShopId() {
		return shopId;
	}
	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}
	
}
