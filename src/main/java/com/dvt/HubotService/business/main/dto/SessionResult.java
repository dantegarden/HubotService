package com.dvt.HubotService.business.main.dto;

import java.util.List;

public class SessionResult {
	private String session_id;//多轮会话ID，参考请求参数中的说明
	private List<SessionAction> action_list;//动作列表
	private SessionSchema schema;//解析的schema 解析意图、词槽结果都从这里面获取
	private SessionBotRes qu_res;//bot解析的结果
	
	
	public String getSession_id() {
		return session_id;
	}
	public void setSession_id(String session_id) {
		this.session_id = session_id;
	}
	public List<SessionAction> getAction_list() {
		return action_list;
	}
	public void setAction_list(List<SessionAction> action_list) {
		this.action_list = action_list;
	}
	public SessionSchema getSchema() {
		return schema;
	}
	public void setSchema(SessionSchema schema) {
		this.schema = schema;
	}
	public SessionBotRes getQu_res() {
		return qu_res;
	}
	public void setQu_res(SessionBotRes qu_res) {
		this.qu_res = qu_res;
	}
	public SessionResult(String session_id, List<SessionAction> action_list,
			SessionSchema schema, SessionBotRes qu_res) {
		super();
		this.session_id = session_id;
		this.action_list = action_list;
		this.schema = schema;
		this.qu_res = qu_res;
	}
	public SessionResult() {
		super();
	}
	
	
}
