package com.christopher.o2o.service;

import com.christopher.o2o.entity.PersonInfo;

public interface PersonInfoService {

	/**
	 * 根据用户Id获取personInfo信息
	 * @param userId 用户id
	 * @return
	 */
	PersonInfo getPersonInfoById(Long userId);

}
