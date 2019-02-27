package com.christopher.o2o.dto;

/**
 * @ClassName: Result 
 * @Description: 封装返回结果的Json对象，所有返回结果 都可以使用它
 * @author christopher chan
 * @date 2018年10月28日  
 *
 */
public class Result<T> {

	//是否操作成功的标识
	private boolean success;
	//成功时返回的数据
	private T data;
	//错误信息
	private String errorMsg;
	//错误代码
	private int errorCode;
	
	public Result() {
		super();
	}
	
	//成功时的构造器
	public Result(boolean success, T data) {
		this.success = success;
		this.data = data;
	}
	
	//错误时的构造器
	public Result(boolean success, int errorCode, String errorMsg) {
		this.success = success;
		this.errorCode = errorCode;
		this.errorMsg = errorMsg;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
	
}
