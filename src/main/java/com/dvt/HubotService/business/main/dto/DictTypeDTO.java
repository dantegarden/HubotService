package com.dvt.HubotService.business.main.dto;

import java.util.Date;
import java.util.List;

public class DictTypeDTO {
	
	private List<DictDTO> dict;
	private Date accessTime;
	private String codeType;
	
	
	
	public String getCodeType() {
		return codeType;
	}
	public void setCodeType(String codeType) {
		this.codeType = codeType;
	}
	public List<DictDTO> getDict() {
		return dict;
	}
	public void setDict(List<DictDTO> dict) {
		this.dict = dict;
	}
	public Date getAccessTime() {
		return accessTime;
	}
	public void setAccessTime(Date accessTime) {
		this.accessTime = accessTime;
	}
	
	public DictTypeDTO(List<DictDTO> dict, Date accessTime, String codeType) {
		super();
		this.dict = dict;
		this.accessTime = accessTime;
		this.codeType = codeType;
	}
	public DictTypeDTO() {
		super();
	}
	
	
}
