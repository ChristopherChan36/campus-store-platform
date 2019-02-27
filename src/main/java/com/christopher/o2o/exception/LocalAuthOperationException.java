package com.christopher.o2o.exception;

/**
 * @ClassName: ShopOperationException 
 * @Description: 本地账号信息操作异常类
 * @author christopher chan
 * @date 2018年10月18日  
 *
 */
public class LocalAuthOperationException extends RuntimeException {

	/**  
	 * @Fields field:field:{todo}(系统自动生成)  
	 */  
	private static final long serialVersionUID = -6307482246688384810L;

	public LocalAuthOperationException(String msg) {
		super(msg);
	}
	
}
