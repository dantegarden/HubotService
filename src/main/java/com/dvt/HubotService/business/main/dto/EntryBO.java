package com.dvt.HubotService.business.main.dto;

import java.util.Map.Entry;

public class EntryBO extends ConfigBO  {
	
	private Entry<String,Object> entry;
	
	public EntryBO(String key, String slot, String type, Entry<String, Object> entry) {
		super(key,slot,type);
		this.entry = entry;
	}
	public EntryBO() {
		super();
	}
	public Entry<String, Object> getEntry() {
		return entry;
	}
	public void setEntry(Entry<String, Object> entry) {
		this.entry = entry;
	}
	
}
