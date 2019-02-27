package com.christopher.o2o.cache;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * 指定redis的JedisPool接口构造函数，这样才能在centos成功创建jedispool
 * @ClassName: JedisPoolWriper 
 * @author christopher chan
 * @date 2018年12月10日  
 *
 */
public class JedisPoolWriper {

	/** Redis连接池对象 */
	private JedisPool jedisPool;

	/**
	 * 创建一个新的实例 JedisPoolWriper.  
	 *  
	 * @param poolConfig Redis连接池的属性设置信息
	 * @param host 主机名
	 * @param port 端口号
	 */
	public JedisPoolWriper(final JedisPoolConfig poolConfig, final String host,
			final int port) {
		try {
			jedisPool = new JedisPool(poolConfig, host, port);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取Redis连接池对象
	 * @return
	 */
	public JedisPool getJedisPool() {
		return jedisPool;
	}

	/**
	 * 注入Redis连接池对象
	 * @param jedisPool Redis连接池对象
	 */
	public void setJedisPool(JedisPool jedisPool) {
		this.jedisPool = jedisPool;
	}

}
