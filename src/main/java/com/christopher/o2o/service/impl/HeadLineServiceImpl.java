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
import com.christopher.o2o.dao.HeadLineDao;
import com.christopher.o2o.entity.HeadLine;
import com.christopher.o2o.exception.HeadLineOperationException;
import com.christopher.o2o.service.HeadLineService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class HeadLineServiceImpl implements HeadLineService {

	@Autowired
	private HeadLineDao headLineDao;
	@Autowired
	private JedisUtil.Keys jedisKeys;
	@Autowired
	private JedisUtil.Strings jedisStrings;
	
	private static Logger logger = LoggerFactory.getLogger(HeadLineServiceImpl.class);
	
	@Override
	@Transactional
	public List<HeadLine> getHeadLineList(HeadLine headLineCondition) throws IOException {
		// 定义redis的key前缀
		String key = HLLISTKEY;
		// 定义头条列表信息的接收对象
		List<HeadLine> headLineList = null;
		// 定义jackson的数据转换操作类对象
		ObjectMapper mapper = new ObjectMapper();
		// 拼接出redis的key( 区分查询条件中的： enableStatus )
		if(headLineCondition != null && headLineCondition.getEnableStatus() != null) {
			key = key + "_" + headLineCondition.getEnableStatus();
		}
		// 判断Redis数据库中是否存在当前key值
		if(!jedisKeys.exists(key)) {
			// 如果不存在，则从数据库中取出相应的数据
			headLineList = headLineDao.queryHeadLine(headLineCondition);
			// 将查询数据库得到的区域列表信息转成Json字符串并以key为键保存至redis数据库中
			String headLineListStr = "";
			try {
				headLineListStr = mapper.writeValueAsString(headLineList);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
				// 记录错误日志
				logger.error(e.getMessage());
				throw new HeadLineOperationException(e.getMessage());
			}
			// 若无异常，则将头条信息保存至redis中
			jedisStrings.set(key, headLineListStr);
		}else {
			// 若存在，则直接从redis数据库中获取相应的数据
			String headLineListStr = jedisStrings.get(key);
			// 指定要将String转换成的集合类型
			JavaType javaType = mapper.getTypeFactory().
					constructParametricType(ArrayList.class, HeadLine.class);
			try {
				headLineList = mapper.readValue(headLineListStr, javaType);
			} catch (JsonParseException e) {
				e.printStackTrace();
				logger.error(e.getMessage());
				throw new HeadLineOperationException(e.getMessage());
			} catch (JsonMappingException e) {
				e.printStackTrace();
				logger.error(e.getMessage());
				throw new HeadLineOperationException(e.getMessage());
			} catch (IOException e) {
				e.printStackTrace();
				logger.error(e.getMessage());
				throw new HeadLineOperationException(e.getMessage());
			}
		}
		return headLineList;
	}

}
