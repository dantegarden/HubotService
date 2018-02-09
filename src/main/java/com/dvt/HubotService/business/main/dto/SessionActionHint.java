package com.dvt.HubotService.business.main.dto;

public class SessionActionHint {
	private String hint_query;//引导query

	public String getHint_query() {
		return hint_query;
	}

	public void setHint_query(String hint_query) {
		this.hint_query = hint_query;
	}

	public SessionActionHint(String hint_query) {
		super();
		this.hint_query = hint_query;
	}

	public SessionActionHint() {
		super();
	}
	
	
}
