package com.christopher.o2o.service;

import java.io.IOException;
import java.util.List;

import com.christopher.o2o.entity.HeadLine;

/**
 * @ClassName: HeadLineService 
 * @Description: 头条管理的业务层 
 * @author christopher chan
 * @date 2018年11月13日  
 *
 */
public interface HeadLineService {

	// 头条列表信息的key值
	public static final String HLLISTKEY = "headlinelist";
	
	/**
	 * 根据传入的条件返回指定的头条列表信息，优先从缓存获取
	 * @param headLineCondition 头条筛选条件
	 * @return
	 * @throws IOException
	 * @date 2018年11月13日
	 */
	List<HeadLine> getHeadLineList(HeadLine headLineCondition) throws IOException;
}
