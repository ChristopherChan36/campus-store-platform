package com.christopher.o2o.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.christopher.o2o.entity.HeadLine;

/**
 * @ClassName: HeadLineDao 
 * @Description: 头条展示 
 * @author christopher chan
 * @date 2018年11月13日  
 *
 */
public interface HeadLineDao {
	
	/**
	 * 根据传入的查询条件（头条名称查询头条信息） 
	 * @param headLineCondition
	 * @return
	 * @date 2018年11月13日
	 */
	List<HeadLine> queryHeadLine(@Param("headLineCondition") HeadLine headLineCondition);
}
