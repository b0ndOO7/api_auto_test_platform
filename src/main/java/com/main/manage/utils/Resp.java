package com.main.manage.utils;

/**
 * @author renzh
 */
public class Resp<T> {

	/**响应码  200：成功   非200：异常*/
	private String code;

	/** 前端提示信息 */
	private String msg;

	/** 数据体：对象数组或者字符串，根据接口区分 */
	private T data;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public Resp() { }

	public Resp(String code, String msg) {
		this.code = code;
		this.msg = msg;
	}
}
