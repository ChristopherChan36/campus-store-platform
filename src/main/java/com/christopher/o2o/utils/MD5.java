package com.christopher.o2o.utils;

import java.security.MessageDigest;

/**
 * MD5加密算法工具类
 * 
 * @ClassName: MD5
 * @author christopher chan
 * @date 2018年12月13日
 */
public class MD5 {

	/**
	 * 对传入的字符串进行MD5加密
	 * @param str 目标字符串
	 * @return
	 */
	public static final String getMd5(String s) {
		// 十六进制数组
		char hexDigits[] = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};
		try {
			char str[];
			// 对传入的字符串转换成byte数组
			byte strTemp[] = s.getBytes();
			// 获取MD5加密对象
			MessageDigest mdTemp = MessageDigest.getInstance("MD5");
			// 传入需要加密的目标数组
			mdTemp.update(strTemp);
			// 获取加密后的数组
			byte md[] = mdTemp.digest();
			int j = md.length;
			str = new char[j * 2];
			int k = 0;
			// 将数组作位移
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}

			return new String(str);
		} catch (Exception e) {
			return null;
		}
	}

	public static void main(String[] args) {
		System.out.println(MD5.getMd5("newadmin"));
		byte b = 7;
		System.out.println(b & 0X0F);
		System.out.println(b & 0xf);
	}
}
