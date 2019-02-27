package com.christopher.o2o.utils;

import java.security.Key;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * DES是一种对称加密算法，所谓对称加密算法即：加密和解密使用的是相同秘钥的算法
 * 使用Java代码实现
 * @ClassName: DESUtil 
 * @author christopher chan
 * @date 2018年12月7日  
 *
 */
public class DESUtil {

	// 密钥对象
	private static Key key;
	// 设置密钥key
	private static String KEY_STR = "campus";
	private static String CHARSETNAME = "UTF-8";
	// 算法名称
	private static String ALGORITHM = "DES";

	// 生成密钥对象
	static {
		try {
			// 生成DES算法对象
			KeyGenerator generator = KeyGenerator.getInstance(ALGORITHM);
			// 运用SHA-1的安全策略
			SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
			// 设置密钥种子
			secureRandom.setSeed(KEY_STR.getBytes());
			// 初始化基于SHA-1的算法对象 
			generator.init(secureRandom);
			// 生成密钥对象
			key = generator.generateKey();
			generator = null;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 获取加密后的信息
	 * @param str 原字符
	 * @return
	 * @date 2018年12月7日
	 */
	public static String getEncryptString(String str) {
		// 基于BASE64编码，接收byte[]并且转换成String
		BASE64Encoder base64encoder = new BASE64Encoder();
		try {
			// 将目标字符串按UTF-8进行编码
			byte[] bytes = str.getBytes(CHARSETNAME);
			// 获取DES加密对象
			Cipher cipher = Cipher.getInstance(ALGORITHM);
			// 初始化密钥信息
			cipher.init(Cipher.ENCRYPT_MODE, key);
			// 对目标信息进行加密
			byte[] doFinal = cipher.doFinal(bytes);
			// 基于base64进行编码并且返回
			return base64encoder.encode(doFinal);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 获取解密之后的信息
	 * @param str 加密之后的字符
	 * @return
	 * @date 2018年12月10日
	 */
	public static String getDecryptString(String str) {
		// 基于Base64解码，接口byte[]数组并且转成String
		BASE64Decoder base64decoder = new BASE64Decoder();
		try {
			// 将加密后的字符串decode成byte[]
			byte[] bytes = base64decoder.decodeBuffer(str);
			// 获取加密对象
			Cipher cipher = Cipher.getInstance(ALGORITHM);
			// 初始化解密信息
			cipher.init(Cipher.DECRYPT_MODE, key);
			// 解密
			byte[] doFinal = cipher.doFinal(bytes);
			// 返回解密后的信息
			return new String(doFinal, CHARSETNAME);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static void main(String[] args) {
		System.out.println(getEncryptString("christopher"));
		System.out.println(getEncryptString("ChristopherChan"));
		
		System.out.println(getEncryptString("root"));
		System.out.println(getEncryptString("123"));
	}

}