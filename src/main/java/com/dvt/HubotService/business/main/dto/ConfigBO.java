package com.dvt.HubotService.business.main.dto;

import java.io.Serializable;

public class ConfigBO implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String key;
	private String type;
	private String dateFormat;
	private String dateRef;//结束时间和哪个开始时间组成时间对
	private String comment;
	private String codeType;//selection中many2one的对应模型
	private Boolean required = Boolean.FALSE;
	
	private String useClass;
	private String useMethod;
	
	private String tempValue;
	
	private String slot;
	private Boolean isInUse = Boolean.FALSE;
	
	public ConfigBO() {
		super();
	}
	
	public ConfigBO(String key, String type) {
		super();
		this.key = key;
		this.type = type;
	}

	public ConfigBO(String key, String slot, String type) {
		super();
		this.key = key;
		this.slot = slot;
		this.type = type;
	}

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

	public String getUseClass() {
		return useClass;
	}

	public void setUseClass(String useClass) {
		this.useClass = useClass;
	}

	public String getUseMethod() {
		return useMethod;
	}

	public void setUseMethod(String useMethod) {
		this.useMethod = useMethod;
	}

	public String getTempValue() {
		return tempValue;
	}

	public void setTempValue(String tempValue) {
		this.tempValue = tempValue;
	}

	public String getSlot() {
		return slot;
	}

	public void setSlot(String slot) {
		this.slot = slot;
	}

	public Boolean getIsInUse() {
		return isInUse;
	}

	public void setIsInUse(Boolean isInUse) {
		this.isInUse = isInUse;
	}

	public String getDateRef() {
		return dateRef;
	}

	public void setDateRef(String dateRef) {
		this.dateRef = dateRef;
	}

	public String getCodeType() {
		return codeType;
	}

	public void setCodeType(String codeType) {
		this.codeType = codeType;
	}
	
}
