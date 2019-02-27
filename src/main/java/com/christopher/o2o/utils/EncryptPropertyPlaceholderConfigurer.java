package com.christopher.o2o.utils;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

/**
 * 对数据库连接属性值进行解密
 * @ClassName: EncryptPropertyPlaceholderConfigurer 
 * @Description: <context:property-placeholder location="classpath:jdbc.properties"/>
 * @author christopher chan
 * @date 2018年12月10日  
 *
 */
public class EncryptPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {

	// 需要加密的字段数组
	private String[] encryptPropNames = {"jdbc.username", "jdbc.password"};
	
	/**
	 * 对关键的数据库连接属性信息进行转换
	 * @param propertyName 连接属性名称
	 * @param propertyValue 连接属性值
	 * @return 解密之后的信息
	 * @Date 2018年12月10日
	 */
	@Override
	protected String convertProperty(String propertyName, String propertyValue) {
		if(isEncryptProp(propertyName)) {
			// 对以加密的字段进行解密操作
			String decryptValue = DESUtil.getDecryptString(propertyValue);
			return decryptValue;
		}else {
			return propertyValue;
		}
	}
	
	/**
	 * 判断当前属性名称是否为需要解密的连接属性
	 * @param propertyName
	 * @return
	 * @date 2018年12月10日
	 */
	private boolean isEncryptProp(String propertyName) {
		for (String encryptpropertyName : encryptPropNames) {
			if (encryptpropertyName.equals(propertyName))
				return true;
		}
		return false;
	}
}
