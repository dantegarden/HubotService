package com.dvt.HubotService.business.main.dto;

public class SessionDTO {
	private String log_id;//请求标识码，随机数，唯一
	private String error_code;//请求出错时返回，错误码
	private String error_msg;//请求出错时返回 错误描述信息
	private SessionResult result;//正确时的返回的数据集 
	
	public SessionDTO(String log_id, String error_code, String error_msg,
			SessionResult result) {
		super();
		this.log_id = log_id;
		this.error_code = error_code;
		this.error_msg = error_msg;
		this.result = result;
	}
	public String getLog_id() {
		return log_id;
	}
	public void setLog_id(String log_id) {
		this.log_id = log_id;
	}
	public String getError_code() {
		return error_code;
	}
	public void setError_code(String error_code) {
		this.error_code = error_code;
	}
	public String getError_msg() {
		return error_msg;
	}
	public void setError_msg(String error_msg) {
		this.error_msg = error_msg;
	}
	public SessionResult getResult() {
		return result;
	}
	public void setResult(SessionResult result) {
		this.result = result;
	}
	public SessionDTO() {
		super();
	}
	
	
}
