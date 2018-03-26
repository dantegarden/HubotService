package com.dvt.HubotService.business.main.dto;

public class DictDTO {
	private String dictKey;
	private String dictValue;
	
	public String getDictKey() {
		return dictKey;
	}
	public void setDictKey(String dictKey) {
		this.dictKey = dictKey.trim();
	}
	public String getDictValue() {
		return dictValue;
	}
	public void setDictValue(String dictValue) {
		this.dictValue = dictValue.trim();
	}
	public DictDTO(String dictKey, String dictValue) {
		super();
		this.dictKey = dictKey.trim();
		this.dictValue = dictValue.trim();
	}
	public DictDTO() {
		super();
	}
	
	
}
