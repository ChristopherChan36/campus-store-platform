package com.christopher.o2o.entity;

import java.util.Date;

/**
 * @ClassName: HeadLine 
 * @Description: 头条实体类  
 * @author christopher chan
 * @date 2018年10月12日  
 *
 */
public class HeadLine {
	
	//头条Id
	private Long lineId;
	//头条名称
	private String lineName;
	//头条链接
	private String lineLink;
	//头条图片
	private String lineImg;
	//权重  0：不可用| 1：可用
	private Integer priority;
	//状态 
	private Integer enableStatus;
	//创建时间
	private Date createTime;
	//修改时间
	private Date lastEditTime;
	
	
	//============= getter and setter ============
	public Long getLineId() {
		return lineId;
	}
	public void setLineId(Long lineId) {
		this.lineId = lineId;
	}
	public String getLineName() {
		return lineName;
	}
	public void setLineName(String lineName) {
		this.lineName = lineName;
	}
	public String getLineLink() {
		return lineLink;
	}
	public void setLineLink(String lineLink) {
		this.lineLink = lineLink;
	}
	public String getLineImg() {
		return lineImg;
	}
	public void setLineImg(String lineImg) {
		this.lineImg = lineImg;
	}
	public Integer getPriority() {
		return priority;
	}
	public void setPriority(Integer priority) {
		this.priority = priority;
	}
	public Integer getEnableStatus() {
		return enableStatus;
	}
	public void setEnableStatus(Integer enableStatus) {
		this.enableStatus = enableStatus;
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
	//============== getter and setter ==============
	
}
