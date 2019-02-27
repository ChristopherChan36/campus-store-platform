package com.christopher.o2o.dao;

import java.util.List;

import com.christopher.o2o.entity.Area;

public interface AreaDao {

	/**
	 * 列出区域列表
	 * @return areaList 区域列表结果
	 * @date 2018年10月13日
	 */
	List<Area> queryArea();
}
