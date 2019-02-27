package com.christopher.o2o.utils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.christopher.o2o.dto.ImageHolder;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;

/**
 * 图片处理工具类
 * 
 * @ClassName: ImageUtil
 * @Description: 使用google开发的开源工具类thumbnailator 进行图片压缩操作
 * @author christopher chan
 * @date 2018年10月15日
 *
 */
public class ImageUtil {

	// 获取当前项目 classpath的绝对路径
	private static final String BASE_PATH = 
			Thread.currentThread().getContextClassLoader().getResource("").getPath();

	// 格式化
	private static final SimpleDateFormat sDate_Format = new SimpleDateFormat("yyyyMMddHHmmss");
	// 随机数对象
	private static final Random RANDOM = new Random();

	// 日志对象
	private static Logger logger = LoggerFactory.getLogger(ImageUtil.class);

	public static String generateNormalImg(ImageHolder thumbnail, String targetAddr) {
		// 文件的真实路径 - 系统随机生成
		String realFileName = getRandomFileName();
		// 获取输入文件流的扩展名
		String extension = getFileExtension(thumbnail.getImageName());
		// 在系统中创建目标路径所涉及到的目录
		makeDirPath(targetAddr);

		// 保存文件相关路径
		String relativeAddr = targetAddr + realFileName + extension;
		logger.debug("current relativeAddr is: " + relativeAddr);
		File dest = new File(PathUtil.getImgBasePath() + relativeAddr);
		logger.debug("current complete addr is: " + PathUtil.getImgBasePath() + relativeAddr);
		// 对保存文件进行 添加水印、压缩处理
		try {
			Thumbnails.of(thumbnail.getImageInputStream()).size(337, 640)
					.watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File(BASE_PATH + "/watermark.jpg")), 0.25f)
					.outputQuality(0.9f).toFile(dest);
		} catch (Exception e) {
			logger.error(e.toString());
			throw new RuntimeException("创建缩略图失败：" + e.toString());
		}
		return relativeAddr;
	}
	/**
	 * 处理店铺缩略图的方法
	 * 
	 * @param thumbnail
	 *            用户传递的文件信息
	 * @param targetAddr
	 *            文件保存的目标路径
	 * @return relativeAddr 文件的保存地址
	 * @date 2018年10月17日
	 */
	public static String generateThumbnail(ImageHolder thumbnail, String targetAddr) {
		// 文件的真实路径 - 系统随机生成
		String realFileName = getRandomFileName();
		// 获取输入文件流的扩展名
		String extension = getFileExtension(thumbnail.getImageName());
		// 在系统中创建目标路径所涉及到的目录
		makeDirPath(targetAddr);

		// 保存文件相关路径
		String relativeAddr = targetAddr + realFileName + extension;
		logger.debug("current relativeAddr is: " + relativeAddr);
		File dest = new File(PathUtil.getImgBasePath() + relativeAddr);
		logger.debug("current complete addr is: " + PathUtil.getImgBasePath() + relativeAddr);
		// 对保存文件进行 添加水印、压缩处理
		try {
			Thumbnails.of(thumbnail.getImageInputStream()).size(200, 200)
					.watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File(BASE_PATH + "/watermark.jpg")), 0.25f)
					.outputQuality(0.8f).toFile(dest);
		} catch (Exception e) {
			logger.error(e.toString());
			e.printStackTrace();
		}
		return relativeAddr;
	}

	/**
	 * 生成随机文件名，当前年月日小时分钟秒钟 + 五位随机数
	 * 
	 * @return 生成的随机五位数
	 * @date 2018年10月17日
	 */
	public static String getRandomFileName() {
		// 生成随机文件名：当前年月日时分秒+五位随机数（为了在实际项目中防止文件同名而进行的处理）
		int ranNum = RANDOM.nextInt(90000) + 10000;
		String nowTimeStr = sDate_Format.format(new Date());
		return nowTimeStr + ranNum;
	}

	/**
	 * 获取输入文件流的扩展名
	 * 
	 * @param fileName
	 *            文件全级名称
	 * @return 文件扩展名
	 * @date 2018年10月17日
	 */
	private static String getFileExtension(String fileName) {
		// 获取输入文件流的原始文件名称
		return fileName.substring(fileName.lastIndexOf("."));
	}

	/**
	 * 创建目标路径所涉及到的目录
	 * 
	 * @param targetAddr
	 * @date 2018年10月18日
	 */
	private static void makeDirPath(String targetAddr) {
		// 获取图片的真实物理路径
		String realFileParentPath = PathUtil.getImgBasePath() + targetAddr;
		File dirPath = new File(realFileParentPath);
		// 判断父文件夹是否存在
		if (!dirPath.exists()) {
			dirPath.mkdirs();
		}
	}

	/**
	 * storePath是文件的路径 还是目录的路径 如果storePath是文件路径则删除该文件，
	 * 如果storePath是目录路径则删除该目录下的所有文件
	 * 
	 * @param storePath
	 *            需要删除文件的相对路径或目录
	 * @date 2018年10月25日
	 */
	public static void deleteFileOrPath(String storePath) {
		File fileOrPath = new File(PathUtil.getImgBasePath() + storePath);
		// 判断文件是否存在
		if (fileOrPath.exists()) {
			// 该文件路径为目录
			if (fileOrPath.isDirectory()) {
				File[] files = fileOrPath.listFiles();
				for (int i = 0; i < files.length; i++) {
					files[i].delete();
				}
			}
			fileOrPath.delete();
		}
	}

	public static void main(String[] args) throws IOException {
		// 用于测试Thumbnails的示例
		System.out.println(BASE_PATH);
		// 引入thumbnailator的主类 Thumbnails,传入文件资源
		Thumbnails.of(new File("E:/picture/image/xiaohuangren.jpg")).size(200, 200)
				.watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File(BASE_PATH + "/watermark.jpg")), 0.25f)
				.outputQuality(0.8f).toFile("E:/picture/image/xiaohuangrennew.jpg");
	}
}
