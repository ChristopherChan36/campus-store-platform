package com.christopher.o2o.utils.wechat;

import java.security.MessageDigest;
import java.util.Arrays;

/**
 * 微信请求校验工具类
 * 
 * 校验流程： 
 * 	1）将token、timestamp、nonce三个参数进行字典序排序
 * 	2）将三个参数字符串拼接成一个字符串进行sha-1加密 
 *  3）开发者获得加密后的字符串可与signature对比，标识该请求来源于微信
 * @ClassName: SignUtil
 * @author christopher chan
 * @date 2018年11月30日
 *
 */
public class SignUtil {

	// 与微信公众号测试接口配置信息中的Token一致
	private static String token = "campus";

	/**
	 * 验证微信加密签名
	 * 
	 * @param signature
	 *            微信加密签名
	 * @param timestamp
	 *            时间戳
	 * @param nonce
	 *            随机数
	 * @date 2018年11月30日
	 */
	public static boolean checkSignature(String signature, String timestamp, String nonce) {
		
		/*
		 *  1）将token、timestamp、nonce三个参数进行字典序排序 
		 *  2）将三个参数字符串拼接成一个字符串进行sha1加密 
		 *  3）开发者获得加密后的字符串可与signature对比，标识该请求来源于微信
		 */
		
		// signature结合了开发者填写的token参数和请求中的timestamp参数、nonce参数
		String[] array = new String[] { token, timestamp, nonce };
		// 将token、timestamp、nonce三个上述进行字典序排序
		Arrays.sort(array);

		// 将这三个参数拼成字符串
		StringBuilder content = new StringBuilder();
		for (int i = 0; i < array.length; i++) {
			content.append(array[i]);
		}

		MessageDigest md = null;
		String tmpStr = null;
		try {
			// 使用SHA-1算法进行加密
			md = MessageDigest.getInstance("SHA-1");
			// 将三个参数字符串拼接成一个字符串进行SHA-1加密
			byte[] digest = md.digest(content.toString().getBytes());
			// 将字节数组转换层十六进制的字符串
			tmpStr = byteToStr(digest);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		content = null;
		// 将经过SHA-1加密后的字符串与signature对比，标识该请求来源于微信
		return tmpStr != null ? tmpStr.equals(signature.toUpperCase()) : false;
	}

	/**
	 * 将字节数组转换层十六进制的字符串
	 * 
	 * @param digest
	 *            字节数组
	 * @return String 十六进制的字符串
	 * @date 2018年11月30日
	 */
	private static String byteToStr(byte[] byteArray) {
		String strDigest = "";
		// 遍历字节数组
		for (int i = 0; i < byteArray.length; i++) {
			strDigest += byteToHexStr(byteArray[i]);
		}
		return strDigest;
	}

	/**
	 * 将字节转换为十六进制字符串
	 * 
	 * @param mByte
	 *            字节数据
	 * @return String 十六进制的字符串
	 * @date 2018年11月30日
	 */
	private static String byteToHexStr(byte mByte) {
		// 十六进制下数字到字符的映射数组
		char[] Digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
		/*
		 * Java中byte用二进制表示占用8位，而十六进制的每个字符需要用4位二进制来表示
		 * 所以将字节转换成十六进制字符串时，可以将每个byte转换成两个相应的16进制字符，
		 * 即把byte的高4位和低4位分别转成相应的16进制字符H和L，并且组合起来得到结果
		 */
		char[] tempArr = new char[2];
		// 高4位
		tempArr[0] = Digit[(mByte >>> 4) & 0X0F];
		// 低4位
		tempArr[1] = Digit[mByte & 0X0F];

		String s = new String(tempArr);
		return s;
	}
}
