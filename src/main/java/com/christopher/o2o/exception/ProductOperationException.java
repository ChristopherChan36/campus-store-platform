package com.christopher.o2o.exception;

/**
 * 商品管理相关操作抛出的异常类型
 * @ClassName: ProductOperationExeception 
 * @Description:  
 * @author christopher chan
 * @date 2018年11月1日  
 *
 */
public class ProductOperationException extends RuntimeException {

	/**  
	 * @Fields field:field:{todo}(系统自动生成)  
	 */  
	private static final long serialVersionUID = -1813762002645023651L;

	public ProductOperationException(String msg) {
		super(msg);
	}
}
