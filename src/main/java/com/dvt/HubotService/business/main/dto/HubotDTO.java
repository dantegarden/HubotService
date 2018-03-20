package com.dvt.HubotService.business.main.dto;

public class HubotDTO {
	private String username;
	private String userid;//用户id
	private String message;//用户说的话
	private String roomid;//rocket.chat的频道id
	private String sceneid;//百度ai的场景id
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
	public HubotDTO(String username, String userid, String roomid, String message) {
		super();
		this.username = username;
		this.userid = userid;
		this.roomid = roomid;
		this.message = message;
	}
	public HubotDTO() {
		super();
	}
	public String getRoomid() {
		return roomid;
	}
	public void setRoomid(String roomid) {
		this.roomid = roomid;
	}
	public String getSceneid() {
		return sceneid;
	}
	public void setSceneid(String sceneid) {
		this.sceneid = sceneid;
	}
	
	
}
