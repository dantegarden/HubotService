package com.dvt.HubotService.business.main.dto;

import java.util.Map.Entry;

public class EntryBO {
	private String key;
	private String type;
	private String dateFormat;
	private String comment;
	private Boolean required = Boolean.FALSE;
	private Entry<String,Object> entry;
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDateFormat() {
		return dateFormat;
	}
	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}
	public Entry<String, Object> getEntry() {
		return entry;
	}
	public void setEntry(Entry<String, Object> entry) {
		this.entry = entry;
	}
	public EntryBO(String key, String type, Entry<String, Object> entry) {
		super();
		this.key = key;
		this.type = type;
		this.entry = entry;
	}
	public EntryBO() {
		super();
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public Boolean getRequired() {
		return required;
	}
	public void setRequired(Boolean required) {
		this.required = required;
	}
	
	
	
	
	
	
}
