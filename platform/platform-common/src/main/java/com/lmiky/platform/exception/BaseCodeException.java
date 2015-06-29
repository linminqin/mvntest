package com.lmiky.platform.exception;
/**
 * 带编码的异常
 * @author lmiky
 * @date 2014-1-27
 */
public class BaseCodeException extends Exception {
	private static final long serialVersionUID = 6964382460357872032L;
	private Integer code;
	
	public BaseCodeException() {
		super();
	}
	
	public BaseCodeException(String message) {
		super(message);
	}
	
	public BaseCodeException(Integer code) {
		this();
		this.code = code;
	}
	
	public BaseCodeException(String message, Integer code) {
		this(message);
		this.code = code;
	}

	/**
	 * @return the code
	 */
	public Integer getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(Integer code) {
		this.code = code;
	}
}
