package com.christopher.o2o.dto;

import java.io.InputStream;

/**
 * 图片信息封装类
 * @ClassName: ImageHolder 
 * @Description:  
 * @author christopher chan
 * @date 2018年11月2日  
 *
 */
public class ImageHolder {

	//图片名称
	private String imageName;
	//图片输入流
	private InputStream imageInputStream;
	
	public ImageHolder(String imageName, InputStream imageInputStream) {
		super();
		this.imageName = imageName;
		this.imageInputStream = imageInputStream;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public InputStream getImageInputStream() {
		return imageInputStream;
	}

	public void setImageInputStream(InputStream imageInputStream) {
		this.imageInputStream = imageInputStream;
	}
	
}
