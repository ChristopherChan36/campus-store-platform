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
import com.christopher.o2o.dao.ShopCategoryDao;
import com.christopher.o2o.entity.ShopCategory;
import com.christopher.o2o.exception.ShopCategoryOperationException;
import com.christopher.o2o.service.ShopCategoryService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ShopCategoryServiceImpl implements ShopCategoryService {

	@Autowired
	private ShopCategoryDao shopCategoryDao;
	@Autowired
	private JedisUtil.Keys jedisKeys;
	@Autowired
	private JedisUtil.Strings jedisStrings;

	private static Logger logger = LoggerFactory.getLogger(ShopCategoryServiceImpl.class);

	@Override
	@Transactional
	public List<ShopCategory> getShopCategoryList(ShopCategory shopCategoryCondition) {
		// 定义redis的key前缀
		String key = SCLISTKEY;
		// 定义接收对象
		List<ShopCategory> shopCategoryList = null;
		// 定义jackson数据转换操作类
		ObjectMapper mapper = new ObjectMapper();
		// 拼接出redis的key
		if(shopCategoryCondition == null) {
			// 若查询条件为空，则列出所有首页大类，即parentId为空的店铺类别
			key += "_allfirstlevel";
		}else if(shopCategoryCondition != null && shopCategoryCondition.getParent() != null
				&& shopCategoryCondition.getParent().getShopCategoryId() != null) {
			// 若parentId为非空，则列出该parentId下的所有子类别
			key = key + "_parent" + shopCategoryCondition.getParent().getShopCategoryId();
		}else if(shopCategoryCondition != null) {
			// 列出所有子类别，不管其属于哪个大类，都列出来
			key += "_allsecondlevel";
		}
		// 判断Redis数据库中是否存在当前key值
		if(!jedisKeys.exists(key)) {
			// 如果不存在，则从数据库中取出相应的数据
			shopCategoryList = shopCategoryDao.queryShopCategory(shopCategoryCondition);
			// 将查询数据库得到的店铺类别列表信息转成Json字符串并以key为键保存至redis数据库中
			String shopCategoryListStr = "";
			try {
				shopCategoryListStr = mapper.writeValueAsString(shopCategoryList);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
				// 记录错误日志
				logger.error(e.getMessage());
				throw new ShopCategoryOperationException(e.getMessage());
			}
			// 若无异常，则将店铺类别信息保存至redis中
			jedisStrings.set(key, shopCategoryListStr);
		}else {
			// 若存在，则直接从redis数据库中获取相应的数据
			String shopCategoryListStr = jedisStrings.get(key);
			// 指定要将String转换成的集合类型
			JavaType javaType = mapper.getTypeFactory().
					constructParametricType(ArrayList.class, ShopCategory.class);
			try {
				shopCategoryList = mapper.readValue(shopCategoryListStr, javaType);
			} catch (JsonParseException e) {
				e.printStackTrace();
				logger.error(e.getMessage());
				throw new ShopCategoryOperationException(e.getMessage());
			} catch (JsonMappingException e) {
				e.printStackTrace();
				logger.error(e.getMessage());
				throw new ShopCategoryOperationException(e.getMessage());
			} catch (IOException e) {
				e.printStackTrace();
				logger.error(e.getMessage());
				throw new ShopCategoryOperationException(e.getMessage());
			}
		}
		return shopCategoryList;
	}

}
