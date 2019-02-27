package com.christopher.o2o.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.christopher.o2o.cache.JedisUtil;
import com.christopher.o2o.dao.AreaDao;
import com.christopher.o2o.entity.Area;
import com.christopher.o2o.exception.AreaOperationException;
import com.christopher.o2o.service.AreaService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class AreaServiceImpl implements AreaService {

	@Autowired
	private AreaDao areaDao;
	@Autowired
	private JedisUtil.Keys jedisKeys;
	@Autowired
	private JedisUtil.Strings jedisStrings;
	
	// 定义slf4J日志门面对象
	private static Logger logger = LoggerFactory.getLogger(AreaServiceImpl.class);
	
	/**
	 * @Title: getAreaList  
	 * @Description: 获取所有的区域信息
	 * @return areaList 区域列表结果
	 * @Date 2018年10月14日 
	 * @see com.christopher.o2o.service.AreaService#getAreaList()
	 */
	@Override
	@Transactional
	public List<Area> getAreaList() {
		String key = AREALISTKEY;
		// 用于接收查询到的区域列表信息
		List<Area> areaList = null;
		// 创建ObjectMapper对象，用于对象的序列化
		ObjectMapper mapper = new ObjectMapper();
		// 判断Redis数据库中是否存在当前key值
		if(!jedisKeys.exists(key)) {
			areaList = areaDao.queryArea();
			// 将查询数据库得到的区域列表信息转成Json字符串保存至redis数据库中
			String areaListStr = "";
			try {
				areaListStr = mapper.writeValueAsString(areaList);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
				// 记录错误日志
				logger.error(e.getMessage());
				throw new AreaOperationException(e.getMessage());
			}
			// 若无异常，则将区域信息保存至redis中
			jedisStrings.set(key, areaListStr);
		}else {
			// 直接从redis数据库中获取相应的数据
			String areaListStr = jedisStrings.get(key);
			// 将获取的json字符串转换成业务对象
			JavaType javaType = mapper.getTypeFactory().
					constructParametricType(ArrayList.class, Area.class);
			try {
				areaList = mapper.readValue(areaListStr, javaType);
			} catch (JsonParseException e) {
				e.printStackTrace();
				logger.error(e.getMessage());
				throw new AreaOperationException(e.getMessage());
			} catch (JsonMappingException e) {
				e.printStackTrace();
				logger.error(e.getMessage());
				throw new AreaOperationException(e.getMessage());
			} catch (IOException e) {
				e.printStackTrace();
				logger.error(e.getMessage());
				throw new AreaOperationException(e.getMessage());
			}
		}
		return areaList;
	}

}
