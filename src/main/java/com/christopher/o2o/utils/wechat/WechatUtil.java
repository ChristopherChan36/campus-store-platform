package com.christopher.o2o.utils.wechat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.URL;
import java.security.SecureRandom;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.christopher.o2o.dto.UserAccessToken;
import com.christopher.o2o.dto.WechatUser;
import com.christopher.o2o.entity.PersonInfo;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 微信工具类
 * 
 * @ClassName: WechatUtil
 * @author christopher chan
 * @date 2018年12月3日
 *
 */
public class WechatUtil {

	private static Logger logger = LoggerFactory.getLogger(WechatUtil.class);

	/**
	 * 将WechatUser里的信息转换成PersonInfo的信息并返回PersonInfo实体类
	 * 
	 * @param user
	 *            微信账户信息
	 * @return PersonInfo
	 * @date 2018年12月6日
	 */
	public static PersonInfo getPersonInfoFromRequest(WechatUser user) {
		PersonInfo personInfo = new PersonInfo();
		personInfo.setName(user.getNickName());
		personInfo.setGender(user.getSex() + "");
		personInfo.setProfileImg(user.getHeadimgurl());
		personInfo.setEnableStatus(1);
		return personInfo;
	}

	/**
	 * 通过微信公众号传输过来的code获取UserAccessToken实体类
	 * 
	 * @param code
	 *            换取access_token的票据
	 * @return UserAccessToken
	 * @throws IOException
	 * @date 2018年12月3日
	 */
	public static UserAccessToken getUserAccessToken(String code) throws IOException {
		// 测试公众号的appId
		String appId = "wxef4d51981996b443";
		logger.debug("appId: " + appId);
		// 测试公众号的appsecret
		String appsecret = "2db4e1b08f5dac034f229518162a5c4b";
		logger.debug("secret: " + appsecret);

		// 根据传入的code，拼接出访问微信定义好的接口的URL
		String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + appId + "&secret=" + appsecret
				+ "&code=" + code + "&grant_type=authorization_code";
		// 向响应的URL发送请求获取token json字符串
		String tokenStr = httpsRequest(url, "GET", null);
		logger.debug("userAccessToken: " + tokenStr);

		UserAccessToken token = new UserAccessToken();
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			// 将json字符串转换成相应的对象
			token = objectMapper.readValue(tokenStr, UserAccessToken.class);
		} catch (JsonParseException e) {
			logger.error("获取用户access_token失败： " + e.getMessage());
			e.printStackTrace();
		} catch (JsonMappingException e) {
			logger.error("获取用户access_token失败： " + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			logger.error("获取用户access_token失败： " + e.getMessage());
			e.printStackTrace();
		}

		// 非空判断
		if (token == null) {
			logger.error("获取用户accessToken失败！");
			return null;
		}
		return token;
	}

	/**
	 * 通过access_token 以及 openId 获取WechatUser实体类
	 * 
	 * @param accessToken
	 *            网页授权接口调用凭证
	 * @param openId
	 *            微信用户的唯一标识
	 * @return
	 */
	public static WechatUser getUserInfo(String accessToken, String openId) {
		// 根据传入的accessToken以及openId拼接出访问微信定义的端口并获取用户信息的URL
		String url = "https://api.weixin.qq.com/sns/userinfo?access_token=" + accessToken + "&openid=" + openId
				+ "&lang=zh_CN";
		// 访问该URL获取用户信息json 字符串
		String userStr = httpsRequest(url, "GET", null);
		logger.debug("user info :" + userStr);
		WechatUser user = new WechatUser();
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			// 将json字符串转换成相应对象
			user = objectMapper.readValue(userStr, WechatUser.class);
		} catch (JsonParseException e) {
			logger.error("获取用户信息失败: " + e.getMessage());
			e.printStackTrace();
		} catch (JsonMappingException e) {
			logger.error("获取用户信息失败: " + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			logger.error("获取用户信息失败: " + e.getMessage());
			e.printStackTrace();
		}
		if (user == null) {
			logger.error("获取用户信息失败。");
			return null;
		}
		return user;
	}

	/**
	 * 发起https请求并且获取结果
	 * 
	 * @param requestUrl
	 *            请求地址
	 * @param requestMethod
	 *            请求方式（GET、POST）
	 * @param outputStr
	 *            提交的数据
	 * @return json字符串
	 * @date 2018年12月3日
	 */
	private static String httpsRequest(String requestUrl, String requestMethod, String outputStr) {
		StringBuffer buffer = new StringBuffer();
		try {
			// 创建SSLContext对象，并且使用指定的信任管理器进行初始化
			TrustManager[] trustManagers = { new MyX509TrustManager() };
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			sslContext.init(null, trustManagers, new SecureRandom());
			// 从上述SSLContext对象中得到SSLSocketFactory对象
			SSLSocketFactory ssf = sslContext.getSocketFactory();

			URL url = new URL(requestUrl);
			HttpsURLConnection httpsUrlConn = (HttpsURLConnection) url.openConnection();
			httpsUrlConn.setSSLSocketFactory(ssf);

			httpsUrlConn.setDoOutput(true);
			httpsUrlConn.setDoInput(true);
			httpsUrlConn.setUseCaches(false);
			// 设置请求方式（GET/POST）
			httpsUrlConn.setRequestMethod(requestMethod);

			if ("GET".equalsIgnoreCase(requestMethod)) {
				httpsUrlConn.connect();
			}

			// 当有数据需要提交时
			if (outputStr != null && !"".equals(outputStr)) {
				OutputStream outputStream = httpsUrlConn.getOutputStream();
				// 注意编码格式，防止中文乱码
				outputStream.write(outputStr.getBytes("UTF-8"));
				outputStream.close();
			}

			// 将返回的输入流转换成字符串
			InputStream inputStream = httpsUrlConn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			bufferedReader.close();
			inputStreamReader.close();
			// 释放资源
			inputStream.close();
			inputStream = null;
			httpsUrlConn.disconnect();
			logger.debug("https buffer:" + buffer.toString());
		} catch (ConnectException ce) {
			logger.error("wechat server connection timed out!");
		} catch (Exception e) {
			logger.error("https request error: {}", e);
		}
		return buffer.toString();
	}
}
