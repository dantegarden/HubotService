package com.dvt.HubotService.business.main.dto;

public class HubotDTO {
	private String username;
	private String userid;
	private String message;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public HubotDTO(String username, String userid, String message) {
		super();
		this.username = username;
		this.userid = userid;
		this.message = message;
	}
	public HubotDTO() {
		super();
	}
	
	
}
