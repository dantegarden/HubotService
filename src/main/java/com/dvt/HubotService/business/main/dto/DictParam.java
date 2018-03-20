package com.dvt.HubotService.business.main.dto;

public class DictParam {
	private String model;
	private String field;
	public DictParam(String model, String field) {
		super();
		this.model = model;
		this.field = field;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	public DictParam() {
		super();
	}
	
	
}
