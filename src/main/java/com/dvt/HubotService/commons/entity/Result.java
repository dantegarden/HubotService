package com.dvt.HubotService.commons.entity;

public class Result {
	private Integer error_code;
	private Boolean success; // 是否成功
	private String msg; // 消息
	private Object data; // 返回的数据
	private String actionType;//动作类型代码
	
	public Result() {

	}
	
	public static Result getInstance(Integer error_code, String actionType, Object data){
		Result r= new Result(error_code, data);
		r.setActionType(actionType);
		return r;
	}
	
	public Result(Boolean success) {
		super();
		this.success = success;
	}

	public Result(Boolean success, Object data) {
		super();
		this.success = success;
		this.data = data;
	}

	public Result(Boolean success, String msg, Object data) {
		super();
		this.success = success;
		this.msg = msg;
		this.data = data;
	}

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	

	public Integer getError_code() {
		return error_code;
	}

	public void setError_code(Integer error_code) {
		this.error_code = error_code;
	}

	public Result(Integer error_code, String msg, Object data) {
		super();
		this.error_code = error_code;
		this.msg = msg;
		this.data = data;
	}
	public Result(Integer error_code,  Object data) {
		super();
		this.error_code = error_code;
		this.data = data;
	}

	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}
	
	
}
