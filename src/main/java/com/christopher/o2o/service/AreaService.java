package com.christopher.o2o.service;

import java.util.List;

import com.christopher.o2o.entity.Area;

/**
 * @ClassName: AreaService 
 * @Description: 区域业务处理
 * @author christopher chan
 * @date 2018年10月14日  
 *
 */
public interface AreaService {
	
	public static final String AREALISTKEY = "arealist";
	
	/**
	 * 获取区域列表信息，优先从缓存获取
	 * @return
	 */
	List<Area> getAreaList();
}
