package com.christopher.o2o.utils;

/**
 * @ClassName: PathUtil 
 * @Description: 处理项目路径的工具类
 * @author christopher chan
 * @date 2018年10月17日  
 *
 */
public class PathUtil {

	//系统文件分隔符
	private static final String SEPARATOR = System.getProperty("file.separator");
	
	/**
	 * 根据系统获取根目录路径,即项目图片的根路径
	 * @return basePath
	 * @date 2018年10月17日
	 */
	public static String getImgBasePath() {
		//获取当前项目运行的系统名称
		String os = System.getProperty("os.name");
		String basePath = "";
		if(os.toLowerCase().startsWith("win")) {
<<<<<<< HEAD
			basePath = "E:/picture/image/electronic-shop";
		}else {
			basePath = "/usr/christopher/work/image/electronic-shop";
=======
			basePath = "E:/picture/image/o2o/";
		}else {
			basePath = "/home/christopher/image/electronic-shop/";
>>>>>>> 89f2009fde0685066fd0d4fcffe0ddb1799f5084
		}
		basePath = basePath.replace("/", SEPARATOR);
		return basePath;
	}
	
	/**
	 * 根据不同的店铺id获取店铺图片的子路径
	 * @param shopId
	 * @return imagePath
	 * @date 2018年10月17日
	 */
	public static String getShopImagePath(long shopId) {
<<<<<<< HEAD
		String imagePath = "/upload/images/item/shop/" + shopId + "/";
=======
		String imagePath = "upload/item/shop/" + shopId + "/";
>>>>>>> 89f2009fde0685066fd0d4fcffe0ddb1799f5084
		return imagePath.replace("/", SEPARATOR);
	}
}
