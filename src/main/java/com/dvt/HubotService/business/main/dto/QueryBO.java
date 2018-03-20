package com.dvt.HubotService.business.main.dto;

import java.util.List;

public class QueryBO extends ConfigBO {
	
	private List<String> condition;

	public List<String> getCondition() {
		return condition;
	}

	public void setCondition(List<String> condition) {
		this.condition = condition;
	}


	public QueryBO() {
		super();
	}

	public QueryBO(String key,String slot, String type, List<String> condition) {
		super(key,slot,type);
		this.condition = condition;
	}
	
}
