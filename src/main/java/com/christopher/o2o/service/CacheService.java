package com.christopher.o2o.service;

/**
 * 处理缓存信息的业务类
 * @ClassName: CacheService 
 * @author christopher chan
 * @date 2018年12月11日  
 *
 */
public interface CacheService {

	/**
	 * 依据key前缀删除匹配该模式下的所有key-value
	 * 如： 传入 shopcategory,则 shopcategory_allfirstlevel等以shopcategory打头的key-value都会被清空
	 * @param keyPrefix redis数据库中key前缀名称
	 */
	void removeFromCache(String keyPrefix);
}
