package com.christopher.o2o.exception;

/**
 * @ClassName: ShopOperationException 
 * @Description: 店铺操作异常类
 * @author christopher chan
 * @date 2018年10月18日  
 *
 */
public class WechatAuthOperationException extends RuntimeException {

	/**  
	 * @Fields field:field:{todo}(系统自动生成)  
	 */  
	private static final long serialVersionUID = -7269605864334054336L;

	public WechatAuthOperationException(String msg) {
		super(msg);
	}
	
}
